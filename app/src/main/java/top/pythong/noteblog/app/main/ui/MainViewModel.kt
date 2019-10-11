package top.pythong.noteblog.app.main.ui

import android.content.Context
import kotlinx.coroutines.*
import top.pythong.noteblog.app.download.DownloadService
import top.pythong.noteblog.app.download.model.DownloadResource
import top.pythong.noteblog.app.download.service.impl.DownloadTaskServiceImpl
import top.pythong.noteblog.app.main.service.IMainService
import top.pythong.noteblog.base.factory.ServiceFactory
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.utils.NetworkUtils
import top.pythong.noteblog.utils.getStringFromSharedPreferences

/**
 *
 * @author ChangJiahong
 * @date 2019/8/24
 */
class MainViewModel(private val mainService: IMainService) : BaseViewModel() {

    fun autoLogin(mainActivity: MainActivity) = launch {

        val result = mainService.autoLogin()
        if (!result.isOk) {
            withContext(Dispatchers.Main) {
                mainActivity.onErrorResult(result.msgCode)
            }
        }
    }

    /**
     * 检查下载服务
     */
    fun downloadService(context: Context) {
        val token = context.getStringFromSharedPreferences(Constant.TOKEN)
        val isAutoDownload = true
        if (token.isNotBlank()) {
            val downloadService = ServiceFactory.getSimpleService(context, DownloadTaskServiceImpl::class)
            val tasks = downloadService.getTasks()
            if (tasks.isNotEmpty()) {
                if (NetworkUtils.isWifiConnected(context) && isAutoDownload) {
                    // WiFi网络自动下载
                    tasks.forEach {
                        if (it.state != DownloadResource.COMPLETE) {
                            DownloadService.addDownload(context, it)
                        }
                    }
                } else {
                    tasks.forEach {
                        if (it.state == DownloadResource.DOWNLOADING) {
                            downloadService.saveDownloadResource(it, DownloadResource.SUSPEND)
                        }
                    }
                }
            }
        }
    }
}