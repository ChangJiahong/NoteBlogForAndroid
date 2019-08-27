package top.pythong.noteblog.data

import okhttp3.Headers
import okhttp3.Response

/**
 * 返回viewModel层的数据类
 * 携带请求头信息
 * @author ChangJiahong
 * @date 2019/8/23
 */
class RestEntity<T> private constructor(
    val isSuccessful: Boolean,
    val code: Int,
    val msg: String,
    val headers: Headers?,
    val restResponse: RestResponse<T>?
) {

    companion object {

        fun <T> ok(response: Response, restResponse: RestResponse<T>): RestEntity<T> {
            return RestEntity(
                response.isSuccessful,
                response.code,
                response.message,
                response.headers,
                restResponse
            )
        }

        fun <T> fail(response: Response): RestEntity<T> {
            return RestEntity(false, response.code, response.message, response.headers, null)
        }

        fun <T> fail(code: Int, msg: String): RestEntity<T> {
            return RestEntity(false, code, msg, null, null)
        }

    }

    fun toFailRestResponse(): RestResponse<T> {
        return RestResponse.fail(this.code, this.msg)
    }
}
