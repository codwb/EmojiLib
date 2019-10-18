package com.cwb.libemoji.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.cwb.libemoji.R
import com.nineoldandroids.animation.Animator
import com.nineoldandroids.animation.AnimatorSet
import com.nineoldandroids.animation.ObjectAnimator
import java.util.*

/**
 * 表情ViewPager下标指示器
 */
class FaceIndicatorView @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null
) : LinearLayout(mContext, attrs) {

    private var mImageViews: ArrayList<ImageView>? = null
    private val bmpSelect: Bitmap
    private val bmpNormal: Bitmap
    private val mMaxHeight: Int
    private val mMaxWidth: Int

    private var mPlayToAnimatorSet: AnimatorSet? = null
    private var mPlayByInAnimatorSet: AnimatorSet? = null
    private var mPlayByOutAnimatorSet: AnimatorSet? = null

    init {
        this.orientation = HORIZONTAL
        mMaxHeight = dip2px(mContext, mHeight)
        mMaxWidth = dip2px(mContext, mHeight)
        bmpSelect = BitmapFactory.decodeResource(resources, R.drawable.indicator_point_select)
        bmpNormal = BitmapFactory.decodeResource(resources, R.drawable.indicator_point_nomal)
    }

    fun init(count: Int) {
        mImageViews = ArrayList()
        this.removeAllViews()
        for (i in 0 until count) {
            val rl = RelativeLayout(mContext)
            val params = LayoutParams(mMaxWidth, mMaxHeight)
            val layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
            val imageView = ImageView(mContext)
            if (i == 0) {
                imageView.setImageBitmap(bmpSelect)
                rl.addView(imageView, layoutParams)
            } else {
                imageView.setImageBitmap(bmpNormal)
                rl.addView(imageView, layoutParams)
            }
            this.addView(rl, params)
            mImageViews!!.add(imageView)
        }
    }

    fun setIndicatorCount(count: Int) {
        if (mImageViews == null || count > mImageViews!!.size) {
            return
        }
        for (i in mImageViews!!.indices) {
            if (i >= count) {
                mImageViews!![i].visibility = View.GONE
                (mImageViews!![i].parent as View).visibility = View.GONE
            } else {
                mImageViews!![i].visibility = View.VISIBLE
                (mImageViews!![i].parent as View).visibility = View.VISIBLE
            }
        }
    }

    fun playTo(position: Int) {
        for (iv in mImageViews!!) {
            iv.setImageBitmap(bmpNormal)
        }
        mImageViews!![position].setImageBitmap(bmpSelect)
        val imageViewStart = mImageViews!![position]
        val animIn1 = ObjectAnimator.ofFloat(imageViewStart, "scaleX", 0.25f, 1.0f)
        val animIn2 = ObjectAnimator.ofFloat(imageViewStart, "scaleY", 0.25f, 1.0f)

        if (mPlayToAnimatorSet != null && mPlayToAnimatorSet!!.isRunning) {
            mPlayToAnimatorSet!!.cancel()
            mPlayToAnimatorSet = null
        }
        mPlayToAnimatorSet = AnimatorSet()
        mPlayToAnimatorSet!!.play(animIn1).with(animIn2)
        mPlayToAnimatorSet!!.duration = 100
        mPlayToAnimatorSet!!.start()
    }

    fun playBy(startPosition: Int, nextPosition: Int) {
        var start = startPosition
        var next = nextPosition
        if (start < 0 || next < 0 || next == start) {
            next = 0
            start = next
        }

        val imageViewStrat = mImageViews!![start]
        val imageViewNext = mImageViews!![next]

        val anim1 = ObjectAnimator.ofFloat(imageViewStrat, "scaleX", 1.0f, 0.25f)
        val anim2 = ObjectAnimator.ofFloat(imageViewStrat, "scaleY", 1.0f, 0.25f)

        if (mPlayByOutAnimatorSet != null && mPlayByOutAnimatorSet!!.isRunning) {
            mPlayByOutAnimatorSet!!.cancel()
            mPlayByOutAnimatorSet = null
        }
        mPlayByOutAnimatorSet = AnimatorSet()
        mPlayByOutAnimatorSet!!.play(anim1).with(anim2)
        mPlayByOutAnimatorSet!!.duration = 100

        val animIn1 = ObjectAnimator.ofFloat(imageViewNext, "scaleX", 0.25f, 1.0f)
        val animIn2 = ObjectAnimator.ofFloat(imageViewNext, "scaleY", 0.25f, 1.0f)

        if (mPlayByInAnimatorSet != null && mPlayByInAnimatorSet!!.isRunning) {
            mPlayByInAnimatorSet!!.cancel()
            mPlayByInAnimatorSet = null
        }
        mPlayByInAnimatorSet = AnimatorSet()
        mPlayByInAnimatorSet!!.play(animIn1).with(animIn2)
        mPlayByInAnimatorSet!!.duration = 100

        anim1.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                imageViewStrat.setImageBitmap(bmpNormal)
                val animFil1l = ObjectAnimator.ofFloat(imageViewStrat, "scaleX", 1.0f)
                val animFill2 = ObjectAnimator.ofFloat(imageViewStrat, "scaleY", 1.0f)
                val mFillAnimatorSet = AnimatorSet()
                mFillAnimatorSet.play(animFil1l).with(animFill2)
                mFillAnimatorSet.start()
                imageViewNext.setImageBitmap(bmpSelect)
                mPlayByInAnimatorSet!!.start()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        mPlayByOutAnimatorSet!!.start()
    }

    private fun dip2px(mContext: Context, dpValue: Int): Int {
        return (0.5f + dpValue * mContext.resources.displayMetrics.density).toInt()
    }

    companion object {

        private const val mHeight = 16
    }


}
