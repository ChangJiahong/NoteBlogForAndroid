package top.pythong.noteblog.app.home.dao

import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.RestResponse

interface IHomeDataSource {
    fun getArticles(page: Int, size: Int): RestResponse<PageInfo<Article>>

}
