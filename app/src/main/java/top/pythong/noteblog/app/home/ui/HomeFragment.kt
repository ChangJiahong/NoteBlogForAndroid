package top.pythong.noteblog.app.home.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.header.BezierRadarHeader
import kotlinx.android.synthetic.main.home_fragment.*
import org.jetbrains.anko.support.v4.toast

import top.pythong.noteblog.R
import top.pythong.noteblog.app.home.adapter.ArticleAdpater
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.utils.SmoothScrollLayoutManager

/**
 * 发现页
 */
class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    private val articleList = ArrayList<Article>()

    private lateinit var adapter: ArticleAdpater

    private val TAG = "HomeFragment"

    var page = 1

    var size = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        initView()

        initData()

    }


    /**
     * 加载数据
     */
    private fun initData() {
        viewModel.refresh(size)

        viewModel.articles.observe(this, Observer {
            val homeResult = it ?: return@Observer
            if (homeResult.isOk) {
                val articleView = homeResult.viewData!!
                articleList.clear()
                articleList.addAll(articleView.articles)
                adapter.notifyDataSetChanged()
            } else {
                toast(homeResult.msgCode.msg)
            }
        })
    }

    /**
     * 加载页面
     */
    private fun initView() {

        //设置 Header 为 贝塞尔雷达 样式
        refreshLayout.setRefreshHeader(BezierRadarHeader(this.context).setEnableHorizontalDrag(true))
        //设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(BallPulseFooter(this.context).setSpinnerStyle(SpinnerStyle.Scale))

        // 刷新监听
        refreshLayout.setOnRefreshListener {
            val ok = viewModel.refresh(size)
            if (ok) {
                page = 1
            }
            it.finishRefresh(1000, ok, false)
        }

        // 加载更多
        refreshLayout.setOnLoadMoreListener {
            val (ok, hasMore) = viewModel.loadMore(page + 1, size)
            if (ok) {
                page += 1
            }
            it.finishLoadMore(1000, ok, !hasMore)
            //传入false表示刷新失败
        }

        adapter = ArticleAdpater(articleList)
        recyclerView.layoutManager = SmoothScrollLayoutManager(this.context)
        recyclerView.adapter = adapter
    }

    fun refresh() {
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
