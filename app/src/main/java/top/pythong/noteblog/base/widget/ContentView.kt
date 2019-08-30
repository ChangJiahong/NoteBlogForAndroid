package top.pythong.noteblog.base.widget

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.v4.view.NestedScrollingChild
import android.support.v4.view.NestedScrollingChildHelper
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import org.apache.commons.lang3.StringUtils
import org.jetbrains.anko.toast
import top.pythong.noteblog.utils.HtmlHelper
import top.pythong.noteblog.R

/**
 *
 * @author ChangJiahong
 * @date 2019/8/29
 */
class ContentView : WebView {

    /**
     * 内容加载回调
     */
    var onProgressChanged: ((progress: Int) -> Unit)? = null

    fun setOnProgressChangedListener(on: (progress: Int) -> Unit) {
        onProgressChanged = on
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {

        webChromeClient = ChromeClient()

        scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

        val settings = settings
        settings.setAppCachePath(context.cacheDir.path)
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        settings.defaultTextEncodingName = "utf-8"
        // 是否加载图片
        val isLoadImageEnable = true
        settings.loadsImagesAutomatically = isLoadImageEnable
        settings.blockNetworkImage = !isLoadImageEnable
        setOnLongClickListener(OnLongClickListener {
            val result = hitTestResult
            if (hitLinkResult(result) && !StringUtils.isBlank(result.extra)) {
                copyToClipboard(result.extra)
                return@OnLongClickListener true
            }
            false
        })


    }

    private fun copyToClipboard(str: String?) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("", str)
        clipboard.primaryClip = clip
        context.toast(context.getString(R.string.copyed))
    }


    fun setContentText(content: String) {
        if (StringUtils.isBlank(content)) {
            return
        }
        val html = HtmlHelper.generateArticleContentHtml(content)
        loadPage(html)
    }

    private fun loadPage(page: String) {
        loadPageWithBaseUrl("file:///android_asset/css/", page)
    }

    private fun loadPageWithBaseUrl(baseUrl: String, page: String) {
        loadDataWithBaseURL(baseUrl, page, "text/html", "utf-8", null)
    }

    private inner class ChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView, progress: Int) {
            super.onProgressChanged(view, progress)
            if (onProgressChanged != null)
                onProgressChanged!!(progress)

        }
    }

    private fun hitLinkResult(result: WebView.HitTestResult): Boolean {
        return result.type == WebView.HitTestResult.SRC_ANCHOR_TYPE ||
                result.type == WebView.HitTestResult.IMAGE_TYPE ||
                result.type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE
    }


}