package com.cn.aixiyi.myAdapter;

import android.content.Context;
import android.text.TextUtils;
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
 * Created by Administrator on 2016/11/15.
 */
public class MySuggestAdapter  extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<Map<String, Object>> data=new ArrayList<>();
    public MySuggestAdapter(Context mContext , ArrayList<Map<String, Object>> data ) {
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
            convertView=layoutInflater.inflate(R.layout.suggest_back_item_lay,null);
            viewHolder.mTxtNum= (TextView) convertView.findViewById(R.id.MMA_Txt_title);
            viewHolder.mTxtName= (TextView) convertView.findViewById(R.id.MMA_Txt_content);
            viewHolder.mTxtTime= (TextView) convertView.findViewById(R.id.MMA_Txte_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        if (data.size()==0){
            return null;
        }else {
            viewHolder.mTxtName.setText(TextUtil.getTextToString(data.get(position).get("title")));
            viewHolder.mTxtTime.setText(TextUtil.getTextToString(data.get(position).get("createTime")));
            return convertView;
        }

    }

    class  ViewHolder{
        TextView mTxtNum;
        TextView mTxtName;
        TextView mTxtTime;

    }
}