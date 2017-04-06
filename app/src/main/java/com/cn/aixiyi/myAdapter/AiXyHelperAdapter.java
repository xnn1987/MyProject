package com.cn.aixiyi.myAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.aixiyi.R;
import com.cn.aixiyi.activity.AiMainActivity;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.utils.PictureUtil;
import com.cn.aixiyi.utils.TextUtil;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by sty on 2016/8/26.
 */
public class AiXyHelperAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<Map<String, Object>> data=new ArrayList<>();
    public AiXyHelperAdapter(Context mContext , ArrayList<Map<String, Object>> data ) {
        this.mContext = mContext;
        this.data = data;
        layoutInflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.aixy_helper_item_lay,null);
            viewHolder.mImgTitle= (ImageView) convertView.findViewById(R.id.help_img);
            viewHolder.mTxtContent= (TextView) convertView.findViewById(R.id.help_txt);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        if (data.size()==0){
            return null;
        }else {
            viewHolder.mTxtContent.setText(TextUtil.getTextToString(data.get(position).get("title")));
            String imgUrl=TextUtil.getTextToString(data.get(position).get("img"));
            MyApplication.options=PictureUtil.setImageOptions(mContext,R.drawable.pic_load,4);
            MyApplication.imageLoader.displayImage(imgUrl, viewHolder.mImgTitle, MyApplication.options);
            return convertView;
        }
    }

    class  ViewHolder{
        ImageView mImgTitle;
        TextView mTxtContent;
    }
}

