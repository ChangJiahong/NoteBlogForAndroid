package top.pythong.noteblog.app.download.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import top.pythong.noteblog.app.download.model.DownloadResource
import top.pythong.noteblog.app.download.service.IDownloadTaskService
import top.pythong.noteblog.base.viewModel.BaseViewModel

/**
 *
 * @author ChangJiahong
 * @date 2019/9/10
 */
class DownloadTaskViewModel(private val downloadTaskService: IDownloadTaskService) : BaseViewModel() {

    private val _downloadTasks = MutableLiveData<ArrayList<DownloadResource>>()
    val downloadTasks: LiveData<ArrayList<DownloadResource>> = _downloadTasks

    fun putTask(resource: DownloadResource, state: Int) {
        downloadTaskService.saveDownloadResource(resource, state)
    }

    fun getTasks(): List<DownloadResource> {
        return downloadTaskService.getTasks()
    }


    fun removeTask(resource: DownloadResource) {
        downloadTaskService.removeTask(resource)
    }


}