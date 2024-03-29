package top.pythong.noteblog.data

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * gson 反序列化获取type
 * @author ChangJiahong
 * @date 2019/8/23
 */
class RestResponseType(val type: Type) : ParameterizedType {

    override fun getRawType(): Type {
        return RestResponse::class.java
    }

    override fun getOwnerType(): Type? {
        return null
    }

    override fun getActualTypeArguments(): Array<Type> {
        return arrayOf(type)
    }
}