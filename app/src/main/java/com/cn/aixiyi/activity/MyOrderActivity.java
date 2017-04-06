package com.cn.aixiyi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.myAdapter.MyOrderAdapter;
import com.cn.aixiyi.myview.PullToRefreshView;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.SetTitleBar;
import com.cn.aixiyi.utils.TextUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.BaseFragmentActivity;
import com.cn.aixiyi.utils.common.CommomAcitivity;
import com.cn.aixiyi.utils.common.SlidBackActivity;
import com.tec.key.Ptlmaner;
import com.ypy.eventbus.EventBus;

import org.ddq.common.util.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date 2016/11/1
 * @author xingdapeng
 * @description 我的订单
 */
public class MyOrderActivity extends SlidBackActivity implements  PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{
    private Intent intent;
    private LinearLayout mMenuLay;
    private TextView topTextView;
    private ListView mLvWashing;
    private PullToRefreshView mPrfContain;
    private ArrayList<Map<String, Object>> data=new ArrayList<>();
    private MyOrderAdapter mAdapter;
    private ImageView mIvJl;
    private int currentPage = 1;
    private final int TYPE_NO = 0;
    private final int TYPE_HEAD = 1;
    private final int TYPE_FOOT = 2;
    private  int order=0;
    private String kcode ="";//夸克编码
    private  String snum="";//序列号
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TYPE_NO:
                    resetRefresh(-1,true);
                    break;
                case TYPE_HEAD:
                    mPrfContain.onHeaderRefreshComplete();
                    break;
                case TYPE_FOOT:
                    mPrfContain.onFooterRefreshComplete(data.size());
                    break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_lay);
        initView();
        intent = getIntent();
        order= intent.getIntExtra("order",0);
        kcode = intent.getStringExtra("kcode");
        snum = intent.getStringExtra("snum");
        DebugLog.e("Response", "order=" + order);
        setListeners();
        setAdapter();
        zeroStatus(data);
        sendType(TYPE_NO);
    }
    private void initView() {
        mMenuLay= (LinearLayout)findViewById(R.id.titleBar_ll_back);
        topTextView= (TextView)findViewById(R.id.titleBar_tv_title);
        mLvWashing= (ListView) findViewById(R.id.MMA_lv_msg);
        mIvJl = (ImageView) findViewById(R.id.MMA_iv_wujilu);
        mPrfContain = (PullToRefreshView) findViewById(R.id.MMA_ptr_contain);
        mPrfContain.setOnHeaderRefreshListener(this);
        mPrfContain.setOnFooterRefreshListener(this);
        mPrfContain.setHeadBackgroudResource(R.color.backgroud_f7f7f7);
        mPrfContain.setFootBackgroudResource(R.color.backgroud_f7f7f7);
        topTextView.setText("我的订单");
    }
    private void setListeners() {
        mMenuLay.setOnClickListener(this);
    }
    private void setAdapter() {
        mAdapter=new MyOrderAdapter(MyOrderActivity.this,kcode,snum,data);
        mLvWashing.setAdapter(mAdapter);

    }
    public void getMyOrderData(final int type, boolean isDialog) {
        String  orderUrls=Urls.MYORDER_URLS+"?p="+currentPage;
        MyOkHttpUtils.getRequest(orderUrls).execute(new MapCallback(MyOrderActivity.this,isDialog) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap!=null){
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        List<Map<String, Object>> bannersList = (List<Map<String, Object>>) rMap.get("data");
                        if (bannersList.size() == 0) {
                            currentPage--;
                        } else {
                            data.addAll(bannersList);
                            mAdapter.notifyDataSetChanged();
                            zeroStatus(data);
                        }
                        sendType(type);
                    }
                }
            }
        });
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.titleBar_ll_back:
                if(order==0){
                    finish();
                }else  if (order==1){
                    EventBus.getDefault().post(Contanct.MAINACTIVTTY);
                    finish();
                }
                break;
        }
    }
    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        currentPage++;
        getMyOrderData(TYPE_FOOT,false);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        resetRefresh(TYPE_HEAD,false);
    }

    //  重置刷新
    private void resetRefresh(int type,boolean isDialog){
        currentPage = 1;
        data.clear();
        getMyOrderData(type,isDialog);
    }
    /**
     * 是否有数据
     * @param list
     */
    private void zeroStatus(List list){
        mIvJl.setVisibility(list.size()>0? View.GONE:View.VISIBLE);
    }
    private void sendType(int type){
        handler.sendEmptyMessage(type);
    }


}
