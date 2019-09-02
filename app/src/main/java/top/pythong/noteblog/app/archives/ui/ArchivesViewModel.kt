package top.pythong.noteblog.app.archives.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.pythong.noteblog.app.archives.model.Archive
import top.pythong.noteblog.app.archives.model.ArchiveView
import top.pythong.noteblog.app.archives.service.IArchivesService
import top.pythong.noteblog.app.home.model.ArticleCardItem
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.data.Result

class ArchivesViewModel(private val archivesService: IArchivesService) : BaseViewModel() {

    private val _archives = MutableLiveData<ArrayList<ArchiveView>>()

    val archives: LiveData<ArrayList<ArchiveView>> = _archives

    private var page: Int = 1
    private val size: Int = 20

    fun loadArchives(refreshLayout: RefreshLayout) = launch(Dispatchers.IO) {

        val result: Result<PageInfo<Archive>> = archivesService.getArchives(1, size)
        val archiveList = ArrayList<ArchiveView>()
        if (result.isOk) {
            val pageInfo = result.viewData!!
            val newArchives = pageInfo.list!!
            newArchives.forEach {
                archiveList.add(ArchiveView(it))
            }
            page = pageInfo.nextPage

            withContext(Dispatchers.Main) {
                _archives.value = archiveList
                refreshLayout.finishRefresh(1000, true, !pageInfo.hasNextPage)
            }
        } else {
            withContext(Dispatchers.Main) {
                _error.value = result.msgCode
                refreshLayout.finishRefresh(1000, false, false)
            }
        }
    }

    fun loadMore(refreshLayout: RefreshLayout) = launch(Dispatchers.IO) {
        val archiveList = _archives.value!!

        val result = archivesService.getArchives(page, size)
        if (result.isOk) {
            val pageInfo = result.viewData!!
            val newArticles = pageInfo.list!!
            newArticles.forEach {
                archiveList.add(ArchiveView(it))
            }
            page = pageInfo.nextPage

            withContext(Dispatchers.Main) {
                _archives.value = archiveList
                refreshLayout.finishLoadMore(1000, true, !pageInfo.hasNextPage)
            }
        } else {
            withContext(Dispatchers.Main) {
                _error.value = result.msgCode
                refreshLayout.finishLoadMore(1000, false, false)
            }
        }
    }
}
