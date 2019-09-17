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
import kotlinx.android.synthetic.main.activity_file_manager.loadingView
import kotlinx.android.synthetic.main.activity_file_manager.refreshLayout
import kotlinx.android.synthetic.main.archives_fragment.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import top.pythong.noteblog.app.download.DownloadService
import top.pythong.noteblog.app.download.ui.DownloadTaskActivity
import top.pythong.noteblog.app.login.ui.LoginActivity
import top.pythong.noteblog.clearLoginUser
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
        fileManagerViewModel = ViewModelFactory.createViewModelWithContext(this, FileManagerViewModel::class)
        return fileManagerViewModel
    }

    override fun getContentView(): Int {
        return R.layout.activity_file_manager
    }

    override fun initView() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.inflateMenu(R.menu.download)
        toolbar.setOnMenuItemClickListener {
            startActivity<DownloadTaskActivity>()
            true
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

        adapter = FileManagerAdapter(fileDirList, currentPath)

        adapter.fileOnClickListener { v, file ->
            fileManagerViewModel.openOrDownloadFile(this,file.toDownloadResource(currentPath))
        }

        val smoothScrollLayoutManager = SmoothScrollLayoutManager(this)
        recyclerView.layoutManager = smoothScrollLayoutManager
        //添加Android自带的分割线
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
    }

    override fun initData() {

        refreshLayout.autoRefresh()

    }

    override fun onErrorResult(error: MsgCode) {
        if (!error.isLoginError()) {
            loadingView.errorMsg {
                it.text = error.msg
            }
            loadingView.errorBtn {
                loadingView.show()
                refreshLayout.autoRefresh()
            }
            loadingView.showError(true)
        } else {
            toast(error.msg)
            loadingView.errorBtn {
                it.text = "去登陆"
                it.setOnClickListener {
                    clearLoginUser()
                    startActivity<LoginActivity>()
                }
            }
            loadingView.errorImg {
                it.setImageResource(R.drawable.squint_eyed)
            }
            loadingView.errorMsg {
                it.text = "登录才给看哟"
            }
            loadingView.showError(true)
        }
    }
}
