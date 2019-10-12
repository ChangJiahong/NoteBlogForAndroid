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
    const val root = "${http}root.pythong.top:2580"

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

    /**
     * 获取普通用户信息
     */
    val userInfo: (username: String) -> String = { username -> "$user/$username" }

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
     * 分类归档
     */
    const val categoryArchives = "$article/category"

    /**
     * 个人分类归档
     */
    const val uCategoryArchives = "$article/u/category"

    /**
     * 分类文章集
     */
    val category: (category: String) -> String = { category -> "$categoryArchives/$category" }

    /**
     * 个人分类文章集
     */
    val uCategory: (category: String) -> String = { category -> "$uCategoryArchives/$category" }

    /**
     * 标签归档
     */
    const val tagArchives = "$article/tag"

    /**
     * 个人标签归档
     */
    const val uTagArchives = "$article/u/tag"

    /**
     * 标签文章集
     */
    val tag: (tag: String) -> String = { tag -> "$tagArchives/$tag" }

    /**
     * 个人标签文章集
     */
    val uTtag: (tag: String) -> String = { tag -> "$uTagArchives/$tag" }

    /**
     * 个人文章集
     */
    const val ulist = "$article/u/list"

    /**
     * 一般用户文章集
     */
    const val list = "$article/list"

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