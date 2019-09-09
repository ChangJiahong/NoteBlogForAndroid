package top.pythong.noteblog.app.filemanager.model

import top.pythong.noteblog.app.download.model.DownloadResource
import top.pythong.noteblog.data.constant.Api

data class FileDir(
    /**
     * 文件名
     */
    val name: String,

    /**
     * 文件类型
     */
    val type: String,

    /* 文件夹类型属性 */
    /**
     * 当前路径
     */
    val currentPath: String,

    /**
     * 文件夹下文件数量
     */
    val count: Int,


    /* 文件类型属性 */
    /**
     * 文件id
     */
    val fileId: String,


    val path: String,

    val author: String,

    val protective: String,

    val created: String
) {
    constructor(name: String) : this(
        name = name,
        type = "dir",
        currentPath = "\\$name",
        count = 0,
        fileId = "",
        path = "",
        author = "admin",
        protective = "public",
        created = "2019/09/04 12:00:00"
    )

    companion object {
        const val DIR = "dir"
    }

    fun toDownloadResource(toPath: String): DownloadResource? {
        if (type != DIR) {
            return DownloadResource(name = this.name, url = "${Api.download}/${this.fileId}", toPath = toPath)
        }
        return null
    }

}
