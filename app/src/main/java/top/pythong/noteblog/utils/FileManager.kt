package top.pythong.noteblog.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.os.Environment
import android.util.Log
import top.pythong.noteblog.app.download.model.DownloadResource
import top.pythong.noteblog.utils.FileManager.appRoot
import java.io.File
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.content.FileProvider
import android.webkit.MimeTypeMap
import top.pythong.noteblog.utils.FileManager.TAG




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

    fun initDirs(context: Context) {

        // app文件文件夹创建
        initFile(context.fileDirectoryPath)
        initFile(context.imageDirectoryPath)
        initFile(context.tempDirectoryPath)

    }

    fun initFile(path: String) {
        val f = File(path)
        if (!f.exists()) {
            f.mkdirs()
        }
        Log.v(TAG, "初始化文件夹：" + f.path)
    }


    fun copyFiletoFile(fromFile: File, toFile: File) {
        val buffer = ByteArray(2048) //缓冲数组2kB
        var len: Int
        fromFile.inputStream().use { input ->
            toFile.outputStream().use {
                while (input.read(buffer).also { len = it } != -1) {
                    it.write(buffer, 0, len)
                }
            }
        }
    }

    /**
     * 获取文件的媒体类型
     */
    fun getFileMimeType(file: File): String {
        // FIXME：该方法对某些文件类型不支持
        val ext= MimeTypeMap.getFileExtensionFromUrl(file.absolutePath)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)?:""
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

fun Context.getResourceFile(resource: DownloadResource): File {
    val refileDir = File("$fileDirectoryPath/${resource.toPath}".replace("\\", "/"))
    val refile = File(refileDir, resource.name)
    return refile
}

fun Context.getResourceFileOrCreate(resource: DownloadResource): File {
    val refileDir = File("$fileDirectoryPath/${resource.toPath}".replace("\\", "/"))
    val refile = File(refileDir, resource.name)
    if (!refileDir.exists()) {
        refileDir.mkdirs()
    }
    if (!refile.exists()) {
        // 创建文件
        refile.createNewFile()
    }
    return refile
}

fun Context.getResourceTempFile(resource: DownloadResource): File {
    val tempFilePath = "$tempDirectoryPath/${resource.id}.ntbg"
    return File(tempFilePath)
}

fun Context.openFileByThirdPartyApp(file: File, type: String) {

    fun hasIntent(intent: Intent): Boolean {
        val pm = this.packageManager
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        val list =
            pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY or PackageManager.GET_INTENT_FILTERS)
        return list?.isNotEmpty() ?: false
    }

    try {
        var mime = type
        if (mime.isBlank()){
            mime = FileManager.getFileMimeType(file)
        }
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val uri = FileProvider.getUriForFile(this, "top.pythong.noteblog.fileprovider", file)
        intent.setDataAndType(uri, mime)
        if (!hasIntent(intent)) {
            intent.setDataAndType(uri, "text/*")
        }
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Log.e(TAG, e.message)
    }
}