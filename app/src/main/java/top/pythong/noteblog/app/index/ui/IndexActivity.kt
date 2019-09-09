package top.pythong.noteblog.app.index.ui

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import kotlinx.coroutines.*
import org.jetbrains.anko.startActivity
import top.pythong.noteblog.R
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.app.login.ui.LoginActivity
import top.pythong.noteblog.app.main.ui.MainActivity
import top.pythong.noteblog.data.FileManager
import top.pythong.noteblog.utils.*
import java.util.ArrayList
import kotlin.reflect.KClass

/**
 * 开屏页
 * @author ChangJiahong
 * @date 2019/8/22
 */
@ExperimentalCoroutinesApi
class IndexActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    var isp = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)

        initPermission()

        // 创建通知通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = Constant.CHANNEL_ID_DOWNLOAD
            val name = "下载通知"
            val descriptionText = "正在下载..."
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            createNotificationChannel(
                channelId = channelId,
                name = name,
                des = descriptionText,
                importance = importance
            )
        }

        jump()

    }

    private fun initPermission() {
        val permissions = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE)

        val toApplyList = ArrayList<String>()

        for (perm in permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm)
                // 进入到这里代表没有权限.
                Log.v("Index",perm+"未获取权限")
            }
        }
        val tmpList = arrayOfNulls<String>(toApplyList.size)
        isp = toApplyList.size
        Log.v("权限注册",""+isp)
        if (!toApplyList.isEmpty()) {

            ActivityCompat.requestPermissions(this, toApplyList.toTypedArray(), 123)
            Log.v("Index","请求权限")
        }

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
        when (requestCode) {
            123 -> {
                for (i in 0 until grantResults.size) {

                    val grantResult = grantResults[i]
                    val s = permissions[i]
                    if (grantResult == PackageManager.PERMISSION_GRANTED) { //这个是权限拒绝
                        Log.v("Index", s + "权限申请成功")

                        //Toast.makeText(this, s + "权限申请成功", Toast.LENGTH_SHORT).show()
                    } else { //授权成功了
                        //do Something
                        Log.v("Index", s + "权限被拒绝了")
                        //Toast.makeText(this, s + "权限被拒绝了", Toast.LENGTH_SHORT).show()
                    }
                    isp -= 1
                    Log.v("权限",""+isp)
                }
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, name: String, des: String, importance: Int) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = des
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    private fun jump() = launch(Dispatchers.IO) {

        // TODO: 网络请求，初始化，获取app更新等信息
        while (isp!=0){}

        // 初始化app
        FileManager.initDirs(this@IndexActivity)

        delay(1000)
        //startActivity<LoginActivity>()
        // 获取本地token，没有则跳转到login
//        val token = getStringFromSharedPreferences(Constant.TOKEN)
//        if (token.isEmpty()) {
//            goTo(LoginActivity::class)
//        }else{
//            goTo(MainActivity::class)
//        }

        goTo(MainActivity::class)

    }


    /**
     * 跳转页面
     */
    private suspend inline fun <reified T : Activity> goTo(clas: KClass<T>) = withContext(Dispatchers.Main) {
        startActivity<T>()
        finish()
    }
}
