package top.pythong.noteblog.app.article.service.impl

import android.content.Context
import top.pythong.noteblog.app.article.dao.IArticleDataSource
import top.pythong.noteblog.app.article.service.IArticleService
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.Result

/**
 *
 * @author ChangJiahong
 * @date 2019/8/29
 */
class ArticleServiceImpl(val content: Context, val articleDataSource: IArticleDataSource) : IArticleService {
    /**
     * 加载文章
     */
    override fun getArticle(articleId: String): Result<Article> {
        val restResponse = articleDataSource.getArticle(articleId)
        if (restResponse.isOk()) {
            return Result.ok(restResponse)
        }
        return Result.fail(restResponse)
    }

    override fun like(articleId: Int): Result<Any> {
        val restResponse = articleDataSource.like(articleId.toString())
        if (restResponse.isOk()) {
            return Result.ok(restResponse)
        }
        return Result.fail(restResponse)
    }

    override fun comment(articleId: Int, comments: String): Result<Any> {
        val restResponse = articleDataSource.comment(articleId.toString())
        if (restResponse.isOk()) {
            return Result.ok(restResponse)
        }
        return Result.fail(restResponse)
    }
}