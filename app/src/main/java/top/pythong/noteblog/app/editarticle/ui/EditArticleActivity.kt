package top.pythong.noteblog.app.editarticle.ui

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import kotlinx.android.synthetic.main.activity_edit_article.*
import me.imid.swipebacklayout.lib.SwipeBackLayout
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import top.pythong.noteblog.R
import top.pythong.noteblog.app.editarticle.fragment.edit.ui.EditFragment
import top.pythong.noteblog.app.editarticle.fragment.preview.ui.PreviewFragment
import top.pythong.noteblog.app.main.adapter.ViewPagerAdapter
import top.pythong.noteblog.base.activity.BaseActivity
import top.pythong.noteblog.data.constant.Constant

class EditArticleActivity : BaseActivity() {

    /**
     * 编辑内容
     */
    var mdContent: String = ""

    lateinit var articleId: String

    private lateinit var adapter: ViewPagerAdapter
    private var saved = false

    private val fragments by lazy {
        arrayListOf(
            Pair("编辑", EditFragment.newInstance()),
            Pair("预览", PreviewFragment.newInstance())
        )
    }

    override fun getContentView(): Int {
        return R.layout.activity_edit_article
    }

    override fun initView() {
        toolbar.title = ""
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            if (mdContent.isNotBlank()) {
                alert {
                    title = "提示"
                    message = "当前有正在编辑的内容尚未保存，是否保存到草稿？"
                    neutralPressed("取消") {
                        it.dismiss()
                    }
                    negativeButton("退出") {
                        finish()
                    }
                    positiveButton("保存到草稿") {
                        // TODO：保存到草稿
                    }
                }.show()
                return@setNavigationOnClickListener
            }
            finish()
        }
        setSwipeBackEnable(false)
        articleId = intent.getStringExtra(Constant.ARTICLE_ID)

        adapter = ViewPagerAdapter(fragments, this.supportFragmentManager)
        viewPager.adapter = adapter

        tabs.setupWithViewPager(viewPager)

    }

    override fun initData() {


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit, menu)
        return true
    }

    companion object {
        fun start(context: Context, articleId: String) {
            context.startActivity<EditArticleActivity>(Constant.ARTICLE_ID to articleId)
        }
    }
}
