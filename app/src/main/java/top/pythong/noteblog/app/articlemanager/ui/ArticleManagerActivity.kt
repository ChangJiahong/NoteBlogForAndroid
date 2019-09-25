package top.pythong.noteblog.app.articlemanager.ui

import kotlinx.android.synthetic.main.activity_article_manager.*
import kotlinx.android.synthetic.main.activity_article_manager.refreshLayout
import kotlinx.android.synthetic.main.activity_article_manager.toolbar
import org.jetbrains.anko.toast
import top.pythong.noteblog.R
import top.pythong.noteblog.app.articlemanager.fragment.article.ui.ArticlesFragment
import top.pythong.noteblog.app.articlemanager.fragment.category.ui.TypeFragment
import top.pythong.noteblog.app.home.model.Type
import top.pythong.noteblog.app.main.adapter.ViewPagerAdapter
import top.pythong.noteblog.app.main.ui.MainActivity
import top.pythong.noteblog.base.activity.BaseActivity
import top.pythong.noteblog.clearLoginUser
import top.pythong.noteblog.data.constant.MsgCode

class ArticleManagerActivity : BaseActivity() {

    private val TAG = ArticleManagerActivity::class.java.simpleName

    private lateinit var adapter: ViewPagerAdapter

    private val fragments by lazy {
        arrayListOf(
            Pair("文章", ArticlesFragment.instance()),
            Pair("分类", TypeFragment.instance(Type.CATEGORY)),
            Pair("标签", TypeFragment.instance(Type.TAG))
        )
    }

    override fun getContentView(): Int {
        return R.layout.activity_article_manager
    }

    override fun initView() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        adapter = ViewPagerAdapter(fragments, this.supportFragmentManager)
        viewPager.adapter = adapter

        tabs.setupWithViewPager(viewPager)

        refreshLayout.setOnRefreshListener {
            fragments[viewPager.currentItem].second.refresh(it)
        }

        refreshLayout.setOnLoadMoreListener {
            fragments[viewPager.currentItem].second.loadMore(it)
        }

    }

    override fun initData() {

    }

    override fun onErrorResult(error: MsgCode) {
        if (error.isLoginError()) {
            toast(error.msg)
            loadingView.errorBtn {
                it.text = getString(R.string.goToLogin)
                it.setOnClickListener {
                    clearLoginUser()
                    startLoginActivity()
                }
            }
            loadingView.errorImg {
                it.setImageResource(R.drawable.squint_eyed)
            }
            loadingView.errorMsg {
                it.text = getString(R.string.loginToSee)
            }
            loadingView.showError(true)
        }
    }

    override fun reload() {
        loadingView.show()
        fragments[viewPager.currentItem].second.refresh(refreshLayout)
        setResult(MainActivity.NEED_TO_REFRESH)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 销毁fragment
        fragments.clear()
    }
}
