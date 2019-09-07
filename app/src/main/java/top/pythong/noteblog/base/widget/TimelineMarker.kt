package top.pythong.noteblog.base.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import org.jetbrains.anko.dip
import top.pythong.noteblog.R


/**
 * 时间轴控件
 * @author ChangJiahong
 * @date 2019/9/2
 */
class TimelineMarker : View {

    companion object {
        const val HEAD = 0
        const val BODY = 1
        const val FOOT = 2
        const val ONLY_ONE = 3
    }

    /**
     * 类型
     */
    private var mScheme = TimelineMarker.BODY

    /**
     * 中心标志
     */
    private lateinit var mMiddleMarker: Drawable
    private var mMarkerSize: Int = dip(30)

    /**
     * 线条
     */
    private lateinit var mTopLine: Drawable
    private lateinit var mBottomLine: Drawable
    private var mLineWidth: Int = dip(1)


    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init(context, attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init(context, attributeSet)
    }


    fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val tp = getContext().obtainStyledAttributes(attrs, R.styleable.TimelineMarker)
            try {
                mScheme = tp.getInteger(R.styleable.TimelineMarker_scheme, TimelineMarker.BODY)
                mMiddleMarker =
                    tp.getDrawable(R.styleable.TimelineMarker_middle_marker) ?: ColorDrawable(Color.RED)
                mMarkerSize = tp.getDimension(R.styleable.TimelineMarker_marker_size, mMarkerSize.toFloat()).toInt()
                mLineWidth = tp.getDimension(R.styleable.TimelineMarker_line_width, mLineWidth.toFloat()).toInt()
                val lineColor = tp.getColor(R.styleable.TimelineMarker_line_color, Color.GRAY)
                val topLineColor = tp.getColor(R.styleable.TimelineMarker_top_line_color, lineColor)
                val bottomLineColor = tp.getColor(R.styleable.TimelineMarker_bottom_line_color, lineColor)
                mTopLine = ColorDrawable(topLineColor)
                mBottomLine = ColorDrawable(bottomLineColor)
            } finally {
                tp.recycle()
            }


        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mMiddleMarker.draw(canvas)

        if (mScheme == BODY || mScheme == FOOT) {
            mTopLine.draw(canvas)
        }

        if (mScheme == BODY || mScheme == HEAD) {
            mBottomLine.draw(canvas)
        }

    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateDrawableSize()
    }

    private fun calculateDrawableSize() {
        val h = height
        val w = width

        val markerSize = Math.min(mMarkerSize, Math.min(w, h))

        val l = (w - markerSize - paddingRight - paddingLeft) / 2 + paddingLeft
        val t = (h - markerSize) / 2

        mMiddleMarker.setBounds(l, t, l + markerSize, t + markerSize)

        val lineWidth = Math.min(mLineWidth, Math.min(w, h))

        // 线条
        // height
        val lh = t
        // top line
        // left
        val tl = (w - lineWidth - paddingRight - paddingLeft) / 2 + paddingLeft
        // top
        val tt = 0
        mTopLine.setBounds(tl, tt, tl + lineWidth, tt + lh)

        // bottom line
        // left
        val bl = (w - lineWidth - paddingRight - paddingLeft) / 2 + paddingLeft
        // top
        val bt = t + markerSize
        mBottomLine.setBounds(bl, bt, bl + lineWidth, bt + lh)

    }


/*
    private fun calculateDrawableSize() {
        val h = height
        val w = width

        val markerSize = Math.min(mMarkerSize, Math.min(w, h))

        val l = (w - markerSize) / 2
        val t = (h - markerSize) / 2

        mMiddleMarker.setBounds(l, t, l + markerSize, t + markerSize)

        val lineWidth = Math.min(mLineWidth, Math.min(w, h))

        // 线条
        // height
        val lh = t
        // top line
        // left
        val tl = (w - lineWidth) / 2
        // top
        val tt = 0
        mTopLine.setBounds(tl, tt, tl + lineWidth, tt + lh)

        // bottom line
        // left
        val bl = (w - lineWidth) / 2
        // top
        val bt = t + markerSize
        mBottomLine.setBounds(bl, bt, bl + lineWidth, bt + lh)

    }*/


    fun setScheme(scheme: Int) {
        if (TimelineMarker.BODY == scheme || TimelineMarker.HEAD == scheme || TimelineMarker.FOOT == scheme || TimelineMarker.ONLY_ONE == scheme) {
            mScheme = scheme
            invalidate()
        }
    }

    fun setMiddleMarker(midderMarker: Drawable) {
        if (mMiddleMarker != midderMarker) {
            mMiddleMarker = midderMarker
            calculateDrawableSize()
            invalidate()
        }
    }

    fun setMarkerSize(markerSize: Int) {
        if (mMarkerSize != markerSize) {
            mMarkerSize = markerSize
            calculateDrawableSize()
            invalidate()
        }
    }

    fun setTopLineColor(topLineColor: Int) {
        mTopLine = ColorDrawable(topLineColor)
        calculateDrawableSize()
        invalidate()
    }

    fun setBottomLineColor(bottomLineColor: Int) {
        mBottomLine = ColorDrawable(bottomLineColor)
        calculateDrawableSize()
        invalidate()
    }

    fun setLineWidth(lineWidth: Int) {
        if (mLineWidth != lineWidth) {
            mLineWidth = lineWidth
            calculateDrawableSize()
            invalidate()
        }
    }
}