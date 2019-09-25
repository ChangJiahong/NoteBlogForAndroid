package top.pythong.noteblog.app.home.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.ArticleCardItem
import top.pythong.noteblog.app.home.service.IHomeService
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.utils.LoadDataHelper

class HomeViewModel(private val homeService: IHomeService) : BaseViewModel() {

    val TAG = "HomeViewModel"

    private val _articles = MutableLiveData<Pair<Boolean, ArrayList<ArticleCardItem>>>()

    val articles: LiveData<Pair<Boolean, ArrayList<ArticleCardItem>>> = _articles

    private val loadDataHelper = LoadDataHelper<Article>()

    fun loadData(refreshLayout: RefreshLayout, append: Boolean = false) = loadDataHelper.apply {
        result { page, size ->
            homeService.getArticles(page, size)
        }
        onSuccess { pageInfo, _ ->
            val newArticles = pageInfo.list!!
            val articleList = ArrayList<ArticleCardItem>()
            newArticles.forEach {
                articleList.add(ArticleCardItem(it))
            }
            _articles.value = Pair(append, articleList)
        }
        onError(postError)
    }.loadData(refreshLayout, append)

}
