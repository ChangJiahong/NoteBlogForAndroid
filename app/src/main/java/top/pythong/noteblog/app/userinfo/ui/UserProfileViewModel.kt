package top.pythong.noteblog.app.userinfo.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.pythong.noteblog.app.home.model.ArticleCardItem
import top.pythong.noteblog.app.login.model.LoggedInUser
import top.pythong.noteblog.app.userinfo.service.IUserProfileService
import top.pythong.noteblog.base.viewModel.BaseViewModel
import top.pythong.noteblog.data.Result

/**
 *
 * @author ChangJiahong
 * @date 2019/9/22
 */
class UserProfileViewModel(private val userProfileService: IUserProfileService) : BaseViewModel() {

    private val _user = MutableLiveData<LoggedInUser>()
    val user: LiveData<LoggedInUser> = _user

    private val _articles = MutableLiveData<ArrayList<ArticleCardItem>>()
    val articles: LiveData<ArrayList<ArticleCardItem>> = _articles

    var page: Int = 1
    private val size: Int = 20

    fun loadUser(username: String) = launch {
        val result: Result<LoggedInUser> = userProfileService.getUser(username)

        withContext(Dispatchers.Main) {
            if (result.isOk) {
                _user.value = result.viewData
            } else {
                postError(result.msgCode)
            }
        }

    }

    fun loadMore(username: String, refreshLayout: RefreshLayout) = launch {
        val articleList = if (page == 1) {
            ArrayList()
        } else {
            _articles.value ?: ArrayList()
        }

        val result = userProfileService.getArticles(username, page, size)
        if (result.isOk) {
            val pageInfo = result.viewData!!
            val newArticles = pageInfo.list!!
            newArticles.forEach {
                articleList.add(ArticleCardItem(it))
            }
            page = pageInfo.nextPage

            withContext(Dispatchers.Main) {
                _articles.value = articleList
                refreshLayout.finishLoadMore(1000, true, !pageInfo.hasNextPage)
            }
        } else {
            withContext(Dispatchers.Main) {
                postError(result.msgCode)
                refreshLayout.finishLoadMore(1000, false, false)
            }
        }

    }


}