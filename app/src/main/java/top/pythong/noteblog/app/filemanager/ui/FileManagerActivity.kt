package top.pythong.noteblog.app.filemanager.ui

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_file_manager.*
import top.pythong.noteblog.R
import top.pythong.noteblog.base.activity.BaseActivity
import top.pythong.noteblog.base.factory.ViewModelFactory
import top.pythong.noteblog.base.viewModel.BaseViewModel

class FileManagerActivity : BaseActivity() {

    lateinit var fileManagerViewModel: FileManagerViewModel


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
    }

    override fun initData() {

    }
}
