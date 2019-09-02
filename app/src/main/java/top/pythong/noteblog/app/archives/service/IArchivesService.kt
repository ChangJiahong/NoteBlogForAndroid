package top.pythong.noteblog.app.archives.service

import top.pythong.noteblog.app.archives.model.Archive
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.Result

interface IArchivesService {

    fun getArchives(page: Int, size: Int): Result<PageInfo<Archive>>

}
