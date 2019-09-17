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

    }

    private var callback: (resource: DownloadResource) -> Unit = { _ ->
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val downloadResource = intent.getSerializableExtra("download") as DownloadResource? ?: return
        when (action) {
            DOWNLOADING_ACTION -> {
                // 广播下载回调
                callback(downloadResource)
            }
            SUSPEND_DOWNLOAD_ACTION -> {
                // 暂停
                DownloadService.suspendDownload(context, downloadResource)
            }
        }

    }

    fun downloadCallback(callback: (resource: DownloadResource) -> Unit) {
        this.callback = callback
    }
}