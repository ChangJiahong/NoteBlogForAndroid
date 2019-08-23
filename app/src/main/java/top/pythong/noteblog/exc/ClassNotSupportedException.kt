package top.pythong.noteblog.exc

/**
 * # 类不支持异常
 * 该类将从[SharedPreferencesHelper][top.pythong.noteblog.utils.SharedPreferencesHelper.put]
 * 抛出，对<code>put</code>方法不支持的类抛出运行时异常
 * @see top.pythong.noteblog.utils.SharedPreferencesHelper.put(java.lang.String, Any)
 * @author ChangJiahong
 * @date 2019/8/22
 */
class ClassNotSupportedException(msg: String = "") : RuntimeException(msg)