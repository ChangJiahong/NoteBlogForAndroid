package top.pythong.noteblog.app.index.ui

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import org.jetbrains.anko.startActivity
import top.pythong.noteblog.R
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.app.login.ui.LoginActivity
import top.pythong.noteblog.app.main.ui.MainActivity
import top.pythong.noteblog.utils.*
import kotlin.reflect.KClass

/**
 * 开屏页
 * @author ChangJiahong
 * @date 2019/8/22
 */
@ExperimentalCoroutinesApi
class IndexActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)

        jump()

    }

    private fun jump() = launch(Dispatchers.IO) {

        // TODO: 网络请求，初始化，获取app更新等信息
        delay(1000)

        // 获取本地token，没有则跳转到login
        val token = getStringFromSharedPreferences(Constant.TOKEN)
        if (token.isEmpty()) {
            goTo(LoginActivity::class)
        }else{
            goTo(MainActivity::class)
        }


    }


    /**
     * 跳转页面
     */
    private suspend inline fun <reified T: Activity> goTo(clas: KClass<T>) = withContext(Dispatchers.Main){
        startActivity<T>()
        finish()
    }
}
