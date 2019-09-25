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
import top.pythong.noteblog.utils.LoadDataHelper

class ArchivesViewModel(private val archivesService: IArchivesService) : BaseViewModel() {

    private val _archives = MutableLiveData<Pair<Boolean, ArrayList<ArchiveView>>>()

    val archives: LiveData<Pair<Boolean, ArrayList<ArchiveView>>> = _archives

    private val loadDataHelper = LoadDataHelper<Archive>()

    fun loadData(refreshLayout: RefreshLayout, append: Boolean = false) = loadDataHelper.apply {
        result { page, size ->
            archivesService.getArchives(page, size)
        }
        onSuccess { pageInfo, _ ->
            val archiveList = ArrayList<ArchiveView>()
            val newArchives = pageInfo.list!!
            newArchives.forEach {
                archiveList.add(ArchiveView(it))
            }
            _archives.value = Pair(append, archiveList)
        }
        onError(postError)
    }.loadData(refreshLayout, append)

}
