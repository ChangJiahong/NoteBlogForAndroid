package top.pythong.noteblog.app.download.ui

import android.content.IntentFilter
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_download_task.*
import top.pythong.noteblog.R
import top.pythong.noteblog.app.download.DownloadReceiver
import top.pythong.noteblog.app.download.adapter.DownloadTaskAdpater
import top.pythong.noteblog.app.download.model.DownloadResource
import top.pythong.noteblog.app.home.utils.SmoothScrollLayoutManager
import top.pythong.noteblog.base.activity.BaseActivity
import top.pythong.noteblog.base.factory.ViewModelFactory
import top.pythong.noteblog.base.viewModel.BaseViewModel
import android.support.v7.widget.DividerItemDecoration
import android.view.Menu


/**
 * 下载任务页面
 */
class DownloadTaskActivity : BaseActivity() {

    val TAG = DownloadTaskActivity::class.java.simpleName

    lateinit var taskModel: DownloadTaskViewModel

    val downloadTasks = ArrayList<DownloadResource>()

    /**
     * 广播
     */
    private val downloadReceiver = DownloadReceiver()

    private lateinit var adapter: DownloadTaskAdpater

    override fun getViewModel(): BaseViewModel {
        taskModel = ViewModelFactory.createViewModel(this, DownloadTaskViewModel::class)
        return taskModel
    }

    override fun getContentView(): Int {
        return R.layout.activity_download_task
    }


    override fun initView() {
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // 注册广播
        val filter = IntentFilter(DownloadReceiver.DOWNLOADING_ACTION)
        registerReceiver(downloadReceiver, filter)

        adapter = DownloadTaskAdpater(downloadTasks)
        val smoothScrollLayoutManager = SmoothScrollLayoutManager(this)
        smoothScrollLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = smoothScrollLayoutManager

        recyclerView.adapter = adapter
        //添加Android自带的分割线
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        downloadReceiver.downloadCallback { resource ->
            adapter.notifyChanged(resource)
        }
    }

    override fun initData() {

        downloadTasks.addAll(taskModel.getTasks())
        if (downloadTasks.isEmpty()){
            loadingView.emptyMsg {
                it.text = "还没有下载的任务"
            }
            loadingView.showEmpty(true)
        }else {
            adapter.notifyData()
        }

        val picks = intent.getSerializableExtra("picks") as ArrayList<DownloadResource>?
        if (picks!=null && picks.isNotEmpty()){
            adapter.selected(picks, 1000)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // 注销广播
        unregisterReceiver(downloadReceiver)
    }
}
