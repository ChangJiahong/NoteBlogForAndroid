package top.pythong.noteblog.app.search.ui

import android.arch.lifecycle.Observer
import android.support.v7.widget.SearchView
import android.view.Gravity
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_search.*
import org.apache.commons.lang3.StringUtils
import org.jetbrains.anko.*
import top.pythong.noteblog.R
import top.pythong.noteblog.app.search.adapter.HistoryAdapter
import top.pythong.noteblog.app.search.model.SearchHistory
import top.pythong.noteblog.base.activity.BaseActivity
import top.pythong.noteblog.base.factory.ViewModelFactory
import top.pythong.noteblog.base.viewModel.BaseViewModel


class SearchActivity : BaseActivity() {

    private val TAG = SearchActivity::class.java.simpleName

    private lateinit var searchViewModel: SearchViewModel

    private lateinit var historyView: ListView

    private val historyData = ArrayList<SearchHistory>()

    private lateinit var adapter: HistoryAdapter

    override fun getContentView(): Int {
        return R.layout.activity_search
    }

    override fun initView() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        searchView.onActionViewExpanded()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (StringUtils.isBlank(query)) {
                    return false
                }
                loadingView.showLoading(true)
                searchViewModel.search(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                historyView.isTextFilterEnabled = true
                adapter.filter.filter(newText)
                return false
            }
        })

        loadingView.initDefinePage {
            UI {
                historyView = listView()
            }.view
        }

        adapter = HistoryAdapter(this, historyData)
        adapter.delOnClick = { _, item, _ ->
            searchViewModel.remove(item.id)
            adapter.remove(item)
            adapter.notifyDataSetChanged()
        }

        historyView.adapter = adapter
        historyView.addFooterView(UI {
            textView(getString(R.string.clearHistory)) {
                gravity = Gravity.CENTER
                padding = dip(5)
                backgroundResource = R.drawable.defult_click_bg_ripple
            }
        }.view)

        loadingView.showDefinePage(true)

        historyView.setOnItemClickListener { parent, view, position, id ->
            if (position == historyData.size) {
                // 删除所有
                searchViewModel.delAll()
                adapter.removeAll()
                adapter.notifyDataSetChanged()
                toast(R.string.deleteSuccessfully)
            } else {
                // 搜索该条
                val keyWords = historyData[position].name
                searchView.setQuery(keyWords, true)
            }
        }
    }

    override fun initData() {

        searchViewModel.historys.observe(this, Observer {
            val histories = it ?: return@Observer
            historyData.clear()
            historyData.addAll(histories)
            adapter.notifyDataSetChanged()
        })

        /**
         * 搜索结果回调
         */
        searchViewModel.searchResults.observe(this, Observer {
            val (append, result) = it ?: return@Observer
            loadingView.show()

        })
        searchViewModel.getAllHistroy()
    }

    override fun getViewModel(): BaseViewModel? {
        searchViewModel = ViewModelFactory.createViewModel(this, SearchViewModel::class)
        return searchViewModel
    }
}
