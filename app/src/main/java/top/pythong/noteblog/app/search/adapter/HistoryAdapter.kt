package top.pythong.noteblog.app.search.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.jetbrains.anko.find
import top.pythong.noteblog.R
import top.pythong.noteblog.app.search.model.SearchHistory
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log


/**
 *
 * @author ChangJiahong
 * @date 2019/10/11
 */
class HistoryAdapter(
    private val context: Context,
    /**
     * 显示数据
     */
    private val searchHistorys: ArrayList<SearchHistory>
) :
    BaseAdapter(), Filterable {

    /**
     * 元数据
     */
    private var data: ArrayList<SearchHistory>? = null

    private var constraint: String = ""

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var itemView = convertView
        val holder: Holder
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.search_item, null)
            holder = Holder(itemView)
            itemView!!.tag = holder
        } else {
            holder = itemView.tag as Holder
        }

        val item = searchHistorys[position]
        holder.apply {
            val spannableString = SpannableString(item.name)
            val start = item.name.indexOf(constraint)
            val end = start + constraint.length
            spannableString.setSpan(ForegroundColorSpan(Color.MAGENTA), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            name.text = spannableString
            del.setOnClickListener {
                delOnClick(it, item, position)
            }
        }

        return itemView
    }

    inner class Holder(val v: View) {
        val name = v.find<TextView>(R.id.name)
        val del = v.find<ImageView>(R.id.del)
    }

    override fun getItem(position: Int): Any {
        return searchHistorys[position]
    }

    override fun getItemId(position: Int): Long {
        return searchHistorys[position].id.toLong()
    }

    override fun getCount(): Int {
        return searchHistorys.size
    }

    var delOnClick: (v: View, item: SearchHistory, position: Int) -> Unit = { _, _, _ -> }

    override fun getFilter(): Filter {
        return SuggestsFilter()
    }

    fun remove(item: SearchHistory) {
        searchHistorys.remove(item)
        data?.remove(item)
    }

    fun removeAll() {
        searchHistorys.clear()
        data?.clear()
    }

    inner class SuggestsFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            if (data == null) {
                data = ArrayList(searchHistorys)
            }

            val results = Filter.FilterResults()
            val prefix = constraint.toString().toLowerCase()

            val list = ArrayList<SearchHistory>(data)
            var nList: List<SearchHistory> = list
            if (prefix.isNotBlank()) {
                nList = list.filter {
                    it.name.toLowerCase().contains(prefix)
                }
            }
            results.values = nList
            results.count = nList.size
            return results

        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            this@HistoryAdapter.constraint = constraint.toString()
            searchHistorys.clear()
            searchHistorys.addAll(results.values as ArrayList<SearchHistory>)

            if (results.count > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }
}