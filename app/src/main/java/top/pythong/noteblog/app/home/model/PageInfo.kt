package top.pythong.noteblog.app.home.model

import java.nio.file.Files.size


/**
 * 包装Page对象
 * @author ChangJiahong
 * @date 2019/8/26
 */
class PageInfo<T> {
    //当前页
    var pageNum: Int = 0
    //每页的数量
    var pageSize: Int = 0
    //当前页的数量
    var size: Int = 0

    //由于startRow和endRow不常用，这里说个具体的用法
    //可以在页面中"显示startRow到endRow 共size条数据"

    //当前页面第一个元素在数据库中的行号
    var startRow: Int = 0
    //当前页面最后一个元素在数据库中的行号
    var endRow: Int = 0
    //总记录数
    var total: Long = 0
    //总页数
    var pages: Int = 0
    //结果集
    var list: List<T>? = null

    //前一页
    var prePage: Int = 0
    //下一页
    var nextPage: Int = 0

    //是否为第一页
    var isFirstPage = false
    //是否为最后一页
    var isLastPage = false
    //是否有前一页
    var hasPreviousPage = false
    //是否有下一页
    var hasNextPage = false
    //导航页码数
    var navigatePages: Int = 0
    //所有导航页号
    var navigatepageNums: IntArray? = null
    //导航条上的第一页
    // firstPage就是1, 此函数获取的是导航条上的第一页, 容易产生歧义
    var firstPage: Int = 0
    //导航条上的最后一页
    // 请用getPages()来获取最后一页, 此函数获取的是导航条上的最后一页, 容易产生歧义.
    var lastPage: Int = 0

    override fun toString(): String {
        val sb = StringBuffer("PageInfo{")
        sb.append("pageNum=").append(pageNum)
        sb.append(", pageSize=").append(pageSize)
        sb.append(", size=").append(size)
        sb.append(", startRow=").append(startRow)
        sb.append(", endRow=").append(endRow)
        sb.append(", total=").append(total)
        sb.append(", pages=").append(pages)
        sb.append(", list=").append(list)
        sb.append(", prePage=").append(prePage)
        sb.append(", nextPage=").append(nextPage)
        sb.append(", isFirstPage=").append(isFirstPage)
        sb.append(", isLastPage=").append(isLastPage)
        sb.append(", hasPreviousPage=").append(hasPreviousPage)
        sb.append(", hasNextPage=").append(hasNextPage)
        sb.append(", navigatePages=").append(navigatePages)
        sb.append(", firstPage=").append(firstPage)
        sb.append(", lastPage=").append(lastPage)
        sb.append(", navigatepageNums=")
        if (navigatepageNums == null)
            sb.append("null")
        else {
            sb.append('[')
            for (i in navigatepageNums!!.indices)
                sb.append(if (i == 0) "" else ", ").append(navigatepageNums!![i])
            sb.append(']')
        }
        sb.append('}')
        return sb.toString()
    }

    companion object {
        private val serialVersionUID = 1L
    }
}

