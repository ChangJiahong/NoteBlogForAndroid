package top.pythong.noteblog.app.main.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.*
import top.pythong.noteblog.app.main.service.IMainService
import top.pythong.noteblog.data.Result

/**
 *
 * @author ChangJiahong
 * @date 2019/8/24
 */
class MainViewModel(private val mainService: IMainService) : ViewModel(), CoroutineScope by MainScope() {

    private val _autoLoginResult = MutableLiveData<Result<Any>>()

    val autoLoginResult: LiveData<Result<Any>>
        get() = _autoLoginResult

    fun autoLogin() = launch(Dispatchers.IO) {
        val restResponse = mainService.autoLogin()
        withContext(Dispatchers.Main) {
            if (restResponse.isOk) {
                _autoLoginResult.value = Result.ok()
            } else {
                _autoLoginResult.value = Result.fail(restResponse)
            }
        }
    }

}