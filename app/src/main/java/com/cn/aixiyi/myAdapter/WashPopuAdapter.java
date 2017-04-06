package com.cn.aixiyi.myAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.aixiyi.R;
import com.cn.aixiyi.utils.TextUtil;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/18.
 */
public class WashPopuAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<Map<String, Object>> data=new ArrayList<>();
    public WashPopuAdapter(Context mContext , ArrayList<Map<String, Object>> data ) {
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
            convertView=layoutInflater.inflate(R.layout.wash_floor_popu_item,null);
            viewHolder.mTxtContent= (TextView) convertView.findViewById(R.id.floor_txt);

            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        if (data.size()==0){
            return null;
        }else {
            viewHolder.mTxtContent.setText(TextUtil.getTextToString(data.get(position).get("floor")));
            return convertView;
        }

    }

    class  ViewHolder{
        TextView mTxtContent;
    }
}