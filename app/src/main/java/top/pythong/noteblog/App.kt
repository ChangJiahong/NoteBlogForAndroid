package top.pythong.noteblog

import android.app.Application
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import android.R
import android.R.attr.colorPrimary
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator
import android.R.attr.colorPrimary
import android.content.Context
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.header.BezierRadarHeader
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
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout -> BezierRadarHeader(context).setEnableHorizontalDrag(true) }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            //指定为经典Footer，默认是 BallPulseFooter
            BallPulseFooter(context).setSpinnerStyle(SpinnerStyle.Scale)
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