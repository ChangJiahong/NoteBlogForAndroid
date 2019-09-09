package top.pythong.noteblog.data

import android.content.Context
import android.os.Environment
import android.util.Log
import top.pythong.noteblog.data.FileManager.appRoot
import java.io.File

/**
 *
 * @author ChangJiahong
 * @date 2019/9/9
 */
object FileManager {

    val TAG = FileManager::class.java.simpleName

    val Context.appRoot: File
        get() = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            Environment.getExternalStorageDirectory()
        } else {
            this.filesDir
        }


    val isSdCardExist = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    fun initDirs(context: Context){

        // app文件文件夹创建
        initFile(context.fileDirectoryPath)
        initFile(context.imageDirectoryPath)
        initFile(context.tempDirectoryPath)

    }

    fun initFile(path: String){
        val f = File(path)
        if(!f.exists()){
            f.mkdirs()
        }
        Log.v(TAG,"初始化文件夹："+ f.path)
    }


    fun copyFiletoFile(fromFile: File, toFile: File){
        val buffer = ByteArray(2048) //缓冲数组2kB
        var len: Int
        fromFile.inputStream().use {input->
            toFile.outputStream().use {
                while (input.read(buffer).also { len = it } != -1) {
                    it.write(buffer, 0, len)
                }
            }
        }
    }
}

val Context.appRootDirectoryPath: String
    get() = "${appRoot.absolutePath}/NoteBlog"

val Context.fileDirectoryPath: String
    get() = "$appRootDirectoryPath/file_recv"

val Context.imageDirectoryPath: String
    get() = "$appRootDirectoryPath/images"

val Context.tempDirectoryPath: String
    get() = "$appRootDirectoryPath/temp"