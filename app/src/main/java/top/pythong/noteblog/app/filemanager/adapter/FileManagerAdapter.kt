package top.pythong.noteblog.app.filemanager.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.apache.commons.lang3.StringUtils
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.w3c.dom.Text
import top.pythong.noteblog.R
import top.pythong.noteblog.app.filemanager.model.FileDir
import top.pythong.noteblog.app.filemanager.ui.FileManagerActivity

/**
 *
 * @author ChangJiahong
 * @date 2019/9/6
 */
class FileManagerAdapter(private val fileDirs: ArrayList<FileDir>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIR = 0
    val FILE = 1

    override fun onCreateViewHolder(parent: ViewGroup, type: Int) =
        when (type) {
            DIR -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.dir_item, null)
                DirHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.file_item, null)
                FileHolder(v)
            }
        }


    override fun getItemCount(): Int {
        return fileDirs.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = fileDirs[position]
        when (item.type) {
            FileDir.DIR -> {
                viewHolder as DirHolder
                viewHolder.apply {
                    v.setOnClickListener {
                        v.context.startActivity<FileManagerActivity>("path" to item.currentPath)
                    }
                    name.text = item.name
                    count.text = "${item.count}项"
                }
            }
            else -> {
                // 文件
                viewHolder as FileHolder
                viewHolder.apply {
                    v.setOnClickListener {
                        // TODO: 下载文件 Or 打开文件
                        v.context.toast(item.name)
                    }
                    name.text = item.name
                    created.text = "${item.created} \uD83D\uDD57"
                    locked.text = if (item.protective == "public") "" else "\uD83D\uDD12"
                    val fileIcon = getFileIcon(item.name)
                    icon.setImageResource(fileIcon)
                }
            }
        }
    }

    /**
     * 匹配文件图标
     */
    private fun getFileIcon(name: String) = kotlin.run {
        val suffix = name.substringAfterLast(".").toLowerCase()
        when (suffix) {
            "png", "jpg" -> R.drawable.image
            "xls", "xlsx" -> R.drawable.excel
            "ppt", "pptx" -> R.drawable.ppt
            "doc", "docx" -> R.drawable.word
            "pdf" -> R.drawable.pdf
            "mp3" -> R.drawable.music
            "mp4" -> R.drawable.video
            "java", "kt", "c", "cpp", "py", "xml", "json", "html", "js" -> R.drawable.code
            "md" -> R.drawable.md
            "txt" -> R.drawable.txt
            else -> R.drawable.unkown_file
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = fileDirs[position]
        if (item.type == FileDir.DIR) {
            return DIR
        }
        return FILE
    }

    class DirHolder(val v: View) : RecyclerView.ViewHolder(v) {
        val name = v.find<TextView>(R.id.name)
        val count = v.find<TextView>(R.id.count)

        val created = v.find<TextView>(R.id.created)
    }

    class FileHolder(val v: View) : RecyclerView.ViewHolder(v) {
        val icon = v.find<ImageView>(R.id.icon)
        val name = v.find<TextView>(R.id.name)
        val created = v.find<TextView>(R.id.created)
        val locked = v.find<TextView>(R.id.locked)
    }
}