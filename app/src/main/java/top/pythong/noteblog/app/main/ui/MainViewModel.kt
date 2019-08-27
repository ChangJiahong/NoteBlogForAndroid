package top.pythong.noteblog.app.main.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.*
import top.pythong.noteblog.app.main.service.IMainService
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.data.Result

/**
 *
 * @author ChangJiahong
 * @date 2019/8/24
 */
class MainViewModel(private val mainService: IMainService) : BaseViewModel() {

    fun autoLogin() = launch(Dispatchers.IO) {
        val result = mainService.autoLogin()
        if (!result.isOk) {
            withContext(Dispatchers.Main) {
                _error.value = result.msgCode
            }
        }
    }

}