package top.pythong.noteblog.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author ChangJiahong
 * @date 2019/8/27
 */
object DateKit {

    fun timesAgo(dateS: String, format: String): String {
        val locale = Locale.CHINA
        val sdf = SimpleDateFormat(format, locale)
        val date = sdf.parse(dateS)!!
        // 时间差 s
        val diff = (Date().time - date.time) / 1000
        return when (diff) {
            // 0~59s
            in 0 until 60 -> "刚刚"
            // 1~60min
            in 60 until 60 * 60 -> "${diff / 60}分钟前"
            // 1~24h
            in 60 * 60 until 60 * 60 * 24 -> "${diff / (60 * 60)}小时前"
            // 1~30天
            in 60 * 60 * 24 until 60 * 60 * 24 * 30 -> "${diff / (60 * 60 * 24)}天前"
            // 1~12月
            in 60 * 60 * 24 * 30 until 60 * 60 * 24 * 30 * 12 -> "${diff / (60 * 60 * 24 * 30)}月前"
            else -> "${diff / (60 * 60 * 24 * 30 * 12)}年前"
        }
    }

    fun format(dateS: String, fromFormat: String, toFormat: String): String {
        val locale = Locale.CHINA
        val fromSdf = SimpleDateFormat(fromFormat, locale)
        val date = fromSdf.parse(dateS)!!
        val toSdf = SimpleDateFormat(toFormat, locale)
        return toSdf.format(date)
    }
}