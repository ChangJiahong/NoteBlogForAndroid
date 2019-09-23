package top.pythong.noteblog.base

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import org.jetbrains.anko.startActivity
import top.pythong.noteblog.app.CodeOrImagePreviewActivity

/**
 *
 * @author ChangJiahong
 * @date 2019/8/31
 */
class ContentJavaScriptInterface(private val context: Context) {
    val TAG = ContentJavaScriptInterface::class.java.simpleName

    @JavascriptInterface
    fun openImage(imgUrl: String) {
        context.startActivity<CodeOrImagePreviewActivity>(
            "mediaType" to "img",
            "imgUrl" to imgUrl
        )
    }

    @JavascriptInterface
    fun openCode(codeSource: String, codeClass: String) {
        context.startActivity<CodeOrImagePreviewActivity>(
            "mediaType" to "code",
            "codeSource" to codeSource,
            "codeClass" to codeClass
        )
    }
}