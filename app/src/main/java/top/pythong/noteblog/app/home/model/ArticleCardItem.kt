package top.pythong.noteblog.app.home.model

import top.pythong.noteblog.utils.DateKit


/**
 *
 * @author ChangJiahong
 * @date 2019/8/25
 */
data class ArticleCardItem(

    /**
     * 文章id
     */
    val id: Int,

    /**
     * 文章标题
     */
    val title: String,

    /**
     * 作者 username
     */
    val author: String,
    val authorImgUrl: String,

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
     * 创建时间
     */
    val timeAgo: String,
    val frontCoverImgUrl: String
) {
    constructor(article: Article) : this(
        id = article.id,
        title = article.title,
        author = article.author,
        authorImgUrl = article.authorImgUrl,
        tags = article.tags,
        categorys = article.categorys,
        info = article.info,
        hits = article.hits,
        timeAgo = DateKit.timesAgo(article.created, "yyyy-MM-dd HH:mm:ss"),
        frontCoverImgUrl = article.frontCoverImgUrl
    )
}