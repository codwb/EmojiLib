package com.cwb.libemoji.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cwb.libemoji.R;
import com.cwb.libemoji.bean.FaceBean;
import com.cwb.libemoji.util.AssetsUtil;

import java.util.List;

public class FaceGVAdapter extends BaseAdapter {

    private List<FaceBean> list;
    private Context mContext;

    public FaceGVAdapter(List<FaceBean> list, Context mContext) {
        super();
        this.list = list;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_face, null);
            holder.iv = convertView.findViewById(R.id.face_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FaceBean faceBean = list.get(position);
        if (faceBean != null) {
            //   holder.iv.setImageBitmap(EmojiUtil.decodeSampledBitmapFromResource(getActivity().getResources(), list.get(position).getImageUri(),
            //           EmojiUtil.dip2px(getActivity(), 32), EmojiUtil.dip2px(getActivity(), 32)));

            //先处理image，将string转为资源id
            String image = faceBean.getImage();
            int id = AssetsUtil.INSTANCE.getIdByName(mContext, image);
            holder.iv.setImageResource(id);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv;
    }
}