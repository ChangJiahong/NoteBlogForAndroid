package top.pythong.noteblog.base.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import top.pythong.noteblog.app.login.ui.LoginActivity
import top.pythong.noteblog.base.errorListener.OnErrorListener
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.data.constant.MsgCode
import top.pythong.noteblog.utils.putToSharedPreferences

/**
 * 基础activity
 * @author ChangJiahong
 * @date 2019/8/26
 */
abstract class BaseActivity : AppCompatActivity(), OnErrorListener {
    protected lateinit var viewModel: BaseViewModel

    internal abstract fun getViewModel(): BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())

        viewModel = getViewModel()
        viewModel.error.observe(this, Observer {
            onErrorResult(it ?: return@Observer)
        })

        initView()

        initData()
    }

    abstract fun getContentView(): Int

    abstract fun initView()

    abstract fun initData()

    override fun onErrorResult(error: MsgCode) {
        when (error) {
            MsgCode.LogonStateFailure, MsgCode.TokenExpired, MsgCode.TokenIsEmpty, MsgCode.TokenIsNotValid, MsgCode.UserNotLoggedIn -> {
                alert {
                    title = "提示"
                    message = error.msg + ",请重新登录!!!"
                    isCancelable = false
                    positiveButton("登录") { i ->
                        putToSharedPreferences {
                            put(Constant.TOKEN, "")
                        }
                        // 启动登录
                        startActivity<LoginActivity>()
                        // 关闭
                        i.dismiss()
                        // 关闭
                        finish()
                    }
                }.show()
            }
            else -> {

            }
        }
    }

}