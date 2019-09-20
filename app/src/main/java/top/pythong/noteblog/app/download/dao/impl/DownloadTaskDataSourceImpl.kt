package top.pythong.noteblog.app.download.dao.impl

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import top.pythong.noteblog.app.download.dao.IDownloadTaskDataSource
import top.pythong.noteblog.app.download.model.DownloadResource
import top.pythong.noteblog.utils.MyDbOpenHelper

/**
 *
 * @author ChangJiahong
 * @date 2019/9/10
 */
class DownloadTaskDataSourceImpl(private val context: Context) : IDownloadTaskDataSource {

    val TAG = DownloadTaskDataSourceImpl::class.java.simpleName
    val TABLE = "TasksTable"

    /**
     * 插入下载任务
     */
    override fun insert(resource: DownloadResource) {
        val writeDataBase = MyDbOpenHelper.writeDb(context)
        val cv = ContentValues()
        cv.put("name", resource.name)
        cv.put("fileId", resource.fileId())
        cv.put("toPath", resource.toPath)
        cv.put("type", resource.type)
        cv.put("state", resource.state)
        cv.put("contentLen", resource.contentLen)
        cv.put("downloadLen", resource.downloadLen)
        val id = writeDataBase.insert(TABLE, null, cv)

        resource.id = id.toInt()
    }

    override fun selectById(id: Int): DownloadResource? {
        val readDatabase = MyDbOpenHelper.readDb(context)
        val sql = "SELECT * From $TABLE Where id = ?"
        val re = readDatabase.rawQuery(sql, arrayOf(id.toString()))
        re.moveToFirst()
        var res: DownloadResource? = null
        if (re.isFirst) {
            res = cursorToTarget(re)
        }
        re.close()
        return res
    }

    /**
     * 未下载完成
     */
    override fun selectNotCompleteByFileId(resource: DownloadResource): List<DownloadResource> {
        val tasks = ArrayList<DownloadResource>()
        if (resource.fileId().isNotBlank()) {
            val sql = "SELECT * From $TABLE Where fileId = ? and state != ? "
            val readDatabase = MyDbOpenHelper.readDb(context)
            val re = readDatabase.rawQuery(sql, arrayOf(resource.fileId(), DownloadResource.COMPLETE.toString()))
            re.moveToFirst()
            while (!re.isAfterLast) {
                val res = cursorToTarget(re)
                tasks.add(res)
                re.moveToNext()
            }
            re.close()
        }
        return tasks
    }

    override fun select(resource: DownloadResource): List<DownloadResource> {
        var sql = "SELECT * From $TABLE Where 1=1 "
        resource.apply {
            if (name.isNotBlank()) {
                sql += " and name = '$name'"
            }
            if (fileId().isNotBlank()) {
                sql += " and fileId = '${fileId()}'"
            }
            if (toPath.isNotBlank()) {
                sql += " and toPath = '$toPath'"
            }
            if (type.isNotBlank()) {
                sql += " and type = '$type"
            }
            if (state >= 0) {
                sql += " and state = $state"
            }
            if (contentLen >= 0) {
                sql += " and contentLen = $contentLen"
            }
            if (downloadLen >= 0) {
                sql += " and downloadLen = $downloadLen"
            }
        }
        val readDatabase = MyDbOpenHelper.readDb(context)
        val tasks = ArrayList<DownloadResource>()
        val re = readDatabase.rawQuery(sql, null)
        re.moveToFirst()
        while (!re.isAfterLast) {
            val res = cursorToTarget(re)
            tasks.add(res)
            re.moveToNext()
        }
        re.close()
        return tasks
    }

    /**
     * 查询全部
     */
    override fun selectAll(): List<DownloadResource> {
        val readDatabase = MyDbOpenHelper.readDb(context)
        val sql = "SELECT * From $TABLE"
        val tasks = ArrayList<DownloadResource>()
        val re = readDatabase.rawQuery(sql, null)
        re.moveToFirst()
        while (!re.isAfterLast) {
            val res = cursorToTarget(re)
            tasks.add(res)
            re.moveToNext()
        }
        re.close()
        return tasks
    }

    override fun update(resource: DownloadResource) {
        val writeDataBase = MyDbOpenHelper.writeDb(context)
        val cv = ContentValues()
        resource.apply {
            if (name.isNotBlank()) {
                cv.put("name", name)
            }
            if (fileId().isNotBlank()) {
                cv.put("fileId", fileId())
            }
            if (toPath.isNotBlank()) {
                cv.put("toPath", toPath)
            }
            if (state >= 0) {
                cv.put("state", state)
            }
            if (contentLen >= 0) {
                cv.put("contentLen", contentLen)
            }
            if (downloadLen >= 0) {
                cv.put("downloadLen", downloadLen)
            }
        }
        writeDataBase.update(TABLE, cv, "id = ?", arrayOf(resource.id.toString()))
    }

    override fun delete(resource: DownloadResource) {
        val writeDataBase = MyDbOpenHelper.writeDb(context)
        writeDataBase.delete(TABLE, "id = ?", arrayOf(resource.id.toString()))
    }

    private fun cursorToTarget(re: Cursor): DownloadResource {
        val res = DownloadResource.resource(
            name = re.getString(re.getColumnIndex("name")),
            fileId = re.getString(re.getColumnIndex("fileId")),
            toPath = re.getString(re.getColumnIndex("toPath")),
            type = re.getString(re.getColumnIndex("type"))
        )
        res.id = re.getInt(re.getColumnIndex("id"))
        res.state = re.getInt(re.getColumnIndex("state"))
        res.downloadLen = re.getLong(re.getColumnIndex("downloadLen"))
        res.contentLen = re.getLong(re.getColumnIndex("contentLen"))
        return res
    }
}