package top.pythong.noteblog.app.home.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.ArticleView
import top.pythong.noteblog.data.Result

class HomeViewModel : ViewModel() {

    private val _articles = MutableLiveData<Result<ArticleView>>()

    val articles: LiveData<Result<ArticleView>> = _articles


    fun loadMore(page: Int, size: Int): Pair<Boolean, Boolean> {

        val articleList = ArrayList(_articles.value!!.viewData!!.articles)
        if (page > 5) {
            _articles.value = Result.ok(ArticleView(articleList))
            return Pair(first = true, second = false)
        }
        for (i in 1..size) {
            articleList.add(Article("文章标题"))
        }

        _articles.value = Result.ok(ArticleView(articleList))
        return Pair(first = true, second = true)
    }

    fun refresh(size: Int): Boolean {
        val articleList = ArrayList<Article>()
        for (i in 1..size) {
            articleList.add(Article("文章标题"))
        }
        _articles.value = Result.ok(ArticleView(articleList))
        return true
    }

}
