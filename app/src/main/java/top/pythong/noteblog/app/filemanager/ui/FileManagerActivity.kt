package top.pythong.noteblog.app.filemanager.ui

import android.arch.lifecycle.Observer
import android.util.Log
import kotlinx.android.synthetic.main.activity_file_manager.*
import top.pythong.noteblog.R
import top.pythong.noteblog.app.filemanager.adapter.FileManagerAdapter
import top.pythong.noteblog.app.filemanager.model.FileDir
import top.pythong.noteblog.app.home.utils.SmoothScrollLayoutManager
import top.pythong.noteblog.base.activity.BaseActivity
import top.pythong.noteblog.base.factory.ViewModelFactory
import top.pythong.noteblog.base.viewModel.BaseViewModel
import android.support.v7.widget.DividerItemDecoration
import org.jetbrains.anko.toast
import top.pythong.noteblog.data.constant.MsgCode


class FileManagerActivity : BaseActivity() {

    val TAG = FileManagerActivity::class.java.simpleName

    lateinit var fileManagerViewModel: FileManagerViewModel

    lateinit var currentPath: String

    private lateinit var adapter: FileManagerAdapter

    private val fileDirList = ArrayList<FileDir>()

    override fun getViewModel(): BaseViewModel {
        fileManagerViewModel = ViewModelFactory.createViewModel(this, FileManagerViewModel::class)
        return fileManagerViewModel
    }

    override fun getContentView(): Int {
        return R.layout.activity_file_manager
    }

    override fun initView() {
        toolbar.setNavigationOnClickListener {
            finish()
        }

        currentPath = intent.getStringExtra("path") ?: "\\"


        if (currentPath != "\\") {
            var title = "文件管理"
            val path = currentPath
            title += path.replace("\\", ">")
            toolbar.title = "$title>"
        }

        fileManagerViewModel.fileDirs.observe(this, Observer {
            val fileDirs = it ?: return@Observer
            fileDirList.clear()
            fileDirList.addAll(fileDirs)
            adapter.notifyDataSetChanged()
        })


//        for (i in 0..20) {
//            fileDirList.add(FileDir("文件夹$i"))
//        }

        adapter = FileManagerAdapter(fileDirList)
        val smoothScrollLayoutManager = SmoothScrollLayoutManager(this)
        recyclerView.layoutManager = smoothScrollLayoutManager
        //添加Android自带的分割线
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
    }

    override fun initData() {

        // 获取文件
        fileManagerViewModel.loadFileList(currentPath)

    }

    override fun onErrorResult(error: MsgCode) {
        super.onErrorResult(error)
        if (!error.isLoginError()){
            toast(error.msg+"。错误码：${error.code}")
        }
    }
}
