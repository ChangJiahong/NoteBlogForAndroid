package top.pythong.noteblog.base.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.scwang.smartrefresh.layout.util.SmartUtil.fling
import android.R.string.cancel
import android.widget.OverScroller
import com.scwang.smartrefresh.layout.util.SmartUtil.fling


/**
 *
 * @author ChangJiahong
 * @date 2019/8/29
 */
class ArticleBrowsingLayout : LinearLayout {

    val TAG = ArticleBrowsingLayout::class.java.simpleName

    lateinit var mTopView: View

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()

        mTopView = getChildAt(0)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var height = 0
        for (i in 0 until childCount) {
            val c = getChildAt(i)
            val lp = c.layoutParams as MarginLayoutParams
            measureChild(c, widthMeasureSpec, heightMeasureSpec)
            height += c.measuredHeight
        }

        setMeasuredDimension(widthMeasureSpec, height)
    }


    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {

        return nestedScrollAxes == SCROLL_AXIS_VERTICAL
    }


    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        Log.d(TAG, "消费前：dx:$dx, dy:$dy")
        // dy 纵轴 负值向下滑动

        val h = mTopView.height
        val hidden = dy > 0 && Math.abs(scrollY) < h
        // 子view不能往下滑
        val show = dy < 0 && !target.canScrollVertically(-1)
        // 消费高度
        var consumptionHeight = 0
        if (hidden) {
            // 向上滚动 隐藏
            // top控件显示出的高度
            val headerShowHeight = h - scrollY

            // 默认全部消费
            consumptionHeight = dy

            if (dy > headerShowHeight) {
                // 滑动距离大于显示高度，只消费显示的高度
                consumptionHeight = headerShowHeight
            }
        } else if (show) {
            // 向上滚动 隐藏
            // top控件隐藏出的高度
            val headerHiddenHeight = Math.abs(scrollY)

            // 默认消费
            consumptionHeight = dy

            if (Math.abs(dy) > headerHiddenHeight) {
                // 滑动距离大于隐藏高度，只消费隐藏的高度
                consumptionHeight = headerHiddenHeight * -1
            }
        }

        consumed[1] = consumptionHeight
        scrollBy(0, consumptionHeight)
    }

    override fun onNestedScroll(target: View?, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {

        Log.d(TAG, "未消耗距离：$dyUnconsumed")
    }

    override fun onNestedFling(target: View?, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {

        Log.d(TAG, "能否消耗：" + consumed)
        return super.onNestedFling(target, velocityX, velocityY, consumed)
    }


    override fun onNestedPreFling(target: View?, velocityX: Float, velocityY: Float): Boolean {

        return super.onNestedPreFling(target, velocityX, velocityY)
    }


}