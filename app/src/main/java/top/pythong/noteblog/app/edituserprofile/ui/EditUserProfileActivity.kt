package top.pythong.noteblog.app.edituserprofile.ui

import android.view.View
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_edit_user_profile.*
import org.jetbrains.anko.toast
import top.pythong.noteblog.R
import top.pythong.noteblog.app.aboutme.ui.AboutMeFragment
import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.base.activity.BaseActivity
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.utils.getStringFromSharedPreferences

class EditUserProfileActivity : BaseActivity() {

    private var modifyMark = false

    override fun getContentView(): Int {
        return R.layout.activity_edit_user_profile
    }

    override fun initView() {
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val userJson = getStringFromSharedPreferences(Constant.LOGGED_IN_USER)
        val user = Gson().fromJson<LoggedInUser>(userJson, LoggedInUser::class.java)

        Glide.with(this).load(user.imgUrl).into(icon)
        username.text = user.username
        email.text = user.email
        info.text = ""
        age.text = user.age.toString()

    }

    override fun initData() {
        if (modifyMark) {
            setResult(AboutMeFragment.NEED_TO_REFRESH)
        }
    }

    fun modifyAvatar(v: View) {
        toast("修改头像")
    }

    fun modifyUsername(v: View) {
        toast("修改用户名")
    }

    fun modifyInfo(v: View) {
        toast("修改签名")
    }

    fun modifyAge(v: View) {
        toast("修改年龄")
    }
}
