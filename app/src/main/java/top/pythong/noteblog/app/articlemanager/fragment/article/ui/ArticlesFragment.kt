package top.pythong.noteblog.app.articlemanager.fragment.article.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.android.synthetic.main.articles_fragment.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.toast

import top.pythong.noteblog.R
import top.pythong.noteblog.app.article.ui.ArticleActivity
import top.pythong.noteblog.app.articlemanager.model.SimpleArticle
import top.pythong.noteblog.app.articlemanager.ui.ArticleManagerActivity
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.base.adapter.SimpleAdapter
import top.pythong.noteblog.base.factory.ViewModelFactory
import top.pythong.noteblog.base.fragment.BaseFragment
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.base.widget.RecyclerViewWithContextMenu
import top.pythong.noteblog.data.constant.Constant.ARTICLE_ID
import top.pythong.noteblog.data.constant.MsgCode

class ArticlesFragment : BaseFragment() {

    companion object {
        fun instance(): ArticlesFragment {
            return ArticlesFragment()
        }

        fun instance(articles: List<SimpleArticle>): ArticlesFragment {
            return ArticlesFragment().apply {
                initialArticles = articles
            }
        }
    }

    private val TAG = ArticlesFragment::class.java.simpleName

    private lateinit var viewModel: ArticlesViewModel

    private lateinit var adapter: SimpleAdapter

    private val articles = ArrayList<HashMap<String, String>>()

    private val keys = arrayOf("title", "info", "hits", "time", "status")

    private var parentActivity: ArticleManagerActivity? = null

    private var initialArticles: List<SimpleArticle>? = null

    /**
     * 初始化标志
     */
    private var isInit = false

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.articles_fragment, container, false)
    }

    override fun initView() {

        loadingView.errorBtn {
            it.setOnClickListener {
                loadingView.showLoading()
                viewModel.loadData()
            }
        }

        adapter =
            SimpleAdapter(articles, R.layout.article_item, keys, arrayOf(R.id.title, R.id.info, R.id.hits, R.id.time))
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { _, position ->
            this.context?.startActivity<ArticleActivity>(ARTICLE_ID to articles[position]["id"])

        }
        adapter.extendedBind { itemV, position ->
            itemV.find<ImageView>(R.id.status).visibility = when (articles[position]["status"]) {
                Article.DRAFT -> View.VISIBLE
                else -> View.GONE
            }
//            itemV.setOnCreateContextMenuListener(this)
        }
        registerForContextMenu(recyclerView)
    }

    override fun onBaseStart() {
        if (this.activity is ArticleManagerActivity) {
            parentActivity = this.activity as ArticleManagerActivity?
        }
    }

    override fun initData() {
        viewModel.article.observe(this, Observer {
            val (append, simples) = it ?: return@Observer
            if (!append) {
                articles.clear()
            }
            simples.forEach { sim ->
                articles.add(
                    hashMapOf(
                        "id" to sim.id.toString(),
                        keys[0] to sim.title,
                        keys[1] to sim.info,
                        keys[2] to " · 浏览 ${sim.hits}",
                        keys[3] to sim.time,
                        keys[4] to sim.status
                    )
                )
            }
            adapter.notifyDataSetChanged()
            loadingView.show()
            toast(getString(R.string.refreshSuccessfully))
        })

        loadData()
    }

    override fun getViewModel(): BaseViewModel {
        viewModel = ViewModelFactory.createViewModel(this, ArticlesViewModel::class)
        return viewModel
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser && !isInit) {
            isInit = true
            loadData()
        }
        super.setUserVisibleHint(isVisibleToUser)
    }

    fun loadData() {
        if (initialArticles != null) {

            initialArticles!!.forEach { sim ->
                articles.add(
                    hashMapOf(
                        "id" to sim.id.toString(),
                        keys[0] to sim.title,
                        keys[1] to sim.info,
                        keys[2] to " · 浏览 ${sim.hits}",
                        keys[3] to sim.time,
                        keys[4] to sim.status
                    )
                )
            }
            adapter.notifyDataSetChanged()
            loadingView.show()
            return
        }

        if (isInit && isVisible) {
            loadingView.showLoading(true)
            viewModel.loadData()
        }
    }

    override fun refresh(refreshLayout: RefreshLayout) {
        viewModel.loadData(refreshLayout)
    }

    override fun loadMore(refreshLayout: RefreshLayout) {
        viewModel.loadData(refreshLayout, true)
    }

    override fun onErrorResult(error: MsgCode) {
        if (!error.isLoginError()) {
            loadingView.errorMsg {
                it.text = error.msg
            }
            loadingView.showError(true)
        } else {
            parentActivity?.onErrorResult(error)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        parentActivity?.menuInflater?.inflate(R.menu.article_edit, menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        val menuInfo = item?.menuInfo as RecyclerViewWithContextMenu.RecyclerViewContextInfo
        val position = menuInfo.getPosition()
        val id = articles[position]["id"] ?: ""
        when (item.itemId) {
            R.id.edit -> {
                toast("编辑")
                // 跳转编辑页面
            }
            R.id.publish -> {
                viewModel.publish(id) { (isOk, data, msg) ->
                    if (isOk) {
                        toast("发布成功")
                        articles[position]["status"] = Article.PUBLISH
                        adapter.notifyItemChanged(position)
                    } else {
                        toast(msg.msg)
                    }
                }
            }
            R.id.del -> {
                viewModel.deleteArticle(id) { (isOk, data, msg) ->
                    if (isOk) {
                        toast("删除成功")
                        adapter.notifyItemRemoved(position)
                    } else {
                        toast(msg.msg)
                    }
                }
            }
        }
        return true
    }
}
