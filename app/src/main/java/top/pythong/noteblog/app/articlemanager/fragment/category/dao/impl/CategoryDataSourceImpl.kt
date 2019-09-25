package top.pythong.noteblog.app.articlemanager.fragment.category.dao.impl

import android.content.Context
import top.pythong.noteblog.app.archives.model.Archive
import top.pythong.noteblog.app.articlemanager.fragment.category.dao.ICategoryDataSource
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.constant.Api
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.utils.HttpHelper
import top.pythong.noteblog.utils.getStringFromSharedPreferences

/**
 *
 * @author ChangJiahong
 * @date 2019/9/25
 */
class CategoryDataSourceImpl(private val context: Context) : ICategoryDataSource {

    override fun getUCategoryArchives(page: String, size: String) = HttpHelper(context).apply {
        val token = context.getStringFromSharedPreferences(Constant.TOKEN)
        url = Api.uCategoryArchives
        headers {
            Constant.TOKEN - token
        }
        params {
            Constant.PAGE - page
            Constant.SIZE - size
        }
    }.getForRestResponsePage(Archive::class)
}