package top.pythong.noteblog.base.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import me.imid.swipebacklayout.lib.app.SwipeBackActivity
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import top.pythong.noteblog.app.login.ui.LoginActivity
import top.pythong.noteblog.base.errorListener.OnErrorListener
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.clearLoginUser
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.data.constant.MsgCode
import top.pythong.noteblog.utils.putToSharedPreferences

/**
 * 基础activity
 * @author ChangJiahong
 * @date 2019/8/26
 */
abstract class BaseActivity : SwipeBackActivity(), OnErrorListener {
    private lateinit var viewModel: BaseViewModel

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
        if (error.isLoginError()) {
            alert {
                title = "提示"
                message = error.msg + ",去登陆试试!!!"
                positiveButton("登录") { i ->
                    clearLoginUser()
                    // 启动登录
                    startActivity<LoginActivity>()
                }
                negativeButton("再等等") {
                    it.dismiss()
                }
                neutralPressed("不要来烦我") {
                    putToSharedPreferences {
                        // 下次是否询问登录
                        put(Constant.ASK_ABOUT_LOGIN, false)
                    }
                    it.dismiss()
                }

            }.show()
        }
    }

}