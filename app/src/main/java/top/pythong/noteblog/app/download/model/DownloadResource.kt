package top.pythong.noteblog.app.download.model

import java.io.Serializable

/**
 *
 * @author ChangJiahong
 * @date 2019/9/9
 */
data class DownloadResource(val name: String, val url: String, val toPath: String) : Serializable {
    /**
     * 下载队列id
     */
    var id: Int = 0
    var contentLen = 0L
    var downloadLen = 0L
}