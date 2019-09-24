package top.pythong.noteblog.base.utils

import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.FragmentActivity
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import top.pythong.noteblog.R
import top.pythong.noteblog.app.article.ui.ArticleActivity
import top.pythong.noteblog.base.receiver.CopyBroadcastReceiver
import top.pythong.noteblog.base.receiver.ShareBroadcastReceiver
import top.pythong.noteblog.data.constant.Api
import top.pythong.noteblog.data.constant.Constant
import java.util.*

/**
 *
 * @author ChangJiahong
 * @date 2019/9/23
 */
object AppOpener {
    /**
     * 内置浏览器打开
     */
    fun openInCustomTabsOrBrowser(context: Context, url: String) {
        var url = url
        if (url.isBlank()) {
            context.toast("链接空")
            return
        }
        //check http prefix
        if (!url.contains("//")) {
            url = "http://$url"
        }
        val packageName = CustomTabsHelper.getBestPackageName(context)
        if (packageName != null) {
            context.getDrawable(R.drawable.arrow_back)
            val shareIntent = Intent(context.applicationContext, ShareBroadcastReceiver::class.java)
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val sharePendingIntent = PendingIntent.getBroadcast(
                context.applicationContext, 0, shareIntent, 0
            )
            val copyIntent = Intent(context.applicationContext, CopyBroadcastReceiver::class.java)
            copyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val copyPendingIntent = PendingIntent.getBroadcast(
                context.applicationContext, 0, copyIntent, 0
            )
            val customTabsIntent = CustomTabsIntent.Builder()
                .setToolbarColor(context.resources.getColor(R.color.colorPrimary))
                .setShowTitle(true)
                .addMenuItem(context.getString(R.string.share), sharePendingIntent)
                .addMenuItem(context.getString(R.string.copyUrl), copyPendingIntent)
                .build()
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(context, Uri.parse(url))

        } else {
            openInBrowser(context, url)
        }

    }

    fun launchUrl(context: Context, uri: Uri) {
        val url = uri.toString()
        if (url.startsWith(Api.recommend)) {
            // 文章url
            val articleId = url.substringAfter(Api.recommend + "/")
            context.startActivity<ArticleActivity>(Constant.ARTICLE_ID to articleId)
            return
        }

        // 内置浏览器打开
        openInCustomTabsOrBrowser(context, url)

    }

    fun launchEmail(context: Context, email: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(
                Intent.createChooser(intent, context.getString(R.string.sendEmail))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } catch (e: ActivityNotFoundException) {
            context.toast(context.getString(R.string.noEmailClients))
        }

    }

    fun shareText(context: Context, content: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, content)
        shareIntent.type = "text/plain"
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(
                Intent.createChooser(shareIntent, context.getString(R.string.shareTo))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } catch (e: ActivityNotFoundException) {
            context.toast(context.getString(R.string.noShareClients))
        }
    }

    private val ALIPAY_PACKAGE_NAME = "com.eg.android.AlipayGphone"

    fun checkApkExist(context: Context, packageName: String): Boolean {
        if (packageName.isBlank())
            return false
        return try {
            val info = context.packageManager.getPackageInfo(
                packageName,
                0
            )
            info != null
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * 跳转支付页面
     */
    fun startAlipayClient(activity: FragmentActivity, URLcode: String) {

        val intentFullUrl = "alipays://platformapi/startapp?saId=10000007&" +
                "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2F$URLcode%3F_s" +
                "%3Dweb-other&_t=${System.currentTimeMillis()}"

        if (checkApkExist(activity.baseContext, ALIPAY_PACKAGE_NAME)) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(intentFullUrl))
                activity.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                activity.toast(activity.getString(R.string.noPaymentApplication))
            }
        }else{
            activity.toast(activity.getString(R.string.noPaymentApplication))
        }
    }

    /**
     * 调用默认浏览器
     */
    fun openInWebBrowser(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri).addCategory(Intent.CATEGORY_BROWSABLE)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * 效果不好看
     */
    fun openInBrowser(context: Context, url: String) {
        val uri = Uri.parse(url)
        var intent: Intent? = Intent(Intent.ACTION_VIEW, uri).addCategory(Intent.CATEGORY_BROWSABLE)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent = createActivityChooserIntent(context, intent, uri, VIEW_IGNORE_PACKAGE)
        if (intent != null) {
            context.startActivity(intent)
        } else {
            context.toast(context.getString(R.string.no_browser_clients))
        }
    }

    private fun createActivityChooserIntent(
        context: Context, intent: Intent,
        uri: Uri, ignorPackageList: List<String>?
    ): Intent? {
        val pm = context.packageManager
        val activities = pm.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        val chooserIntents = ArrayList<Intent>()
        val ourPackageName = context.packageName

        Collections.sort(activities, ResolveInfo.DisplayNameComparator(pm))

        for (resInfo in activities) {
            val info = resInfo.activityInfo
            if (!info.enabled || !info.exported) {
                continue
            }
            if (info.packageName == ourPackageName) {
                continue
            }
            if (ignorPackageList != null && ignorPackageList.contains(info.packageName)) {
                continue
            }

            val targetIntent = Intent(intent)
            targetIntent.setPackage(info.packageName)
            targetIntent.setDataAndType(uri, intent.type)
            chooserIntents.add(targetIntent)
        }

        if (chooserIntents.isEmpty()) {
            return null
        }

        val lastIntent = chooserIntents.removeAt(chooserIntents.size - 1)
        if (chooserIntents.isEmpty()) {
            // there was only one, no need to showImage the chooser
            return lastIntent
        }

        val chooserIntent = Intent.createChooser(lastIntent, null)
        chooserIntent.putExtra(
            Intent.EXTRA_INITIAL_INTENTS,
            chooserIntents.toTypedArray()
        )
        return chooserIntent
    }

    private val VIEW_IGNORE_PACKAGE = arrayListOf(
        "com.gh4a", "com.fastaccess", "com.taobao.taobao"
    )


}