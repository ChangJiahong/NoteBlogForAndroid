package top.pythong.noteblog.app.main.service

import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.Result

/**
 *
 * @author ChangJiahong
 * @date 2019/8/24
 */
interface IMainService {

    /**
     * 自动登录
     */
    fun autoLogin(): Result<LoggedInUser>

}