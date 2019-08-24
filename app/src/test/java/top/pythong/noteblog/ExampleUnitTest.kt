package top.pythong.noteblog

import android.content.Context
import org.junit.Test

import org.junit.Assert.*
import top.pythong.noteblog.app.main.service.IMainService
import top.pythong.noteblog.app.main.ui.MainActivity
import top.pythong.noteblog.app.main.ui.MainViewModel

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    /**
     * viewModel 反射测试
     */
    @Test
    fun factory(){
        reflex(MainViewModel::class.java).autoLogin()
    }

    fun <T> reflex(modelClass: Class<T>): T{

        val modelSimpleName = modelClass.name
//        println(modelSimpleName)
        val modelName = modelSimpleName.substringAfterLast(".")
//        println(modelName)
        val packageName = modelSimpleName.substringBeforeLast(".ui")
//        println(packageName)
        val name = modelName.substringBefore("ViewModel")
//        println(name)
        val serviceName = "$packageName.service.impl.${name}ServiceImpl"
        val iServiceName = "$packageName.service.I${name}Service"
//        println(serviceName)
        val dataSourceName = "$packageName.dao.impl.${name}DataSourceImpl"
        val iDataSourceName = "$packageName.dao.I${name}DataSource"
//        println(dataSourceName)

        val constructor = Class.forName(dataSourceName).getConstructor()
        val dataSourceClass = Class.forName(dataSourceName)
        val dataSource = constructor.newInstance()

        val serviceClass = Class.forName(serviceName)
        val serviceConstructor = serviceClass.getConstructor(Context::class.java, Class.forName(iDataSourceName))
        val service = serviceConstructor.newInstance(MainActivity(), dataSource)

        val modelConstructor = modelClass.getConstructor(Class.forName(iServiceName))
        val model = modelConstructor.newInstance(service)

        return model




    }
}
