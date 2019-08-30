package top.pythong.noteblog.app.article.dao

import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.data.RestResponse

interface IArticleDataSource {
    /**
     * 获取文章
     */
    fun getArticle(articleId: String): RestResponse<Article>

}
