package top.pythong.noteblog.app.download.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.coroutines.*
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.find
import top.pythong.noteblog.R
import top.pythong.noteblog.app.download.DownloadService
import top.pythong.noteblog.app.download.model.DownloadResource
import top.pythong.noteblog.utils.getResourceFile
import top.pythong.noteblog.utils.openFileByThirdPartyApp

/**
 * 下载任务管理adapter
 * @author ChangJiahong
 * @date 2019/9/10
 */
class DownloadTaskAdpater(val downloadTasks: ArrayList<DownloadResource>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = DownloadTaskAdpater::class.java.simpleName

    val selected = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View
        if (viewType == DownloadResource.COMPLETE) {
            v = LayoutInflater.from(parent.context).inflate(R.layout.download_task_item_complete, null)
            return CompleteViewHolder(v)
        }
        v = LayoutInflater.from(parent.context).inflate(R.layout.download_task_item, null)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return downloadTasks.size
    }

    override fun getItemViewType(position: Int): Int {
        return downloadTasks[position].state
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val item = downloadTasks[position]

        if (selected.contains(item.id)) {
            viewHolder.itemView.backgroundColorResource = R.color.card_pressed
        } else {
            viewHolder.itemView.backgroundResource = R.drawable.defult_click_bg_ripple
        }

        if (item.state == DownloadResource.COMPLETE) {
            viewHolder as CompleteViewHolder

            viewHolder.apply {
                mName.text = item.name
                v.setOnClickListener {
                    val file = context.getResourceFile(item)
                    context.openFileByThirdPartyApp(file, item.type)
                }
            }
            return
        }

        viewHolder as ViewHolder
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

    private fun sort() {
        downloadTasks.sortByDescending {
            it.state
        }
    }

    fun notifyData() {
        sort()
        notifyDataSetChanged()
    }

    fun selected(selected: List<DownloadResource>, delay: Long) = GlobalScope.launch(Dispatchers.Main) {
        selected(selected)
        val job = async(Dispatchers.IO) {
            delay(delay)
        }
        job.await()
        unSelected()
    }


    fun selected(selected: List<DownloadResource>) {
        selected.forEach {
            this.selected.add(it.id)
        }
        this.notifyData()
    }

    fun unSelected() {
        this.selected.clear()
        this.notifyData()
    }

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        val mName = v.find<TextView>(R.id.mName)
        val mSpeed = v.find<TextView>(R.id.speed)
        val mProgress = v.find<TextView>(R.id.mProgress)
        val mProgressBar = v.find<ProgressBar>(R.id.mProgressBar)
        val mDownBtn = v.find<Button>(R.id.downBtn)
    }

    class CompleteViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        val context: Context = v.context
        val mName = v.find<TextView>(R.id.mName)
    }
}