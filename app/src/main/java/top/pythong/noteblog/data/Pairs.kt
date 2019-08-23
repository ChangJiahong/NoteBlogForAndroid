package top.pythong.noteblog.data

/**
 * 参数对
 * @author ChangJiahong
 * @date 2019/8/23
 */
open class Pairs<T> {
    var pairs: MutableMap<String, T> = HashMap()
    open operator fun String.minus(value: T) {
        pairs[this] = value
    }
}