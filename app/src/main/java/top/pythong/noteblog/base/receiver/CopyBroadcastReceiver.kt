package top.pythong.noteblog.base.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import top.pythong.noteblog.copyToClipboard

/**
 *
 * @author ChangJiahong
 * @date 2019/9/23
 */
class CopyBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val uri = intent.data
        if (uri != null) {
            val content = uri.toString()
            context.copyToClipboard(content)
        }
    }
}