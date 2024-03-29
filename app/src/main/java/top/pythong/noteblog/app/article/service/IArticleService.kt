package top.pythong.noteblog.app.article.service

import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.data.Result

interface IArticleService {
    /**
     * 加载文章
     */
    fun getArticle(articleId: String): Result<Article>

    fun like(articleId: Int): Result<Any>

    fun comment(articleId: Int, comments: String): Result<Any>

}
