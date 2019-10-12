package top.pythong.noteblog.app.article.ui

import android.arch.lifecycle.Observer
import android.support.v4.widget.NestedScrollView
import android.view.View
import kotlinx.android.synthetic.main.activity_article.*
import top.pythong.noteblog.R
import top.pythong.noteblog.base.activity.BaseActivity
import top.pythong.noteblog.base.factory.ViewModelFactory
import top.pythong.noteblog.base.viewModel.BaseViewModel
import android.graphics.Rect
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import org.jetbrains.anko.*
import org.w3c.dom.Text
import top.pythong.noteblog.app.home.model.Type
import top.pythong.noteblog.app.type.ui.TypeActivity
import top.pythong.noteblog.app.userinfo.ui.UserProfileActivity
import top.pythong.noteblog.base.ContentJavaScriptInterface
import top.pythong.noteblog.data.constant.Constant.ARTICLE_ID
import top.pythong.noteblog.data.constant.MsgCode


/**
 * 文章浏览页
 *
 * @author ChangJiaHong
 * @date 2019/8/28
 */
class ArticleActivity : BaseActivity() {
    val TAG = ArticleActivity::class.java.simpleName


    lateinit var articleViewModel: ArticleViewModel

    lateinit var articleId: String

    lateinit var loadingProcess: ProgressBar

    override fun getViewModel(): BaseViewModel {
        articleViewModel = ViewModelFactory.createViewModel(this, ArticleViewModel::class)
        return articleViewModel
    }

    override fun getContentView(): Int {
        return R.layout.activity_article
    }

    override fun initView() {

        toolbar.setNavigationOnClickListener {
            finish()
        }

        loadingView.initDefinePage {
            LayoutInflater.from(this).inflate(R.layout.top_loading, null)
        }

        loadingProcess = loadingView.definePage!!.find(R.id.loadingProgress)

        mContent.setOnProgressChangedListener {
            loadingProcess.progress = it
            if (it == 100) {
                loadingView.show()
            }

        }


        srcollView.setOnScrollChangeListener { _: NestedScrollView?, _: Int, _: Int, _: Int, _: Int ->
            val scrollRect = Rect()
            srcollView.getHitRect(scrollRect)
            if (titleV.getLocalVisibleRect(scrollRect)) {
                buttonBarLayout.visibility = View.GONE
            } else {
                ////子控件完全不在可视范围内
                buttonBarLayout.visibility = View.VISIBLE
            }

        }

        articleViewModel.cardItem.observe(this, Observer {
            val article = it ?: return@Observer
            Glide.with(this).load(article.authorImgUrl).into(uIconbar)
            Glide.with(this).load(article.authorImgUrl).into(uIcon)
            uIcon.setOnClickListener {
                UserProfileActivity.start(this, article.author)
            }
            uIconbar.setOnClickListener {
                UserProfileActivity.start(this, article.author)
            }
            uNameBar.text = article.author
            uName.text = article.author
            mHitsAndLike.text = "0 赞 · ${article.hits} 浏览"
            loadTypes(article.tags, mTags) { tv ->
                startActivity<TypeActivity>("type" to Type.TAG, "name" to tv.text)
            }
            mTitle.text = article.title
            mContent.setContentText(article.content)
            loadTypes(article.categorys, mCategorys) { tv ->
                startActivity<TypeActivity>("type" to Type.CATEGORY, "name" to tv.text)
            }
            editTime.text = getString(R.string.editTime) + " : " + article.createTime

        })

    }


    override fun initData() {
        loadingView.showDefinePage()
//        loadingView.showLoading(true)
        articleId = intent.getStringExtra(ARTICLE_ID)
        articleViewModel.loadArticle(articleId)
    }

    override fun onErrorResult(error: MsgCode) {

        loadingView.errorMsg {
            it.text = error.msg
        }
        loadingView.errorBtn {
            it.setOnClickListener {
                loadingView.showDefinePage(true)
//                loadingView.showLoading(true)
                articleViewModel.loadArticle(articleId)
            }
        }
        loadingView.showError(true)
    }


    fun loadTypes(types: String, vg: ViewGroup, typeClick: (v: TextView) -> Unit = {}) {
        types.split(",").forEach {
            if (it.isBlank()) {
                return@forEach
            }
            val text = TextView(this).apply {
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(0, 0, 10, 0)
                layoutParams = lp
                padding = dip(3)
                text = it
                background = getDrawable(R.drawable.type_bg_ripple)
                setOnClickListener { v ->
                    typeClick(v as TextView)
                }
            }
            vg.addView(text)
        }
    }
}
