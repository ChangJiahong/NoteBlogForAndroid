package top.pythong.noteblog.data.constant

import android.icu.util.ULocale.getLanguage
import java.util.*


/**
 *
 * @author ChangJiahong
 * @date 2019/8/24
 */
enum class MsgCode(
    val code: Int,
    /**
     * 英文信息
     */
    private var msgUS: String,
    /**
     * 中文信息
     */
    private var msgCN: String
) {

    OK(200, "OK", "成功"),
    /**
     * 账号不存在或密码错误
     */
    NoAccountOrPasswordError(20002, "No Account Or Password Error", "账号不存在或密码错误"),
    /**
     * 账号已被禁用
     */
    TheAccountHasBeenDisabled(20003, "The AccountHas Been Disabled", "账号已被禁用"),
    /**
     * 用户不存在
     */
    UsersDonTExist(20004, "Users DonT Exist", "用户不存在"),
    /**
     * 用户已存在
     */
    TheUserAlreadyExists(20005, "The User Already Exists", "用户已存在"),
    /**
     * 用户权限不足
     */
    InadequateUserRights(20006, "Inadequate User Rights", "用户权限不足"),
    /**
     * 密码错误
     */
    PasswordMistake(20007, "Password Mistake", "密码错误"),

    /**
     * 用户未登录
     */
    UserNotLoggedIn(20001, "User Not Logged In", "用户未登录"),

    /**
     * token过期
     */
    TokenExpired(80001, "Token Expired", "token已过期"),
    /**
     * token不合法
     */
    TokenIsNotValid(80002, "Token Is Not Valid", "token不合法"),
    /**
     * token为空
     */
    TokenIsEmpty(80003, "Token Not Found", "没有找到token"),

    /**
     * 登录状态失效
     */
    LogonStateFailure(80004, "Logon State Failure", "登录状态失效"),

    RequestMsg(-200, "Request Returned Error", "请求返回的错误"),

    ServerError(-500, "The Server Flew Away", "服务器飞走了"),

    NetworkError(0, "The Network Is Getting Worse", "网络开小差了"),

    UnknownMistake(-1, "Unknown Mistake", "未知错误");


    var msg: String
        get() {
            val lan = Locale.getDefault().language
            return if (lan == "zh") msgCN else msgUS
        }
        set(value) {
            val lan = Locale.getDefault().language
            if (lan == "zh")
                msgCN = value
            else
                msgUS = value
        }

    companion object {
        fun valueOf(code: Int): MsgCode {
            MsgCode.values().forEach {
                if (it.code == code) {
                    return it
                }
            }
            return RequestMsg
        }

        fun isLoginError(msgCode: MsgCode): Boolean {
            return when (msgCode) {
                MsgCode.LogonStateFailure, MsgCode.TokenExpired, MsgCode.TokenIsEmpty, MsgCode.TokenIsNotValid, MsgCode.UserNotLoggedIn -> true
                else -> false
            }
        }
    }

    fun isLoginError(): Boolean {
        return when (this) {
            MsgCode.LogonStateFailure, MsgCode.TokenExpired, MsgCode.TokenIsEmpty, MsgCode.TokenIsNotValid, MsgCode.UserNotLoggedIn -> true
            else -> false
        }
    }

}