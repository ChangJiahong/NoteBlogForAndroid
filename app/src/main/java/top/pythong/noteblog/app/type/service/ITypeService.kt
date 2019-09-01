package top.pythong.noteblog.app.type.service

import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.Result

interface ITypeService {

    /**
     * 根据分类 获取文章
     */
    fun getArticlesByType(page: Int, size: Int, type: String, typeName: String): Result<PageInfo<Article>>

}
