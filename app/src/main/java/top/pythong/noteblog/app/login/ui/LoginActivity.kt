package top.pythong.noteblog.app.login.ui

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

import top.pythong.noteblog.R
import top.pythong.noteblog.app.login.model.LoggedInUserView
import top.pythong.noteblog.app.main.ui.MainActivity
import top.pythong.noteblog.base.activity.BaseActivity
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.base.factory.ViewModelFactory
import top.pythong.noteblog.data.constant.MsgCode

/**
 * 登录页面
 * @author ChangJiahong
 * @date 2019/8/22
 */
class LoginActivity : BaseActivity() {

    private lateinit var loginViewModel: LoginViewModel


    override fun getViewModel(): BaseViewModel {
        loginViewModel = ViewModelFactory.createViewModel(this, LoginViewModel::class)
        return loginViewModel
    }

    override fun getContentView(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { v, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        // 隐藏软键盘
                        val imm: InputMethodManager =
                            v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                    }
                }
                false
            }

            // 登录
            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                // 隐藏软键盘
                val imm: InputMethodManager =
                    it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }

    override fun initData() {
        // 登录表单状态回调
        // 参数验证回调
        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // 禁用登录按钮，除非用户名/密码都有效
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })
        // 登录结果回调
        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val user = it ?: return@Observer

            loading.visibility = View.GONE

            updateUiWithUser(user)


        })


    }

    /**
     * 错误回调
     */
    override fun onErrorResult(error: MsgCode) {
        loading.visibility = View.GONE
        when (error) {
            MsgCode.UsersDonTExist -> {
                username.requestFocus()
                username.error = error.msg
            }
            MsgCode.PasswordMistake -> {
                password.requestFocus()
                password.error = error.msg
            }
            else -> showLoginFailed(error.msg)
        }

    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        toast("$welcome $displayName")

        // 开启主页
//        startActivity<MainActivity>()
        setResult(Activity.RESULT_OK)
        // 成功完成并销毁登录活动
        finish()

    }

    private fun showLoginFailed(errorString: String) {
        toast(errorString)
    }
}

/**
 * 扩展函数，用于简化将afterTextChanged操作设置为EditText组件。
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
