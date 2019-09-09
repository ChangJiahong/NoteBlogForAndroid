package top.pythong.noteblog.app.download

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.jetbrains.anko.notificationManager
import org.jetbrains.anko.startService
import top.pythong.noteblog.R
import top.pythong.noteblog.app.download.model.DownloadResource
import top.pythong.noteblog.data.FileManager
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.data.constant.Constant.CHANNEL_ID_DOWNLOAD
import top.pythong.noteblog.data.fileDirectoryPath
import top.pythong.noteblog.data.tempDirectoryPath
import top.pythong.noteblog.utils.HttpHelper
import top.pythong.noteblog.utils.getLongFromSharedPreferences
import top.pythong.noteblog.utils.getStringFromSharedPreferences
import top.pythong.noteblog.utils.putToSharedPreferences
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class DownloadService : Service(), CoroutineScope by MainScope() {

    val TAG = DownloadService::class.java.simpleName

    companion object {
        fun addDownload(context: Context, resource: DownloadResource?) {
            if (resource != null) {
                context.startService<DownloadService>("download" to resource)
            }
        }
    }

    /**
     * 下载通道
     */
    private val channel = Channel<DownloadResource>()

    private val notifys = ConcurrentHashMap<Int, NotificationCompat.Builder>()

    private val https = ConcurrentHashMap<Int, HttpHelper>()

    /**
     * 添加下载任务
     */
    private suspend fun addDownloadTask(down: DownloadResource) {
        channel.send(down)
    }

    /**
     * 处理下载
     */
    @ExperimentalCoroutinesApi
    suspend fun processingDownloadTasks() {
        while (!channel.isEmpty) {
            val down = channel.receive()
            // 开启下载
            startDownload(down)

        }
        waitingToStop()
    }

    private fun startDownload(resource: DownloadResource) {
        val tempFilePath = "$tempDirectoryPath/${resource.name}.ntbg"

        val tempFile = File(tempFilePath)
        if (!tempFile.exists()) {
            // 临时下载文件不存在，可能被删除或者第一次下载,重置下载进度
            tempFile.createNewFile()
            resource.downloadLen = 0L
        } else {
            // 不为空，获取下载进度
            // 断点续传
            val downloadLen = getLongFromSharedPreferences("downLoad-${resource.name}-dLen", 0L)
            resource.downloadLen = downloadLen + 1
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

        https[resource.id] = httpHelper

        val restResponse = httpHelper.downloadToTemp(tempFile) { clen, dlen ->
            resource.contentLen = clen
            resource.downloadLen = dlen
            // 保存下载进度
            this.putToSharedPreferences {
                put("downLoad-${resource.name}-dLen", dlen)
            }
            // 更新通知
            val builder = notifys[resource.id]!!
            val progress: String = String.format("%.2f", ((dlen * 1.0 / clen) * 100))
            builder.setContentText("下载进度：$progress%")
            builder.setProgress(clen.toInt(), dlen.toInt(), false)
            notificationManager.notify(resource.id, builder.build())
        }

        if (restResponse.isOk()) {
            // 下载成功
            https.remove(resource.id)
            // 更新通知
            val build1 = getNotificationBuilder(resource.name)
            build1.setContentText("进度：100%")
            build1.setProgress(0, 0, true)
            notificationManager.notify(resource.id, build1.build())
            Log.d(TAG, "下载成功")
            // 更新广播
            // TODO:
            // 拷贝临时文件到下载目录
            val refile = File("$fileDirectoryPath/${resource.name}")
            if (!refile.exists()) {
                // 创建文件
                refile.createNewFile()
            }

            FileManager.copyFiletoFile(tempFile, refile)
            tempFile.delete()
            // 删除sp
            putToSharedPreferences {
                put("downLoad-${resource.name}-dLen", "")
            }

            // 拷贝完成，更新通知,
            // TODO：链接到打开文件
            notificationManager.cancel(resource.id)
            val build2 = getNotificationBuilder(resource.name)
            build2.setContentText("下载完成")
            notificationManager.notify(resource.id, build2.build())
            // 更新广播
            // TODO:

        } else {
            // 下载失败
            // TODO:
            Log.d(TAG, "下载失败：${restResponse.msg}")
        }

    }

    /**
     * 关闭下载
     */
    @ExperimentalCoroutinesApi
    suspend fun waitingToStop() {
        Log.d(TAG, "等待...${channel.isEmpty}")
        delay(11000)
        Log.d(TAG, "每11s检查是否有下载任务,当前任务：${channel.isEmpty}")
        if (channel.isEmpty) {
            Log.d(TAG, "没有下载任务，最后5s到底有没有,当前任务：${channel.isEmpty}")
            delay(5000)
            Log.d(TAG, "延迟，当前任务：${channel.isEmpty}")
            if (channel.isEmpty) {
                Log.d(TAG, "真的没有下载任务，我要关闭了")
                channel.cancel()
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

    // 任务数量
    var count = 0

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val GROUP_KEY_WORK_EMAIL = "com.android.example.WORK_EMAIL"

        val downloadResource: DownloadResource? = intent.getSerializableExtra("download") as DownloadResource

        launch {
            if (downloadResource != null) {
                count++
                downloadResource.id = count
                // 显示通知
                // TODO：通知不需关闭
                // TODO: 取消按钮
                showNotification(downloadResource)
                // 添加下载任务
                addDownloadTask(downloadResource)
            }
        }



        return super.onStartCommand(intent, flags, startId)
    }

    private fun showNotification(downloadResource: DownloadResource) {
        notifys
        val builder = getNotificationBuilder(downloadResource.name)
        builder.setProgress(0, 0, true)
        notifys[downloadResource.id] = builder
        notificationManager.notify(downloadResource.id, builder.build())
    }

    private fun getNotificationBuilder(title: String) =
        NotificationCompat.Builder(this, CHANNEL_ID_DOWNLOAD).apply {
            setContentTitle(title)
            setContentText("等待下载...")
            setSmallIcon(R.drawable.icon)
            priority = NotificationCompat.PRIORITY_LOW
        }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "关闭服务")
    }
}

