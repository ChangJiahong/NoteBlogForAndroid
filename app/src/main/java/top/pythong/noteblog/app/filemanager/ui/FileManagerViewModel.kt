package top.pythong.noteblog.app.filemanager.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.pythong.noteblog.app.filemanager.model.FileDir
import top.pythong.noteblog.app.filemanager.service.IFileManagerService
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.data.Result

class FileManagerViewModel(private val fileManagerService: IFileManagerService) : BaseViewModel() {

    private val _fileDirs = MutableLiveData<List<FileDir>>()

    val fileDirs: LiveData<List<FileDir>> = _fileDirs


    /**
     * 获取该目录下的文件列表
     */
    fun loadFileList(currentPath: String, refreshLayout: RefreshLayout) = launch(Dispatchers.IO) {

        val result: Result<List<FileDir>> = fileManagerService.getFiles(currentPath)
        withContext(Dispatchers.Main) {
            if (result.isOk) {
                _fileDirs.value = result.viewData
                refreshLayout.finishRefresh(true)
            }else{
                _error.value = result.msgCode
                refreshLayout.finishRefresh(false)
            }
        }

    }


}
