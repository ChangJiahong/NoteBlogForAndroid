package top.pythong.noteblog.base.errorListener

import top.pythong.noteblog.data.constant.MsgCode

/**
 *
 * @author ChangJiahong
 * @date 2019/8/26
 */
interface OnErrorListener {
    fun onErrorResult(error: MsgCode)
}