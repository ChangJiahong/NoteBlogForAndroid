package top.pythong.noteblog.app.search.dao.impl

import android.content.ContentValues
import android.content.Context
import top.pythong.noteblog.app.search.dao.ISearchDataSource
import top.pythong.noteblog.app.search.model.SearchHistory
import top.pythong.noteblog.data.SqlString
import top.pythong.noteblog.utils.MyDbOpenHelper
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * @author ChangJiahong
 * @date 2019/9/26
 */
class SearchDataSourceImpl(private val context: Context) : ISearchDataSource {

    val TABLE = SqlString.SearchHistory.name

    override fun insert(name: String) {
        val writeDb = MyDbOpenHelper.writeDb(context)
        val cv = ContentValues()
        cv.put("name", name)
        cv.put("created", Date().time)
        writeDb.insert(TABLE, null, cv)
    }

    override fun updateTimeByName(name: String) {
        val writeDb = MyDbOpenHelper.writeDb(context)
        val cv = ContentValues()
        cv.put("created", Date().time)
        writeDb.update(TABLE, cv, "name = ?", arrayOf(name))
    }

    override fun deleteById(id: String) {
        val writeDb = MyDbOpenHelper.writeDb(context)
        writeDb.delete(TABLE, "_id = ?", arrayOf(id))
    }

    override fun deleteAll() {
        val writeDb = MyDbOpenHelper.writeDb(context)
        writeDb.delete(TABLE, null, null)
    }

    override fun existName(name: String): Boolean {
        val readeDb = MyDbOpenHelper.readDb(context)
        val sql = "select name from $TABLE where name = ?"
        val re = readeDb.rawQuery(sql, arrayOf(name))
        re.moveToFirst()
        if (re.isLast) {
            return true
        }
        re.close()
        return false
    }

    override fun selectByLikeName(name: String): List<SearchHistory> {
        val readeDb = MyDbOpenHelper.readDb(context)
        val sql = "select _id, name, created from $TABLE where name like '%$name%' order by created desc"
        val re = readeDb.rawQuery(sql, arrayOf())
        re.moveToFirst()
        val histories = ArrayList<SearchHistory>()
        while (!re.isAfterLast) {
            val history = SearchHistory(
                id = re.getInt(re.getColumnIndex("_id")),
                name = re.getString(re.getColumnIndex("name")),
                time = Date(re.getLong(re.getColumnIndex("created")))
            )
            histories.add(history)
            re.moveToNext()
        }
        re.close()
        return histories
    }

    override fun selectAll(): List<SearchHistory> {
        val readeDb = MyDbOpenHelper.readDb(context)
        val sql = "select _id, name, created from $TABLE order by created desc"
        val re = readeDb.rawQuery(sql, null)
        re.moveToFirst()
        val histories = ArrayList<SearchHistory>()
        while (!re.isAfterLast) {
            val history = SearchHistory(
                id = re.getInt(re.getColumnIndex("_id")),
                name = re.getString(re.getColumnIndex("name")),
                time = Date(re.getLong(re.getColumnIndex("created")))
            )
            histories.add(history)
            re.moveToNext()
        }
        re.close()
        return histories
    }

}