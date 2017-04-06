package com.cn.aixiyi.myAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.aixiyi.R;
import com.cn.aixiyi.activity.MySaveActivity;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.utils.PictureUtil;
import com.cn.aixiyi.utils.TextUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/6.
 */
public class MyXyMachingAdapter  extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<Map<String, Object>> data=new ArrayList<>();
    private View.OnClickListener onClickListener;
    public MyXyMachingAdapter(Context mContext ,  View.OnClickListener onClickListener,ArrayList<Map<String, Object>> data ) {
        this.mContext = mContext;
        this.data = data;
        this.onClickListener=onClickListener;
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
            convertView=layoutInflater.inflate(R.layout.my_xy_maching_item_frag_lay,null);
            viewHolder.mImgTitle= (ImageView) convertView.findViewById(R.id.washing_title);
            viewHolder.mTxtTitle= (TextView) convertView.findViewById(R.id.waching_style_txt);
            viewHolder.mTxtContent= (TextView) convertView.findViewById(R.id.waching_address_txt);
            viewHolder.mTxtTime= (TextView) convertView.findViewById(R.id.waching_state_txt);
            viewHolder.mBtnYuding= (Button) convertView.findViewById(R.id.yuding_btn);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        if (data.size()==0){
            return null;
        }else {
            viewHolder.mTxtTitle.setText(TextUtil.getTextToString(data.get(position).get("machineName")));
            String content=TextUtil.getTextToString(data.get(position).get("position"))+TextUtil.getTextToString(data.get(position).get("floor")+"楼");
            viewHolder.mTxtContent.setText(content);
            String washingStatus=TextUtil.getTextToString(data.get(position).get("washingStatus"));
            String washingStatusStr="";
            if ("1".equals(washingStatus)){
                washingStatusStr="空闲";
            }else{
                washingStatusStr="使用中";
            }
            viewHolder.mTxtTime.setText(washingStatusStr);
            viewHolder.mBtnYuding .setTag(position);
            viewHolder.mBtnYuding .setOnClickListener(this.onClickListener);
            DisplayImageOptions options = PictureUtil.setImageOptions(mContext,R.mipmap.my_save_xiyiji02,4);
            MyApplication.imageLoader.displayImage(TextUtil.getTextToString(data.get(position).get("img")),viewHolder.mImgTitle,options);
            return convertView;
        }

    }

    class  ViewHolder{
        ImageView mImgTitle;
        TextView mTxtTitle;
        TextView mTxtContent;
        TextView mTxtTime;
        Button mBtnYuding;
    }
}

