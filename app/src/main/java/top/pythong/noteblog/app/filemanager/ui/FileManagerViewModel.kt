package top.pythong.noteblog.app.filemanager.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import top.pythong.noteblog.app.download.DownloadService
import top.pythong.noteblog.app.download.model.DownloadResource
import top.pythong.noteblog.app.download.service.IDownloadTaskService
import top.pythong.noteblog.app.download.ui.DownloadTaskActivity
import top.pythong.noteblog.app.filemanager.model.FileDir
import top.pythong.noteblog.app.filemanager.service.IFileManagerService
import top.pythong.noteblog.base.factory.ServiceFactory
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.data.Result
import top.pythong.noteblog.utils.getResourceFile
import top.pythong.noteblog.utils.openFileByThirdPartyApp

class FileManagerViewModel(context: Context, private val fileManagerService: IFileManagerService) : BaseViewModel() {

    private val _fileDirs = MutableLiveData<List<FileDir>>()

    val fileDirs: LiveData<List<FileDir>> = _fileDirs

    val downloadTaskService = ServiceFactory.getSimpleService(context, IDownloadTaskService::class)


    /**
     * 获取该目录下的文件列表
     */
    fun loadFileList(currentPath: String, refreshLayout: RefreshLayout) = launch(Dispatchers.IO) {

        val result: Result<List<FileDir>> = fileManagerService.getFiles(currentPath)
        withContext(Dispatchers.Main) {
            if (result.isOk) {
                _fileDirs.value = result.viewData
                refreshLayout.finishRefresh(true)
            } else {
                _error.value = result.msgCode
                refreshLayout.finishRefresh(false)
            }
        }

    }

    fun openOrDownloadFile(context: Context, resource: DownloadResource?) {
        if (resource == null) {
            return
        }
        // TODO: 判断文件类型，开启预览

        val refile = context.getResourceFile(resource)
        if (refile.exists()) {
            context.openFileByThirdPartyApp(refile, resource.type)
            return
        }

        val downs = downloadTaskService.selectByUrl(resource.url)

        val picks = ArrayList<DownloadResource>(downs)
        if (downs.isEmpty()) {
            // 未下载，开启下载
            // 下载
            DownloadService.addDownload(context, resource)
            context.toast("已加入下载列表")
            return
        }

        if (downs.all { it.state == DownloadResource.COMPLETE }) {
            // 下载完成的,重新下载
            resource.id = downs[0].id
            DownloadService.addDownload(context, resource)
            context.toast("已加入下载列表.")
            return
        }

        // 有未下载完成的
        // 跳转下载页面
        // 继续下载
        context.startActivity<DownloadTaskActivity>("picks" to picks)


    }


}
