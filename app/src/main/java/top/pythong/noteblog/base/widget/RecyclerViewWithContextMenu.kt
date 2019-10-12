package top.pythong.noteblog.base.widget

import android.content.Context
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ContextMenu

/**
 * 上下文菜单RecyclerView
 *
 * @author ChangJiahong
 * @date 2019/10/12
 */
class RecyclerViewWithContextMenu :
    RecyclerView {

    private val mContextInfo = RecyclerViewContextInfo()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    override fun showContextMenuForChild(originalView: View): Boolean {
        getPositionByChild(originalView)
        return super.showContextMenuForChild(originalView)
    }

    private fun getPositionByChild(originalView: View) {
        val layoutManager = layoutManager
        if (layoutManager != null) {
            val position = layoutManager.getPosition(originalView)
            mContextInfo.setPosition(position)
        }
    }

    override fun showContextMenuForChild(originalView: View, x: Float, y: Float): Boolean {
        getPositionByChild(originalView)
        return super.showContextMenuForChild(originalView, x, y)
    }

    override fun getContextMenuInfo(): ContextMenu.ContextMenuInfo {
        return mContextInfo
    }

    inner class RecyclerViewContextInfo : ContextMenu.ContextMenuInfo {
        private var mPosition = -1
        fun getPosition(): Int {
            return this.mPosition
        }

        fun setPosition(position: Int): Int {
            this.mPosition = position
            return position
        }
    }
}