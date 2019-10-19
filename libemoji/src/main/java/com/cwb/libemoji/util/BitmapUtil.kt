package com.cwb.libemoji.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import kotlin.math.roundToInt

/**
 * Create by cwb on 2019/10/19
 *
 * Describe: 处理bitmap
 */
object BitmapUtil {

    fun getBitmap(context: Context, id: Int, size: Float): Bitmap {
        val bitmap = BitmapFactory.decodeResource(context.resources, id)
        val wh = dp2px(context, size).toFloat()
        return zoomBitmap(bitmap, wh, wh)
    }

    /**
     *  bitmap缩放
     */
    private fun zoomBitmap(bitmap: Bitmap, width: Float, height: Float): Bitmap {
        val bW = bitmap.width
        val bH = bitmap.height
        Log.e("bitmap", "w:$bW, h:$bH want: w $width, h $height")
        val wScale = width / bW
        val hScale = height / bH
        val matrix = Matrix()
        matrix.postScale(wScale, hScale)
        return Bitmap.createBitmap(bitmap, 0, 0, bW, bH, matrix, true)
    }


    /**
     * 已弃用
     *
     *  加载bitmap 只能对像素整数倍缩放
     */
    @Deprecated("out")
    private fun decodeBitmapFromResource(
        res: Resources, resId: Int,
        reqWidth: Int, reqHeight: Int
    ): Bitmap {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    /**
     *  计算bitmap缩放比例
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int, reqHeight: Int
    ): Int {
        // 源图片的高度和宽度
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
            val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        return inSampleSize
    }

    private fun dp2px(mContext: Context, dpValue: Float): Int {
        return (0.5f + dpValue * mContext.resources.displayMetrics.density).toInt()
    }
}
