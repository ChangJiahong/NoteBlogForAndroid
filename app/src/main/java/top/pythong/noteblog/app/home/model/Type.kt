package top.pythong.noteblog.app.home.model

/**
 *
 * @author ChangJiahong
 * @date 2019/8/27
 */
data class Type(
    /**
     * 名字
     */
    val name: String,

    /**
     * 类型
     */
    val type: String
) {
    companion object {
        /**
         * 标签
         */
        val TAG = "tag"

        /**
         * 分类
         */
        val CATEGORY = "category"
    }
}