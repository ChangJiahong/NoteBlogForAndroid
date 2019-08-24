package top.pythong.noteblog.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 *
 * @author ChangJiahong
 * @date 2019/8/24
 */
object NetworkUtils {

    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo.isConnected
    }

}