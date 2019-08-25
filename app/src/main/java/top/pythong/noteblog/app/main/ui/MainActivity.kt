package top.pythong.noteblog.app.main.ui

import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TabHost
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.onPageChangeListener
import top.pythong.noteblog.R
import top.pythong.noteblog.app.aboutMe.ui.AboutMeFragment
import top.pythong.noteblog.app.archives.ui.ArchivesFragment
import top.pythong.noteblog.app.home.ui.HomeFragment
import top.pythong.noteblog.app.login.ui.LoginActivity
import top.pythong.noteblog.app.main.adapter.ViewPagerAdapter
import top.pythong.noteblog.app.message.ui.MessageFragment
import top.pythong.noteblog.data.ViewModelFactory
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.data.constant.MsgCode
import top.pythong.noteblog.utils.putToSharedPreferences
import kotlin.reflect.KClass

/**
 * 首页
 * @author ChangJiahong
 * @date 2019/8/22
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    /**
     * 登录回调码
     */
    private val LOGIN_REQUEST = 0

    /**
     * 底部菜单栏
     */
    private val tabs = arrayOf(
        Triple(R.string.home, R.drawable.home_bg, HomeFragment::class),
        Triple(R.string.archives, R.drawable.archives_bg, ArchivesFragment::class),
        Triple(R.string.msg, R.drawable.msg_bg, MessageFragment::class),
        Triple(R.string.about_me, R.drawable.me_bg, AboutMeFragment::class)
    )

    /**
     * 页面组件
     */
    private val fragments = arrayListOf(
        HomeFragment.newInstance(),
        ArchivesFragment.newInstance(),
        MessageFragment.newInstance(),
        AboutMeFragment.newInstance()
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        mainViewModel = ViewModelFactory.createViewModel(this, MainViewModel::class.java)

        // 在main页面验证token有效，请求服务器，验证token，失败跳转到login
        mainViewModel.autoLogin()

        // 自动登录回调
        mainViewModel.autoLoginResult.observe(this, Observer {
            val loginResult = it ?: return@Observer
            if (!loginResult.isOk) {
                if (loginResult.msgCode == MsgCode.RequestMsg) {
                    alert {
                        title = "提示"
                        message = loginResult.msgCode.msg
                        okButton {
                            putToSharedPreferences {
                                put(Constant.TOKEN, "")
                            }
                            // 启动登录
                            startActivityForResult(
                                Intent(this@MainActivity, LoginActivity::class.java),
                                LOGIN_REQUEST
                            )
                        }
                    }.show()
                } else {
                    toast(loginResult.msgCode.msg)
                }
            }
        })


    }

    /**
     * 初始化视图
     */
    private fun initView() {
        // 设置主视图
        tabMode.setup(this, supportFragmentManager, R.id.viewPager)
        // 添加菜单按钮
        tabs.forEach {
            tabMode.addTab(getTab(it), it.third.java, null)
        }

        // 取消分割线
        tabMode.tabWidget.showDividers = LinearLayout.SHOW_DIVIDER_NONE

        // viewPage 适配器装载
        val viewPagerAdapter = ViewPagerAdapter(fragments, supportFragmentManager)
        viewPager.adapter = viewPagerAdapter

        // 监听页面改变
        viewPager.onPageChangeListener {
            onPageSelected {
                tabMode.currentTab = it
            }
        }

        tabMode.tabWidget.getChildAt(0).setOnClickListener {
            if (viewPager.currentItem == tabMode.currentTab && tabMode.currentTab == 0) {
                (fragments[0] as HomeFragment).refresh()
            }
            viewPager.currentItem = 0
        }

        // 监听菜单tab点击
        tabMode.setOnTabChangedListener {
            viewPager.currentItem = tabMode.currentTab
        }
    }

    /**
     * 获取菜单按钮
     */
    private fun getTab(tab: Triple<Int, Int, KClass<out Fragment>>): TabHost.TabSpec {
        val tabView = LayoutInflater.from(this).inflate(R.layout.tab_item, null)
        tabView.setBackgroundResource(R.drawable.tab_bg_ripple)
        tabView.find<ImageView>(R.id.tab_icon).setBackgroundResource(tab.second)
        tabView.find<TextView>(R.id.tab_text).text = resources.getString(tab.first)
        return tabMode.newTabSpec(resources.getString(tab.first)).setIndicator(tabView)
    }

    /**
     * 登录回调
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LOGIN_REQUEST) {

        }
    }
}
