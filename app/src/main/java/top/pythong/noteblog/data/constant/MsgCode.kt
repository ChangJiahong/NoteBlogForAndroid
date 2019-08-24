package top.pythong.noteblog.data.constant

/**
 *
 * @author ChangJiahong
 * @date 2019/8/24
 */
enum class MsgCode(var msg: String, var code: Int) {

    RequestMsg("请求返回的错误", 200),

    ServerError("服务器飞走了", -500),

    NetworkError("网络开小差了", 0),

    UnknownMistake("未知错误", -1);

    companion object {
        fun getCode(code: Int): MsgCode {
            MsgCode.values().forEach {
                if (it.code == code) {
                    return it
                }
            }
            return RequestMsg
        }
    }


}