package top.pythong.noteblog.app.type.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.ArticleCardItem
import top.pythong.noteblog.app.type.service.ITypeService
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.utils.LoadDataHelper

/**
 *
 * @author ChangJiahong
 * @date 2019/9/1
 */
class TypeViewModel(private val typeService: ITypeService) : BaseViewModel() {

    private val TAG = TypeViewModel::class.java.simpleName

    private val _articles = MutableLiveData<Pair<Boolean, ArrayList<ArticleCardItem>>>()

    val articles: LiveData<Pair<Boolean, ArrayList<ArticleCardItem>>> = _articles

    private val loadDataHelper = LoadDataHelper<Article>()

    fun loadData(refreshLayout: RefreshLayout, append: Boolean=false, type: String, typeName: String) =
        loadDataHelper.apply {
            result { page, size ->
                typeService.getArticlesByType(page, size, type, typeName)
            }
            onSuccess { pageInfo, _ ->
            val articleList = ArrayList<ArticleCardItem>()
                val newArticles = pageInfo.list!!
                newArticles.forEach {
                    articleList.add(ArticleCardItem(it))
                }
                _articles.value = Pair(append, articleList)
            }
            onError(postError)
        }.loadData(refreshLayout, append)

}