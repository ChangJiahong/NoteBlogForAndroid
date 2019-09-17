package top.pythong.noteblog.base.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import kotlin.reflect.KClass

/**
 *
 * @author ChangJiahong
 * @date 2019/9/16
 */
object ServiceFactory {

    fun <T : Any> get(factory: Factory, clazz: KClass<T>): T {
        return factory.create(clazz.java)
    }

    fun <T : Any> getSimpleService(context: Context, clazz: KClass<T>): T {
        return SimpleFactory(context).create(clazz.java)
    }


    class SimpleFactory(val context: Context) : Factory {

        override fun <T> create(iServiceClass: Class<T>): T {
            // service接口类全名
            val serviceSimpleName = iServiceClass.name
            // target类简单类名
            val targetName = serviceSimpleName.substringAfterLast(".")
            // 所在包名
            val packageName = serviceSimpleName.substringBeforeLast(".service")
            // 模块名
            val name = targetName.substringBefore("Service").substringAfter("I")
            // 拼接服务实现类名
            val serviceName = "$packageName.service.impl.${name}ServiceImpl"
            // 拼接数据源实现类名
            val dataSourceName = "$packageName.dao.impl.${name}DataSourceImpl"
            // 拼接数据源接口类名
            val iDataSourceName = "$packageName.dao.I${name}DataSource"
            // 数据源接口类对象
            val iDataSourceClass = Class.forName(iDataSourceName)
            // 数据源对象
            val dataSource = Class.forName(dataSourceName)
                .getConstructor(Context::class.java)
                .newInstance(context)

            val serviceClass = Class.forName(serviceName)

            return serviceClass
                .getConstructor(Context::class.java, iDataSourceClass)
                .newInstance(context, dataSource) as T
        }
    }

    interface Factory {
        fun <T> create(iServiceClass: Class<T>): T
    }
}