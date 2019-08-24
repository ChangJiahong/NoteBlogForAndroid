package top.pythong.noteblog.app.main.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import top.pythong.noteblog.app.login.dao.impl.LoginDataSourceImpl
import top.pythong.noteblog.app.login.service.impl.LoginServiceImpl
import top.pythong.noteblog.app.login.ui.LoginViewModel
import top.pythong.noteblog.app.main.dao.impl.MainDataSourceImpl
import top.pythong.noteblog.app.main.service.impl.MainServiceImpl

/**
 *
 * @author ChangJiahong
 * @date 2019/8/24
 */
class MainViewModelFactory(val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {



        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                mainService = MainServiceImpl(
                    context = context,
                    mainDataSource = MainDataSourceImpl()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}