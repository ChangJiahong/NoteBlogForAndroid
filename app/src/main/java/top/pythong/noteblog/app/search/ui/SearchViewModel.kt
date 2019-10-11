package top.pythong.noteblog.app.search.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.database.Cursor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.toast
import top.pythong.noteblog.app.search.model.SearchHistory
import top.pythong.noteblog.app.search.service.ISearchService
import top.pythong.noteblog.base.viewModel.BaseViewModel
import kotlin.coroutines.CoroutineContext

/**
 *
 * @author ChangJiahong
 * @date 2019/9/26
 */
class SearchViewModel(val searchService: ISearchService) : BaseViewModel() {

    private val _historys = MutableLiveData<List<SearchHistory>>()
    val historys: LiveData<List<SearchHistory>> = _historys

    private val _searchResults = MutableLiveData<Pair<Boolean, Any>>()
    val searchResults: LiveData<Pair<Boolean, Any>> = _searchResults

    fun getSuggests(name: String) = launch {
        val histories = searchService.getSuggests(name)
        withContext(Dispatchers.Main) {
            _historys.value = histories
        }
    }

    fun getAllHistroy() = launch {
        val histories = searchService.getAll()
        withContext(Dispatchers.Main) {
            _historys.value = histories
        }

    }

    fun search(query: String) = launch {
        searchService.saveSearchHistory(query)

        delay(2000)

        _searchResults.postValue(Pair(true, Any()))

    }

    fun remove(id: Int) = launch {
        searchService.removeSearchHistory(id.toString())
    }

    fun delAll() = launch {
        searchService.removeAll()
    }

}