package top.pythong.noteblog.app.articlemanager.fragment.category.dao

import top.pythong.noteblog.app.archives.model.Archive
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.RestResponse

interface ICategoryDataSource {
    fun getUCategoryArchives(page: String, size: String): RestResponse<PageInfo<Archive>>

}
