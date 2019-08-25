package top.pythong.noteblog.app.home.utils

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics

/**
 * 平滑滑动控制器
 * @author ChangJiahong
 * @date 2019/8/25
 */
class SmoothScrollLayoutManager(context: Context?) : LinearLayoutManager(context) {

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {

        val smoothScroller = object : LinearSmoothScroller(recyclerView!!.context) {
            // 返回：滑过1px时经历的时间(ms)。

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return 50f / displayMetrics.densityDpi
            }
        }

        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }
}