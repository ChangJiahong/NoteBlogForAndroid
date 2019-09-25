package top.pythong.noteblog.app.articlemanager.fragment.category.service.impl

import android.content.Context
import top.pythong.noteblog.app.archives.model.Archive
import top.pythong.noteblog.app.articlemanager.fragment.category.dao.ICategoryDataSource
import top.pythong.noteblog.app.articlemanager.fragment.category.service.ICategoryService
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.Result

/**
 *
 * @author ChangJiahong
 * @date 2019/9/25
 */
class CategoryServiceImpl(private val context: Context, private val categoryDataSource: ICategoryDataSource) :
    ICategoryService {

    override fun getUCategoryArchives(page: Int, size: Int): Result<PageInfo<Archive>> {
        val restResponse: RestResponse<PageInfo<Archive>> =
            categoryDataSource.getUCategoryArchives(page.toString(), size.toString())
        if (restResponse.isOk()) {
            return Result.ok(restResponse)
        }
        return Result.fail(restResponse)
    }
}