package top.pythong.noteblog.app.articlemanager.fragment.category.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.articles_fragment.*
import org.jetbrains.anko.support.v4.toast

import top.pythong.noteblog.R
import top.pythong.noteblog.app.home.ui.HomeFragment
import top.pythong.noteblog.base.adapter.SimpleAdapter
import top.pythong.noteblog.base.fragment.BaseFragment
import top.pythong.noteblog.base.viewModel.BaseViewModel

class CategoryFragment : BaseFragment() {

    companion object {
        val instance: CategoryFragment by lazy {
            CategoryFragment()
        }
    }

    private lateinit var viewModel: CategoryViewModel

    private lateinit var adapter: SimpleAdapter

    private val categorys = ArrayList<Map<String, String>>()

    private val keys = arrayOf("name", "count")

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.category_fragment, container, false)
    }

    override fun initView() {
        for (i in 0..20) {
            categorys.add(
                mapOf(
                    "id" to "$i",
                    keys[0] to "文章$i",
                    keys[1] to "10"
                )
            )
        }
        adapter =
            SimpleAdapter(categorys, R.layout.category_item, keys, arrayOf(R.id.mName, R.id.count))
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { v, position ->
            toast(categorys[position]["id"] ?: "")
        }
    }

    override fun initData() {

    }

    override fun getViewModel(): BaseViewModel {
        viewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        return viewModel
    }
}
