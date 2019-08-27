package top.pythong.noteblog.app.home.model

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

    /**
     * 文章标签、种类
     */
    val types: ArrayList<Type>,

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
    val content: String
) {
    companion object {

        val PUBLISH = "publish"

        val DRAFT = "draft"
    }
}
