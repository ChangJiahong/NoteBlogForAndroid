package top.pythong.noteblog.app.articlemanager.fragment.article.dao

import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.RestResponse

interface IArticlesDataSource {

    fun getUserArticleList(page: Int, size: Int): RestResponse<PageInfo<Article>>

}
