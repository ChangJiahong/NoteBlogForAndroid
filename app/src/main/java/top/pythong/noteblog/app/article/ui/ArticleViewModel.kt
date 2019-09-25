package top.pythong.noteblog.app.article.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.pythong.noteblog.app.article.model.ArticleView
import top.pythong.noteblog.app.article.service.IArticleService
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.data.Result

class ArticleViewModel(private val articleService: IArticleService) : BaseViewModel() {

    private val _articleView = MutableLiveData<ArticleView>()
    val cardItem: LiveData<ArticleView> = _articleView

    fun loadArticle(articleId: String) = launch(Dispatchers.IO) {
        val result: Result<Article> = articleService.getArticle(articleId)

        withContext(Dispatchers.Main) {
            if (result.isOk) {
                _articleView.value = ArticleView(result.viewData!!)
            } else {
                postError(result.msgCode)
            }
        }

    }
}
