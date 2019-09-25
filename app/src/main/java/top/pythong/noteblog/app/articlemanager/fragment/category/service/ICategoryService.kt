package top.pythong.noteblog.app.articlemanager.fragment.category.service

import top.pythong.noteblog.app.archives.model.Archive
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.Result

interface ICategoryService {

    fun getUCategoryArchives(page: Int, size: Int): Result<PageInfo<Archive>>

}
