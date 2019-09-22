package top.pythong.noteblog.base.adapter

import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.TextView
import org.jetbrains.anko.find

/**
 *
 * @author ChangJiahong
 * @date 2019/9/22
 */
class SimpleAdapter(
    val data: ArrayList<Map<String, String>>, @LayoutRes val resource: Int,
    val from: Array<String>, @IdRes val to: Array<Int>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(resource, null)
        return SimpleViewHolder(v, to)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, p1: Int) {
        holder as SimpleViewHolder
        holder.mTexts.forEachIndexed { index, textView ->
            textView.text = data[p1][from[index]]
        }
        holder.itemView.setOnClickListener {
            onItemClickListener(it, p1)
        }
    }

    class SimpleViewHolder(v: View, private val ids: Array<Int>) : RecyclerView.ViewHolder(v) {

        var mTexts = Array<TextView>(ids.size) {
            itemView.find(ids[it])
        }

    }

    private var onItemClickListener: (v: View, position: Int) -> Unit = {_,_-> }

    fun setOnItemClickListener(click:(v: View, position: Int) -> Unit){
        onItemClickListener = click
    }
}