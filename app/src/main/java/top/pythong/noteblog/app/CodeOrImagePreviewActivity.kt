package top.pythong.noteblog.app

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_code_preview.*
import me.imid.swipebacklayout.lib.app.SwipeBackActivity
import top.pythong.noteblog.R

class CodeOrImagePreviewActivity : SwipeBackActivity() {

    val TAG = CodeOrImagePreviewActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_preview)
        val mediaType = intent.getStringExtra("mediaType")
        if (mediaType == "code") {
            val codeSource = intent.getStringExtra("codeSource") ?: ""
            val codeClass = intent.getStringExtra("codeClass") ?: ""
            codeView.setCodeSourceText(codeSource, codeClass)

        } else if (mediaType == "img") {
            toolbar.title = "图片"
            val imgUrl = intent.getStringExtra("imgUrl") ?: ""
            Log.d(TAG, imgUrl)
            codeView.setContentViewBackgroundColor(R.color.black)
            codeView.loadImage(imgUrl)
        }

        toolbar.setNavigationOnClickListener {
            finish()
        }


        fullscreen.setOnClickListener {
            toolbar.visibility = View.GONE
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (toolbar.visibility == View.GONE) {
                    toolbar.visibility = View.VISIBLE
                    return false
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
