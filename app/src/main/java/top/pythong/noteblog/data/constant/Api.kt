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
    const val root = "${http}172.16.112.74" //192.168.43.15

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

    private const val article = "$root/api/article"

    /**
     * 获取推荐文章
     */
    const val recommend = article

    val browseArticle: (articleId: String) -> String = { articleId -> "$article/$articleId" }

    /**
     * 个人时间归档信息
     */
    const val archives = "$article/u/archives"

    /**
     * 个人分类归档
     */
    const val categoryArchives = "$article/u/category"

    /**
     * 分类文章集
     */
    val category: (category: String) -> String = { category -> "$categoryArchives/$category" }

    /**
     * 个人标签归档
     */
    const val tagArchives = "$article/u/tag"

    /**
     * 归档文章集
     */
    val tag: (tag: String) -> String = { tag -> "$tagArchives/$tag" }

    /**
     * 点赞
     */
    val like: (articleId: String) -> String = { articleId -> "$article/u/like/$articleId" }

    /**
     * 文件列表
     */
    const val fileList = "$root/file/list"

    const val download = "$root/file/download"
}