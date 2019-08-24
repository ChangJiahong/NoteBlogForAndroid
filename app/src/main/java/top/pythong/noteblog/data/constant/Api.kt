package top.pythong.noteblog.data.constant

/**
 *
 * @author ChangJiahong
 * @date 2019/8/23
 */
object Api {

    private const val http = "http://"

    /**
     * 根
     */
    const val root = "${http}192.168.43.15"

    /**
     * 登录
     */
    const val login = "$root/login"

    /**
     * 注销
     */
    const val logout = "$root/logout"

    /**
     * 注册
     */
    const val register = "$root/register"


    /**
     * 用户信息
     */
    const val user = "$root/user"
}