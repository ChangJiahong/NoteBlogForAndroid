package top.pythong.noteblog.app.type.dao

import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.RestResponse

interface ITypeDataSource {


    fun getArticlesByType(page: String, size: String, type: String, typeName: String): RestResponse<PageInfo<Article>>

}
