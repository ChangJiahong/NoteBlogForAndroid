package top.pythong.noteblog.app.aboutMe.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import top.pythong.noteblog.R
import top.pythong.noteblog.base.fragment.BaseFragment
import top.pythong.noteblog.base.viewModel.BaseViewModel
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import kotlinx.android.synthetic.main.about_me_fragment.*
import android.support.v4.widget.NestedScrollView
import com.scwang.smartrefresh.layout.util.SmartUtil.dp2px


class AboutMeFragment : BaseFragment() {

    val TAG = AboutMeFragment::class.java.simpleName

    private lateinit var viewModel: AboutMeViewModel

    /**
     * 创建视图
     */
    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.about_me_fragment, container, false)
    }

    private var mOffset = 0f
    private var mScrollY = 0f

    override fun onBaseStart() {
        this.activity!!.title = "我的"
    }

    /**
     * 加载控件
     */
    override fun initView() {
        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderMoving(
                header: RefreshHeader?,
                isDragging: Boolean,
                percent: Float,
                offset: Int,
                headerHeight: Int,
                maxDragHeight: Int
            ) {
                mOffset = offset / 1.5f
                parallax.translationY = mOffset - mScrollY
            }

        })


        scrollView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            private var lastScrollY = 0f
            private val h = dp2px(200f).toFloat()
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                var sy = scrollY.toFloat()
                if (lastScrollY < h) {
                    sy = Math.min(h, sy)
                    mScrollY = if (scrollY > h) h else sy
                    parallax.translationY = mOffset - mScrollY
                }
                lastScrollY = sy
            }
        })
    }

    /**
     * 加载数据
     */
    override fun initData() {

    }

    /**
     * 获得viewModel
     */
    override fun getViewModel(): BaseViewModel {
        viewModel = ViewModelProviders.of(this).get(AboutMeViewModel::class.java)
        return viewModel
    }
}
