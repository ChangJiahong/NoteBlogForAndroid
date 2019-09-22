package top.pythong.noteblog.app.userinfo.service.impl

import android.content.Context
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.app.userinfo.dao.IUserProfileDataSource
import top.pythong.noteblog.app.userinfo.service.IUserProfileService
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.Result

/**
 *
 * @author ChangJiahong
 * @date 2019/9/22
 */
class UserProfileServiceImpl(private val context: Context, private val userProfileDataSource: IUserProfileDataSource) : IUserProfileService {

    override fun getArticles(username: String, page: Int, size: Int): Result<PageInfo<Article>> {
        val restResponse: RestResponse<PageInfo<Article>> = userProfileDataSource.getArticles(username, page, size)

        if (restResponse.isOk()){
            val pageInfo = restResponse.data!!
            return Result.ok(pageInfo)
        }
        return Result.fail(restResponse)
    }

    override fun getUser(username: String): Result<LoggedInUser> {
        val restResponse: RestResponse<LoggedInUser> = userProfileDataSource.getUser(username)

        if (restResponse.isOk()){
            val pageInfo = restResponse.data!!
            return Result.ok(pageInfo)
        }
        return Result.fail(restResponse)
    }
}