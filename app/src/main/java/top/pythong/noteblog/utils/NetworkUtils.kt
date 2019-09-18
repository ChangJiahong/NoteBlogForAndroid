package top.pythong.noteblog.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


/**
 *
 * @author ChangJiahong
 * @date 2019/8/24
 */
object NetworkUtils {

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    fun isNetworkConnected(context: Context): Boolean {
        // 获取NetworkInfo对象
        val networkInfo = getActiveNetworkInfo(context)
        //判断NetworkInfo对象是否为空
        return networkInfo?.isConnected ?: false
    }

    fun getActiveNetworkInfo(context: Context): NetworkInfo? {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // 获取NetworkInfo对象
        return manager.activeNetworkInfo
    }

    /**
     * 判断WIFI网络是否可用
     *
     */
    fun isWifiConnected(context: Context): Boolean {
        val networkInfo = getActiveNetworkInfo(context)
        if (networkInfo?.isConnected == true) {
            return networkInfo.type == ConnectivityManager.TYPE_WIFI
        }
        return false
    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @param context
     * @return
     */
    fun isMobileConnected(context: Context): Boolean {
        val networkInfo = getActiveNetworkInfo(context)
        if (networkInfo?.isConnected == true) {
            return networkInfo.type == ConnectivityManager.TYPE_MOBILE
        }
        return false
    }
}