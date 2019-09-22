package top.pythong.noteblog.app.articlemanager.fragment.article.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.articles_fragment.*
import org.jetbrains.anko.support.v4.toast

import top.pythong.noteblog.R
import top.pythong.noteblog.app.article.model.ArticleView
import top.pythong.noteblog.app.articlemanager.fragment.category.ui.CategoryFragment
import top.pythong.noteblog.app.articlemanager.model.SimpleArticle
import top.pythong.noteblog.app.home.model.ArticleCardItem
import top.pythong.noteblog.base.adapter.SimpleAdapter
import top.pythong.noteblog.base.fragment.BaseFragment
import top.pythong.noteblog.base.viewModel.BaseViewModel

class ArticlesFragment : BaseFragment() {

    companion object {
        val instance: ArticlesFragment by lazy {
            ArticlesFragment()
        }
    }

    private lateinit var viewModel: ArticlesViewModel

    private lateinit var adapter: SimpleAdapter

    private val articles = ArrayList<Map<String, String>>()

    private val keys = arrayOf("title", "info", "hits", "time")

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.articles_fragment, container, false)
    }

    override fun initView() {
        for (i in 0..20) {
            articles.add(
                mapOf(
                    "id" to "$i",
                    keys[0] to "文章$i",
                    keys[1] to "内容",
                    keys[2] to "10",
                    keys[3] to "2019年11月11日"
                )
            )
        }
        adapter =
            SimpleAdapter(articles, R.layout.article_item, keys, arrayOf(R.id.title, R.id.info, R.id.hits, R.id.time))
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { v, position ->
            toast(articles[position]["id"]?:"")
        }
    }

    override fun initData() {

    }

    override fun getViewModel(): BaseViewModel {
        viewModel = ViewModelProviders.of(this).get(ArticlesViewModel::class.java)
        return viewModel
    }
}
