package top.pythong.noteblog.app.archives.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_type.refreshLayout
import kotlinx.android.synthetic.main.archives_fragment.*
import kotlinx.android.synthetic.main.archives_fragment.loadingView
import kotlinx.android.synthetic.main.home_fragment.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

import top.pythong.noteblog.R
import top.pythong.noteblog.app.archives.adapter.ArchivesAdapter
import top.pythong.noteblog.app.archives.model.ArchiveHolder
import top.pythong.noteblog.app.archives.model.ArchiveView
import top.pythong.noteblog.app.home.utils.SmoothScrollLayoutManager
import top.pythong.noteblog.app.login.ui.LoginActivity
import top.pythong.noteblog.app.main.ui.MainActivity
import top.pythong.noteblog.base.factory.ViewModelFactory
import top.pythong.noteblog.base.fragment.BaseFragment
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.clearLoginUser
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.data.constant.MsgCode
import top.pythong.noteblog.utils.putToSharedPreferences

class ArchivesFragment : BaseFragment() {

    val TAG = ArchivesFragment::class.java.simpleName

    private lateinit var viewModel: ArchivesViewModel

    private val archiveList = ArrayList<ArchiveHolder>()

    private lateinit var adapter: ArchivesAdapter

    private lateinit var parentActivity: MainActivity

    /**
     * 创建视图
     */
    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.archives_fragment, container, false)
    }

    override fun onBaseStart() {
        this.activity!!.title = "归档"
        this.parentActivity = this.activity as MainActivity
    }

    /**
     * 加载控件
     */
    override fun initView() {

        // 刷新监听
        refreshLayout.setOnRefreshListener {
            viewModel.loadArchives(it)
        }

        // 加载更多
        refreshLayout.setOnLoadMoreListener {
            viewModel.loadMore(it)
        }

        adapter = ArchivesAdapter(archiveList)
        val smoothScrollLayoutManager = SmoothScrollLayoutManager(this.context)
        smoothScrollLayoutManager.orientation = LinearLayoutManager.VERTICAL
        archivesView.layoutManager = smoothScrollLayoutManager

        archivesView.adapter = adapter


    }

    /**
     * 加载数据
     */
    override fun initData() {

        refresh()

        viewModel.archives.observe(this, Observer {

            val archives = it ?: return@Observer
            archiveList.clear()
            archives.forEach { archive ->
                archiveList.add(ArchiveHolder(ArchiveHolder.ARCHIVE, archive))
            }
            adapter.notifyDataSetChanged()

        })

    }

    /**
     * 获得viewModel
     */
    override fun getViewModel(): BaseViewModel {
        viewModel = ViewModelFactory.createViewModel(this, ArchivesViewModel::class)
        return viewModel
    }

    /**
     * 错误回调
     */
    override fun onErrorResult(error: MsgCode) {
        if (!error.isLoginError()) {

            // 初始化loadingView
            loadingView.errorBtn {
                it.setOnClickListener {
                    refresh()
                }
            }
            loadingView.errorMsg {
                it.text = error.msg
            }
            loadingView.showError(true)

        } else {
            toast(error.msg)
            loadingView.errorBtn {
                it.text = "去登陆"
                it.setOnClickListener {
                    context!!.clearLoginUser()
                    this.parentActivity.startLoginActivity()
                }
            }
            loadingView.errorImg {
                it.setImageResource(R.drawable.squint_eyed)
            }
            loadingView.errorMsg {
                it.text = "登录才给看哟"
            }
            loadingView.showError(true)

        }
    }

    fun refresh() {
        loadingView.show()

        // 判断是否在顶部
        if (!archivesView.canScrollVertically(-1)) {
            // 自动刷新
            refreshLayout.autoRefresh()
        } else {
            // 滑动到顶部
            archivesView.smoothScrollToPosition(0)
        }
    }
}
