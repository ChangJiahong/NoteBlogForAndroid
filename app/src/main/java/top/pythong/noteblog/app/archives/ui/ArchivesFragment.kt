package top.pythong.noteblog.app.archives.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import top.pythong.noteblog.R

class ArchivesFragment : Fragment() {

    companion object {
        fun newInstance() = ArchivesFragment()
    }

    private lateinit var viewModel: ArchivesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.archives_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ArchivesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
