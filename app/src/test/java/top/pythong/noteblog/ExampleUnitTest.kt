package top.pythong.noteblog

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import org.junit.Test

import org.junit.Assert.*
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.app.main.service.IMainService
import top.pythong.noteblog.app.main.ui.MainActivity
import top.pythong.noteblog.app.main.ui.MainViewModel
import top.pythong.noteblog.data.PageInfoType
import top.pythong.noteblog.data.RestEntity
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.RestResponseType
import top.pythong.noteblog.data.constant.Api
import top.pythong.noteblog.utils.HttpHelper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KClass

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
     * 反序列化测试
     */
    @Test
    fun gson() {
        val json = "{\n" +
                "    \"status\": 200,\n" +
                "    \"msg\": \"成功\",\n" +
                "    \"data\": {\n" +
                "        \"pageNum\": 1,\n" +
                "        \"pageSize\": 12,\n" +
                "        \"size\": 2,\n" +
                "        \"startRow\": 1,\n" +
                "        \"endRow\": 2,\n" +
                "        \"total\": 2,\n" +
                "        \"pages\": 1,\n" +
                "        \"list\": [\n" +
                "            {\n" +
                "                \"id\": 15,\n" +
                "                \"title\": \"新创建\",\n" +
                "                \"alias\": null,\n" +
                "                \"author\": \"admin\",\n" +
                "                \"types\": [\n" +
                "                    {\n" +
                "                        \"name\": \"defult\",\n" +
                "                        \"type\": \"category\",\n" +
                "                        \"created\": \"2019-07-19 15:55:59\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": \"安卓开发\",\n" +
                "                        \"type\": \"category\",\n" +
                "                        \"created\": \"2019-07-22 13:38:53\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": \"哈哈\",\n" +
                "                        \"type\": \"tag\",\n" +
                "                        \"created\": \"2019-08-20 11:12:03\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"info\": \"# 你好\",\n" +
                "                \"hits\": 0,\n" +
                "                \"modified\": \"2019-08-20 13:52:52\",\n" +
                "                \"created\": \"2019-08-20 13:52:52\",\n" +
                "                \"status\": \"publish\",\n" +
                "                \"content\": null\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 9,\n" +
                "                \"title\": \"参数校验1\",\n" +
                "                \"alias\": \"b\",\n" +
                "                \"author\": \"admin\",\n" +
                "                \"types\": [\n" +
                "                    {\n" +
                "                        \"name\": \"安卓开发\",\n" +
                "                        \"type\": \"category\",\n" +
                "                        \"created\": \"2019-07-22 13:38:53\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": \"后端\",\n" +
                "                        \"type\": \"category\",\n" +
                "                        \"created\": \"2019-07-22 14:14:43\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": \"参数校验1\",\n" +
                "                        \"type\": \"tag\",\n" +
                "                        \"created\": \"2019-08-06 10:31:30\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\": \"参数校验2\",\n" +
                "                        \"type\": \"tag\",\n" +
                "                        \"created\": \"2019-08-13 16:51:18\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"info\": \"#Hello Wolrd!\",\n" +
                "                \"hits\": 33,\n" +
                "                \"modified\": \"2019-08-13 16:56:30\",\n" +
                "                \"created\": \"2019-08-05 14:41:21\",\n" +
                "                \"status\": \"publish\",\n" +
                "                \"content\": null\n" +
                "            }\n" +
                "        ],\n" +
                "        \"prePage\": 0,\n" +
                "        \"nextPage\": 0,\n" +
                "        \"isFirstPage\": true,\n" +
                "        \"isLastPage\": true,\n" +
                "        \"hasPreviousPage\": false,\n" +
                "        \"hasNextPage\": false,\n" +
                "        \"navigatePages\": 8,\n" +
                "        \"navigatepageNums\": [\n" +
                "            1\n" +
                "        ],\n" +
                "        \"navigateFirstPage\": 1,\n" +
                "        \"navigateLastPage\": 1,\n" +
                "        \"lastPage\": 1,\n" +
                "        \"firstPage\": 1\n" +
                "    },\n" +
                "    \"timestamp\": 1566811939,\n" +
                "    \"ok\": true\n" +
                "}"

        val infoType = PageInfoType(Article::class.java)
        val restResponseType = RestResponseType(infoType)

        val restResponse: RestResponse<PageInfo<Article>> = jsonP(json)

        println(restResponse.data!!.list!![0].title)


    }

    private fun <T : Any, B : Any> forRestEntityP(
        kClass: KClass<T>,
        method: String,
        json: String,
        josnPe: (json: String) -> RestResponse<B>
    ): RestResponse<B> {

        return josnPe(json)
    }

    fun <T> jsonP(json: String): RestResponse<PageInfo<T>> {
        val restResponse: RestResponse<PageInfo<T>> = forRestEntityP(Article::class, "GET", json = json) {
            val infoType = PageInfoType(Article::class.java)
            val restResponseType = RestResponseType(infoType)
            val restResponse: RestResponse<PageInfo<T>> = Gson().fromJson(json, restResponseType)
            restResponse
        }
        return restResponse
    }

    @Test
    fun httpHelper() {
        val restEntity = HttpHelper(Activity())
            .apply {
                url = Api.login
                params {

                    "name" - "admin"
                    "password" - "123456"
                }
            }.postForEntity(LoggedInUser::class)
        println(restEntity.restResponse!!.data!!.email)
    }

    /**
     * viewModel 反射测试
     */
    @Test
    fun factory() {
//        reflex(MainViewModel::class.java).autoLogin()
    }

    fun <T> reflex(modelClass: Class<T>): T {

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

    /**
     * 距今时间
     */
    @Test
    fun date() {
        val created = "2018-09-29 14:a:21"
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = sdf.parse(created)!!
        // 时间差 s
        val diff = (Date().time - date.time) / 1000

        val ass = when (diff) {
            // 0~59s
            in 0 until 60 -> "刚刚"
            // 1~60min
            in 60 until 60 * 60 -> "${diff / 60}分钟前"
            // 1~24h
            in 60 * 60 until 60 * 60 * 24 -> "${diff / (60 * 60)}小时前"
            // 1~30天
            in 60 * 60 * 24 until 60 * 60 * 24 * 30 -> "${diff / (60 * 60 * 24)}天前"
            // 1~12月
            in 60 * 60 * 24 * 30 until 60 * 60 * 24 * 30 * 12 -> "${diff / (60 * 60 * 24 * 30)}月前"
            else -> "${diff / (60 * 60 * 24 * 30 * 12)}年前"
        }

        println(ass)
    }

    @Test
    fun string() {
        ",".split(",1").forEach {
            if (it.isBlank()) {
                return@forEach
            }
            println(1)
        }
    }
}
