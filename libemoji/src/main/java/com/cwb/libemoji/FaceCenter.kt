package com.cwb.libemoji

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.cwb.libemoji.bean.FaceBean
import com.cwb.libemoji.util.AssetsUtil
import java.io.IOException
import java.util.regex.Pattern
import kotlin.math.roundToInt

object FaceCenter {

    private const val assetsPath = "json/icon.json"

    private val faceList = mutableListOf<FaceBean>()

    /**
     *  加载表情资源
     */
    fun getFaceList(context: Context): MutableList<FaceBean>? {
        val json = AssetsUtil.readAssets2String(context, assetsPath, "utf-8")
        if (faceList.size == 0) {
            val list = JSON.parseObject(
                json,
                object {
                    var data: MutableList<FaceBean>? = null
                }::class.java
            ).data
            if (list != null) {
                faceList.addAll(list)
            }
        }
        //加载资源的ID
        for (i in faceList) {
            i.imgId = AssetsUtil.getIdByName(context, i.image)
        }
        return faceList
    }

    /**
     * 展示表情
     *
     * @param textView 需要展示表情的view
     * @param content 文本
     * @param size 表情图标大小(dp)
     */
    @Throws(IOException::class)
    fun handlerFaceText(textView: TextView, content: String, size: Float) {
        val context = textView.context
        val sb = SpannableStringBuilder(content)
        val regex = "\\[(\\S+?)\\]"
        val p = Pattern.compile(regex)
        val m = p.matcher(content)

        //遍历对照符合表情的文本
        var iterator: Iterator<FaceBean>
        var face: FaceBean?
        while (m.find()) {
            iterator = faceList.iterator()
            val tempText = m.group()
            while (iterator.hasNext()) {
                face = iterator.next()
                if (tempText == face.content) {
                    //转换为Span并设置Span的大小
                    sb.setSpan(
                        ImageSpan(
                            context,
                            decodeBitmapFromResource(
                                context.resources,
                                face.imgId,
                                dip2px(context, size),
                                dip2px(context, size)
                            )
                        ),
                        m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    break
                }
            }
        }
        textView.text = sb
    }

    /**
     *  点击删除按钮删除表情
     */
    fun deleteFace(textView: TextView, size: Float) {
        val text = textView.text.toString()
        val regex = "\\[(\\S+?)\\]"
        val p = Pattern.compile(regex)
        val m = p.matcher(text)
        var temp = ""
        while (m.find()) {
            temp = m.group()
        }
        if (temp.isNotEmpty()) {
            val index = text.lastIndexOf(temp)
            val result = text.subSequence(0, index).toString()
            //重新加载表情
            handlerFaceText(textView, result, size)
        }
    }

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

    private fun dip2px(mContext: Context, dpValue: Float): Int {
        return (0.5f + dpValue * mContext.resources.displayMetrics.density).toInt()
    }

}