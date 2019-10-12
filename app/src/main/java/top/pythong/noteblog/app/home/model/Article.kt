package top.pythong.noteblog.app.home.model

import java.io.Serializable

data class Article(
    /**
     * 文章id
     */
    val id: Int,

    /**
     * 文章标题
     */
    val title: String,

    /**
     * 文章别名
     */
    val alias: String,

    /**
     * 作者 username
     */
    val author: String,
    val authorImgUrl: String,

    /**
     * 文章标签、种类
     */
    val tags: String,

    val categorys: String,

    /**
     * 描述信息
     */
    val info: String,

    /**
     * 点击数
     */
    val hits: Int = 0,

    /**
     * 最近修改时间
     */
    val modified: String,

    /**
     * 创建时间
     */
    val created: String,

    /**
     * 文章状态
     */
    val status: String,

    /**
     * 文章内容
     */
    val content: String,
    val frontCoverImgUrl: String,
    val liked: Boolean
) : Serializable {
    companion object {

        /* 文章状态定义 */

        val PUBLISH = "publish"

        val DRAFT = "draft"

        /* 文章内容格式定义 */

        val HTML = "html"

        val MD = "md"
    }
}
