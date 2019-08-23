package top.pythong.noteblog.data


/**
 * 返回视图层的泛型类
 */
class Result<out T: Any> private constructor(
    val isOk: Boolean = false,
    val viewData: T? = null,
    val msg: String = ""
) {

    companion object {
        fun ok() =
            Result(true, null)

        fun <T : Any> ok(viewData: T) =
            Result<T>(true, viewData)

        fun <T : Any> fail(msg: String) =
            Result<T>(false, viewData = null, msg = msg)
    }

}
