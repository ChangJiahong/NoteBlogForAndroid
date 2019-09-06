package top.pythong.noteblog.app.filemanager.service.impl

import android.content.Context
import top.pythong.noteblog.app.filemanager.dao.IFileManagerDataSource
import top.pythong.noteblog.app.filemanager.model.FileDir
import top.pythong.noteblog.app.filemanager.service.IFileManagerService
import top.pythong.noteblog.data.RestResponse
import top.pythong.noteblog.data.Result

/**
 *
 * @author ChangJiahong
 * @date 2019/9/5
 */
class FileManagerServiceImpl(private val context: Context, private val fileManagerDataSource: IFileManagerDataSource) :
    IFileManagerService {

    /**
     * 获取文件
     */
    override fun getFiles(currentPath: String): Result<List<FileDir>> {
        val restResponse: RestResponse<List<FileDir>> = fileManagerDataSource.getFiles(currentPath)
        if (restResponse.isOk()) {
            return Result.ok(restResponse)
        }
        return Result.fail(restResponse)
    }
}