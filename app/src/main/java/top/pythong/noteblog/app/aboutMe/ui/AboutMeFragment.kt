package top.pythong.noteblog.app.aboutMe.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import top.pythong.noteblog.R
import top.pythong.noteblog.base.fragment.BaseFragment
import top.pythong.noteblog.base.viewModel.BaseViewModel
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import kotlinx.android.synthetic.main.about_me_fragment.*
import android.support.v4.widget.NestedScrollView
import android.util.Log
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.util.SmartUtil.dp2px
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.support.v4.startActivityForResult
import org.jetbrains.anko.support.v4.toast
import top.pythong.noteblog.app.articlemanager.ui.ArticleManagerActivity
import top.pythong.noteblog.app.download.ui.DownloadTaskActivity
import top.pythong.noteblog.app.filemanager.ui.FileManagerActivity
import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.app.main.ui.MainActivity
import top.pythong.noteblog.app.userinfo.ui.UserProfileActivity
import top.pythong.noteblog.base.factory.ViewModelFactory
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.utils.getStringFromSharedPreferences


class AboutMeFragment : BaseFragment(), View.OnClickListener {

    val TAG = AboutMeFragment::class.java.simpleName

    private lateinit var viewModel: AboutMeViewModel

    private lateinit var parentActivity: MainActivity


    companion object{
        const val REFRESH_REQUEST = 11
        const val NEED_TO_REFRESH = 12
    }

    /**
     * 创建视图
     */
    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.about_me_fragment, container, false)
    }

    private var mOffset = 0f
    private var mScrollY = 0f

    override fun onBaseStart() {
        this.activity!!.title = "我的"
        this.parentActivity = this.activity as MainActivity
    }

    /**
     * 加载控件
     */
    override fun initView() {
        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderMoving(
                header: RefreshHeader?,
                isDragging: Boolean,
                percent: Float,
                offset: Int,
                headerHeight: Int,
                maxDragHeight: Int
            ) {
                mOffset = offset / 1.5f
                parallax.translationY = mOffset - mScrollY
            }

        })


        scrollView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            private var lastScrollY = 0f
            private val h = dp2px(200f).toFloat()
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                var sy = scrollY.toFloat()
                if (lastScrollY < h) {
                    sy = Math.min(h, sy)
                    mScrollY = if (scrollY > h) h else sy
                    parallax.translationY = mOffset - mScrollY
                }
                lastScrollY = sy
            }
        })

        fileManager.setOnClickListener(this)

        articleManager.setOnClickListener(this)

        downloadTask.setOnClickListener(this)

        aboutUs.setOnClickListener(this)


    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.fileManager -> {
                this.activity!!.startActivityForResult<FileManagerActivity>(MainActivity.OTHER_ACTIVITY)
            }

            R.id.articleManager -> {
                this.activity!!.startActivityForResult<ArticleManagerActivity>(MainActivity.OTHER_ACTIVITY)
            }

            R.id.downloadTask -> {
                this.activity!!.startActivity<DownloadTaskActivity>()
            }

            R.id.aboutUs -> {
//                this.activity!!.startActivity<FileManagerActivity>()
                toast("关于我们")
            }

            else -> {
            }
        }
    }


    /**
     * 加载数据
     */
    override fun initData() {
        // 加载头像，用户名，说明
        val userJson = this.context!!.getStringFromSharedPreferences(Constant.LOGGED_IN_USER)
        val user = Gson().fromJson<LoggedInUser>(userJson, LoggedInUser::class.java)
        if (user != null) {
            Glide.with(this).load(user.imgUrl).into(userIcon)
            username.text = user.username
            perStatement.text = "个人说明"
            userIcon.setOnClickListener {
                startActivityForResult<UserProfileActivity>(REFRESH_REQUEST, "username" to user.username)
            }
        } else {
            username.text = "未登录,点击登录"
            username.setOnClickListener {
                parentActivity.startLoginActivity()
            }
            userIcon.setOnClickListener {
                parentActivity.startLoginActivity()
            }
            perStatement.text = ""
        }


    }

    /**
     * 获得viewModel
     */
    override fun getViewModel(): BaseViewModel {
        viewModel = ViewModelFactory.createViewModel(this, AboutMeViewModel::class)
        return viewModel
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REFRESH_REQUEST && resultCode == NEED_TO_REFRESH){
            refresh()
        }
    }

    fun refresh(){
        Log.d(TAG, "刷新用户信息")
        initData()
    }
}
