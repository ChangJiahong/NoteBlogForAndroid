package top.pythong.noteblog.base.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import top.pythong.noteblog.data.constant.MsgCode

abstract class BaseViewModel : ViewModel(), CoroutineScope by MainScope() {

    protected val _error = MutableLiveData<MsgCode>()
    public val error: LiveData<MsgCode> = _error

}
