package top.pythong.noteblog.app.home.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import top.pythong.noteblog.R
import top.pythong.noteblog.app.article.ui.ArticleActivity
import top.pythong.noteblog.app.home.model.ArticleCardItem
import top.pythong.noteblog.app.userinfo.ui.UserProfileActivity
import top.pythong.noteblog.data.constant.Constant.ARTICLE_ID

/**
 *
 * @author ChangJiahong
 * @date 2019/8/25
 */
class ArticleAdapter(val articleList: List<ArticleCardItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = ArticleActivity::class.java.simpleName

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.article_item_crad, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = articleList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = articleList[position]
        viewHolder as ViewHolder
        viewHolder.apply {
            author.text = item.author
            timeAgo.text = " · ${item.timeAgo}"
            tags.text = item.tags
            title.text = item.title
            info.text = item.info
            authorIcon.setOnClickListener {
                UserProfileActivity.start(it.context, item.author)
            }
            Glide.with(v.context).load(item.authorImgUrl).into(authorIcon)
            Glide.with(v.context).load(item.frontCoverImgUrl).into(articleIcon)
            v.setOnClickListener {
                it.context.startActivity<ArticleActivity>(ARTICLE_ID to item.id.toString())
            }
            like.setOnClickListener {
                viewHolder.liked = !viewHolder.liked
                viewHolder.likeIcon.setImageResource(
                    if (viewHolder.liked)
                        R.drawable.like_1 else R.drawable.like_0
                )
                it.context.toast("点赞")
            }
            comment.setOnClickListener {
                it.context.toast("评论")
            }
            share.setOnClickListener {
                it.context.toast("分享")
            }
        }
    }

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v) {

        var liked = false
        val title = v.find<TextView>(R.id.title)
        val author = v.find<TextView>(R.id.author)
        val authorIcon = v.find<CircleImageView>(R.id.authorIcon)
        val timeAgo = v.find<TextView>(R.id.timeAgo)
        val tags = v.find<TextView>(R.id.tags)
        val info = v.find<TextView>(R.id.info)
        val likeIcon = v.find<ImageView>(R.id.likeIcon)
        val like = v.find<View>(R.id.like)

        val comment = v.find<View>(R.id.comment)
        val mComment = v.find<TextView>(R.id.mComment)

        val share = v.find<View>(R.id.share)

        val articleIcon = v.find<ImageView>(R.id.articleIcon)


    }
}