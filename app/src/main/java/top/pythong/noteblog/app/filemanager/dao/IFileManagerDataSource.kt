package top.pythong.noteblog.app.filemanager.dao

import top.pythong.noteblog.app.filemanager.model.FileDir
import top.pythong.noteblog.data.RestResponse

interface IFileManagerDataSource {


    fun getFiles(currentPath: String): RestResponse<List<FileDir>>

}
