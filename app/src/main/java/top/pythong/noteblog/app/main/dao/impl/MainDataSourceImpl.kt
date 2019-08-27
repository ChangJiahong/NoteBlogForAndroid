package top.pythong.noteblog.app.main.dao.impl

import android.content.Context
import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.app.main.dao.IMainDataSource
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.constant.Api
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.utils.HttpHelper

/**
 *
 * @author ChangJiahong
 * @date 2019/8/24
 */
class MainDataSourceImpl(val context: Context) : IMainDataSource {

    override fun autoLogin(token: String) = HttpHelper(context).apply {
        url = Api.user
        headers {
            Constant.TOKEN - token
        }

    }.getForRestResponse(LoggedInUser::class)
}