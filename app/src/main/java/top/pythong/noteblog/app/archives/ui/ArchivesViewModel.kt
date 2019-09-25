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
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.data.Result

class ArchivesViewModel(private val archivesService: IArchivesService) : BaseViewModel() {

    private val _archives = MutableLiveData<Pair<Boolean, ArrayList<ArchiveView>>>()

    val archives: LiveData<Pair<Boolean, ArrayList<ArchiveView>>> = _archives

    private var page: Int = 1
    private val size: Int = 20

    /**
     * @param append 获取数据是否追加
     */
    fun loadData(refreshLayout: RefreshLayout, append: Boolean = false) = launch(Dispatchers.IO) {
        if (!append) {
            page = 1
        }
        val result: Result<PageInfo<Archive>> = archivesService.getArchives(page, size)
        val archiveList = ArrayList<ArchiveView>()
        if (result.isOk) {
            val pageInfo = result.viewData!!
            val newArchives = pageInfo.list!!
            newArchives.forEach {
                archiveList.add(ArchiveView(it))
            }
            page = pageInfo.nextPage

            withContext(Dispatchers.Main) {
                _archives.value = Pair(append, archiveList)
                if (append) {
                    refreshLayout.finishLoadMore(1000, true, !pageInfo.hasNextPage)
                } else {
                    refreshLayout.finishRefresh(1000, true, !pageInfo.hasNextPage)
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                postError(result.msgCode)
                if (append) {
                    refreshLayout.finishLoadMore(1000, false, false)
                } else {
                    refreshLayout.finishRefresh(1000, false, false)
                }
            }
        }
    }
}
