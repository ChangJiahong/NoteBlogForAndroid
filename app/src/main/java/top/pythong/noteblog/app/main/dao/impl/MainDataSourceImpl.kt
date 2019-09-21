package top.pythong.noteblog.app.main.dao.impl

import android.content.Context
import top.pythong.noteblog.R
import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.app.main.dao.IMainDataSource
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.constant.Api
import top.pythong.noteblog.data.constant.Constant.TOKEN
import top.pythong.noteblog.data.constant.MsgCode
import top.pythong.noteblog.utils.HttpHelper
import top.pythong.noteblog.utils.getStringFromSharedPreferences

/**
 *
 * @author ChangJiahong
 * @date 2019/8/24
 */
class MainDataSourceImpl(val context: Context) : IMainDataSource {

    override fun autoLogin(): RestResponse<LoggedInUser> {
        val token = context.getStringFromSharedPreferences(TOKEN)
        if (token.isBlank()) {
            return RestResponse.fail(
                MsgCode.UserNotLoggedIn.code,
                context.getString(R.string.YouHaveNotLoggedInYet)
            )
        }
        return HttpHelper(context).apply {
            url = Api.user
            headers {
                TOKEN - token
            }

        }.getForRestResponse(LoggedInUser::class)
    }
}