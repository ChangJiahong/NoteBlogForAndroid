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

class CategoryViewModel(private val categoryService: ICategoryService) : BaseViewModel() {

    private val _archives = MutableLiveData<Pair<Boolean, List<Archive>>>()

    val archives: LiveData<Pair<Boolean, List<Archive>>> = _archives


    private var page: Int = 1
    private val size: Int = 20

    private var noHasMore = false

    fun loadData(refreshLayout: RefreshLayout? = null, append: Boolean = false, type: String) = launch(Dispatchers.IO) {
        if (!append) {
            // 是刷新,重置page为第一页
            page = 1
            noHasMore = false
        }

        if (noHasMore) {
            withContext(Dispatchers.Main) {
                refreshLayout?.finishLoadMore(1000, true, noHasMore)
            }
            return@launch
        }

        val result: Result<PageInfo<Archive>> = categoryService.getUCategoryArchives(page, size, type)
        if (result.isOk) {
            val pageInfo = result.viewData!!
            val newArchives = pageInfo.list!!

            page = pageInfo.nextPage
            noHasMore = !pageInfo.hasNextPage

            withContext(Dispatchers.Main) {
                _archives.value = Pair(append, newArchives)
                refreshLayout?.run {
                    if (append) {
                        finishLoadMore(1000, true, !pageInfo.hasNextPage)
                    } else {
                        finishRefresh(1000, true, !pageInfo.hasNextPage)
                    }
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                postError(result.msgCode)

                refreshLayout?.run {
                    if (append) {
                        finishLoadMore(1000, false, false)
                    } else {
                        finishRefresh(1000, false, false)
                    }
                }
            }
        }
    }
}
