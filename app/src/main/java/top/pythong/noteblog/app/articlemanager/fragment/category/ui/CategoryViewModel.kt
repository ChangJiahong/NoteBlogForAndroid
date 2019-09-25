package top.pythong.noteblog.app.articlemanager.fragment.category.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.pythong.noteblog.app.archives.model.Archive
import top.pythong.noteblog.app.articlemanager.fragment.category.service.ICategoryService
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.data.Result
import top.pythong.noteblog.utils.LoadDataHelper

class CategoryViewModel(private val categoryService: ICategoryService) : BaseViewModel() {

    private val _archives = MutableLiveData<Pair<Boolean, List<Archive>>>()

    val archives: LiveData<Pair<Boolean, List<Archive>>> = _archives

    private val loadDataHelper = LoadDataHelper<Archive>()

    fun loadData(refreshLayout: RefreshLayout? = null, append: Boolean = false, type: String) = loadDataHelper.apply {
        result { page, size ->
            categoryService.getUCategoryArchives(page, size, type)
        }
        onSuccess { pageInfo, _ ->
            val newArchives = pageInfo.list!!
            _archives.value = Pair(append, newArchives)
        }
        onError(postError)
    }.loadData(refreshLayout, append)

}
