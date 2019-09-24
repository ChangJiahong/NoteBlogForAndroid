package top.pythong.noteblog.app.articlemanager.fragment.article.service

import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.Result

interface IArticlesService {

    fun getUserArticleList(page: Int, size: Int): Result<PageInfo<Article>>

}
