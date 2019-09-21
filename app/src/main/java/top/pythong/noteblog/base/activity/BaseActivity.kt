package top.pythong.noteblog.base.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.*
import android.os.Bundle
import android.util.Log
import me.imid.swipebacklayout.lib.app.SwipeBackActivity
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivityForResult
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

    private val TAG = BaseActivity::class.java.simpleName

    /**
     * 登录回调码
     */
    private val LOGIN_REQUEST = 0

    private var viewModel: BaseViewModel? = null

    open fun isEnableNetworkChangeListener() = false

    open fun isEnableViewModel() = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())

        onBaseStart()

        if (isEnableViewModel()) {
            viewModel = getViewModel()
            viewModel?.error?.observe(this, Observer {
                onErrorResult(it ?: return@Observer)
            })
        }

        initView()

        initData()

        if (isEnableNetworkChangeListener()) {
            registerNetworkChangeListener()
        }

    }

    open fun onBaseStart() {}

    open fun getViewModel(): BaseViewModel? {
        return null
    }

    private fun registerNetworkChangeListener() {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        manager.requestNetwork(NetworkRequest.Builder().build(), object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network?) {
                this@BaseActivity.onAvailable(network)
            }

            override fun onCapabilitiesChanged(network: Network?, networkCapabilities: NetworkCapabilities?) {
                this@BaseActivity.onCapabilitiesChanged(network, networkCapabilities)
            }

            override fun onLost(network: Network?) {
                this@BaseActivity.onLost(network)
            }
        })
    }

    /**
     * 网络连接
     */
    open fun onAvailable(network: Network?) {}

    /**
     * 网络改变
     */
    open fun onCapabilitiesChanged(network: Network?, networkCapabilities: NetworkCapabilities?) {}

    /**
     * 网络丢失
     */
    open fun onLost(network: Network?) {}

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
                    startLoginActivity()
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

    fun startLoginActivity() {
        startActivityForResult<LoginActivity>(LOGIN_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGIN_REQUEST && resultCode == Activity.RESULT_OK) {
            reload()
        }
    }

    open fun reload() {
//        finish()
//        overridePendingTransition(0, 0)
//        startActivity(intent)
//        overridePendingTransition(0, 0)
        recreate()
    }

}