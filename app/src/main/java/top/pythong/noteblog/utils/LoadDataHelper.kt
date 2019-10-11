package top.pythong.noteblog.utils

import com.scwang.smartrefresh.layout.api.RefreshLayout
import kotlinx.coroutines.*
import top.pythong.noteblog.app.home.model.PageInfo
import top.pythong.noteblog.data.Result
import top.pythong.noteblog.data.constant.MsgCode

/**
 *
 * @author ChangJiahong
 * @date 2019/9/24
 */
class LoadDataHelper<T> : CoroutineScope by MainScope() {

    private var page: Int = 1
    private val size: Int = 20

    private var noHasMore = false

    private lateinit var result: (page: Int, size: Int) -> Result<PageInfo<T>>

    private lateinit var success: (pageInfo: PageInfo<T>, msgCode: MsgCode) -> Unit

    private lateinit var error: (msgCode: MsgCode) -> Unit

    fun result(result: (page: Int, size: Int) -> Result<PageInfo<T>>): LoadDataHelper<T> {
        this.result = result
        return this
    }

    fun onSuccess(success: (pageInfo: PageInfo<T>, msgCode: MsgCode) -> Unit): LoadDataHelper<T> {
        this.success = success
        return this
    }

    fun onError(error: (msgCode: MsgCode) -> Unit): LoadDataHelper<T> {
        this.error = error
        return this
    }

    /**
     * @param append 获取数据是否追加
     */
    fun loadData(refreshLayout: RefreshLayout? = null, append: Boolean = false) = launch {

        if (!append) {
            // 是刷新,重置page为第一页
            page = 1
            noHasMore = false
        }

        if (noHasMore) {
            refreshLayout?.finishLoadMore(1000, true, noHasMore)
            return@launch
        }

        val job = async(Dispatchers.IO) {
            result(page, size)
        }

        val res = job.await()

        if (res.isOk) {
            val pageInfo = res.viewData!!
            success(pageInfo, res.msgCode)
            page = pageInfo.nextPage
            noHasMore = !pageInfo.hasNextPage
            refreshLayout?.run {
                if (append) {
                    finishLoadMore(1000, true, !pageInfo.hasNextPage)
                } else {
                    finishRefresh(1000, true, !pageInfo.hasNextPage)
                }
            }
        } else {
            error(res.msgCode)
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