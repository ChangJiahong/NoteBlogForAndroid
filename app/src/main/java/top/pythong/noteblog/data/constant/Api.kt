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
    const val root = "${http}172.16.111.215" //192.168.43.15

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

    const val article = "$root/api/article"

    /**
     * 归档信息
     */
    const val archives = "$article/u/archives"

    /**
     * 文件列表
     */
    const val fileList = "$root/file/list"

    const val download = "$root/file/download"
}