package top.pythong.noteblog.data

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *
 * @author ChangJiahong
 * @date 2019/9/6
 */
class ListType(val type: Type) : ParameterizedType {

    override fun getRawType(): Type {
        return List::class.java
    }

    override fun getOwnerType(): Type? {
        return null
    }

    override fun getActualTypeArguments(): Array<Type> {
        return arrayOf(type)
    }
}