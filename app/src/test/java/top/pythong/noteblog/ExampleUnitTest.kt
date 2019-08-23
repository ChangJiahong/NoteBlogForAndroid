package top.pythong.noteblog

import com.google.gson.Gson
import org.junit.Test

import org.junit.Assert.*
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.RestResponseType
import top.pythong.noteblog.app.login.model.LoggedInUser
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
        var json = "{\n" +
                "    \"status\": 201,\n" +
                "    \"msg\": \"你的账户已登录!不用重新登录！\",\n" +
                "    \"data\": {\n" +
                "        \"uid\": 1,\n" +
                "        \"username\": \"admin\",\n" +
                "        \"email\": \"2327085154@qq.com\",\n" +
                "        \"imgUrl\": \"https://avatars1.githubusercontent.com/u/24603481?s=400&u=4523152ed09d679775b3175e1581f05357d2b3b4&v=4\",\n" +
                "        \"sex\": true,\n" +
                "        \"age\": 22,\n" +
                "        \"created\": \"2019-07-17 10:28:18\",\n" +
                "        \"roles\": [\n" +
                "            \"user\"\n" +
                "        ]\n" +
                "    },\n" +
                "    \"timestamp\": 1566542112,\n" +
                "    \"ok\": true\n" +
                "}"
        var restResponse = restResponse(json, LoggedInUser::class)
//        val type = object : TypeToken<RestResponse<LoggedInUser>>() {}.type
//
//        println(type.toString())
//        var restResponse: RestResponse<LoggedInUser> = Gson().fromJson(json, type)
//
        println(restResponse.data.created)


    }

    private inline fun <reified T : Any> restResponse(json: String, kClass: KClass<T>): RestResponse<T>{
        println(T::class.java.simpleName)
        val type = RestResponseType(kClass.java)
        return Gson().fromJson(json, type)
    }
}
