package top.pythong.noteblog.utils

import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import top.pythong.noteblog.data.Pairs
import top.pythong.noteblog.data.RestEntity
import top.pythong.noteblog.data.RestResponse
import kotlin.reflect.KClass
import top.pythong.noteblog.data.RestResponseType
import java.io.IOException


/**
 *
 * @author ChangJiahong
 * @date 2019/8/23
 */
object HttpHelper {

    val TAG = "HttpHelper"

    private val mOkHttpClient by lazy {
        OkHttpClient()
    }

    var url = ""

    private var mHeaders: Headers.Builder = Headers.Builder()

    private var mParams: MutableMap<String, String> = HashMap()

    private var mRequestBody: RequestBody = FormBody.Builder().build()

    fun params(makePairs: Pairs<String>.() -> Unit) {
        val requestPairs = Pairs<String>()
        requestPairs.makePairs()
        mParams.putAll(requestPairs.pairs)
    }

    fun headers(makePairs: Pairs<String>.() -> Unit) {
        val headerPairs = Pairs<String>()
        headerPairs.makePairs()
        headerPairs.pairs.forEach {
            mHeaders.add(it.key, it.value)
        }
    }

    fun requestBody(builderBody: () -> RequestBody): HttpHelper {
        mRequestBody = builderBody()
        return this
    }

    private var er: (code: Int, msg: String) -> Unit = { code, msg ->
        Log.e("HttpHelper", "error code : $code, error msg : $msg")
    }

    fun error(e: (code: Int, msg: String) -> Unit): HttpHelper {
        er = e
        return this
    }

    fun <T : Any> postForRestResponse(kClass: KClass<T>): RestResponse<T> {
        try {
            val response = post()
            if (response.isSuccessful) {
                val json = response.body!!.string()
                val type = RestResponseType(kClass.java)
                return Gson().fromJson(json, type)
            }
            er(response.code, response.message)
            return RestResponse.fail(response.code, response.message)
        } catch (e: IOException) {
            // io错误
            return RestResponse.fail(0, "网络开小差了")
        }
    }

    fun <T : Any> postForEntity(kClass: KClass<T>): RestEntity<T> {
        try {
            val response = post()
            if (response.isSuccessful) {
                val json = response.body!!.string()
                Log.d(TAG, "[$url - post]: $json")
                val type = RestResponseType(kClass.java)
                val restResponse: RestResponse<T> = Gson().fromJson(json, type)
                Log.d(TAG, "json -> RestResponse: $restResponse")
                return RestEntity.ok(response, restResponse)
            }
            er(response.code, response.message)
            return RestEntity.fail(response)
        } catch (e: IOException) {
            // io错误
            return RestEntity.fail("网络开小差了")
        }
    }

    fun post(): Response {
        val okHttpClient = getClinet()
        val request = Request.Builder()
            .url(url)
            .headers(mHeaders.build())
            .post(mRequestBody)
            .build()

        val call = okHttpClient.newCall(request)
        return call.execute()
    }

    private fun getClinet(): OkHttpClient {
        return mOkHttpClient
    }


}