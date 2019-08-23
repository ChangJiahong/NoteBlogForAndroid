package top.pythong.noteblog.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import top.pythong.noteblog.R

/**
 * 首页
 * @author ChangJiahong
 * @date 2019/8/22
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 在main页面验证token有效，请求服务器，验证token，失败跳转到login
    }
}
