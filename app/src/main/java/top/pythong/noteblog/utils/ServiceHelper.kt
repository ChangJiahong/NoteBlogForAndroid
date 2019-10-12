package top.pythong.noteblog.utils

import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.Result

/**
 *
 * @author ChangJiahong
 * @date 2019/10/12
 */
class ServiceHelper {

    companion object {
        fun getInstence(): ServiceHelper {
            return ServiceHelper()
        }
    }

    fun <T : Any> result(response: RestResponse<T>): Result<T> {
        if (response.isOk()) {
            return Result.ok(response)
        }
        return Result.fail(response)
    }
}