package top.pythong.noteblog.base.widget

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.webkit.*
import org.apache.commons.lang3.StringUtils
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import top.pythong.noteblog.utils.HtmlHelper
import top.pythong.noteblog.R
import top.pythong.noteblog.base.ContentJavaScriptInterface
import top.pythong.noteblog.data.constant.Api
import top.pythong.noteblog.app.article.ui.ArticleActivity
import top.pythong.noteblog.data.constant.Constant.ARTICLE_ID
import java.util.*



/**
 *
 * @author ChangJiahong
 * @date 2019/8/29
 */
class ContentView : WebView {

    val TAG = ContentView::class.java.simpleName

    private var mBackgroundColor: Int? = null

    /**
     * 内容加载回调
     */
    var onProgressChanged: ((progress: Int) -> Unit)? = null

    fun setOnProgressChangedListener(on: (progress: Int) -> Unit) {
        onProgressChanged = on
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {

        if (attrs != null) {
            val tp = getContext().obtainStyledAttributes(attrs, R.styleable.ContentView)
            try {
                mBackgroundColor = tp.getColor(
                    R.styleable.ContentView_webview_background, getColorAttr(context, android.R.attr.windowBackground)
                )
                setBackgroundColor(mBackgroundColor!!)
            } finally {
                tp.recycle()
            }
        }


        webChromeClient = ChromeClient()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            webViewClient = WebClientN()
        } else {
            webViewClient = WebClient()
        }
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
        settings.javaScriptEnabled = true

        // javascript 接口
        addJavascriptInterface(ContentJavaScriptInterface(this.context), "listener")
    }

    private fun copyToClipboard(str: String?) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("", str)
        clipboard.primaryClip = clip
        context.toast(context.getString(R.string.copyed))
    }


    fun setContentText(content: String) {
        if (content.isBlank()) {
            return
        }
        val html = HtmlHelper.generateArticleContentHtml(content)
        loadPage(html)
    }

    fun setCodeSourceText(codeSource: String, codeClass: String) {
        if (codeSource.isBlank()) {
            return
        }
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
        scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        val html = HtmlHelper.generateCodeHtml(codeSource, codeClass)
        loadPage(html)
    }

    fun loadImage(imgUrl: String) {
        if (imgUrl.isBlank()) {
            return
        }
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        val html = HtmlHelper.generateImageHtml(imgUrl, getCodeBackgroundColor())
        loadData(html, "text/html", null)
    }

    private fun loadPage(page: String) {
        loadPageWithBaseUrl("file:///android_asset/", page)
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

    private inner class WebClientN : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            startActivity(request.url)
            return true
        }

    }

    private inner class WebClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            startActivity(Uri.parse(url))
            return true
        }

    }

    private fun getColorAttr(context: Context, attr: Int): Int {
        val theme = context.theme
        val typedArray = theme.obtainStyledAttributes(intArrayOf(attr))
        val color = typedArray.getColor(0, Color.LTGRAY)
        typedArray.recycle()
        return color
    }

    fun setContentViewBackgroundColor(color: Int){
        mBackgroundColor = color

    }
    private fun getCodeBackgroundColor(): String {
        return "#" + Integer.toHexString(this.mBackgroundColor!!).substring(2).toUpperCase()
    }

    private fun startActivity(uri: Uri?) {
        if (uri == null) return
        val url = uri.toString()
        if (!url.startsWith(Api.article)){
//            openInBrowser(this.context, url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            this.context.startActivity(intent)
            return
        }

        val articleId = url.substringAfter(Api.article+"/")

        this.context.startActivity<ArticleActivity>(ARTICLE_ID to articleId)
    }

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