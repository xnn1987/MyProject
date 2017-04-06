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
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.myAdapter.MyWashingMachAdapter;
import com.cn.aixiyi.myview.PullToRefreshView;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.MathCommon;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.TextUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.SlidBackActivity;

import org.ddq.common.util.JsonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * @date 2016/11/1
 * @author xingdapeng
 * @description 附近的洗衣机
 */
public class WashingMachineActivity extends SlidBackActivity implements  PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{
    private  Intent intent;
    private ListView mLvWashing;
    private PullToRefreshView mPrfContain;
    private ArrayList<Map<String, Object>> data=new ArrayList<>();
    private MyWashingMachAdapter mAdapter;
    private LinearLayout mMenuLay, mRightlay;
    private ImageView mIvJl;
    private ImageView mIvRIcon;
    private TextView topTextView;
    private int positions=0;
    private int currentPage = 1;
    private String messageId = "";
    private final int TYPE_NO = 0;
    private final int TYPE_HEAD = 1;
    private final int TYPE_FOOT = 2;
    private  double lac=0;
    private  double lon=0;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.washing_machine_lay);
        initView();
        intent=getIntent();
        lac=intent.getDoubleExtra("lac",0);
        lon=intent.getDoubleExtra("lon",0);
        setListener();
        setAdapter();
        zeroStatus(data);
        sendType(TYPE_NO);
    }
    private void initView() {
        mLvWashing= (ListView) findViewById(R.id.MMA_lv_msg);
        mIvJl = (ImageView) findViewById(R.id.MMA_iv_wujilu);
        mMenuLay = (LinearLayout) findViewById(R.id.ThreeTitle2Icon_ll_back);
        topTextView= (TextView) findViewById(R.id.titleBar_tv_title);
        mRightlay = (LinearLayout) findViewById(R.id.ThreeTitle2Icon_ll_Icon);
        mIvRIcon = (ImageView) findViewById(R.id.ThreeTitle2Icon_iv_icon);
        mPrfContain = (PullToRefreshView) findViewById(R.id.MMA_ptr_contain);
        mPrfContain.setOnHeaderRefreshListener(this);
        mPrfContain.setOnFooterRefreshListener(this);
        mPrfContain.setHeadBackgroudResource(R.color.backgroud_f7f7f7);
        mPrfContain.setFootBackgroudResource(R.color.backgroud_f7f7f7);
        mIvRIcon.setImageResource(R.mipmap.wash_dingwei);
        topTextView.setText("附近");
    }

    private void setAdapter() {
        mAdapter=new MyWashingMachAdapter(WashingMachineActivity.this,data);
        mLvWashing.setAdapter(mAdapter);
        mLvWashing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map=data.get(i);
                if (MathCommon.isLogin(WashingMachineActivity.this)){
                    Intent intent = new Intent();
                    intent.setClass(WashingMachineActivity.this, WashMachListActivity.class);
                    intent.putExtra("washingAreaId", TextUtil.getTextToString(map.get("id")));
                    intent.putExtra("washingArea", TextUtil.getTextToString(map.get("displayName")));
                    intent.putExtra("freeNums", TextUtil.getTextToString(map.get("freeNums")));
                    startActivity(intent);
                }
            }
        });

    }
    private void setListener() {
        mMenuLay.setOnClickListener(this);
        mRightlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.ThreeTitle2Icon_ll_back:
                finish();
                break;
            case R.id.ThreeTitle2Icon_ll_Icon:
                Intent intent = new Intent();
                intent.setClass(WashingMachineActivity.this, WashAddressMapActivity.class);
                 intent.putExtra("lists",(Serializable)data);
                startActivity(intent);
                break;
        }
    }
    public void getWashingData(final int type, boolean isDialog) {
        DebugLog.e("Response", "getWashingData=" +"longitude="+lon+"&latitude="+lac);
        DebugLog.e("Response", "Cookie=" + MyApplication.cookies);
        String  washUrls=Urls.WASHING_MASHINGNUM_URLS+"?longitude="+lon+"&latitude="+lac+"&p="+currentPage;
        DebugLog.e("Response", "washUrls=" +washUrls);
        MyOkHttpUtils.getRequest(washUrls).execute(new MapCallback(WashingMachineActivity.this,isDialog) {
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
    public void onFooterRefresh(PullToRefreshView view) {
        currentPage++;
        getWashingData(TYPE_FOOT,false);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        resetRefresh(TYPE_HEAD,false);
    }

    //  重置刷新
    private void resetRefresh(int type,boolean isDialog){
        currentPage = 1;
        data.clear();
        getWashingData(type,isDialog);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
