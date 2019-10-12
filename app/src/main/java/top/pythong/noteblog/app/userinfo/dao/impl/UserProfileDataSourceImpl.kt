package top.pythong.noteblog.app.userinfo.dao.impl

import android.content.Context
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.ArticleCardItem
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.app.userinfo.dao.IUserProfileDataSource
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.constant.Api
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.utils.HttpHelper

/**
 *
 * @author ChangJiahong
 * @date 2019/9/22
 */
class UserProfileDataSourceImpl(private val context: Context) : IUserProfileDataSource {
    override fun getArticles(username: String, page: Int, size: Int): RestResponse<PageInfo<Article>> =
        HttpHelper(context).apply {
            url = Api.list
            params {
                Constant.PAGE - page.toString()
                Constant.SIZE - size.toString()
                Constant.USERNAME - username
            }
        }.getForRestResponsePage(Article::class)

    override fun getUser(username: String): RestResponse<LoggedInUser> = HttpHelper(context).apply {
        url = Api.userInfo(username)
    }.getForRestResponse(LoggedInUser::class)
}