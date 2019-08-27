package top.pythong.noteblog.app.home.service

import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.Result

/**
 *
 * @author ChangJiahong
 * @date 2019/8/26
 */
interface IHomeService {

    fun getArticles(page: Int, size: Int): Result<PageInfo<Article>>
}