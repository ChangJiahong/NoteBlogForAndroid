package top.pythong.noteblog.app.articlemanager.model

/**
 *
 * @author ChangJiahong
 * @date 2019/9/22
 */
data class SimpleArticle(
    /**
     * 文章id
     */
    val id: Int,

    /**
     * 文章标题
     */
    val title: String,

    /**
     * 描述信息
     */
    val info: String,
    /**
     * 点击数
     */
    val hits: Int = 0,
    /**
     * 创建时间
     */
    val time: String,

    val status: String
)