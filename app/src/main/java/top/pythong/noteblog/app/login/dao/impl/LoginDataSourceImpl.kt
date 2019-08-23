package top.pythong.noteblog.app.login.dao.impl

import okhttp3.*
import top.pythong.noteblog.app.login.dao.ILoginDataSource
import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.data.constant.Api
import top.pythong.noteblog.utils.HttpHelper


/**
 * 处理具有登录凭据的身份验证并检索用户信息的类。
 */
class LoginDataSourceImpl: ILoginDataSource {

    val TAG = LoginDataSourceImpl::class.java.simpleName

    override fun login(username: String, password: String) = HttpHelper.apply {
        url = Api.login
        requestBody {
            FormBody.Builder().apply {
                add("name", username)
                add("password", password)
            }.build()
        }
    }.postForEntity(LoggedInUser::class)


    override fun logout() {
        // TODO: revoke authentication
    }
}

