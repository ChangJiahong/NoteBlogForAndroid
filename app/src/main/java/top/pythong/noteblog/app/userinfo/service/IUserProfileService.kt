package top.pythong.noteblog.app.userinfo.service

import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.data.Result

interface IUserProfileService {
    fun getArticles(username: String, page: Int, size: Int): Result<PageInfo<Article>>
    fun getUser(username: String): Result<LoggedInUser>

}
