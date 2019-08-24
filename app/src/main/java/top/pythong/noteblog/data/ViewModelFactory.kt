package top.pythong.noteblog.data

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.FragmentActivity
import top.pythong.noteblog.app.main.ui.MainActivity

/**
 *
 * @author ChangJiahong
 * @date 2019/8/24
 */
object ViewModelFactory {

    fun <T : ViewModel> createViewModel(activity: FragmentActivity, clazz: Class<T>): T{
        return ViewModelProviders.of(
            activity,
            Factory(activity, clazz)
        ).get(clazz)
    }

    private class Factory<T: ViewModel>(val context: Context, clazz: Class<T>): ViewModelProvider.Factory{

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
            val iDataSourceClass =  Class.forName(iDataSourceName)
            // 数据源对象
            val dataSource = Class.forName(dataSourceName).getConstructor().newInstance()
            // 数据源接口类对象
            val iServiceClass = Class.forName(iServiceName)
            // 数据源对象
            val service = Class.forName(serviceName)
                .getConstructor(Context::class.java, iDataSourceClass)
                .newInstance(context, dataSource)

            return modelClass.getConstructor(iServiceClass).newInstance(service)
        }
    }


}