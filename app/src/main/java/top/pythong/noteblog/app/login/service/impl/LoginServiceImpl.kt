package top.pythong.noteblog.app.login.service.impl

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import top.pythong.noteblog.app.login.dao.ILoginDataSource
import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.app.login.service.ILoginService
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.data.RestEntity
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.utils.putToSharedPreferences

/**
 * 从远程数据源请求身份验证和用户信息的类
 * 维护登录状态和用户凭证信息的内存缓存。
 */

class LoginServiceImpl(val context: Context, val dataSource: ILoginDataSource) :
    ILoginService {

    val TAG = "LoginService"

    /**
     * 注销
     */
    override fun logout() {

        dataSource.logout()
    }

    /**
     * 登录
     */
    override fun login(username: String, password: String): RestResponse<LoggedInUser> {

        val restEntity = dataSource.login(username, password)

        if (restEntity.isSuccessful) {
            setLoggedInUser(restEntity)
            return restEntity.restResponse!!
        }

        return RestResponse.fail(restEntity.code, restEntity.msg)
    }

    /**
     * 缓存登录信息
     */
    private fun setLoggedInUser(restEntity: RestEntity<LoggedInUser>) {
        val user = restEntity.restResponse!!.data
        val token = restEntity.headers!![Constant.TOKEN] ?: ""

        // 缓存token和userInfo
        context.putToSharedPreferences {
            put {
                Constant.TOKEN - token
                Constant.LOGGED_IN_USER - Gson().toJson(user)
            }
        }
    }
}
