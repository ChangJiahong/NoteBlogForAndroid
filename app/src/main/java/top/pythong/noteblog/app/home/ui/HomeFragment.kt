package top.pythong.noteblog.app.home.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.header.BezierRadarHeader
import kotlinx.android.synthetic.main.home_fragment.*

import top.pythong.noteblog.R
import top.pythong.noteblog.app.home.adapter.ArticleAdpater
import top.pythong.noteblog.app.home.model.CardItem
import top.pythong.noteblog.app.home.utils.SmoothScrollLayoutManager
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

    private val articleList = ArrayList<CardItem>()

    private lateinit var adapter: ArticleAdpater

    private val TAG = "HomeFragment"


    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun getViewModel(): BaseViewModel {
        viewModel = ViewModelFactory.createViewModel(this, HomeViewModel::class)
        return viewModel
    }

    /**
     * 加载页面
     */
    override fun initView() {
        // 初始化loadingView
        loadingView.errorBtn {
            it.setOnClickListener {
                loadingView.show()
                refresh()
            }
        }

        //设置 Header 为 贝塞尔雷达 样式
        refreshLayout.setRefreshHeader(BezierRadarHeader(this.context).setEnableHorizontalDrag(true))
        //设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(BallPulseFooter(this.context).setSpinnerStyle(SpinnerStyle.Scale))

        // 刷新监听
        refreshLayout.setOnRefreshListener {
            viewModel.refresh(it)
        }

        // 加载更多
        refreshLayout.setOnLoadMoreListener {
            viewModel.loadMore(it)

            //传入false表示刷新失败
        }

        adapter = ArticleAdpater(articleList)
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
            loadingView.show()
            val articles = it ?: return@Observer
            articleList.clear()
            articleList.addAll(articles)
            adapter.notifyDataSetChanged()

        })
    }

    /**
     * 错误回调
     */
    override fun onErrorResult(error: MsgCode) {

        loadingView.errorMsg {
            it.text = error.msg
        }
        loadingView.showError(true)

    }

    fun refresh() {
        recyclerView.adapter!!.notifyDataSetChanged()
        // 判断是否在顶部
        if (!recyclerView.canScrollVertically(-1)) {
            // 自动刷新
            refreshLayout.autoRefresh()
        } else {
            // 滑动到顶部
            recyclerView.smoothScrollToPosition(0)
        }

    }

}
