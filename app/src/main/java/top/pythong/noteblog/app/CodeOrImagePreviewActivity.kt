package top.pythong.noteblog.app

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_code_preview.*
import kotlinx.android.synthetic.main.activity_code_preview.backBtn
import me.imid.swipebacklayout.lib.app.SwipeBackActivity
import org.jetbrains.anko.backgroundColorResource
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
            mTitle.text = "图片"
            val imgUrl = intent.getStringExtra("imgUrl") ?: ""
            Log.d(TAG, imgUrl)
            codeView.setContentViewBackgroundColor(R.color.black)
            codeView.loadImage(imgUrl)
        }

        backBtn.setOnClickListener {
            finish()
        }


        fullscreen.setOnClickListener {
            appBarLayout.visibility = View.GONE
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode){
            KeyEvent.KEYCODE_BACK ->{
                if (appBarLayout.visibility == View.GONE){
                    appBarLayout.visibility = View.VISIBLE
                    return false
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
