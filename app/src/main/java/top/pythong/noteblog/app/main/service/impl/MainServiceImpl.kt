package top.pythong.noteblog.app.main.service.impl

import android.content.Context
import com.google.gson.Gson
import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.app.main.dao.IMainDataSource
import top.pythong.noteblog.app.main.service.IMainService
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.Result
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.data.constant.MsgCode
import top.pythong.noteblog.utils.NetworkUtils
import top.pythong.noteblog.utils.getStringFromSharedPreferences
import top.pythong.noteblog.utils.putToSharedPreferences

/**
 *
 * @author ChangJiahong
 * @date 2019/8/24
 */
class MainServiceImpl(private val context: Context, private val mainDataSource: IMainDataSource) : IMainService {

    /**
     * 自动登录
     */
    override fun autoLogin(): Result<LoggedInUser> {
        val response = mainDataSource.autoLogin()
        if (response.isOk()) {
            setLoggedInUser(response)
            return Result.ok(restResponse = response)
        }
        return Result.fail(response)
    }

    /**
     * 缓存登录信息
     */
    private fun setLoggedInUser(response: RestResponse<LoggedInUser>) {
        val user = response.data

        // 缓存userInfo
        context.putToSharedPreferences {
            put {
                Constant.LOGGED_IN_USER - Gson().toJson(user)
            }
        }
    }
}