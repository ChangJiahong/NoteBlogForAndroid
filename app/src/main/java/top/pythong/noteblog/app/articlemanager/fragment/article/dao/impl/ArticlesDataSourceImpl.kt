package top.pythong.noteblog.app.articlemanager.fragment.article.dao.impl

import android.content.Context
import top.pythong.noteblog.app.articlemanager.fragment.article.dao.IArticlesDataSource
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.constant.Api
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.utils.HttpHelper
import top.pythong.noteblog.utils.getStringFromSharedPreferences

/**
 *
 * @author ChangJiahong
 * @date 2019/9/24
 */
class ArticlesDataSourceImpl(private val context: Context) : IArticlesDataSource {

    override fun getUserArticleList(page: Int, size: Int) = HttpHelper(context).apply {
        val token = context.getStringFromSharedPreferences(Constant.TOKEN)
        url = Api.ulist
        headers {
            Constant.TOKEN - token
        }
        params {
            Constant.PAGE - page.toString()
            Constant.SIZE - size.toString()
        }
    }.getForRestResponsePage(Article::class)

}