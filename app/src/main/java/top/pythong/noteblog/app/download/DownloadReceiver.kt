package top.pythong.noteblog.app.download

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import top.pythong.noteblog.app.download.model.DownloadResource

/**
 * 下载广播接收
 * @author ChangJiahong
 * @date 2019/9/10
 */
class DownloadReceiver : BroadcastReceiver() {

    val TAG = DownloadReceiver::class.java.simpleName

    companion object {

        const val DOWNLOADING_ACTION = "downloading"

        const val SUSPEND_DOWNLOAD_ACTION = "suspendDownload"

        const val CANCEL_DOWNLOAD_ACTION = "cancelDownload"

        /**
         * 开始一个下载
         */
        const val START = 0
        /**
         * 下载中
         */
        const val DOWNLOADING = 1
        /**
         * 暂停
         */
        const val SUSPEND = 2
        /**
         * 下载完成，copy中
         */
        const val MERGE = 3
        /**
         * 下载完成
         */
        const val COMPLETE = 4
        /**
         * 下载失败
         */
        const val FAILED = 5
    }

    private var callback: (resource: DownloadResource, state: Int) -> Unit = { _, _ ->
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val downloadResource = intent.getSerializableExtra("download") as DownloadResource? ?: return
        when (action) {
            DOWNLOADING_ACTION -> {
                val state = intent.getIntExtra("state", 0)
                // 广播下载回调
                callback(downloadResource, state)
            }
            SUSPEND_DOWNLOAD_ACTION -> {
                // 暂停
                DownloadService.suspendDownload(context, downloadResource)
            }
        }

    }

    fun downloadCallback(callback: (resource: DownloadResource, state: Int) -> Unit) {
        this.callback = callback
    }
}