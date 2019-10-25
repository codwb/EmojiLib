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
     * @param view 需要展示表情的view
     * @param content 文本
     * @param size 表情图标大小(dp)
     */
    @Throws(IOException::class)
    fun showFace(view: TextView, content: String, size: Float) {
        if (content.isEmpty() || size == 0f) return
        if (faceMap.size == 0) {
            init(view.context)
        }
        show(view, size, content)
        if (view is EditText) {
            view.setSelection(content.length)
        }
    }

    /**
     *  点击删除按钮删除表情
     *
     * @param view 需要展示表情的view
     * @param size 表情图标大小(dp)
     */
    fun deleteFace(view: TextView, size: Float) {
        val text = view.text.toString()
        if (text.isEmpty()) return
        val selection = view.selectionStart
        val result: String
        //光标新的位置
        var index = 0
        if (view is EditText && selection < text.length) {
            val input = text.substring(0, selection)
            val last = text.substring(selection, text.length)
            result = "${delete(input)}${last}"
            index = result.length - last.length
        } else if (view is EditText && selection == text.length) {
            result = delete(text)
            index = result.length
        } else {
            result = delete(text)
        }
        //展示表情
        show(view, size, result)

        if (view is EditText) {
            //重新设置光标位置
            view.setSelection(index)
        }
    }

    /**
     *  textView展示表情
     */
    private fun show(view: TextView, size: Float, content: String) {
        val context = view.context
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
        view.text = sb
    }

    /**
     *  删除逻辑
     *  @return 处理后的字符串
     */
    private fun delete(text: String): String {
        if (text.isEmpty()) return ""
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
                return if (index + temp.length == text.length) {
                    text.substring(0, index)
                } else {
                    text.substring(0, text.lastIndex)
                }
            }
            return text.substring(0, text.lastIndex)
        } else {
            return text.substring(0, text.lastIndex)
        }
    }

}