package top.pythong.noteblog.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import top.pythong.noteblog.app.filemanager.model.FileDir
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.*
import kotlin.reflect.KClass
import top.pythong.noteblog.data.constant.MsgCode
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.URLDecoder
import java.net.URLEncoder


/**
 * 网络请求助手
 * @author ChangJiahong
 * @date 2019/8/23
 */
class HttpHelper(val context: Context) {

    val TAG = "HttpHelper"

    private val GET = "GET"

    private val POST = "POST"

    private val mOkHttpClient by lazy {
        OkHttpClient()
    }

    private lateinit var mCall: Call

    var url = ""

    private var isStop = false

    private var mHeaders: Headers.Builder = Headers.Builder()

    private var mRequestBody: RequestBody = FormBody.Builder().build()

    private fun Map<String, String>.toQueryString(): String =
        this.map { "${it.key}=${URLEncoder.encode(it.value, "UTF-8")}" }.joinToString("&")

    fun params(makePairs: Pairs<String>.() -> Unit): HttpHelper {
        val requestPairs = Pairs<String>()
        requestPairs.makePairs()
        url += "?${requestPairs.pairs.toQueryString()}"
        return this
    }

    fun headers(makePairs: Pairs<String>.() -> Unit): HttpHelper {
        val headerPairs = Pairs<String>()
        headerPairs.makePairs()
        headerPairs.pairs.forEach {
            mHeaders.add(it.key, it.value)
        }
        return this
    }

    fun url(url: String): HttpHelper {
        this.url = url
        return this
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

    private fun getClinet(): OkHttpClient {
        return mOkHttpClient
    }

    fun cancel() {
        mCall.cancel()
    }

    fun stop() {
        isStop = true
    }

    fun post() = method(POST)

    fun get() = method(GET)

    /*****************************************/

    /**
     * GET方法Response 数组
     */
    fun <T : Any> getForRestResponseList(kClass: KClass<T>): RestResponse<List<T>> {
        return forRestResponseList(kClass, GET)
    }

    /**
     * GET方法Response 分页
     */
    fun <T : Any> getForRestResponsePage(kClass: KClass<T>): RestResponse<PageInfo<T>> {

        return forRestResponsePage(kClass, GET)
    }

    /**
     * GET方法Response
     */
    fun <T : Any> getForRestResponse(kClass: KClass<T>): RestResponse<T> {

        return forRestResponse(kClass, GET)
    }

    /**
     * GET方法Entity 分页
     */
    fun <T : Any> getForEntityPage(kClass: KClass<T>): RestEntity<PageInfo<T>> {

        return forRestEntityPage(kClass, GET)
    }

    /**
     * GET方法Entity
     */
    fun <T : Any> getForEntity(kClass: KClass<T>): RestEntity<T> {

        return forRestEntity(kClass, GET)
    }

    /*****************************************/

    /**
     * POST方法Response 分页
     */
    fun <T : Any> postForRestResponsePage(kClass: KClass<T>): RestResponse<PageInfo<T>> {
        return forRestResponsePage(kClass, POST)
    }

    /**
     * POST方法Response
     */
    fun <T : Any> postForRestResponse(kClass: KClass<T>): RestResponse<T> {
        return forRestResponse(kClass, POST)
    }

    /**
     * POST方法Entity
     */
    fun <T : Any> postForEntity(kClass: KClass<T>): RestEntity<T> {
        return forRestEntity(kClass, POST)
    }

    /**
     * POST方法Entity 分页
     */
    fun <T : Any> postForEntityPage(kClass: KClass<T>): RestEntity<PageInfo<T>> {
        return forRestEntityPage(kClass, POST)
    }

    /*****************************************/

    /**
     * 获取Response对象
     */
    private fun <T : Any> forRestResponse(kClass: KClass<T>, method: String): RestResponse<T> {
        val restEntity = forRestEntity(kClass, method)
        if (restEntity.isSuccessful) {
            return restEntity.restResponse!!
        }
        return RestResponse.fail(restEntity.code, restEntity.msg)
    }

    /**
     * 获取Response对象 分页
     */
    private fun <T : Any> forRestResponsePage(kClass: KClass<T>, method: String): RestResponse<PageInfo<T>> {
        val restEntity = forRestEntityPage(kClass, method)
        if (restEntity.isSuccessful) {
            return restEntity.restResponse!!
        }
        return restEntity.toFailRestResponse()
    }

    /**
     * 获取Response对象 数组
     */
    private fun <T : Any> forRestResponseList(kClass: KClass<T>, method: String): RestResponse<List<T>> {
        val restEntity = forRestEntityList(kClass, method)
        if (restEntity.isSuccessful) {
            return restEntity.restResponse!!
        }
        return restEntity.toFailRestResponse()
    }


    /*****************************************/

    /**
     * 数组
     */
    private fun <T : Any> forRestEntityList(kClass: KClass<T>, method: String): RestEntity<List<T>> {
        return forRestEntityAsJsonAnalyze(method) {
            val listType = ListType(kClass.java)
            val restResponseType = RestResponseType(listType)
            val restResponse: RestResponse<List<T>> = Gson().fromJson(it, restResponseType)
            restResponse
        }
    }

    /**
     * 分页请求
     */
    private fun <T : Any> forRestEntityPage(kClass: KClass<T>, method: String): RestEntity<PageInfo<T>> {

        return forRestEntityAsJsonAnalyze(method) {
            val infoType = PageInfoType(kClass.java)
            val restResponseType = RestResponseType(infoType)
            val restResponse: RestResponse<PageInfo<T>> = Gson().fromJson(it, restResponseType)
            restResponse
        }
    }


    /**
     * 正常请求
     */
    private fun <T : Any> forRestEntity(kClass: KClass<T>, method: String): RestEntity<T> {

        return forRestEntityAsJsonAnalyze(method) {
            val type = RestResponseType(kClass.java)
            val restResponse: RestResponse<T> = Gson().fromJson(it, type)
            restResponse
        }

    }

    /**
     * 获取entity对象
     * @param method 请求类型
     * @param jsonAnalyze json解析
     * @param T 返回类型
     */
    private fun <T : Any> forRestEntityAsJsonAnalyze(
        method: String,
        jsonAnalyze: (json: String) -> RestResponse<T>
    ): RestEntity<T> {
        if (!NetworkUtils.isNetworkConnected(context)) {
            // 网络错误
            return RestEntity.fail(MsgCode.NetworkError.code, MsgCode.NetworkError.msg)
        }
        Log.d(TAG, "[$url - $method]")
        try {
            val response = method(method)
            if (response.isSuccessful) {
                val json = response.body!!.string()
                Log.d(TAG, json)

                // 自定义json解析方法
                val restResponse: RestResponse<T> = jsonAnalyze(json)

                Log.d(TAG, "json -> RestResponse: $restResponse")
                return RestEntity.ok(response, restResponse)
            }
            er(response.code, response.message)
            return RestEntity.fail(response)
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, e.message)
            er(MsgCode.ServerError.code, MsgCode.ServerError.msg)
            // 服务器请求超时
            return RestEntity.fail(MsgCode.ServerError.code, MsgCode.ServerError.msg)
        } catch (e: IOException) {
            e.printStackTrace()
            er(MsgCode.ServerError.code, MsgCode.ServerError.msg)
            // 未知IO错误
            return RestEntity.fail(MsgCode.UnknownMistake.code, MsgCode.UnknownMistake.msg)
        }
    }

    var downloadLength = 0L //已经下载好的长度
    var contentLength = 0L//文件的总长度

    /**
     * 下载到临时文件
     */
    fun downloadToTemp(tempFile: File, progress: (len: Long, progress: Long) -> Unit): RestResponse<Any> {
        if (!NetworkUtils.isNetworkConnected(context)) {
            // 网络错误
            return RestResponse.fail(MsgCode.NetworkError.code, MsgCode.NetworkError.msg)
        }
        try {
            val response = method(GET)
            if (response.isSuccessful) {

                if (response.headers["Content-Type"] == "application/json;charset=UTF-8") {
                    // restResponse
                    val json = response.body!!.string()
                    Log.d(TAG, json)
                    val restResponse: RestResponse<*> = Gson().fromJson(json, RestResponse::class.java)
                    return restResponse as RestResponse<Any>
                }

                contentLength = response.headers["Content-Range"]!!.split("/")[1].toLong()

                val bys = response.body!!.byteStream()

                val fileOutputStream = FileOutputStream(tempFile, true)
                val buffer = ByteArray(2048) //缓冲数组2kB

                var len: Int
                bys.use { input ->
                    fileOutputStream.use {
                        while (input.read(buffer).also { len = it } != -1) {
                            it.write(buffer, 0, len)
                            downloadLength += len
                            // 更新进度
                            progress(contentLength, downloadLength)
                            if (isStop) {
                                return@downloadToTemp RestResponse(false, -400, "下载暂停", null)
                            }
                        }
                    }
                }

                return RestResponse(true, 200, "下载成功", null)
            }
            er(response.code, response.message)
            return RestResponse.fail(
                MsgCode.ResponseError.code,
                "Http请求错误：错误码：${response.code},错误信息：${response.message}\n"
            )
        } catch (e: SocketException) {
            Log.d(TAG, "Socket closed; 连接已断开")
            return RestResponse(false, -100, "连接已断开", null)
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, e.message)
            er(MsgCode.ServerError.code, MsgCode.ServerError.msg)
            // 服务器请求超时
            return RestResponse.fail(MsgCode.ServerError.code, MsgCode.ServerError.msg)
        } catch (e: IOException) {
            e.printStackTrace()
            er(MsgCode.ServerError.code, MsgCode.ServerError.msg)
            // 未知IO错误
            return RestResponse.fail(MsgCode.UnknownMistake.code, MsgCode.UnknownMistake.msg)
        }
    }

    /**
     * 统一获取
     */
    private fun method(action: String): Response {

        val okHttpClient = getClinet()
        val requestBuilder = Request.Builder()
            .url(url)
            .headers(mHeaders.build())

        when (action) {
            GET -> requestBuilder.method(action, null)
            POST -> requestBuilder.method(action, mRequestBody)
        }

        val request = requestBuilder.build()

        mCall = okHttpClient.newCall(request)
        return mCall.execute()
    }

}

