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
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.utils.PictureUtil;
import com.cn.aixiyi.utils.TextUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/6.
 */
public class MyWashingMachAdapter  extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<Map<String, Object>> data=new ArrayList<>();

    public MyWashingMachAdapter(Context mContext , ArrayList<Map<String, Object>> data ) {
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
            convertView=layoutInflater.inflate(R.layout.waching_machine_item_lay,null);
            viewHolder.mImgWash= (ImageView) convertView.findViewById(R.id.waching_mach_img);
            viewHolder.mTxtAddress= (TextView) convertView.findViewById(R.id.waching_address_txt);
            viewHolder.mTxtNum= (TextView) convertView.findViewById(R.id.waching_num_txt);
            viewHolder.mTxtDistance= (TextView) convertView.findViewById(R.id.waching_distance_txt);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        if (data.size()==0){
            return null;
        }else {
            viewHolder.mTxtAddress.setText(TextUtil.getTextToString(data.get(position).get("displayName")));
            viewHolder.mTxtNum.setText(TextUtil.getTextToString(data.get(position).get("freeNums"))+"台空闲");
            viewHolder.mTxtDistance.setText(TextUtil.getTextToString(data.get(position).get("distance"))+"m");
            DisplayImageOptions options = PictureUtil.setImageOptions(mContext,R.drawable.pic_load,4);
            MyApplication.imageLoader.displayImage(TextUtil.getTextToString(data.get(position).get("displayImages")),viewHolder.mImgWash,options);
            return convertView;
        }

    }

    class  ViewHolder{
        ImageView mImgWash;
        TextView mTxtAddress;
        TextView mTxtNum;
        TextView mTxtDistance;
    }
}
