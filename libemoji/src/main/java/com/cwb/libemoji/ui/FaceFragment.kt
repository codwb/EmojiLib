package com.cwb.libemoji.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.cwb.libemoji.FaceCenter
import com.cwb.libemoji.R
import com.cwb.libemoji.adapter.FacePageAdapter
import com.cwb.libemoji.adapter.FaceGVAdapter
import com.cwb.libemoji.bean.FaceBean
import com.cwb.libemoji.callback.OnFaceClickListener
import com.cwb.libemoji.view.FaceIndicatorView
import java.util.ArrayList
import kotlin.concurrent.thread

/**
 *   Create by cwb on 2019/10/15
 *
 *   Describe: Emoji表情布局
 */
class FaceFragment : Fragment() {

    //View
    private lateinit var rootView: View
    private lateinit var mVp: ViewPager
    private lateinit var mIndicator: FaceIndicatorView

    private val vpViewList = mutableListOf<View>()

    //参数
    private val columns = 7
    private val rows = 3

    private var oldPosition = 0

    private val mList = arrayListOf<FaceBean>()

    //回调
    private var listener: OnFaceClickListener? = null

    companion object {
        fun getInstance(listener: OnFaceClickListener): FaceFragment {
            val fragment = FaceFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.listener = listener
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rootView = view
        mVp = view.findViewById(R.id.vp)
        mIndicator = view.findViewById(R.id.face_indicator)

        thread {
            if (mList.size > 0) {
                mList.clear()
            }
            mList.addAll(FaceCenter.getFaceList(context!!)!!)
            mHandler.post {
                initViewPager()
                mIndicator.init(getPagerCount(mList))
            }
        }
    }

    private val mHandler = Handler()

    /**
     *  初始化ViewPager的view列表
     */
    private fun initViewPager() {
        vpViewList.clear()
        for (i in 0 until getPagerCount(mList)) {
            vpViewList.add(getViewPagerItem(i, mList))
        }
        val vpAdapter = FacePageAdapter(vpViewList)
        mVp.adapter = vpAdapter
        mVp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mIndicator.playBy(oldPosition, position)
                oldPosition = position
            }

        })
    }

    /**
     * 获取每一个PagerView
     */
    private fun getViewPagerItem(position: Int, list: ArrayList<FaceBean>): View {
        val layout = View.inflate(context, R.layout.layout_face_grid, null)
        val gridView = layout.findViewById(R.id.chart_face_gv) as GridView

        //因为每一页末尾都有一个删除图标，所以每一页的实际表情columns *　rows　－　1; 空出最后一个位置给删除图标
        val subList = ArrayList<FaceBean?>()
        subList.addAll(
            list.subList(
                position * (columns * rows - 1),
                if ((columns * rows - 1) * (position + 1) > list.size)
                    list.size
                else
                    (columns * rows - 1) * (position + 1)
            )
        )

        //末尾添加删除图标
        if (subList.size < columns * rows - 1) {
            for (i in subList.size until columns * rows - 1) {
                subList.add(null)
            }
        }
        val deleteEmoji = FaceBean()
        deleteEmoji.image = "face_delete"
        subList.add(deleteEmoji)

        val mGvAdapter = FaceGVAdapter(subList, context!!)
        gridView.adapter = mGvAdapter
        gridView.numColumns = columns
        // 单击表情执行的操作
        gridView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, i, _ ->
                if (i == columns * rows - 1) {
                    if (listener != null) {
                        listener!!.onDelete()
                    }
                    return@OnItemClickListener
                }
                if (listener != null) {
                    listener!!.onClick(subList[i]!!)
                }
            }

        return gridView
    }

    private fun getPagerCount(list: ArrayList<FaceBean>): Int {
        val count = list.size
        return if (count % (columns * rows - 1) == 0)
            count / (columns * rows - 1)
        else
            count / (columns * rows - 1) + 1
    }
}