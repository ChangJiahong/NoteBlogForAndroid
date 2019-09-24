package top.pythong.noteblog.app.home.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.pythong.noteblog.app.home.model.ArticleCardItem
import top.pythong.noteblog.app.home.service.IHomeService
import top.pythong.noteblog.base.viewModel.BaseViewModel

class HomeViewModel(private val homeService: IHomeService) : BaseViewModel() {

    val TAG = "HomeViewModel"

    private val _articles = MutableLiveData<Pair<Boolean, ArrayList<ArticleCardItem>>>()

    val articles: LiveData<Pair<Boolean, ArrayList<ArticleCardItem>>> = _articles

    private var page: Int = 1
    private val size: Int = 20

    fun loadData(refreshLayout: RefreshLayout, append: Boolean = false) = launch(Dispatchers.IO) {

        if (!append) {
            page = 1
        }

        val articleList = ArrayList<ArticleCardItem>()

        val result = homeService.getArticles(page, size)
        if (result.isOk) {
            val pageInfo = result.viewData!!
            val newArticles = pageInfo.list!!
            newArticles.forEach {
                articleList.add(ArticleCardItem(it))
            }
            page = pageInfo.nextPage

            withContext(Dispatchers.Main) {
                _articles.value = Pair(append, articleList)
                if (append) {
                    refreshLayout.finishLoadMore(1000, true, !pageInfo.hasNextPage)
                } else {
                    refreshLayout.finishRefresh(1000, true, !pageInfo.hasNextPage)
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                _error.value = result.msgCode
                if (append) {
                    refreshLayout.finishLoadMore(1000, false, false)
                } else {
                    refreshLayout.finishRefresh(1000, false, false)
                }
            }
        }

    }
}
