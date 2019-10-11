package top.pythong.noteblog.data.constant

/**
 *
 * @author ChangJiahong
 * @date 2019/8/22
 */
object Constant {

    /**
     * web站点地址
     */
    val WEB_ADDRESS = "http://www.pythong.top"

    val WEB_ARTICLE = "$WEB_ADDRESS/article"

    /** sp参数  **/

    const val LOGGED_IN_USER = "loggedInUser"

    const val ASK_ABOUT_LOGIN = "askAboutLogin"

    /** Intent参数 **/

    const val ARTICLE_ID = "articleId"


    /** http请求参数 **/

    /**
     * 文件夹的根目录
     */
    const val FOLDER_PATH = "folderPath"

    /**
     * 页码
     */
    const val PAGE = "page"

    /**
     * 页码大小
     */
    const val SIZE = "size"

    /**
     * 登录用户名
     */
    const val NAME = "name"

    /**
     * 登录密码
     */
    const val PASSWORD = "password"

    /**
     * 文章分类标签
     */
    const val TYPE = "type"

    /**
     * 通知通道id
     */
    const val CHANNEL_ID_DOWNLOAD: String = "download"


    /** 请求头 **/

    /**
     * 令牌
     */
    const val TOKEN = "Authorization"

    const val ARTICLE_TYPE = "article-type"

    const val ACCEPT_RANGES = "Accept-Ranges"
}