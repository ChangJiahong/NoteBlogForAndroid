package top.pythong.noteblog.app.home.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
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
import top.pythong.noteblog.app.home.model.Article

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

    }

    private fun initView() {

        //设置 Header 为 贝塞尔雷达 样式
        refreshLayout.setRefreshHeader(BezierRadarHeader(this.context).setEnableHorizontalDrag(true))
        //设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(BallPulseFooter(this.context).setSpinnerStyle(SpinnerStyle.Scale))

        // 刷新监听
        refreshLayout.setOnRefreshListener {
            it.finishRefresh(2000, false, false)
        }

        // 加载更多
        refreshLayout.setOnLoadMoreListener {
            it.finishLoadMore(2000, false, false);//传入false表示刷新失败
        }


        for (i in 0..20) {
            articleList.add(Article("文章标题"))
        }

        adapter = ArticleAdpater(articleList)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter
    }

}
