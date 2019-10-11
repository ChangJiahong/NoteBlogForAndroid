package top.pythong.noteblog.app.search.service

import top.pythong.noteblog.app.search.model.SearchHistory

interface ISearchService {

    fun getSuggests(name: String): List<SearchHistory>

    fun saveSearchHistory(name: String)

    fun removeSearchHistory(id: String)

    fun getAll(): List<SearchHistory>

    fun removeAll()
}
