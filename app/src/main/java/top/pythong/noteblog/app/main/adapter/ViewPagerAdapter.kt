package top.pythong.noteblog.app.main.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 *
 * @author ChangJiahong
 * @date 2019/8/24
 */
class ViewPagerAdapter(private val fragments: List<Fragment>, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(p0: Int) = fragments[p0]


    override fun getCount() = fragments.size

}