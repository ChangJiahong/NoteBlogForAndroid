package top.pythong.noteblog.app.articlemanager.ui

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import kotlinx.android.synthetic.main.activity_article_manager.*
import top.pythong.noteblog.R
import top.pythong.noteblog.app.articlemanager.fragment.article.ui.ArticlesFragment
import top.pythong.noteblog.app.articlemanager.fragment.category.ui.CategoryFragment
import top.pythong.noteblog.app.main.adapter.ViewPagerAdapter
import top.pythong.noteblog.base.activity.BaseActivity
import top.pythong.noteblog.base.fragment.BaseFragment

class ArticleManagerActivity : BaseActivity() {

    private lateinit var adapter: ViewPagerAdapter

    private val fragments by lazy {
        arrayListOf(
            Pair("文章", ArticlesFragment.instance),
            Pair("分类", CategoryFragment.instance)
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


    }

    override fun initData() {

    }
}
