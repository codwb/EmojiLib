package com.cwb.libemoji.bean

import com.alibaba.fastjson.annotation.JSONField

class FaceBean {

    @JSONField(name = "desc")
    var content: String = ""

    var image = ""

    var imgId = 0

}