package top.pythong.noteblog.app.home.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import org.w3c.dom.Text
import top.pythong.noteblog.R
import top.pythong.noteblog.app.home.model.Article

/**
 *
 * @author ChangJiahong
 * @date 2019/8/25
 */
class ArticleAdpater(val articleList: List<Article>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemClick: (v: View, positon: Int) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.article_item, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = articleList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = articleList[position]
        viewHolder as ViewHolder
        viewHolder.title.text = item.title
        viewHolder.v.setOnClickListener {
            it.context.toast("点击")
        }
        viewHolder.like.setOnClickListener {
            viewHolder.liked = !viewHolder.liked
            viewHolder.likeIcon.setImageResource(if (viewHolder.liked) R.drawable.like_1 else R.drawable.like_0)
            it.context.toast("点赞")
        }

        viewHolder.comment.setOnClickListener {
            it.context.toast("评论")
        }

        viewHolder.share.setOnClickListener {
            it.context.toast("分享")
        }
    }

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v) {

        var liked = false
        val title = v.find<TextView>(R.id.title)

        val likeIcon = v.find<ImageView>(R.id.likeIcon)
        val like = v.find<View>(R.id.like)

        val comment = v.find<View>(R.id.comment)
        val mComment = v.find<TextView>(R.id.mComment)

        val share = v.find<View>(R.id.share)


    }

    /**
     * 点击事件监听
     */
    fun setOnItemClickListener(click: (v: View, positon: Int) -> Unit) {
        itemClick = click
    }
}