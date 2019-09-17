package top.pythong.noteblog.app.download.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import org.jetbrains.anko.find
import top.pythong.noteblog.R
import top.pythong.noteblog.app.download.DownloadService
import top.pythong.noteblog.app.download.model.DownloadResource

/**
 * 下载任务管理adapter
 * @author ChangJiahong
 * @date 2019/9/10
 */
class DownloadTaskAdpater(val downloadTasks: ArrayList<DownloadResource>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = DownloadTaskAdpater::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.download_task_item, null)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return downloadTasks.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        viewHolder as ViewHolder
        val item = downloadTasks[position]
        viewHolder.apply {
            mName.text = item.name
            mDownBtn.setOnClickListener {
                btnOnClick(it, item)
            }
            mProgressBar.visibility = View.VISIBLE
            when (item.state) {
                DownloadResource.START, DownloadResource.WAITING -> {
                    mProgress.text = ""
                    mProgressBar.isIndeterminate = true
                    mDownBtn.text = "等待下载..."
                    mDownBtn.isEnabled = false
                }
                DownloadResource.SUSPEND -> {
                    // 暂停状态
                    mDownBtn.text = "继续"
                    mDownBtn.isEnabled = true
                    mSpeed.text = ""
                    val progress: String = String.format("%.2f", ((item.downloadLen * 1.0 / item.contentLen) * 100))
                    mProgress.text = "$progress%"
                    mProgressBar.isIndeterminate = false
                    mProgressBar.max = item.contentLen.toInt()
                    mProgressBar.progress = item.downloadLen.toInt()
                }
                DownloadResource.COMPLETE -> {
                    mProgress.text = ""
                    // 下载完成
                    // 隐藏进度条
                    mProgressBar.visibility = View.GONE
                    mDownBtn.text = "重新下载"
                    mDownBtn.isEnabled = true
                }
                DownloadResource.MERGE -> {
                    mProgress.text = ""
                    // copy中
                    mProgressBar.isIndeterminate = true
                    mDownBtn.text = "合并资源"
                    mDownBtn.isEnabled = false
                }
                DownloadResource.DOWNLOADING -> {
                    // 下载中
                    mDownBtn.text = "暂停"
                    mDownBtn.isEnabled = true
                    mSpeed.text = ""
                    val progress: String = String.format("%.2f", ((item.downloadLen * 1.0 / item.contentLen) * 100))
                    mProgress.text = "$progress%"
                    mProgressBar.isIndeterminate = false
                    mProgressBar.max = item.contentLen.toInt()
                    mProgressBar.progress = item.downloadLen.toInt()
                }
                DownloadResource.FAILED -> {
                    mProgress.text = ""
                    mDownBtn.text = "重试"
                    mDownBtn.isEnabled = true
                }
            }


        }

    }

    val btnOnClick: (View, DownloadResource) -> Unit = { v, res ->

        when (res.state) {
            DownloadResource.DOWNLOADING -> {
                // 暂停
                DownloadService.suspendDownload(v.context, res)
            }
            DownloadResource.FAILED, DownloadResource.SUSPEND, DownloadResource.COMPLETE -> {
                // 开启下载
                DownloadService.addDownload(v.context, res)
            }

        }

    }

    fun notifyItemChanged(resource: DownloadResource) {
        val postison = downloadTasks.indexOf(resource)
        val item = downloadTasks[postison]
        item.state = resource.state
        item.contentLen = resource.contentLen
        item.downloadLen = resource.downloadLen
        notifyItemChanged(postison)
    }

    fun notifyChanged(resource: DownloadResource) {
        when (resource.state) {
            DownloadResource.START -> {
                // 开启一个下载任务，刷新页面
                downloadTasks.add(0, resource)
                notifyItemInserted(0)
            }
            DownloadResource.SUSPEND, DownloadResource.WAITING, DownloadResource.DOWNLOADING, DownloadResource.MERGE, DownloadResource.FAILED -> {
                // 正在下载
                notifyItemChanged(resource)
            }
            DownloadResource.COMPLETE -> {
                notifyItemChanged(resource)
            }
        }
    }

    fun sort() {
        downloadTasks.sortByDescending {
            it.state
        }
    }

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        val mName = v.find<TextView>(R.id.mName)
        val mSpeed = v.find<TextView>(R.id.speed)
        val mProgress = v.find<TextView>(R.id.mProgress)
        val mProgressBar = v.find<ProgressBar>(R.id.mProgressBar)
        val mDownBtn = v.find<Button>(R.id.downBtn)
    }
}