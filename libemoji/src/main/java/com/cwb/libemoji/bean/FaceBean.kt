package com.cwb.libemoji.bean

import com.alibaba.fastjson.annotation.JSONField

/**
 *   Create by cwb on 2019/10/15
 *
 *   Describe: 表情实体类
 */
class FaceBean {

    @JSONField(name = "desc")
    var content: String = ""

    var image = ""

    var imgId = 0

}