package top.pythong.noteblog.app

import android.content.Context
import android.os.Bundle
import com.danielstone.materialaboutlibrary.MaterialAboutActivity
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard
import com.danielstone.materialaboutlibrary.model.MaterialAboutList
import kotlinx.android.synthetic.main.activity_about_developers.*
import me.imid.swipebacklayout.lib.SwipeBackLayout
import me.imid.swipebacklayout.lib.Utils
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper
import top.pythong.noteblog.BuildConfig
import top.pythong.noteblog.R
import top.pythong.noteblog.utils.PayUtils

class AboutDevelopersActivity : MaterialAboutActivity(), SwipeBackActivityBase {

    private lateinit var mHelper: SwipeBackActivityHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mHelper = SwipeBackActivityHelper(this)
        mHelper.onActivityCreate()

        setSwipeBackEnable(true)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mHelper.onPostCreate()
    }

    override fun getActivityTitle(): CharSequence? {
        return getString(R.string.about)
    }

    override fun getMaterialAboutList(p0: Context): MaterialAboutList {
        val appBuilder = MaterialAboutCard.Builder()

        appBuilder.addItem(
            MaterialAboutTitleItem.Builder()
                .icon(R.drawable.icon)
                .text(getString(R.string.app_name))
                .desc(getString(R.string.copyright))
                .build()
        )
        appBuilder.addItem(
            MaterialAboutActionItem.Builder()
                .icon(R.drawable.ic_version_black_24dp)
                .text(getString(R.string.version))
                .subText(BuildConfig.VERSION_NAME)
                .setOnClickAction {

                }
                .build()
        )
        appBuilder.addItem(
            MaterialAboutActionItem.Builder()
                .icon(R.drawable.ic_code_black_24dp)
                .text(getString(R.string.sourceCode))
                .subText(getString(R.string.starToMe))
                .setOnClickAction {

                }
                .build()
        )

        val authorBuilder = MaterialAboutCard.Builder()
        authorBuilder.title(R.string.author)
        authorBuilder.addItem(
            MaterialAboutActionItem.Builder()
                .icon(R.drawable.ic_person_black_24dp)
                .text(getString(R.string.authorName))
                .subText(getString(R.string.location))
                .setOnClickAction {

                }
                .build()
        )
        authorBuilder.addItem(
            MaterialAboutActionItem.Builder()
                .icon(R.drawable.ic_github)
                .text(getString(R.string.authorGitHub))
                .subText(getString(R.string.myGitHub))
                .setOnClickAction {

                }
                .build()
        )
        authorBuilder.addItem(
            MaterialAboutActionItem.Builder()
                .icon(R.drawable.ic_email_black_24dp)
                .text(getString(R.string.authorEmail))
                .subText(getString(R.string.myEmail))
                .setOnClickAction {

                }
                .build()
        )
        authorBuilder.addItem(
            MaterialAboutActionItem.Builder()
                .icon(R.drawable.ic_blog)
                .text(getString(R.string.authorBlog))
                .setOnClickAction {

                }
                .build()
        )
        val shareBuilder = MaterialAboutCard.Builder()
        shareBuilder.title(R.string.shareAndSuggest)
        shareBuilder.addItem(
            MaterialAboutActionItem.Builder()
                .text(R.string.share)
                .icon(R.drawable.ic_share_black_24dp)
                .setOnClickAction {

                }
                .build()
        )
        shareBuilder.addItem(
            MaterialAboutActionItem.Builder()
                .text(R.string.suggest)
                .icon(R.drawable.ic_msg_late_black_24dp)
                .setOnClickAction {

                }
                .build()
        )
        shareBuilder.addItem(
            MaterialAboutActionItem.Builder()
                .text(R.string.reward)
                .icon(R.drawable.pay)
                .setOnClickAction {
                    PayUtils.startAlipayClient(this, "FKX007925DBO6LPY9XRJ51")

                }
                .build()
        )


        return MaterialAboutList(appBuilder.build(), authorBuilder.build(), shareBuilder.build())
    }

    override fun getSwipeBackLayout(): SwipeBackLayout {
        return mHelper.swipeBackLayout
    }

    override fun scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this)
        swipeBackLayout.scrollToFinishActivity()
    }

    override fun setSwipeBackEnable(enable: Boolean) {
        swipeBackLayout.setEnableGesture(enable)
    }
}
