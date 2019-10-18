package com.cwb.libemoji.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

import com.cwb.libemoji.R
import com.cwb.libemoji.bean.FaceBean
import com.cwb.libemoji.util.AssetsUtil

class FaceGVAdapter(private val list: List<FaceBean?>, private val mContext: Context) :
    BaseAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any? {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: ViewHolder
        if (view == null) {
            holder = ViewHolder()
            view = LayoutInflater.from(mContext).inflate(R.layout.item_face, null)
            holder.iv = view!!.findViewById(R.id.face_image)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }
        val faceBean = list[position]
        if (faceBean != null) {
            //先处理image，将string转为资源id
            val image = faceBean.image
            val id = AssetsUtil.getIdByName(mContext, image)
            holder.iv!!.setImageResource(id)
        }
        return view
    }

    internal inner class ViewHolder {
        var iv: ImageView? = null
    }
}