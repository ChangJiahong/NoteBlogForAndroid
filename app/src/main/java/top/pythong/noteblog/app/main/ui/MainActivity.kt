package top.pythong.noteblog.app.main.ui

import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TabHost
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import top.pythong.noteblog.R
import top.pythong.noteblog.app.aboutMe.ui.AboutMeFragment
import top.pythong.noteblog.app.archives.ui.ArchivesFragment
import top.pythong.noteblog.app.home.ui.HomeFragment
import top.pythong.noteblog.app.message.ui.MessageFragment
import top.pythong.noteblog.base.activity.BaseActivity
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.base.factory.ViewModelFactory
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.data.constant.MsgCode
import top.pythong.noteblog.utils.getBooleanFromSharedPreferences
import kotlin.reflect.KClass


/**
 * 首页
 * @author ChangJiahong
 * @date 2019/8/22
 */
class MainActivity : BaseActivity() {

    private val TAG = "MainActivity"

    private lateinit var mainViewModel: MainViewModel

    /**
     * 登录回调码
     */
    private val LOGIN_REQUEST = 0

    /**
     * 底部菜单栏
     */
    private val tabs: Array<Triple<String, Int, KClass<out Fragment>>> by lazy {
        arrayOf(
            Triple(resources.getString(R.string.home), R.drawable.home_bg, HomeFragment::class),
            Triple(resources.getString(R.string.archives), R.drawable.archives_bg, ArchivesFragment::class),
            Triple(resources.getString(R.string.msg), R.drawable.msg_bg, MessageFragment::class),
            Triple(resources.getString(R.string.about_me), R.drawable.me_bg, AboutMeFragment::class)
        )
    }

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): BaseViewModel {
        mainViewModel = ViewModelFactory.createViewModel(this, MainViewModel::class)
        return mainViewModel
    }

    /**
     * 初始化视图
     */
    override fun initView() {
        setSwipeBackEnable(false)
        // 设置主视图
        tabMode.setup(this, supportFragmentManager, R.id.fragmentContent)
        // 添加菜单按钮
        tabs.forEach {
            tabMode.addTab(getTab(it), it.third.java, null)
        }

        // 取消分割线
        tabMode.tabWidget.showDividers = LinearLayout.SHOW_DIVIDER_NONE

        tabMode.tabWidget.getChildAt(0).setOnClickListener {
            if (tabMode.currentTab == 0) {
                val firstFragment =
                    supportFragmentManager.findFragmentByTag(tabs[0].first) as HomeFragment
                firstFragment.refresh()
                return@setOnClickListener
            }
            tabMode.currentTab = 0
        }

        tabMode.tabWidget.getChildAt(1).setOnClickListener {
            if (tabMode.currentTab == 1) {
                val secondFragment =
                    supportFragmentManager.findFragmentByTag(tabs[1].first) as ArchivesFragment
                secondFragment.refresh()
                return@setOnClickListener
            }
            tabMode.currentTab = 1
        }
    }

    /**
     * 获取菜单按钮
     */
    private fun getTab(tab: Triple<String, Int, KClass<out Fragment>>): TabHost.TabSpec {
        val tabView = LayoutInflater.from(this).inflate(R.layout.tab_item, null)
        tabView.setBackgroundResource(R.drawable.tab_bg_ripple)
        tabView.find<ImageView>(R.id.tab_icon).setBackgroundResource(tab.second)
        tabView.find<TextView>(R.id.tab_text).text = tab.first
        return tabMode.newTabSpec(tab.first).setIndicator(tabView)
    }

    override fun initData() {
        // 在main页面验证token有效，请求服务器，验证token，失败跳转到login
        mainViewModel.autoLogin()
    }

    override fun onErrorResult(error: MsgCode) {
        val askAboutLogin = getBooleanFromSharedPreferences(Constant.ASK_ABOUT_LOGIN)
        if (askAboutLogin) {
            super.onErrorResult(error)
        }
        toast(error.msg)
    }
}
