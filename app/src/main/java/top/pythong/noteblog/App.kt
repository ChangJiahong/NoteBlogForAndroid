package top.pythong.noteblog

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import android.content.Context
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.header.BezierRadarHeader
import org.jetbrains.anko.toast
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.utils.putToSharedPreferences


/**
 *
 * @author ChangJiahong
 * @date 2019/9/2
 */
class App : Application() {

    init
    {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.colorPrimary, R.color.white)
            BezierRadarHeader(context).setEnableHorizontalDrag(true) }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            //指定为经典Footer，默认是 BallPulseFooter
            BallPulseFooter(context).setSpinnerStyle(SpinnerStyle.FixedBehind)
        }
    }

}

/**
 * 清除登录信息
 */
fun Context.clearLoginUser(){
    putToSharedPreferences {
        put(Constant.TOKEN, "")
        put(Constant.LOGGED_IN_USER, "")
    }
}

fun Context.copyToClipboard(str: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("", str)
    clipboard.primaryClip = clip
    toast(getString(R.string.copyed))
}