package top.pythong.noteblog.app.home.dao.impl

import android.content.Context
import top.pythong.noteblog.app.home.dao.IHomeDataSource
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.constant.Api
import top.pythong.noteblog.utils.HttpHelper

/**
 *
 * @author ChangJiahong
 * @date 2019/8/26
 */
class HomeDataSourceImpl(val context: Context) : IHomeDataSource {

    /**
     * 获得推荐文章
     */
    override fun getArticles(page: Int, size: Int): RestResponse<PageInfo<Article>> =
        HttpHelper(context).apply {
            url = Api.article
            params {
                "page" - page.toString()
                "size" - size.toString()
            }
        }.getForRestResponsePage(Article::class)
}