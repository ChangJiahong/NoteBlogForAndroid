package top.pythong.noteblog.app.filemanager.dao.impl

import android.content.Context
import top.pythong.noteblog.app.filemanager.dao.IFileManagerDataSource
import top.pythong.noteblog.app.filemanager.model.FileDir
import top.pythong.noteblog.data.constant.Api
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.utils.HttpHelper
import top.pythong.noteblog.utils.getStringFromSharedPreferences

/**
 *
 * @author ChangJiahong
 * @date 2019/9/5
 */
class FileManagerDataSourceImpl(private val context: Context) : IFileManagerDataSource {

    override fun getFiles(currentPath: String) = HttpHelper(context).apply {
        val token = context.getStringFromSharedPreferences(Constant.TOKEN)
        url = Api.fileList
        params {
            Constant.FOLDER_PATH - currentPath
        }
        headers {
            Constant.TOKEN - token
        }
    }.getForRestResponseList(FileDir::class)
}