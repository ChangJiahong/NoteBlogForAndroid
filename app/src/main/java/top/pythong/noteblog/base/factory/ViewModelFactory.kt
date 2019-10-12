package top.pythong.noteblog.base.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import kotlin.reflect.KClass

/**
 *
 * @author ChangJiahong
 * @date 2019/8/24
 */
object ViewModelFactory {

    fun <T : ViewModel> createViewModel(activity: FragmentActivity, clazz: KClass<T>): T {
        return ViewModelProviders.of(
            activity,
            SimpleFactory(activity)
        ).get(clazz.java)
    }

    fun <T : ViewModel> createViewModel(fragment: Fragment, clazz: KClass<T>): T {
        return ViewModelProviders.of(
            fragment,
            SimpleFactory(fragment.context!!)
        ).get(clazz.java)
    }

    fun <T : ViewModel> createViewModelWithContext(activity: FragmentActivity, clazz: KClass<T>): T {
        return ViewModelProviders.of(
            activity,
            WithContextFactory(activity)
        ).get(clazz.java)
    }

    fun <T : ViewModel> createViewModelWithContext(fragment: Fragment, clazz: KClass<T>): T {
        return ViewModelProviders.of(
            fragment,
            WithContextFactory(fragment.context!!)
        ).get(clazz.java)
    }

    private class SimpleFactory(val context: Context) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            // model类全名
            val modelSimpleName = modelClass.name
            // model类简单类名
            val modelName = modelSimpleName.substringAfterLast(".")
            // 所在包名
            val packageName = modelSimpleName.substringBeforeLast(".ui")
            // 模块名
            val name = modelName.substringBefore("ViewModel")
            // 拼接服务实现类名
            val serviceName = "$packageName.service.impl.${name}ServiceImpl"
            // 拼接服务接口类名
            val iServiceName = "$packageName.service.I${name}Service"
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
            // 数据源接口类对象
            val iServiceClass = Class.forName(iServiceName)
            // 数据源对象
            val service = Class.forName(serviceName)
                .getConstructor(Context::class.java, iDataSourceClass)
                .newInstance(context, dataSource)

            return modelClass.getConstructor(iServiceClass).newInstance(service)
        }
    }

    private class WithContextFactory(val context: Context) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            // model类全名
            val modelSimpleName = modelClass.name
            // model类简单类名
            val modelName = modelSimpleName.substringAfterLast(".")
            // 所在包名
            val packageName = modelSimpleName.substringBeforeLast(".ui")
            // 模块名
            val name = modelName.substringBefore("ViewModel")
            // 拼接服务实现类名
            val serviceName = "$packageName.service.impl.${name}ServiceImpl"
            // 拼接服务接口类名
            val iServiceName = "$packageName.service.I${name}Service"
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
            // 数据源接口类对象
            val iServiceClass = Class.forName(iServiceName)
            // 数据源对象
            val service = Class.forName(serviceName)
                .getConstructor(Context::class.java, iDataSourceClass)
                .newInstance(context, dataSource)

            return modelClass.getConstructor(Context::class.java, iServiceClass)
                .newInstance(context, service)
        }
    }


}