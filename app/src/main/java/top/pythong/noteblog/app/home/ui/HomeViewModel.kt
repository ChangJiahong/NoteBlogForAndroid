package top.pythong.noteblog.app.home.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.pythong.noteblog.app.home.model.CardItem
import top.pythong.noteblog.app.home.service.IHomeService
import top.pythong.noteblog.base.viewModel.BaseViewModel

class HomeViewModel(private val homeService: IHomeService) : BaseViewModel() {

    val TAG = "HomeViewModel"

    private val _articles = MutableLiveData<ArrayList<CardItem>>()

    val articles: LiveData<ArrayList<CardItem>> = _articles

    private var page: Int = 1
    private val size: Int = 20

    fun loadMore(refreshLayout: RefreshLayout) = launch(Dispatchers.IO) {

        val articleList = _articles.value!!

        val result = homeService.getArticles(page, size)
        if (result.isOk) {
            val pageInfo = result.viewData!!
            val newArticles = pageInfo.list!!
            newArticles.forEach {
                articleList.add(CardItem(it))
            }
            page = pageInfo.nextPage

            withContext(Dispatchers.Main) {
                _articles.value = articleList
                refreshLayout.finishLoadMore(1000, true, !pageInfo.hasNextPage)
            }
        } else {
            withContext(Dispatchers.Main) {
                _error.value = result.msgCode
                refreshLayout.finishLoadMore(1000, false, false)
            }
        }

    }

    fun refresh(refreshLayout: RefreshLayout) = launch(Dispatchers.IO) {

        val result = homeService.getArticles(1, size)
        val articleList = ArrayList<CardItem>()
        if (result.isOk) {
            val pageInfo = result.viewData!!
            val newArticles = pageInfo.list!!
            newArticles.forEach {
                articleList.add(CardItem(it))
            }
            page = pageInfo.nextPage

            withContext(Dispatchers.Main) {
                _articles.value = articleList
                refreshLayout.finishRefresh(1000, true, !pageInfo.hasNextPage)
            }
        } else {
            withContext(Dispatchers.Main) {
                _error.value = result.msgCode
                refreshLayout.finishRefresh(1000, false, false)
            }
        }
    }

}
