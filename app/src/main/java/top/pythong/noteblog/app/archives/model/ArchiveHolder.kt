package top.pythong.noteblog.app.archives.model

/**
 *
 * @author ChangJiahong
 * @date 2019/9/2
 */
data class ArchiveHolder(
    var type: Int,
    var state: Boolean,
    var data: Any
) {
    companion object {
        const val ARCHIVE = 0
        const val ARTICLE = 1
    }

    constructor(type: Int, data: Any) : this(
        type,
        false,
        data
    )
}