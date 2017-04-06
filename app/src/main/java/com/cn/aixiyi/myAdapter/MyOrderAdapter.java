package com.cn.aixiyi.myAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.activity.ControlCenterActivity;
import com.cn.aixiyi.activity.MyOrderActivity;
import com.cn.aixiyi.activity.UserLoginActivity;
import com.cn.aixiyi.activity.WashingMachineActivity;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.MathCommon;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.SharedPrefUtil;
import com.cn.aixiyi.utils.TextUtil;
import com.cn.aixiyi.utils.ToastUtil;

import org.ddq.common.util.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/6.
 */
public class MyOrderAdapter extends BaseAdapter {
    private Activity mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<Map<String, Object>> data=new ArrayList<>();
    private View.OnClickListener onClickListener;
    private  String orderId="";
    private ViewHolder viewHolder;
    private String kcode ="";//夸克编码
    private  String snum="";//序列号
    public MyOrderAdapter(Activity mContext ,String kcode , String snum ,ArrayList<Map<String, Object>> data ) {
        this.mContext = mContext;
        this.data = data;
        this.kcode = kcode;
        this.snum = snum;
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

        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.my_order_item_lay,null);
            viewHolder.cancelImg= (ImageView) convertView.findViewById(R.id.right_img);
            viewHolder.mTxtNum= (TextView) convertView.findViewById(R.id.MMA_Txt_num);
            viewHolder.mTxtTime= (TextView) convertView.findViewById(R.id.MMA_Txt_time);
            viewHolder.mTxtName= (TextView) convertView.findViewById(R.id.xiyiji_name_txt);
            viewHolder.mTxtXyy= (TextView) convertView.findViewById(R.id.xiyiji_ye_txt);
            viewHolder.mTxtRSj= (TextView) convertView.findViewById(R.id.xiyiji_wu_txt);
            viewHolder.mTxtMoney= (TextView) convertView.findViewById(R.id.xiyiji_ddnum_txt);
            viewHolder.mTxtState= (TextView) convertView.findViewById(R.id.MMA_Txt_state);
            viewHolder.mTxtStyle= (TextView) convertView.findViewById(R.id.MMA_Txt_wash_style);
            viewHolder.controlView=convertView.findViewById(R.id.start_cancel_lay);
            viewHolder.mBtnStart= (Button) convertView.findViewById(R.id.start_btn);
            viewHolder.mBtnCancel= (Button) convertView.findViewById(R.id.cancel_btn);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        if (data.size()==0){
            return null;
        }else {
            String xyyStr="";
            String rsjStr="";
            String style="";
            String orderTypeName= TextUtil.getTextToString(data.get(position).get("orderTypeName"));
            if (!TextUtils.isEmpty(orderTypeName)&&orderTypeName.length()==11){
                rsjStr=orderTypeName.substring(0,3);
                xyyStr=orderTypeName.substring(4,7);
                style=orderTypeName.substring(8,11);
            }else if (!TextUtils.isEmpty(orderTypeName)&&orderTypeName.length()==10){
                rsjStr=orderTypeName.substring(0,3);
                xyyStr=orderTypeName.substring(4,7);
                style=orderTypeName.substring(8,10);
            }else if(!TextUtils.isEmpty(orderTypeName)&&orderTypeName.length()==6){
                rsjStr=orderTypeName.substring(0,3);
                style=orderTypeName.substring(4,6);
            }else  if(!TextUtils.isEmpty(orderTypeName)&&orderTypeName.length()==3){
                style=orderTypeName.substring(0,3);
            }else  if(!TextUtils.isEmpty(orderTypeName)&&orderTypeName.length()==2){
                style=orderTypeName.substring(0,2);
            }
            viewHolder.mTxtNum.setText(TextUtil.getTextToString(data.get(position).get("orderNo")));
            viewHolder.mTxtName.setText(TextUtil.getTextToString(data.get(position).get("washingName")));
            viewHolder.mTxtXyy.setText(rsjStr);
            viewHolder.mTxtRSj.setText(xyyStr);
            viewHolder.mTxtStyle.setText(style);
            viewHolder.mTxtMoney.setText(TextUtil.getTextToString(data.get(position).get("amount")));
            String status=TextUtil.getTextToString(data.get(position).get("status"));
            String statusStr="";
            if ("0".equals(status)){
                statusStr="未开始";
                viewHolder.mTxtState.setTextColor(mContext.getResources().getColor(R.color.aixiyi_center_txt_blue));
                viewHolder.controlView.setVisibility(View.VISIBLE);
                viewHolder.cancelImg.setVisibility(View.VISIBLE);
                viewHolder.mTxtTime.setVisibility(View.GONE);
            }else if("1".equals(status)){
                statusStr="使用中";
                viewHolder.mTxtState.setTextColor(mContext.getResources().getColor(R.color.aixiyi_center_txt_blue));
                viewHolder.controlView.setVisibility(View.GONE);
                viewHolder.cancelImg.setVisibility(View.VISIBLE);
                viewHolder.mTxtTime.setVisibility(View.GONE);
            }else if("2".equals(status)){
                statusStr="订单完成";
                viewHolder.mTxtState.setTextColor(mContext.getResources().getColor(R.color.aixiyi_center_txt_green));
                viewHolder.controlView.setVisibility(View.GONE);
                viewHolder.cancelImg.setVisibility(View.GONE);
                viewHolder.mTxtTime.setVisibility(View.VISIBLE);
                viewHolder.mTxtTime.setText(TextUtil.getTextToString(data.get(position).get("washingTime"))+"分钟");
            }else if("3".equals(status)){
                statusStr="订单取消";
                viewHolder.mTxtState.setTextColor(mContext.getResources().getColor(R.color.aixiyi_center_txt_blue));
                viewHolder.controlView.setVisibility(View.GONE);
                viewHolder.cancelImg.setVisibility(View.VISIBLE);
                viewHolder.mTxtTime.setVisibility(View.GONE);
            }else if("4".equals(status)){
                statusStr="订单关闭";
                viewHolder.mTxtState.setTextColor(mContext.getResources().getColor(R.color.aixiyi_center_txt_blue));
                viewHolder.controlView.setVisibility(View.GONE);
                viewHolder.cancelImg.setVisibility(View.VISIBLE);
                viewHolder.mTxtTime.setVisibility(View.GONE);
            }else{
                viewHolder.mTxtState.setTextColor(mContext.getResources().getColor(R.color.aixiyi_center_txt_blue));
                viewHolder.controlView.setVisibility(View.GONE);
                viewHolder.cancelImg.setVisibility(View.VISIBLE);
                viewHolder.mTxtTime.setVisibility(View.GONE);
            }
            viewHolder.mTxtState.setText(statusStr);
            viewHolder.mBtnStart .setOnClickListener(new MyOnClickListner(position));
            viewHolder.mBtnCancel .setOnClickListener(new MyOnClickListner(position));
            return convertView;
        }

    }

    class  ViewHolder{
        ImageView cancelImg;
        TextView mTxtNum;
        TextView mTxtTime;
        TextView mTxtName;
        TextView mTxtXyy;
        TextView mTxtRSj;
        TextView mTxtMoney;
        TextView mTxtState;
        TextView mTxtStyle;
        View controlView;
        Button mBtnCancel;
        Button mBtnStart;

    }

    private    class  MyOnClickListner implements View.OnClickListener{
        private  int position;
        public MyOnClickListner(int position) {
            this.position=position;
        }

        @Override
        public void onClick(View view) {
            int vid=view.getId();
            if (MathCommon.isLogin(mContext)){
                if (vid==viewHolder.mBtnCancel.getId()){
                    Map<String, Object> map=data.get(position);
                    orderId= TextUtil.getTextToString( map.get("orderId"));
                    cancelOrder(position);
                }else if(vid==viewHolder.mBtnStart.getId()){
                    Map<String, Object> map=data.get(position);
                    String orderNo= TextUtil.getTextToString(map.get("orderId")) ;
                    String washTime= TextUtil.getTextToString(map.get("washingTime")) ;
                    Intent intent = new Intent();
                    intent.setClass(mContext, ControlCenterActivity.class);
                    intent.putExtra("orderNo", orderNo);
                    intent.putExtra("kcode", kcode);
                    intent.putExtra("snum", snum);
                    intent.putExtra("washTime", washTime);
                    mContext. startActivity(intent);
                }
            }


        }
    }

    /**
     * 取消订单
     */
    private void cancelOrder(final int position) {
        String params = getParams();
        Map map = new HashMap();
        map.put("data",params);
        MyOkHttpUtils.postRequest(Urls.DELETE_DINGDAN_URLS, map).execute(new MapCallback(mContext,Contanct.DIALOG_UPLOAD) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode==0){
                        data.remove(position);
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }
    private String  getParams() {
        Map<String,Object> map=new HashMap<>();
        map.put("orderId",orderId);
        String data= ParamsUtil.getParamsFromMap(map);
        return  data;
    }
}
