package top.pythong.noteblog.app.download.dao

import top.pythong.noteblog.app.download.model.DownloadResource

interface IDownloadTaskDataSource {
    /**
     * 插入下载任务
     */
    fun insert(resource: DownloadResource, state: Int)

    fun selectNotCompleteByFileId(resource: DownloadResource): List<DownloadResource>

    fun selectById(id: Int): DownloadResource?

    fun select(resource: DownloadResource): List<DownloadResource>

    /**
     * 查询全部
     */
    fun selectAll(): List<DownloadResource>

    fun delete(resource: DownloadResource)

    fun update(resource: DownloadResource, state: Int)
}
