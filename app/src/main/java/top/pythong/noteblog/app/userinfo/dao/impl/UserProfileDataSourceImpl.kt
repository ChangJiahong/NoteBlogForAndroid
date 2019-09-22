package top.pythong.noteblog.app.userinfo.dao.impl

import android.content.Context
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.ArticleCardItem
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.app.userinfo.dao.IUserProfileDataSource
import top.pythong.noteblog.data.RestResponse

/**
 *
 * @author ChangJiahong
 * @date 2019/9/22
 */
class UserProfileDataSourceImpl(private val context: Context) : IUserProfileDataSource {
    override fun getArticles(username: String, page: Int, size: Int): RestResponse<PageInfo<Article>> {
        val pageInfo = PageInfo<Article>()
        pageInfo.nextPage = page + 1
        pageInfo.hasNextPage = true
        val articleList = ArrayList<Article>()
        for (i in 0..20) {
            articleList.add(
                Article(
                    0,
                    "文章i",
                    "",
                    "admin",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "2019-08-31 12:31:05",
                    "2019-08-31 12:31:05",
                    "",
                    "",
                    ""
                )
            )
        }
        pageInfo.list = articleList
        return RestResponse(true, 200, "", pageInfo)
    }

    override fun getUser(username: String): RestResponse<LoggedInUser> {
        val user = LoggedInUser(0, "admin", "", "", true, 0, "", ArrayList())
        return RestResponse(true, 200, "", user)
    }
}