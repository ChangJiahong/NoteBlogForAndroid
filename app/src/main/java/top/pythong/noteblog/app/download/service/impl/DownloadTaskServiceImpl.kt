package top.pythong.noteblog.app.download.service.impl

import android.content.Context
import top.pythong.noteblog.app.download.dao.IDownloadTaskDataSource
import top.pythong.noteblog.app.download.model.DownloadResource
import top.pythong.noteblog.app.download.service.IDownloadTaskService
import top.pythong.noteblog.utils.getLongFromSharedPreferences

/**
 *
 * @author ChangJiahong
 * @date 2019/9/10
 */
class DownloadTaskServiceImpl(
    private val context: Context,
    private val downloadTaskDataSource: IDownloadTaskDataSource
) : IDownloadTaskService {

    /**
     * 保存下载任务
     */
    override fun saveDownloadResource(resource: DownloadResource, state: Int) {
        val res = selectById(resource)
        if (res == null) {
            downloadTaskDataSource.insert(resource, state)

        } else {
            downloadTaskDataSource.update(resource, state)
        }
    }

    override fun selectById(resource: DownloadResource): DownloadResource? {
        return downloadTaskDataSource.selectById(resource.id)
    }

    override fun selectByUrl(url: String): List<DownloadResource> {
        val downloadResource = DownloadResource("", url, "")
        return downloadTaskDataSource.selectNotCompleteByFileId(downloadResource)
    }

    /**
     * 获取所有任务
     */
    override fun getTasks(): List<DownloadResource> {
        val tasks = downloadTaskDataSource.selectAll()
        tasks.forEach {
            it.downloadLen = context.getLongFromSharedPreferences("downLoad-${it.name}-dLen", 0L)
            it.contentLen = context.getLongFromSharedPreferences("downLoad-${it.name}-cLen", 0L)
        }
        return tasks
    }

    override fun removeTask(resource: DownloadResource) {
        if (selectById(resource) != null) {
            downloadTaskDataSource.delete(resource)
        }
    }
}