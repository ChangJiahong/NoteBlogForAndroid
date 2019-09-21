package top.pythong.noteblog.base.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager

/**
 * API < 21
 * 网络状态监听广播
 *
 * @author ChangJiahong
 * @date 2019/9/20
 */
class NetWorkReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {

        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//        registerReceiver(networkChangedReceiver, intentFilter);
    }
}