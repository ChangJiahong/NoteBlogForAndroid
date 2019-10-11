package top.pythong.noteblog.app.search.service.impl

import android.content.Context
import top.pythong.noteblog.app.search.dao.ISearchDataSource
import top.pythong.noteblog.app.search.model.SearchHistory
import top.pythong.noteblog.app.search.service.ISearchService

/**
 *
 * @author ChangJiahong
 * @date 2019/9/26
 */
class SearchServiceImpl(private val context: Context, private val searchDataSource: ISearchDataSource) :
    ISearchService {

    override fun saveSearchHistory(name: String) {
        if (searchDataSource.existName(name)) {
            searchDataSource.updateTimeByName(name)
        } else {
            searchDataSource.insert(name)
        }
    }

    override fun getSuggests(name: String): List<SearchHistory> {
        return searchDataSource.selectByLikeName(name)
    }

    override fun getAll(): List<SearchHistory> {
        return searchDataSource.selectAll()
    }

    override fun removeSearchHistory(id: String) {
        searchDataSource.deleteById(id)
    }

    override fun removeAll() {
        searchDataSource.deleteAll()
    }
}