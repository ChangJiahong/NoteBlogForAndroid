package top.pythong.noteblog.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import java.net.URLEncoder

/**
 *
 * @author ChangJiahong
 * @date 2019/9/22
 */
object PayUtils {

    private val ALIPAY_PACKAGE_NAME = "com.eg.android.AlipayGphone"

    fun checkApkExist(context: Context, packageName: String): Boolean {
        if (packageName.isBlank())
            return false
        return try {
            val info = context.packageManager.getPackageInfo(
                packageName,
                0
            )
            Log.d("pay", "-有糖果")
            info != null
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d("pay", "没0有糖果")
            false
        }
    }

    fun startAlipayClient(activity: FragmentActivity, URLcode: String) {

        Log.d("pay", "请吃糖果")

        val intentFullUrl = "alipays://platformapi/startapp?saId=10000007&" +
                "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2F$URLcode%3F_s" +
                "%3Dweb-other&_t=${System.currentTimeMillis()}"

        if (checkApkExist(activity.baseContext, ALIPAY_PACKAGE_NAME)) {

            Log.d("pay", "吃糖果")
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(intentFullUrl))
                activity.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        } else {

            Log.d("pay", "没有糖果")
        }


    }
}