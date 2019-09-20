package top.pythong.noteblog.app.download

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.jetbrains.anko.*
import top.pythong.noteblog.R
import top.pythong.noteblog.app.download.model.DownloadResource
import top.pythong.noteblog.app.download.service.IDownloadTaskService
import top.pythong.noteblog.app.download.ui.DownloadTaskActivity
import top.pythong.noteblog.base.factory.ServiceFactory
import top.pythong.noteblog.data.*
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.data.constant.Constant.CHANNEL_ID_DOWNLOAD
import top.pythong.noteblog.data.constant.MsgCode
import top.pythong.noteblog.utils.HttpHelper
import top.pythong.noteblog.utils.getLongFromSharedPreferences
import top.pythong.noteblog.utils.getStringFromSharedPreferences
import top.pythong.noteblog.utils.putToSharedPreferences
import java.util.concurrent.ConcurrentHashMap


class DownloadService : Service(), CoroutineScope by MainScope() {


    companion object {

        val TAG = DownloadService::class.java.simpleName

        const val START_DOWNLOAD = "startDownload"
        const val SUSPEND_DOWNLOAD = "suspendDownload"

        fun addDownload(context: Context, resource: DownloadResource) {
            context.startService<DownloadService>(
                "action" to START_DOWNLOAD,
                "download" to resource
            )
        }

        /**
         * 暂停下载
         */
        fun suspendDownload(context: Context, resource: DownloadResource) {
            context.startService<DownloadService>(
                "action" to SUSPEND_DOWNLOAD,
                "download" to resource
            )
        }

    }

    /**
     * 下载队列
     */
    private val downloadQueue = Channel<DownloadResource>()

    private val notifys = ConcurrentHashMap<Int, NotificationCompat.Builder>()

    private val https = ConcurrentHashMap<Int, HttpHelper>()

    private var downloadService = ServiceFactory.getSimpleService(this, IDownloadTaskService::class)

    /**
     * 处理下载
     */
    @ExperimentalCoroutinesApi
    suspend fun processingDownloadTasks() {
        while (!downloadQueue.isEmpty) {
            val down = downloadQueue.receive()
            // 开启下载
            startDownload(down)

        }
        waitingToStop()
    }

    /**
     * 开始下载
     */
    private fun startDownload(resource: DownloadResource) {
        val tempFile = getResourceTempFile(resource)
        if (!tempFile.exists()) {
            // 临时下载文件不存在，可能被删除或者第一次下载,重置下载进度
            tempFile.createNewFile()
            resource.downloadLen = 0L
        } else {
            // 不为空，获取下载进度
            // 断点续传
            val spdownloadLen = getLongFromSharedPreferences("downLoad-${resource.id}-dLen", 0L)
            val downloadLen = tempFile.length()
            Log.d(TAG, "续传文件：保存进度：${resource.downloadLen}, 缓存文件进度：${downloadLen},sp文件进度：$spdownloadLen")
            resource.downloadLen = downloadLen
            Log.d(TAG, "续传文件：开始进度：${resource.downloadLen}")
        }


        val httpHelper = HttpHelper(this).apply {
            url = resource.url
            downloadLength = resource.downloadLen
            headers {
                Constant.TOKEN - getStringFromSharedPreferences(Constant.TOKEN)
                Constant.ACCEPT_RANGES - "bytes=${resource.downloadLen}-"
            }
        }

        https.put(resource.id, httpHelper)

        // 下载进度回调
        val restResponse = httpHelper.downloadToTemp(tempFile) { clen, dlen ->
            resource.contentLen = clen
            resource.downloadLen = dlen
            // 保存下载进度
            this.putToSharedPreferences {
                put("downLoad-${resource.id}-dLen", dlen)
                put("downLoad-${resource.id}-cLen", clen)
            }
            // 更新下载进度，下载状态
            downloadService.saveDownloadResource(resource, DownloadResource.DOWNLOADING)

            // 更新通知
            val builder = notifys[resource.id]!!
            val progress: String = String.format("%.2f", ((dlen * 1.0 / clen) * 100))
            builder.setContentText("下载进度：$progress%")
            builder.setProgress(clen.toInt(), dlen.toInt(), false)
            notificationManager.notify(resource.id, builder.build())

            // 发送广播，下载中
            sendBroadcast(resource, DownloadResource.DOWNLOADING)
        }

        if (restResponse.isOk()) {
            // 下载成功
            https.remove(resource.id)
            // 下载成功，准备拷贝到下载目录
            val build1 = getNotificationBuilder(resource.name)
            build1.setContentText("进度：100%")
            build1.setProgress(0, 0, true)
            notificationManager.notify(resource.id, build1.build())
            Log.d(TAG, "下载成功")

            // 更新广播 下载完成,copy
            sendBroadcast(resource, DownloadResource.MERGE)
            // 更新下载状态
            downloadService.saveDownloadResource(resource, DownloadResource.MERGE)

            val refile = getResourceFileOrCreate(resource)
            // 拷贝临时文件到下载目录

            FileManager.copyFiletoFile(tempFile, refile)
            tempFile.delete()
            // 删除sp
            putToSharedPreferences {
                put("downLoad-${resource.name}-dLen", 0L)
            }

            // 拷贝完成，更新通知,
            notificationManager.cancel(resource.id)
            val build2 = getNotificationBuilderAddAction(resource)
            build2.setContentText("下载完成")
            notificationManager.notify(resource.id, build2.build())


            // 更新广播 下载完成
            sendBroadcast(resource, DownloadResource.COMPLETE)
            // 更新下载状态，完成
            downloadService.saveDownloadResource(resource, DownloadResource.COMPLETE)


        } else {
            // 下载失败
            Log.d(TAG, "下载失败：${restResponse.msg}")
            if (restResponse.status != MsgCode.Suspension.code){
                notificationManager.cancel(resource.id)
                // 发送广播
                sendBroadcast(resource, DownloadResource.FAILED)
                downloadService.saveDownloadResource(resource, DownloadResource.FAILED)
            }
        }

    }

    /**
     * 关闭下载
     */
    @ExperimentalCoroutinesApi
    suspend fun waitingToStop() {
        Log.d(TAG, "等待...${downloadQueue.isEmpty}")
        delay(7000)
        Log.d(TAG, "每11s检查是否有下载任务,当前任务：${downloadQueue.isEmpty}")
        if (downloadQueue.isEmpty) {
            Log.d(TAG, "没有下载任务，最后5s到底有没有,当前任务：${downloadQueue.isEmpty}")
            delay(3000)
            Log.d(TAG, "延迟，当前任务：${downloadQueue.isEmpty}")
            if (downloadQueue.isEmpty) {
                Log.d(TAG, "真的没有下载任务，我要关闭了")
                downloadQueue.cancel()
                // 停止
                stopSelf()
                return
            }
        }
        processingDownloadTasks()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @ExperimentalCoroutinesApi
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "启动服务")

        launch(Dispatchers.IO) {
            // 处理下载程序
            processingDownloadTasks()
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val action = intent.getStringExtra("action") ?: START_DOWNLOAD
        val downloadResource: DownloadResource? = intent.getSerializableExtra("download") as DownloadResource

        launch {
            if (downloadResource != null) {
                when (action) {
                    START_DOWNLOAD -> {
                        startDownLoadTask(downloadResource)
                    }
                    SUSPEND_DOWNLOAD -> {
                        suspendDownloadTask(downloadResource)
                    }
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * 暂停下载任务
     */
    private suspend fun suspendDownloadTask(downloadResource: DownloadResource) {
        val http = https[downloadResource.id]
        http?.apply {
            // 关闭连接
            stop()
            // 关闭通知
            // 我也不知道为啥要延迟，反正加上就正确了
            delay(100)
            notificationManager.cancel(downloadResource.id)
            val builder = getNotificationBuilderAddAction(downloadResource)
            builder.setContentText("下载已暂停")
            notificationManager.notify(downloadResource.id, builder.build())
            // 更新下载状态，暂停
            downloadService.saveDownloadResource(downloadResource, DownloadResource.SUSPEND)
            sendBroadcast(downloadResource, DownloadResource.SUSPEND)
            // 移除http引用
            https.remove(downloadResource.id)

        }
    }

    /**
     * 开启一个下载任务
     */
    private suspend fun startDownLoadTask(downloadResource: DownloadResource) {

//        val down = downloadService.selectById(downloadResource) ?: downloadResource

        if (downloadResource.id == -1) {
            // 最新下载的资源
            // 添加到下载列表
            downloadService.saveDownloadResource(downloadResource, DownloadResource.START)
            // 发送广播
            sendBroadcast(downloadResource, DownloadResource.START)
        }

        sendBroadcast(downloadResource, DownloadResource.WAITING)
        // 显示通知
        putToShowNotification(downloadResource)
        // 添加下载任务
        downloadQueue.send(downloadResource)
    }

    /**
     * 发送广播
     */
    private fun sendBroadcast(downloadResource: DownloadResource, state: Int) {
        downloadResource.state = state
        val intent = Intent()
        intent.action = DownloadReceiver.DOWNLOADING_ACTION
        intent.putExtra("download", downloadResource)
        sendBroadcast(intent)
    }

    /**
     * 显示下载通知
     */
    private fun putToShowNotification(downloadResource: DownloadResource) {
        val builder = getNotificationBuilderAddAction(downloadResource)
        builder.setProgress(0, 0, true)
        notifys[downloadResource.id] = builder
        val notification = builder.build()
        notification.flags = Notification.FLAG_NO_CLEAR
        notificationManager.notify(downloadResource.id, notification)
    }

    private fun getNotificationBuilder(title: String) = kotlin.run {

        NotificationCompat.Builder(this, CHANNEL_ID_DOWNLOAD).apply {
            setContentTitle(title)
            setContentText("等待下载...")
            setSmallIcon(R.drawable.icon)
            priority = NotificationCompat.PRIORITY_LOW
        }
    }


    private fun getNotificationBuilderAddAction(resource: DownloadResource) = kotlin.run {
        // 去下载页面
        val downIntent = Intent(
            this.baseContext,
            DownloadTaskActivity::class.java
        )

        downIntent.putExtra("picks", arrayListOf(resource))

        val downloadingIntent = PendingIntent.getActivity(
            this.baseContext, 1, downIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        getNotificationBuilder(resource.name).apply {
            setContentIntent(downloadingIntent)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "关闭服务")
    }
}

