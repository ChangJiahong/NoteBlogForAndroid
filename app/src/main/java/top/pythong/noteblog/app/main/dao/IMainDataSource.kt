package top.pythong.noteblog.app.main.dao

import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.data.RestResponse

/**
 *
 * @author ChangJiahong
 * @date 2019/8/24
 */
interface IMainDataSource {
    fun autoLogin(token: String): RestResponse<LoggedInUser>
}