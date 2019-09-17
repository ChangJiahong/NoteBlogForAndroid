package top.pythong.noteblog.app.download.service

import top.pythong.noteblog.app.download.model.DownloadResource


interface IDownloadTaskService {

    /**
     * 保存下载任务
     */
    fun saveDownloadResource(resource: DownloadResource, state: Int)

    fun selectById(resource: DownloadResource): DownloadResource?

    fun selectByUrl(url: String): List<DownloadResource>

    /**
     * 获取所有任务
     */
    fun getTasks(): List<DownloadResource>


    fun removeTask(resource: DownloadResource)

}
