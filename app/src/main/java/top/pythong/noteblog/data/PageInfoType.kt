package top.pythong.noteblog.data

import top.pythong.noteblog.app.home.model.PageInfo
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass

/**
 * pageInfo 反序列化type
 * @author ChangJiahong
 * @date 2019/8/26
 */
class PageInfoType(val type: Type) : ParameterizedType {
    override fun getRawType(): Type {
        return PageInfo::class.java
    }

    override fun getOwnerType(): Type? {
        return null
    }

    override fun getActualTypeArguments(): Array<Type> {
        return arrayOf(type)
    }
}