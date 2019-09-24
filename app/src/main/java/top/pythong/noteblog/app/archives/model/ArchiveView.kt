package top.pythong.noteblog.app.archives.model

import top.pythong.noteblog.app.article.model.ArticleView
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.ArticleCardItem
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * @author ChangJiahong
 * @date 2019/9/2
 */
class ArchiveView(
    /**
     * 归档日期
     */
    var date: String,

    /**
     * 归档数量
     */
    var count: String,

    /**
     * 归档内容
     */
    var articleViews: List<Article>
) {
    constructor(archive: Archive) : this(
        date = archive.name.substring(0, 4) + "/" + archive.name.substring(4),
        count = archive.count,
        articleViews = archive.articles
    )
}