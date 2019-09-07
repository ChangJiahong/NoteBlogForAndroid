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
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import top.pythong.noteblog.app.login.ui.LoginActivity
import top.pythong.noteblog.data.constant.Constant
import top.pythong.noteblog.data.constant.MsgCode
import top.pythong.noteblog.utils.putToSharedPreferences


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

        loadingView.errorBtn {
            loadingView.show()
            refreshLayout.autoRefresh()
        }

        loadingView.emptyMsg {
            it.text = "没有文件哦！"
        }

        currentPath = intent.getStringExtra("path") ?: "\\"


        if (currentPath != "\\") {
            var title = "文件管理"
            val path = currentPath
            title += path.replace("\\", ">")
            toolbar.title = "$title>"
        }

        refreshLayout.setOnRefreshListener {
            fileManagerViewModel.loadFileList(currentPath, it)
        }

        fileManagerViewModel.fileDirs.observe(this, Observer {
            val fileDirs = it ?: return@Observer
            if (fileDirs.isEmpty()) {
                loadingView.showEmpty(true)
                return@Observer
            }
            fileDirList.clear()
            fileDirList.addAll(fileDirs)
            adapter.notifyDataSetChanged()
            loadingView.show()
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


    }

    override fun onErrorResult(error: MsgCode) {
        if (!error.isLoginError()) {
            loadingView.errorMsg {
                it.text = error.msg
            }
            loadingView.showError(true)
        } else {
            alert {
                title = "提示"
                message = error.msg + ",去登陆试试!!!"
                positiveButton("登录") { i ->
                    putToSharedPreferences {
                        put(Constant.TOKEN, "")
                    }
                    // 启动登录
                    startActivity<LoginActivity>()
                }
                negativeButton("取消") {
                    finish()
                }

            }.show()
        }
    }
}
