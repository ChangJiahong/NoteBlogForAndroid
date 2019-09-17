package top.pythong.noteblog.app.download.model

import top.pythong.noteblog.app.download.DownloadReceiver
import top.pythong.noteblog.app.download.DownloadService
import top.pythong.noteblog.app.download.ui.DownloadTaskActivity
import top.pythong.noteblog.data.constant.Api
import java.io.Serializable

/**
 *
 * @author ChangJiahong
 * @date 2019/9/9
 */
data class DownloadResource(var name: String, val url: String, val toPath: String) : Serializable {
    /**
     * 下载队列id
     */
    var id: Int = -1
    var contentLen = -1L
    var downloadLen = -1L
    var state: Int = -1

    companion object {

        /**
         * 下载完成
         */
        const val COMPLETE = 1
        /**
         * 暂停
         */
        const val SUSPEND = 2
        /**
         * 下载失败
         */
        const val FAILED = 3
        /**
         * 下载完成，copy中
         */
        const val MERGE = 4
        /**
         * 等待下载
         */
        const val WAITING = 5
        /**
         * 开始一个下载
         */
        const val START = 6
        /**
         * 下载中
         */
        const val DOWNLOADING = 7


        fun resource(name: String, fileId: String, toPath: String) = DownloadResource(
            name = name,
            url = "${Api.download}/$fileId",
            toPath = toPath
        )
    }

    /**
     * 获取文件的下载id
     */
    fun fileId(): String = url.substringAfterLast("/")


}