package top.pythong.noteblog.base.viewModel

import android.arch.lifecycle.ViewModel
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import top.pythong.noteblog.data.constant.MsgCode

abstract class BaseViewModel : ViewModel(), CoroutineScope by MainScope() {

    private val TAG = BaseViewModel::class.java.simpleName

    /**
     * 发布错误
     */
    protected val postError: (error: MsgCode) -> Unit = {
        handleError(it)
    }

    /**
     * 处理错误
     */
    var handleError: (error: MsgCode) -> Unit = {
        Log.e(TAG, "错误信息：$it")
    }

}
