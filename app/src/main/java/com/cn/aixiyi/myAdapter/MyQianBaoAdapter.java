package com.cn.aixiyi.myAdapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.aixiyi.R;
import com.cn.aixiyi.utils.TextUtil;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/7.
 */
public class MyQianBaoAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;
    private ArrayList<Map<String, Object>> data = new ArrayList<>();

    public MyQianBaoAdapter(Context mContext, ArrayList<Map<String, Object>> data) {
        this.mContext = mContext;
        this.data = data;
        layoutInflater = LayoutInflater.from(mContext);
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

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.my_qianbao_item_lay, null);
            viewHolder.mTxtTime = (TextView) convertView.findViewById(R.id.xiyi_time_txt);
            viewHolder.mTxtContent = (TextView) convertView.findViewById(R.id.xiyi_txt);
            viewHolder.mTxtNum = (TextView) convertView.findViewById(R.id.xiyi_num_txt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (data.size() == 0) {
            return null;
        } else {
            if(!TextUtils.isEmpty((String) data.get(position).get("createTime"))){
                String createTime=TextUtil.getTextToString(data.get(position).get("createTime"));
                String createTimeTemp=createTime.substring(5,createTime.length());
                viewHolder.mTxtTime.setText(createTimeTemp);
            }
            viewHolder.mTxtContent.setText(TextUtil.getTextToString(data.get(position).get("fundsName")));
            String amount = TextUtil.getTextToString(data.get(position).get("amount"));
            if (!TextUtils.isEmpty(amount)) {
                float amountF = Float.parseFloat(amount);
                if (amountF < 0) {
                    viewHolder.mTxtNum.setTextColor(mContext.getResources().getColor(R.color.aixiyi_qianbao_red));
                } else if (amountF > 0) {
                    viewHolder.mTxtNum.setText("+" + amount);
                    viewHolder.mTxtNum.setTextColor(mContext.getResources().getColor(R.color.aixiyi_ads_yellow));
                } else {
                    viewHolder.mTxtNum.setText(amount);
                    viewHolder.mTxtNum.setTextColor(mContext.getResources().getColor(R.color.aixiyi_ads_yellow));
                }
            }

            return convertView;
        }

    }

    class ViewHolder {
        TextView mTxtTime;
        TextView mTxtContent;
        TextView mTxtNum;
    }
}

