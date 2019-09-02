package top.pythong.noteblog.app.archives.model

import top.pythong.noteblog.app.home.model.Article


/**
 *
 * @author ChangJiahong
 * @date 2019/9/2
 */
data class Archive(
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
    var articles: List<Article>

)