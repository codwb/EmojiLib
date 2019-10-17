package com.cwb.libemoji.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 *   Create by cwb on 2019/10/16
 *
 *   Describe: ViewPager每页的adapter
 */
class FacePageAdapter(private val mList: MutableList<View>) : PagerAdapter() {

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, bean: Any): Boolean {
        return view == bean
    }

    override fun getCount(): Int {
        return mList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = mList[position]
        container.addView(view)
        return view
    }
}