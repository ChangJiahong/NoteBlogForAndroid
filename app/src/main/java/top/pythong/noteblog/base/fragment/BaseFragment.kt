package top.pythong.noteblog.base.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.startActivity
import top.pythong.noteblog.app.login.ui.LoginActivity
import top.pythong.noteblog.base.errorListener.OnErrorListener
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.data.constant.MsgCode
import top.pythong.noteblog.utils.putToSharedPreferences

/**
 * Fragment基础类
 * 继承此类实现initView,createView,initData,getViewModel方法
 * @author ChangJiahong
 * @date 2019/8/26
 */
abstract class BaseFragment : Fragment(), OnErrorListener {

    private val TAG = "BaseFragment"

    /**
     * 当前 视图
     */
    private var mView: View? = null

    /**
     * 是否加载过
     */
    private var isLoadOver = false

    /**
     * 基础 viewModel
     */
    private lateinit var baseViewModel: BaseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mView == null) {
            mView = this.createView(inflater, container, savedInstanceState)
        }
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        baseViewModel = getViewModel()
        baseViewModel.error.observe(this, Observer {
            onErrorResult(it ?: return@Observer)
        })


        if (!isLoadOver) {
            initView()
            initData()
            isLoadOver = true
        }
    }

    /**
     * 创建视图
     */
    abstract fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View

    /**
     * 加载控件
     */
    abstract fun initView()

    /**
     * 加载数据
     */
    abstract fun initData()

    /**
     * 获得viewModel
     */
    internal abstract fun getViewModel(): BaseViewModel

    override fun onErrorResult(error: MsgCode) {
        if (error.isLoginError()) {
            this.activity!!.apply {
                alert {
                    title = "提示"
                    message = error.msg + ",去登陆试试!!!"
                    positiveButton("登录") { i ->
                        putToSharedPreferences {
                            put(Constant.TOKEN, "")
                        }
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

}