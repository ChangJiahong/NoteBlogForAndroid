package top.pythong.noteblog.app.type.dao.impl

import android.content.Context
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.Type
import top.pythong.noteblog.app.type.dao.ITypeDataSource
import top.pythong.noteblog.data.constant.Api
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.utils.HttpHelper

/**
 *
 * @author ChangJiahong
 * @date 2019/9/1
 */
class TypeDataSourceImpl(private val context: Context) : ITypeDataSource {

    override fun getArticlesByType(
        page: String,
        size: String,
        type: String,
        typeName: String
    ) = HttpHelper(context).apply {

        url = when (type) {
            Type.CATEGORY -> Api.category(typeName)
            Type.TAG -> Api.tag(typeName)
            else -> ""
        }

        params {
            Constant.PAGE - page
            Constant.SIZE - size
        }

    }.getForRestResponsePage(Article::class)
}