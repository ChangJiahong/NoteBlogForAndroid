package top.pythong.noteblog.app.archives.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import top.pythong.noteblog.R
import top.pythong.noteblog.app.archives.model.ArchiveHolder
import top.pythong.noteblog.app.archives.model.ArchiveView
import top.pythong.noteblog.app.article.ui.ArticleActivity
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.ArticleCardItem
import top.pythong.noteblog.base.widget.TimelineMarker
import top.pythong.noteblog.data.constant.Constant

/**
 *
 * @author ChangJiahong
 * @date 2019/9/2
 */
class ArchivesAdapter(private val archiveHolders: ArrayList<ArchiveHolder>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = ArchivesAdapter::class.java.simpleName

    companion object {
        const val ARCHIVE = 0
        const val ARTICLE = 1
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ARTICLE -> {
                val v = LayoutInflater.from(p0.context).inflate(R.layout.archives_item_article, null)
                ArticleViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(p0.context).inflate(R.layout.archives_item, null)
                ArchiveViewHolder(v)
            }
        }

    }

    override fun getItemCount(): Int {
        return archiveHolders.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val item = archiveHolders[position]

        when (item.type) {
            ARTICLE -> {
                // 归档文章
                val articleView = item.data as Article
                val articleViewHolder = viewHolder as ArticleViewHolder
                articleViewHolder.apply {
                    mTitle.text = articleView.title
                    mTime.text = articleView.created.substringBefore(" ")
                    mArticleView.setOnClickListener {
                        it.context.startActivity<ArticleActivity>(Constant.ARTICLE_ID to articleView.id.toString())
                    }
                }

            }
            ARCHIVE -> {
                // 归档菜单
                val archiveView = item.data as ArchiveView
                val archiveViewHolder = viewHolder as ArchiveViewHolder
                archiveViewHolder.apply {
                    when (position) {
                        0 -> timeLineMarker.setScheme(TimelineMarker.HEAD)
                        archiveHolders.size - 1 -> timeLineMarker.setScheme(TimelineMarker.FOOT)
                        else -> timeLineMarker.setScheme(TimelineMarker.BODY)
                    }

                    mTime.text = archiveView.date
                    mCount.text = "${archiveView.count} 篇"
                    unfoldIcon.setImageResource(
                        if (item.state)
                            R.drawable.circle_arrow_bottom
                        else
                            R.drawable.circle_arrow_right
                    )
                    v.setOnClickListener {

                        if (item.state) {
                            // 关闭
                            closeArchives(archiveView, position)
                        } else {
                            // 展开
                            openArchives(archiveView, position)
                        }
                        item.state = !item.state
                    }
                }
            }
            else -> {
                // 略
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return archiveHolders[position].type
    }

    class ArchiveViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        val timeLineMarker = v.find<TimelineMarker>(R.id.timeLineMarker)
        val mTime = v.find<TextView>(R.id.mTime)
        val mCount = v.find<TextView>(R.id.mCount)
        val unfoldIcon = v.find<ImageView>(R.id.unfoldIcon)
    }

    class ArticleViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        val mTitle = v.find<TextView>(R.id.mTitle)
        val mTime = v.find<TextView>(R.id.mTime)
        val mArticleView = v.find<View>(R.id.articleView)
    }

    fun openArchives(archiveView: ArchiveView, position: Int) {
        val archiveHolder = ArrayList<ArchiveHolder>()
        archiveView.articleViews.forEach {
            archiveHolder.add(ArchiveHolder(ArchiveHolder.ARTICLE, it))
        }

        archiveHolders.addAll(position + 1, archiveHolder)

        notifyItemRangeInserted(position + 1, archiveHolder.size)
        notifyItemChanged(position)
    }

    fun closeArchives(archiveView: ArchiveView, position: Int) {
        archiveHolders.removeAll(
            archiveHolders.subList(
                position + 1,
                position + archiveView.articleViews.size + 1
            )
        )
        Log.d(TAG, "删除后：${archiveHolders.size}")
        notifyItemRangeRemoved(position + 1, archiveView.articleViews.size)
        notifyItemChanged(position)
    }
}