package top.pythong.noteblog.app.login.dao

import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.data.RestEntity

/**
 *
 * @author ChangJiahong
 * @date 2019/8/23
 */
interface ILoginDataSource {

    fun login(username: String, password: String): RestEntity<LoggedInUser>

    fun logout()
}