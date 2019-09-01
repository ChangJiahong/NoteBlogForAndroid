package top.pythong.noteblog.app.type.service.impl

import android.content.Context
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.app.home.model.Type
import top.pythong.noteblog.app.type.dao.ITypeDataSource
import top.pythong.noteblog.app.type.service.ITypeService
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.Result
import top.pythong.noteblog.data.constant.MsgCode

/**
 *
 * @author ChangJiahong
 * @date 2019/9/1
 */
class TypeServiceImpl(private val context: Context, private val typeDataSource: ITypeDataSource) : ITypeService {

    /**
     * 根据分类 获取文章
     */
    override fun getArticlesByType(page: Int, size: Int, type: String, typeName: String): Result<PageInfo<Article>> {
        if (type.isBlank() || typeName.isBlank() || (Type.TAG != type && Type.CATEGORY != type)){
            return Result.fail(MsgCode.ParameterIsNull)
        }

        val restResponse: RestResponse<PageInfo<Article>> =
            typeDataSource.getArticlesByType(page.toString(), size.toString(), type, typeName)

        if (restResponse.isOk()){
            return Result.ok(restResponse)
        }
        return Result.fail(restResponse)
    }
}