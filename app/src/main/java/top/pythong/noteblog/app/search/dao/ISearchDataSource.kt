package top.pythong.noteblog.app.search.dao

import top.pythong.noteblog.app.search.model.SearchHistory

interface ISearchDataSource {

    fun insert(name: String)

    fun updateTimeByName(name: String)

    fun deleteById(id: String)

    fun existName(name: String): Boolean

    fun selectByLikeName(name: String): List<SearchHistory>

    fun selectAll(): List<SearchHistory>

    fun deleteAll()

}
