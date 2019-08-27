package top.pythong.noteblog.app.login.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Patterns
import kotlinx.coroutines.*

import top.pythong.noteblog.R
import top.pythong.noteblog.app.login.model.LoggedInUserView
import top.pythong.noteblog.app.login.model.LoginFormState
import top.pythong.noteblog.app.login.service.ILoginService
import top.pythong.noteblog.base.viewModel.BaseViewModel
import java.util.regex.Pattern

class LoginViewModel(private val loginSercice: ILoginService) : BaseViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoggedInUserView>()
    val loginResult: LiveData<LoggedInUserView> = _loginResult

    /**
     * 登录，开启协程
     */
    fun login(username: String, password: String) = launch(Dispatchers.IO) {

        val result = loginSercice.login(username, password)

        withContext(Dispatchers.Main) {
            if (result.isOk) {
                _loginResult.value = LoggedInUserView(result.viewData!!.username)
            } else {
                // 错误回调
                _error.value = result.msgCode
            }
        }
    }

    /**
     * 登录表单输入数据监听
     */
    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value =
                LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value =
                LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    /**
     *  检查用户名合法性
     */
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
                    // 由字母数字组成，3~15之间，开头必须是字母
                    && Pattern.compile("[a-zA-Z]{1}[a-zA-Z0-9_]{2,15}").matcher(username).matches()
        }
    }

    /**
     * 检查密码合法性
     */
    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }
}
