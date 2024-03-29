package top.pythong.noteblog.app.home.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.header.BezierRadarHeader
import kotlinx.android.synthetic.main.home_fragment.*
import org.jetbrains.anko.support.v4.startActivity

import top.pythong.noteblog.R
import top.pythong.noteblog.app.home.adapter.ArticleAdapter
import top.pythong.noteblog.app.home.model.ArticleCardItem
import top.pythong.noteblog.app.home.utils.SmoothScrollLayoutManager
import top.pythong.noteblog.app.search.ui.SearchActivity
import top.pythong.noteblog.base.fragment.BaseFragment
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.base.factory.ViewModelFactory
import top.pythong.noteblog.data.constant.MsgCode

/**
 * 发现页
 */
class HomeFragment : BaseFragment() {

    companion object {
        val instance: HomeFragment by lazy {
            HomeFragment()
        }
    }

    private lateinit var viewModel: HomeViewModel

    private val articleList = ArrayList<ArticleCardItem>()

    private lateinit var adapter: ArticleAdapter

    private val TAG = "HomeFragment"


    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun getViewModel(): BaseViewModel {
        viewModel = ViewModelFactory.createViewModelWithContext(this, HomeViewModel::class)
        return viewModel
    }

    override fun onBaseStart() {
        this.activity!!.title = "发现"
    }

    /**
     * 加载页面
     */
    override fun initView() {

        setHasOptionsMenu(true)

        // 初始化loadingView
        loadingView.errorBtn {
            it.setOnClickListener {
                refresh()
            }
        }

        //设置 Header 为 贝塞尔雷达 样式
        refreshLayout.setRefreshHeader(BezierRadarHeader(this.context).setEnableHorizontalDrag(true))
        //设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(BallPulseFooter(this.context).setSpinnerStyle(SpinnerStyle.Scale))

        // 刷新监听
        refreshLayout.setOnRefreshListener {
            viewModel.loadData(it)
        }

        // 加载更多
        refreshLayout.setOnLoadMoreListener {
            viewModel.loadData(it, true)
            //传入false表示刷新失败
        }

        adapter = ArticleAdapter(articleList)

        adapter.onLike = { id, rest ->
            viewModel.like(id, rest)
        }


        val smoothScrollLayoutManager = SmoothScrollLayoutManager(this.context)
        smoothScrollLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = smoothScrollLayoutManager

        recyclerView.adapter = adapter
    }

    /**
     * 加载数据
     */
    override fun initData() {
        // 初始化刷新
        refresh()
        viewModel.articles.observe(this, Observer {

            val (append, articles) = it ?: return@Observer
            if (!append) {
                articleList.clear()
            }
            articleList.addAll(articles)
            adapter.notifyDataSetChanged()

        })
    }

    /**
     * 错误回调
     */
    override fun onErrorResult(error: MsgCode) {
        if (!error.isLoginError()) {
            loadingView.errorMsg {
                it.text = error.msg
            }
            loadingView.showError(true)
        }
    }

    override fun refresh() {
        loadingView.show()

        // 判断是否在顶部
        if (!recyclerView.canScrollVertically(-1)) {
            // 自动刷新
            refreshLayout.autoRefresh()
        } else {
            // 滑动到顶部
            recyclerView.smoothScrollToPosition(0)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.search, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.app_bar_search) {
            startActivity<SearchActivity>()
        }
        return true
    }
}
