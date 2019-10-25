package com.cwb.libemoji

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.widget.EditText
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.cwb.libemoji.bean.FaceBean
import com.cwb.libemoji.util.AssetsUtil
import com.cwb.libemoji.util.BitmapUtil
import java.io.IOException
import java.util.regex.Pattern

/**
 *   Create by cwb on 2019/10/15
 *
 *   Describe: 表情处理center
 */
object FaceCenter {

    private const val assetsPath = "json/icon.json"

    private val faceList = mutableListOf<FaceBean>()

    private val faceMap = hashMapOf<String, Int>()

    /**
     *  先初始化加载数据
     */
    private fun init(context: Context) {
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

            //map保存文字和图片id对应
            faceMap[i.content] = i.imgId
        }
    }

    /**
     *  加载表情资源
     */
    fun getFaceList(context: Context): MutableList<FaceBean>? {
        if (faceList.size == 0) {
            init(context)
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
    fun showFace(textView: TextView, content: String, size: Float) {
        if (faceMap.size == 0) {
            init(textView.context)
        }
        val context = textView.context
        val sb = SpannableStringBuilder(content)
        val regex = "\\[(\\S+?)]"
        val p = Pattern.compile(regex)
        val m = p.matcher(content)

        //遍历对照符合表情的文本
        while (m.find()) {
            val tempText = m.group()
            if (faceMap.containsKey(tempText)) {
                val id = faceMap[tempText] ?: 0
                val zoomBitmap = BitmapUtil.getBitmap(context, id, size)
                sb.setSpan(
                    ImageSpan(
                        context,
                        zoomBitmap
                    ),
                    m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        textView.text = sb
        if (textView is EditText) {
            textView.setSelection(sb.length)
        }
    }

    /**
     *  点击删除按钮删除表情
     */
    fun deleteFace(textView: TextView, size: Float) {
        val text = textView.text.toString()
        if (text.isEmpty()) return
        if (text.endsWith("]")) {
            val regex = "\\[(\\S+?)]"
            val p = Pattern.compile(regex)
            val m = p.matcher(text)
            var temp = ""
            while (m.find()) {
                temp = m.group()
            }
            if (temp.isNotEmpty()) {
                val index = text.lastIndexOf(temp)
                //判断最后一个matcher是否在最后的字符
                val result = if (index + temp.length == text.length) {
                    text.subSequence(0, index).toString()
                } else {
                    text.subSequence(0, text.lastIndex).toString()
                }
                //重新加载表情
                showFace(textView, result, size)
            }
        } else {
            val result = text.substring(0, text.lastIndex)
            showFace(textView, result, size)
        }
    }

}