package top.pythong.noteblog.data

import top.pythong.noteblog.data.constant.MsgCode


/**
 * 返回ViewModel层的泛型类
 */
class Result<out T : Any> private constructor(
    val isOk: Boolean = false,
    val viewData: T? = null,
    val msgCode: MsgCode
) {

    companion object {
        fun ok() =
            Result(true, null, msgCode = MsgCode.OK)

        fun <T : Any> ok(viewData: T) =
            Result<T>(true, viewData, msgCode = MsgCode.OK)

        fun <T : Any> ok(restResponse: RestResponse<T>) =
            Result<T>(true, restResponse.data, msgCode = MsgCode.OK)

        fun <T : Any> fail(msgCode: MsgCode) =
            Result<T>(false, msgCode = msgCode)

        fun <T : Any> fail(response: RestResponse<T>) = run {
            val msgCode = MsgCode.valueOf(response.status)
            if (msgCode == MsgCode.RequestMsg) {
                msgCode.msg = response.msg
                if (!response.isOk() && response.data != null && response.data is String) {
                    msgCode.msg += ", ${response.data}"
                }
            }
            Result<T>(false, msgCode = msgCode)
        }

        fun <T : Any> fail(restEntity: RestEntity<T>) = run {
            if (restEntity.restResponse != null) {
                return fail(restEntity.restResponse)
            }
            val msgCode = MsgCode.valueOf(restEntity.code)
            if (msgCode == MsgCode.RequestMsg) {
                msgCode.msg = restEntity.msg
            }
            Result<T>(false, msgCode = msgCode)
        }

    }

}
