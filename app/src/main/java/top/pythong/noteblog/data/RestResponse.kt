package top.pythong.noteblog.data


/**
 * 返回viewModel层的数据类
 *
 * @author ChangJiahong
 * @date 2019/7/16
 */
data class RestResponse<T>(
    val ok: Boolean,
    val status: Int,
    val msg: String?,
    val data: T?,
    val timestamp: Long
) {

    constructor(ok: Boolean, status: Int, msg: String, data: T?) : this(
        ok, status, msg,
        data, 0L
    )

    private constructor(restResponse: RestResponse<*>) : this(
        restResponse.ok, restResponse.status,
        restResponse.msg?:"", restResponse.data as T
    )

    fun <T> convertTo(tClass: Class<T>): RestResponse<T> {
        return RestResponse(this)
    }

    fun isOk(): Boolean {
        return ok
    }

    companion object {
        fun <T> fail(status: Int, msg: String): RestResponse<T> {
            return RestResponse(false, status, msg, null)
        }
    }
}
