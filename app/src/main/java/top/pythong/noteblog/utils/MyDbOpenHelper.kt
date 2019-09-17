package top.pythong.noteblog.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import top.pythong.noteblog.data.SqlString

/**
 * Created by CJH
 * on 2018/5/31
 */
class MyDbOpenHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {
    val TAG = "MyDbOpenHelper"

    /**
     * 单例模式
     */
    companion object {
        var instence: MyDbOpenHelper? = null

        /**
         * 数据库名
         */
        val DB_NAME = "NoteBlog.db"


        @Synchronized
        fun getInstence(context: Context): MyDbOpenHelper {
            if (instence == null) {
                instence = MyDbOpenHelper(context)
            }
            return instence!!
        }

        // 写 数据库 线程对象
        private val wtol = ThreadLocal<SQLiteDatabase>()

        // 读 数据库 线程对象
        private val rtol = ThreadLocal<SQLiteDatabase>()

        fun writeDb(context: Context): SQLiteDatabase {
            var db = wtol.get()
            if (db == null || !db.isOpen) {
                db = MyDbOpenHelper(context).writableDatabase
                wtol.set(db)
            }
            return db!!
        }

        fun readDb(context: Context): SQLiteDatabase {
            var db = rtol.get()
            if (db == null || !db.isOpen) {
                db = MyDbOpenHelper(context).readableDatabase
                rtol.set(db)
            }
            return db!!
        }

    }

    /**
     * 更新数据库
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    val TasksTable = "TasksTable"

    override fun onCreate(db: SQLiteDatabase?) {

        Log.d(TAG, "createDB ${DB_NAME} ,loading...！")

        db!!.apply {
            execSQL(SqlString.TasksTable)
        }

        Log.d(TAG, "createDB ${DB_NAME} successful！")

    }

    /**
     * 获取数据库名
     */
    fun getDBName() = when (instence != null) {
        true -> instence!!.databaseName
        false -> DB_NAME
    }


    fun cleanDataBase() {
        var db = writableDatabase

        db.delete(TasksTable, null, null)
    }
}