package top.pythong.noteblog.app.type.ui

import android.arch.lifecycle.Observer
import android.support.v7.widget.StaggeredGridLayoutManager
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.header.BezierRadarHeader
import kotlinx.android.synthetic.main.activity_type.*
import kotlinx.android.synthetic.main.activity_type.loadingView
import kotlinx.android.synthetic.main.home_fragment.refreshLayout
import top.pythong.noteblog.R
import top.pythong.noteblog.app.home.model.ArticleCardItem
import top.pythong.noteblog.app.home.model.Type
import top.pythong.noteblog.app.type.adapter.TypeAdapter
import top.pythong.noteblog.base.activity.BaseActivity
import top.pythong.noteblog.base.factory.ViewModelFactory
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.data.constant.MsgCode

class TypeActivity : BaseActivity() {

    val TAG = TypeActivity::class.java.simpleName

    lateinit var typeViewModel: TypeViewModel

    lateinit var type: String
    lateinit var typeName: String

    lateinit var adapter: TypeAdapter

    val articleList = ArrayList<ArticleCardItem>()


    override fun getViewModel(): BaseViewModel {
        typeViewModel = ViewModelFactory.createViewModel(this, TypeViewModel::class)
        return typeViewModel
    }

    override fun getContentView(): Int {
        return R.layout.activity_type
    }

    override fun initView() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        loadingView.errorBtn {
            it.setOnClickListener {
                loadingView.show()
                refreshLayout.autoRefresh()
            }
        }

        //设置 Header 为 贝塞尔雷达 样式
        refreshLayout.setRefreshHeader(BezierRadarHeader(this).setEnableHorizontalDrag(true))
        //设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale))

        // 刷新监听
        refreshLayout.setOnRefreshListener {
            typeViewModel.loadData(it, type = type, typeName = typeName)
        }

        // 加载更多
        refreshLayout.setOnLoadMoreListener {
            typeViewModel.loadData(it, true, type, typeName)
            //传入false表示刷新失败
        }

        adapter = TypeAdapter(articleList)
        typeView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        typeView.adapter = adapter


    }

    override fun initData() {

        type = intent.getStringExtra("type") ?: ""
        typeName = intent.getStringExtra("name") ?: ""
        var title = "文章"
        if (Type.TAG == type) {
            title += "标签"
        } else if (Type.CATEGORY == type) {
            title += "分类"
        }
        title += " —— $typeName"
        toolbar.title = title

        refreshLayout.autoRefresh()

        typeViewModel.articles.observe(this, Observer {

            val (append,articles) = it ?: return@Observer
            if (!append){
                articleList.clear()
            }
            articleList.addAll(articles)
            adapter.notifyDataSetChanged()

            loadingView.show()
        })

    }

    override fun onErrorResult(error: MsgCode) {
        loadingView.errorMsg {
            it.text = error.msg
        }
        loadingView.showError(true)
    }
}
