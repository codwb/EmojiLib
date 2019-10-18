package com.cwb.libemoji

import android.content.Context
import com.alibaba.fastjson.JSON
import com.cwb.libemoji.bean.FaceBean
import com.cwb.libemoji.util.AssetsUtil

object FaceCenter {

    private const val assetsPath = "json/icon.json"

    fun getFaceList(context: Context): MutableList<FaceBean>? {
        val json = AssetsUtil.readAssets2String(context, assetsPath, "utf-8")
        return JSON.parseObject(
            json,
            object {
                var data: MutableList<FaceBean>? = null
            }::class.java
        ).data
    }

}