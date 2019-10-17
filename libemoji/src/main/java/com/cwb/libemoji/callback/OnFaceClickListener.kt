package com.cwb.libemoji.callback

import com.cwb.libemoji.bean.FaceBean

/**
 *   Create by cwb on 2019/10/17
 *
 *   Describe: 点击表情回调
 */
interface OnFaceClickListener {

    fun onClick(bean: FaceBean)

    fun onDelete()

}