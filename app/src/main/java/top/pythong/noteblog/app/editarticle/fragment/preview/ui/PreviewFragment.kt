package top.pythong.noteblog.app.editarticle.fragment.preview.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import kotlinx.android.synthetic.main.preview_fragment.*
import org.jetbrains.anko.support.v4.dip

import top.pythong.noteblog.R
import top.pythong.noteblog.app.editarticle.ui.EditArticleActivity
import top.pythong.noteblog.base.fragment.BaseFragment
import top.pythong.noteblog.base.viewModel.BaseViewModel

class PreviewFragment : BaseFragment() {

    companion object {
        fun newInstance() = PreviewFragment()
    }

    private lateinit var viewModel: PreviewViewModel

    private lateinit var parent: EditArticleActivity


    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.preview_fragment, container, false)
    }

    override fun initView() {

        if (activity == null || activity !is EditArticleActivity) {
            return
        }
        parent = (this.activity as EditArticleActivity?)!!

        val wm1 = parent.windowManager
        val point = Point()
        wm1.defaultDisplay.getSize(point)
        card.minimumHeight = (point.y - dip(56 + 20 + 3))

        preContent.setMdText("还没有内容呢！")

    }

    override fun initData() {

    }

    override fun getViewModel(): BaseViewModel {
        viewModel = ViewModelProviders.of(this).get(PreviewViewModel::class.java)
        return viewModel
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            val mdSource = parent.mdContent
            if (mdSource.isNotBlank()) {
                preContent.setMdText(mdSource)
            } else {
                preContent.setMdText("还没有内容呢！")
            }
            val manager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(card.windowToken, 0)
        }
    }
}
