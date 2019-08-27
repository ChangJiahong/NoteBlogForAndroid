package top.pythong.noteblog.app.login.service

import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.Result

/**
 *
 * @author ChangJiahong
 * @date 2019/8/23
 */
interface ILoginService {

    fun login(username: String, password: String): Result<LoggedInUser>

    fun logout()
}