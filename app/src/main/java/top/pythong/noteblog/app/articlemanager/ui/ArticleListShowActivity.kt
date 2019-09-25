package top.pythong.noteblog.app.articlemanager.ui

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_article_list_show.*
import org.jetbrains.anko.startActivity
import top.pythong.noteblog.R
import top.pythong.noteblog.app.articlemanager.fragment.article.ui.ArticlesFragment
import top.pythong.noteblog.app.articlemanager.model.SimpleArticle
import top.pythong.noteblog.app.home.model.Article
import top.pythong.noteblog.app.home.model.Type
import top.pythong.noteblog.base.activity.BaseActivity
import top.pythong.noteblog.utils.DateKit

/**
 * 文章集合显示
 */
class ArticleListShowActivity : BaseActivity() {

    override fun getContentView(): Int {
        return R.layout.activity_article_list_show
    }

    override fun initView() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        val type = intent.getStringExtra("type")
        val name = intent.getStringExtra("name")
        val title = when (type) {
            Type.TAG -> getString(R.string.tag)
            Type.CATEGORY -> getString(R.string.category)
            else -> ""
        } + " —— " + name

        toolbar.title = title
    }

    override fun initData() {
        val articles = intent.getSerializableExtra("data") as List<Article>? ?: listOf()
        val articleList = ArrayList<SimpleArticle>()

        articles.forEach {
            articleList.add(
                SimpleArticle(
                    it.id,
                    it.title,
                    it.info,
                    it.hits,
                    DateKit.format(it.created, "yyyy-MM-dd HH:mm:ss", "yyyy年MM月dd日"),
                    it.status
                )
            )
        }

        val articlesFragment = ArticlesFragment.instance(articleList)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContent, articlesFragment).commit()
    }

    companion object {
        fun show(context: Context?, articles: List<Article>, type: String, name: String?) {
            context?.startActivity<ArticleListShowActivity>(
                "data" to articles,
                "type" to type,
                "name" to name
            )
        }
    }
}
