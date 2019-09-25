package top.pythong.noteblog.app.articlemanager.fragment.article.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.coroutines.*
import top.pythong.noteblog.app.articlemanager.fragment.article.service.IArticlesService
import top.pythong.noteblog.app.articlemanager.model.SimpleArticle
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.data.Result
import top.pythong.noteblog.utils.DateKit

class ArticlesViewModel(private val articlesService: IArticlesService) : BaseViewModel() {

    private val TAG = ArticlesViewModel::class.java.simpleName

    private val _articles = MutableLiveData<Pair<Boolean, ArrayList<SimpleArticle>>>()

    val article: LiveData<Pair<Boolean, ArrayList<SimpleArticle>>> = _articles


    private var page: Int = 1
    private val size: Int = 20

    private var noHasMore = false

    fun loadData(refreshLayout: RefreshLayout? = null, append: Boolean = false) = launch(Dispatchers.IO) {

        if (!append) {
            // 是刷新,重置page为第一页
            page = 1
            noHasMore = false
        }

        if (noHasMore){
            withContext(Dispatchers.Main) {
                refreshLayout?.finishLoadMore(1000, true, noHasMore)
            }
            return@launch
        }

        val result: Result<PageInfo<Article>> = articlesService.getUserArticleList(page, size)
        val articleList = ArrayList<SimpleArticle>()
        if (result.isOk) {
            val pageInfo = result.viewData!!
            val newArticles = pageInfo.list!!
            newArticles.forEach {
                articleList.add(
                    SimpleArticle(
                        it.id,
                        it.title,
                        it.info,
                        it.hits,
                        DateKit.format(it.created, "yyyy-MM-dd HH:mm:ss", "yyyy年MM月dd日"),
                        it.status
                    )
                )
            }
            page = pageInfo.nextPage
            noHasMore = !pageInfo.hasNextPage

            withContext(Dispatchers.Main) {
                Log.d(TAG, "刷新${articleList.hashCode()}")
                _articles.value = Pair(append, articleList)
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
