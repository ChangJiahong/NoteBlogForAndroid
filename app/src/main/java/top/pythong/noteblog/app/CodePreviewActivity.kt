package top.pythong.noteblog.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_article.*
import kotlinx.android.synthetic.main.activity_code_preview.*
import kotlinx.android.synthetic.main.activity_code_preview.backBtn
import me.imid.swipebacklayout.lib.app.SwipeBackActivity
import top.pythong.noteblog.R

class CodePreviewActivity : SwipeBackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_preview)

        val codeSource = intent.getStringExtra("codeSource")
        val codeClass = intent.getStringExtra("codeClass")

        backBtn.setOnClickListener {
            finish()
        }

        codeView.setCodeSourceText(codeSource, codeClass)

    }
}
