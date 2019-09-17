package top.pythong.noteblog.app.download.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import top.pythong.noteblog.R
import top.pythong.noteblog.app.download.model.DownloadResource

/**
 * 下载任务管理adapter
 * @author ChangJiahong
 * @date 2019/9/10
 */
class DownloadTaskAdpater (val downloadTasks: ArrayList<DownloadResource>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val TAG = DownloadTaskAdpater::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.download_task_item, null)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return downloadTasks.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, postison: Int) {
        Log.d(TAG, downloadTasks[postison].toString())
    }

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v){

    }
}