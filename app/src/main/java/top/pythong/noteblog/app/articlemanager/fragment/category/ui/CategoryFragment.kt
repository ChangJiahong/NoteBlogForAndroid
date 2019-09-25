package top.pythong.noteblog.app.articlemanager.fragment.category.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.android.synthetic.main.articles_fragment.*
import kotlinx.android.synthetic.main.category_fragment.*
import kotlinx.android.synthetic.main.category_fragment.loadingView
import kotlinx.android.synthetic.main.category_fragment.recyclerView
import org.jetbrains.anko.support.v4.toast

import top.pythong.noteblog.R
import top.pythong.noteblog.app.articlemanager.ui.ArticleManagerActivity
import top.pythong.noteblog.base.adapter.SimpleAdapter
import top.pythong.noteblog.base.factory.ViewModelFactory
import top.pythong.noteblog.base.fragment.BaseFragment
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.data.constant.MsgCode


class CategoryFragment : BaseFragment() {

    companion object {
        val instance: CategoryFragment by lazy {
            CategoryFragment()
        }
    }

    private val TAG = CategoryFragment::class.java.simpleName

    private lateinit var viewModel: CategoryViewModel

    private lateinit var adapter: SimpleAdapter

    private val categorys = ArrayList<Map<String, String>>()

    private val keys = arrayOf("name", "count")

    private var parentActivity: ArticleManagerActivity? = null

    /**
     * 初始化标志
     */
    private var isInit = false

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.category_fragment, container, false)
    }

    override fun onBaseStart() {
        parentActivity = this.activity as ArticleManagerActivity?
    }

    override fun initView() {
        loadingView.errorBtn {
            it.setOnClickListener {
                loadingView.showLoading()
                viewModel.loadData()
            }
        }

        adapter =
            SimpleAdapter(categorys, R.layout.category_item, keys, arrayOf(R.id.mName, R.id.count))
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { v, position ->
            toast(categorys[position]["id"] ?: "")
        }
    }

    override fun initData() {

        viewModel.archives.observe(this, Observer {
            val (append, archives) = it ?: return@Observer
            if (!append) {
                categorys.clear()
            }
            archives.forEach { archive ->
                categorys.add(
                    mapOf(
                        keys[0] to archive.name,
                        keys[1] to archive.count
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
        viewModel = ViewModelFactory.createViewModel(this, CategoryViewModel::class)
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
}
