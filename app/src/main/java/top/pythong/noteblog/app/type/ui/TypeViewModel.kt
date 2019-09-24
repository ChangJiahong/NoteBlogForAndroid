package top.pythong.noteblog.app.type.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.pythong.noteblog.app.home.model.ArticleCardItem
import top.pythong.noteblog.app.type.service.ITypeService
import top.pythong.noteblog.base.viewModel.BaseViewModel

/**
 *
 * @author ChangJiahong
 * @date 2019/9/1
 */
class TypeViewModel(private val typeService: ITypeService) : BaseViewModel() {

    private val TAG = TypeViewModel::class.java.simpleName

    private val _articles = MutableLiveData<Pair<Boolean, ArrayList<ArticleCardItem>>>()

    val articles: LiveData<Pair<Boolean, ArrayList<ArticleCardItem>>> = _articles

    private var page: Int = 1
    private val size: Int = 20

    private var isLoad = false

    fun loadData(refreshLayout: RefreshLayout, append: Boolean=false, type: String, typeName: String) =
        launch(Dispatchers.IO) {

            if (!append) {
                page = 1
                isLoad = true
            }

            if (!isLoad) {
                return@launch
            }

            val result = typeService.getArticlesByType(page, size, type, typeName)
            val articleList = ArrayList<ArticleCardItem>()
            if (result.isOk) {
                val pageInfo = result.viewData!!
                val newArticles = pageInfo.list!!
                newArticles.forEach {
                    articleList.add(ArticleCardItem(it))
                }
                page = pageInfo.nextPage

                Log.d(TAG, pageInfo.toString())
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