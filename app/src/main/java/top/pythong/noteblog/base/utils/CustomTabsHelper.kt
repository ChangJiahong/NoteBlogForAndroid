package top.pythong.noteblog.base.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import java.util.*

/**
 *
 * @author ChangJiahong
 * @date 2019/9/23
 */
object CustomTabsHelper {

    private val ACTION_CUSTOM_TABS_CONNECTION = "android.support.customtabs.action.CustomTabsService"

    private val CHROME_STABLE_PACKAGE = "com.android.chrome"
    private val CHROME_BETA_PACKAGE = "com.chrome.beta"
    private val CHROME_DEV_PACKAGE = "com.chrome.dev"
    private val CHROME_LOCAL_PACKAGE = "com.google.android.apps.chrome"

    private val FIREFOX_STABLE_PACKAGE = "org.mozilla.firefox"
    private val FIREFOX_CN_STABLE_PACKAGE = "cn.mozilla.firefox"
    private val FIREFOX_FOCUS_PACKAGE = "org.mozilla.focus"
    private val FIREFOX_BETA_PACKAGE = "org.mozilla.firefox_beta"
    private val FIREFOX_DEV_PACKAGE = "org.mozilla.fennec_aurora"

    //order for choose best package
    private val ORDERED_PACKAGES = Arrays.asList(
        CHROME_STABLE_PACKAGE, CHROME_BETA_PACKAGE, CHROME_DEV_PACKAGE, CHROME_LOCAL_PACKAGE,
        FIREFOX_STABLE_PACKAGE, FIREFOX_CN_STABLE_PACKAGE, FIREFOX_FOCUS_PACKAGE,
        FIREFOX_BETA_PACKAGE, FIREFOX_DEV_PACKAGE
    )

    private var bestPackageName: String? = null

    /**
     * Goes through all apps that handle VIEW intents and have a warmup service. Picks
     * the one chosen by the user if there is one, otherwise makes a best effort to return a
     * valid package name.
     *
     * This is **not** threadsafe.
     *
     * @param context [Context] to use for accessing [PackageManager].
     * @return The package name recommended to use for connecting to custom tabs related components.
     */
    fun getBestPackageName(context: Context): String? {
        if (bestPackageName != null) return bestPackageName

        val pm = context.packageManager
        // Get default VIEW intent handler.
        val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))
        val defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0)
        var defaultViewHandlerPackageName: String? = null
        if (defaultViewHandlerInfo != null) {
            defaultViewHandlerPackageName = defaultViewHandlerInfo.activityInfo.packageName
        }

        // Get all apps that can handle VIEW intents.
        val resolvedActivityList = pm.queryIntentActivities(activityIntent, 0)
        val packagesSupportingCustomTabs = ArrayList<String>()
        for (info in resolvedActivityList) {
            val serviceIntent = Intent()
            serviceIntent.action = ACTION_CUSTOM_TABS_CONNECTION
            serviceIntent.setPackage(info.activityInfo.packageName)
            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName)
            }
        }

        // Now packagesSupportingCustomTabs contains all apps that can handle both VIEW intents
        // and service calls.
        if (packagesSupportingCustomTabs.isEmpty()) {
            bestPackageName = null
        } else if (packagesSupportingCustomTabs.size == 1) {
            bestPackageName = packagesSupportingCustomTabs[0]
        } else if (!TextUtils.isEmpty(defaultViewHandlerPackageName)
            && !hasSpecializedHandlerIntents(context, activityIntent)
            && packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName)
        ) {
            bestPackageName = defaultViewHandlerPackageName
        } else if (kotlin.run {
                bestPackageName = getFirstMatchedPackage(packagesSupportingCustomTabs)
                bestPackageName
            } != null) {
            //do nothing
        } else {
            packagesSupportingCustomTabs[0]
        }
        return bestPackageName
    }

    /**
     * Used to check whether there is a specialized handler for a given intent.
     * @param intent The intent to check with.
     * @return Whether there is a specialized handler for the given intent.
     */
    private fun hasSpecializedHandlerIntents(context: Context, intent: Intent): Boolean {
        try {
            val pm = context.packageManager
            val handlers = pm.queryIntentActivities(
                intent,
                PackageManager.GET_RESOLVED_FILTER
            )
            if (handlers == null || handlers.size == 0) {
                return false
            }
            for (resolveInfo in handlers) {
                val filter = resolveInfo.filter ?: continue
                if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) continue
                if (resolveInfo.activityInfo == null) continue
                return true
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * Compare local custom tabs packages with ORDERED_PACKAGES, return the first matched
     */
    private fun getFirstMatchedPackage(packagesSupportingCustomTabs: List<String>): String? {
        for (packageName in ORDERED_PACKAGES) {
            if (packagesSupportingCustomTabs.contains(packageName)) {
                return packageName
            }
        }
        return null
    }

}