package top.pythong.noteblog.utils

import android.content.Context
import android.content.SharedPreferences
import top.pythong.noteblog.data.Pairs
import top.pythong.noteblog.exc.ClassNotSupportedException

/**
 * SharedPreferences 帮助类
 * @author ChangJiahong
 * @date 2019/8/22
 */
class SharedPreferencesHelper(context: Context, domain: String = CONFIG) {


    companion object {
        /**
         * 默认存储地址
         */
        const val CONFIG = "config"
    }

    /**
     * SharedPreferences 持有
     */
    private var sp: SharedPreferences = context.getSharedPreferences(domain, Context.MODE_PRIVATE)

    val editHelper = { EditorHelper(sp) }

    fun getString(key: String, defValue: String): String {
        return sp.getString(key, defValue) ?: ""
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sp.getBoolean(key, defValue)
    }

    fun getFloat(key: String, defValue: Float): Float {
        return sp.getFloat(key, defValue)
    }

    fun getInt(key: String, defValue: Int): Int {
        return sp.getInt(key, defValue)
    }

    fun getLong(key: String, defValue: Long): Long {
        return sp.getLong(key, defValue)
    }

    fun getStringSet(key: String, defValue: Set<String>): Set<String> {
        return sp.getStringSet(key, defValue) ?: setOf()
    }

    fun contains(key: String): Boolean {
        return sp.contains(key)
    }

    class EditorHelper(sph: SharedPreferences) {
        private var editor: SharedPreferences.Editor = sph.edit()

        /**
         * 保存仅支持String、Int、Long、Float、Boolean基本类型
         * @throws ClassNotSupportedException
         */
        fun put(key: String, value: Any): EditorHelper {
            when (value) {
                is String -> editor.putString(key, value)
                is Int -> editor.putInt(key, value)
                is Long -> editor.putLong(key, value)
                is Float -> editor.putFloat(key, value)
                is Boolean -> editor.putBoolean(key, value)
                else -> throw ClassNotSupportedException("SharedPreferences不支持存入${value.javaClass.canonicalName}类型的参数")
            }
            return this
        }

        /**
         * 存入set<Stirng>类型的
         */
        fun put(key: String, value: Set<String>): EditorHelper {
            editor.putStringSet(key, value)
            return this
        }


        /**
         * 以参数类型存入
         * @throws ClassNotSupportedException
         */
        fun put(makePairs: Pairs<Any>.() -> Unit): EditorHelper {
            val paramPairs = Pairs<Any>()
            paramPairs.makePairs()
            paramPairs.pairs.forEach {
                put(it.key, it.value)
            }
            return this
        }


        /**
         * 提交到硬盘
         */
        fun commit(): Boolean {
            return editor.commit()
        }
    }


}

/**
 * Context扩展函数
 * 包装SharedPreferencesHelper类，在内部处理SharedPreferences储存
 */
fun Context.putToSharedPreferences(
    domain: String = SharedPreferencesHelper.CONFIG, dos: SharedPreferencesHelper.EditorHelper.() -> Unit
): SharedPreferencesHelper.EditorHelper {
    return SharedPreferencesHelper(this, domain).editHelper().apply {
        // 操作
        dos()
        // 提交
        commit()
    }
}

/**
 * Context扩展函数
 * 包装SharedPreferencesHelper类，在内部处理SharedPreferences储存
 */
fun Context.getFromSharedPreferences(
    domain: String = SharedPreferencesHelper.CONFIG, dos: SharedPreferencesHelper.() -> Unit
): SharedPreferencesHelper {
    return SharedPreferencesHelper(this, domain).apply {
        // 操作
        dos()
    }
}


/**
 * 从SharedPreferences获取String
 */
fun Context.getStringFromSharedPreferences(
    key: String, defValue: String = "", domain: String = SharedPreferencesHelper.CONFIG
): String {
    return SharedPreferencesHelper(this, domain).getString(key, defValue)
}

/**
 * 从SharedPreferences获取String Set
 */
fun Context.getStringSetFromSharedPreferences(
    key: String, defValue: Set<String> = setOf(), domain: String = SharedPreferencesHelper.CONFIG
): Set<String> {
    return SharedPreferencesHelper(this, domain).getStringSet(key, defValue)
}

/**
 * 从SharedPreferences获取Boolean
 */
fun Context.getBooleanFromSharedPreferences(
    key: String, defValue: Boolean = false, domain: String = SharedPreferencesHelper.CONFIG
): Boolean {
    return SharedPreferencesHelper(this, domain).getBoolean(key, defValue)
}

/**
 * 从SharedPreferences获取Float
 */
fun Context.getFloatFromSharedPreferences(
    key: String, defValue: Float = 0F, domain: String = SharedPreferencesHelper.CONFIG
): Float {
    return SharedPreferencesHelper(this, domain).getFloat(key, defValue)
}

/**
 * 从SharedPreferences获取Int
 */
fun Context.getIntFromSharedPreferences(
    key: String, defValue: Int = 0, domain: String = SharedPreferencesHelper.CONFIG
): Int {
    return SharedPreferencesHelper(this, domain).getInt(key, defValue)
}

/**
 * 从SharedPreferences获取Long
 */
fun Context.getLongFromSharedPreferences(
    key: String, defValue: Long = 0L, domain: String = SharedPreferencesHelper.CONFIG
): Long {
    return SharedPreferencesHelper(this, domain).getLong(key, defValue)
}
