package top.pythong.noteblog.app.articlemanager.fragment.article.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.coroutines.*
import top.pythong.noteblog.app.articlemanager.fragment.article.service.IArticlesService
import top.pythong.noteblog.app.articlemanager.model.SimpleArticle
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.data.Result
import top.pythong.noteblog.data.constant.MsgCode
import top.pythong.noteblog.utils.DateKit
import top.pythong.noteblog.utils.LoadDataHelper

class ArticlesViewModel(private val articlesService: IArticlesService) : BaseViewModel() {

    private val TAG = ArticlesViewModel::class.java.simpleName

    private val _articles = MutableLiveData<Pair<Boolean, ArrayList<SimpleArticle>>>()

    val article: LiveData<Pair<Boolean, ArrayList<SimpleArticle>>> = _articles

    private val loadDataHelper = LoadDataHelper<Article>()

    fun loadData(refreshLayout: RefreshLayout? = null, append: Boolean = false) = loadDataHelper.apply {
        result { page, size ->
            articlesService.getUserArticleList(page, size)
        }
        onSuccess { pageInfo, _ ->
            val articleList = ArrayList<SimpleArticle>()
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
            _articles.value = Pair(append, articleList)
        }
        onError(postError)
    }.loadData(refreshLayout, append)

    fun deleteArticle(
        id: String, callback: (resu: Triple<Boolean, Any?, MsgCode>) -> Unit = { }
    ) = launch {
        if (id.isBlank()) {
            return@launch
        }

        val result: Result<Any> = articlesService.deleteArticle(id)
        withContext(Dispatchers.Main) {
            callback(Triple(result.isOk, result.viewData, result.msgCode))
        }

    }

    fun publish(id: String, callback: (resu: Triple<Boolean, Any?, MsgCode>) -> Unit = { }) = launch {
        if (id.isBlank()) {
            return@launch
        }

        val result: Result<Any> = articlesService.publishArticle(id)
        withContext(Dispatchers.Main) {
            callback(Triple(result.isOk, result.viewData, result.msgCode))
        }
    }


}
