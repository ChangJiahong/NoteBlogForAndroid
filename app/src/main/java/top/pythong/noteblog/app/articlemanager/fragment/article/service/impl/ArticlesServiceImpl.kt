package top.pythong.noteblog.app.articlemanager.fragment.article.service.impl

import android.content.Context
import top.pythong.noteblog.app.articlemanager.fragment.article.dao.IArticlesDataSource
import top.pythong.noteblog.app.articlemanager.fragment.article.service.IArticlesService
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.Result

/**
 *
 * @author ChangJiahong
 * @date 2019/9/24
 */
class ArticlesServiceImpl(private val context: Context, private val articlesDataSource: IArticlesDataSource) :
    IArticlesService {

    override fun getUserArticleList(page: Int, size: Int): Result<PageInfo<Article>> {
        val response: RestResponse<PageInfo<Article>> = articlesDataSource.getUserArticleList(page, size)
        if (response.isOk()){
            return Result.ok(response)
        }
        return Result.fail(response)
    }
}