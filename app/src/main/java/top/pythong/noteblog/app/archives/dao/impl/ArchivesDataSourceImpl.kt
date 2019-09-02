package top.pythong.noteblog.app.archives.dao.impl

import android.content.Context
import top.pythong.noteblog.app.archives.dao.IArchivesDataSource
import top.pythong.noteblog.app.archives.model.Archive
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.constant.Api
import top.pythong.noteblog.utils.HttpHelper

/**
 *
 * @author ChangJiahong
 * @date 2019/9/2
 */
class ArchivesDataSourceImpl(private val context: Context): IArchivesDataSource {
    override fun getArchives(page: String, size: String) = HttpHelper(context).apply {
        url = Api.archives
        params {
            "page" - page
            "size" - size
        }
    }.getForRestResponsePage(Archive::class)
}