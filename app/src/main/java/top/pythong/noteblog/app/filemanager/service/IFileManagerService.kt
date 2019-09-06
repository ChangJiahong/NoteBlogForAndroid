package top.pythong.noteblog.app.filemanager.service

import top.pythong.noteblog.app.filemanager.model.FileDir
import top.pythong.noteblog.data.Result

interface IFileManagerService {

    /**
     * 获取文件
     */
    fun getFiles(currentPath: String): Result<List<FileDir>>

}
