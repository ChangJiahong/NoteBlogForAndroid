package top.pythong.noteblog.app.editarticle.fragment.edit.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.edit_fragment.*
import org.jetbrains.anko.support.v4.dip
import org.jetbrains.anko.support.v4.sp

import top.pythong.noteblog.R
import top.pythong.noteblog.app.editarticle.ui.EditArticleActivity
import top.pythong.noteblog.base.factory.ViewModelFactory
import top.pythong.noteblog.base.fragment.BaseFragment
import top.pythong.noteblog.base.viewModel.BaseViewModel


class EditFragment : BaseFragment(), View.OnClickListener {

    companion object {
        fun newInstance() = EditFragment()
    }

    private val TAG = EditFragment::class.java.simpleName

    private lateinit var viewModel: EditViewModel

    private lateinit var parent: EditArticleActivity

    private lateinit var editBars: Array<View>

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.edit_fragment, container, false)
    }

    override fun initView() {
        if (activity == null || activity !is EditArticleActivity) {
            return
        }
        parent = (this.activity as EditArticleActivity?)!!
        val wm1 = parent.windowManager
        val point = Point()
        wm1.defaultDisplay.getSize(point)
        card.minimumHeight = (point.y - dip(56 + 20 + 20 + 3) - sp(25))
        card.setOnClickListener {
            editRequest()
        }
        mdEdit.postDelayed({
            editRequest()
        }, 10)

        mdEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                parent.mdContent = s?.toString() ?: ""
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        editBars = arrayOf(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11)
        editBars.forEach {
            it.setOnClickListener(this@EditFragment)
        }
    }

    private fun editRequest() {
        mdEdit?.apply {
            requestFocus()
            val manager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.showSoftInput(mdEdit, 0)
        }
    }

    override fun initData() {

    }

    override fun onClick(v: View?) {
        val isWrap = parent.mdContent.isBlank() || parent.mdContent.last() == '\n'
        when (v?.id) {
            R.id.t1 -> {
                if (!isWrap) {
                    parent.mdContent += "\n"
                }
                parent.mdContent += "# "

                mdEdit.setText(parent.mdContent)
                mdEdit.setSelection(parent.mdContent.length)
            }
            R.id.t2 -> {
                if (!isWrap) {
                    parent.mdContent += "\n"
                }
                parent.mdContent += "## "
                mdEdit.setText(parent.mdContent)
                mdEdit.setSelection(parent.mdContent.length)
            }
            R.id.t3 -> {
                if (!isWrap) {
                    parent.mdContent += "\n"
                }
                parent.mdContent += "### "
                mdEdit.setText(parent.mdContent)
                mdEdit.setSelection(parent.mdContent.length)
            }
            R.id.t4 -> {
                if (!isWrap) {
                    parent.mdContent += "\n"
                }
                parent.mdContent += "#### "
                mdEdit.setText(parent.mdContent)
                mdEdit.setSelection(parent.mdContent.length)
            }
            R.id.t5 -> {
                if (!isWrap) {
                    parent.mdContent += "\n"
                }
                parent.mdContent += "##### "
                mdEdit.setText(parent.mdContent)
                mdEdit.setSelection(parent.mdContent.length)
            }
            R.id.t6 -> {
                parent.mdContent += "****"
                mdEdit.setText(parent.mdContent)
                mdEdit.setSelection(parent.mdContent.length - 2)
            }
            R.id.t7 -> {
                parent.mdContent += "**"
                mdEdit.setText(parent.mdContent)
                mdEdit.setSelection(parent.mdContent.length - 1)
            }
            R.id.t8 -> {
                if (!isWrap) {
                    parent.mdContent += "\n"
                }
                parent.mdContent += "> "
                mdEdit.setText(parent.mdContent)
                mdEdit.setSelection(parent.mdContent.length)
            }
            R.id.t9 -> {
                if (!isWrap) {
                    parent.mdContent += "\n"
                }
                parent.mdContent += "``````"
                mdEdit.setText(parent.mdContent)
                mdEdit.setSelection(parent.mdContent.length - 3)
            }
            R.id.t10 -> {
                parent.mdContent += "[链接]()"
                mdEdit.setText(parent.mdContent)
                mdEdit.setSelection(parent.mdContent.length - 1)
            }
            R.id.t11 -> {
                if (!isWrap) {
                    parent.mdContent += "\n"
                }
                parent.mdContent += "- "
                mdEdit.setText(parent.mdContent)
                mdEdit.setSelection(parent.mdContent.length)
            }
        }
    }

    override fun getViewModel(): BaseViewModel {
        viewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)
//        viewModel = ViewModelFactory.createViewModel(this, ArticlesViewModel::class)
        return viewModel
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        editRequest()
    }
}
