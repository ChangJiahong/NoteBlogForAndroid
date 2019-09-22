package top.pythong.noteblog.app.userinfo.dao

import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.data.RestResponse

interface IUserProfileDataSource {
    fun getArticles(username: String, page: Int, size: Int): RestResponse<PageInfo<Article>>
    fun getUser(username: String): RestResponse<LoggedInUser>

}
