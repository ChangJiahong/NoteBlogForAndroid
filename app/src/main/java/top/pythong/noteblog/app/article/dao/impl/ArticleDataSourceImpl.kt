package top.pythong.noteblog.app.article.dao.impl

import android.content.Context
import top.pythong.noteblog.app.article.dao.IArticleDataSource
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.data.constant.Api
import top.pythong.noteblog.data.constant.Constant.ARTICLE_TYPE
import top.pythong.noteblog.utils.HttpHelper

/**
 *
 * @author ChangJiahong
 * @date 2019/8/29
 */
class ArticleDataSourceImpl(val context: Context) : IArticleDataSource {

    /**
     * 获取文章
     */
    override fun getArticle(articleId: String) = HttpHelper(context).apply {

        url = Api.browseArticle(articleId)
        headers {
            ARTICLE_TYPE - Article.HTML
        }

    }.getForRestResponse(Article::class)
}