package top.pythong.noteblog.app.archives.dao

import top.pythong.noteblog.app.archives.model.Archive
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.RestResponse

interface IArchivesDataSource {
    fun getArchives(page: String, size: String): RestResponse<PageInfo<Archive>>

}
