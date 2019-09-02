package top.pythong.noteblog.app.home.service.impl

import android.content.Context
import top.pythong.noteblog.app.home.dao.IHomeDataSource
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.app.home.service.IHomeService
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.Result
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.utils.getStringFromSharedPreferences

/**
 *
 * @author ChangJiahong
 * @date 2019/8/26
 */
class HomeServiceImpl(val context: Context, val homeDataSource: IHomeDataSource) : IHomeService {

    override fun getArticles(page: Int, size: Int): Result<PageInfo<Article>> {

        val restResponse = homeDataSource.getArticles(page, size)
        if (restResponse.isOk()) {

            val pageInfo = restResponse.data!!

            return Result.ok(pageInfo)
        }

        return Result.fail(restResponse)
    }
}