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

/**
 * 下载任务页面
 */
class DownloadTaskActivity : BaseActivity() {

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


        downloadReceiver.downloadCallback { resource, state ->
            when (state) {
                DownloadReceiver.START -> {
                    // 保存一个下载任务
                    downloadTasks.add(resource)
                    adapter.notifyDataSetChanged()
                }
                DownloadReceiver.DOWNLOADING -> {
                    // 正在下载

                }
                DownloadReceiver.MERGE -> {
                    // copying

                }
                DownloadReceiver.COMPLETE -> {
                    taskModel.removeTask(resource)
                }
            }
        }
    }

    override fun initData() {

        downloadTasks.addAll(taskModel.getTasks())

        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 注销广播
        unregisterReceiver(downloadReceiver)
    }
}
