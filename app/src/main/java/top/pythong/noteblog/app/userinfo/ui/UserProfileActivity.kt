package top.pythong.noteblog.app.userinfo.ui

import android.arch.lifecycle.Observer
import android.content.Intent
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_user_info.*
import top.pythong.noteblog.R
import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.base.activity.BaseActivity
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.utils.getStringFromSharedPreferences
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.DefaultItemAnimator
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_user_info.recyclerView
import kotlinx.android.synthetic.main.activity_user_info.refreshLayout
import org.jetbrains.anko.startActivityForResult
import top.pythong.noteblog.app.aboutMe.ui.AboutMeFragment
import top.pythong.noteblog.app.edituserprofile.ui.EditUserProfileActivity
import top.pythong.noteblog.app.home.adapter.ArticleAdapter
import top.pythong.noteblog.app.home.model.ArticleCardItem
import top.pythong.noteblog.base.factory.ViewModelFactory
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.data.constant.MsgCode


class UserProfileActivity : BaseActivity() {

    private val TAG = UserProfileActivity::class.java.simpleName

    private lateinit var username: String

    private var isLocalUser = false

    private lateinit var adapter: ArticleAdapter
    private val articleList = ArrayList<ArticleCardItem>()

    private lateinit var userProfileViewModel: UserProfileViewModel

    override fun getContentView(): Int {
        return R.layout.activity_user_info
    }

    override fun getViewModel(): BaseViewModel? {
        userProfileViewModel = ViewModelFactory.createViewModel(this, UserProfileViewModel::class)
        return userProfileViewModel
    }

    override fun initView() {
        toolbar.setNavigationOnClickListener {
            finish()
        }

        username = intent.getStringExtra("username")

        toolbar.title = username

        val userJson = getStringFromSharedPreferences(Constant.LOGGED_IN_USER)
        val user = Gson().fromJson<LoggedInUser>(userJson, LoggedInUser::class.java)
        isLocalUser = username == user.username

        mEdit.visibility = if (isLocalUser) {
            mEdit.setOnClickListener {
                startActivityForResult<EditUserProfileActivity>(AboutMeFragment.REFRESH_REQUEST)
            }
            View.VISIBLE
        } else View.GONE

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ArticleAdapter(articleList)
        recyclerView.adapter = adapter

        userProfileViewModel.user.observe(this, Observer {
            val mUser = it ?: return@Observer
            Glide.with(this).load(mUser.imgUrl).into(userIcon)
            mUsername.text = mUser.username
            mUserInfo.text = "个人签名"
        })

        userProfileViewModel.articles.observe(this, Observer {
            val articles = it ?: return@Observer
            articleList.clear()
            articleList.addAll(articles)
            Log.d(TAG, articles.toString())
            adapter.notifyDataSetChanged()
        })

        refreshLayout.setOnLoadMoreListener {
            userProfileViewModel.loadMore(username, refreshLayout)
        }

        // 初始化loadingView
        loadingView.errorBtn {
            it.setOnClickListener {
                userProfileViewModel.page = 1
                userProfileViewModel.loadMore(username, refreshLayout)
            }
        }
    }

    override fun initData() {
        userProfileViewModel.loadUser(username)

        userProfileViewModel.loadMore(username, refreshLayout)
    }

    override fun onErrorResult(error: MsgCode) {
        if (!error.isLoginError()) {
            loadingView.errorMsg {
                it.text = error.msg
            }
            loadingView.showError(true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AboutMeFragment.REFRESH_REQUEST && resultCode == AboutMeFragment.NEED_TO_REFRESH) {
            setResult(AboutMeFragment.NEED_TO_REFRESH)
        }
    }
}
