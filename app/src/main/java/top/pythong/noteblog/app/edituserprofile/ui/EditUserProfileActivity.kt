package top.pythong.noteblog.app.edituserprofile.ui

import android.os.Bundle
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_edit_user_profile.*
import top.pythong.noteblog.R
import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.base.activity.BaseActivity
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.utils.getStringFromSharedPreferences

class EditUserProfileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_profile)
    }

    override fun getContentView(): Int {
        return R.layout.activity_edit_user_profile
    }

    override fun initView() {
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val userJson = getStringFromSharedPreferences(Constant.LOGGED_IN_USER)
        val user = Gson().fromJson<LoggedInUser>(userJson, LoggedInUser::class.java)


    }

    override fun initData() {

    }
}
