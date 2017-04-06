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
import com.cn.aixiyi.myAdapter.AdsRevenueAdapter;
import com.cn.aixiyi.myview.MyAdsCircleView;
import com.cn.aixiyi.myview.PullToRefreshView;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.SharedPrefUtil;
import com.cn.aixiyi.utils.TextUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.BaseFragmentActivity;
import com.cn.aixiyi.utils.common.CommomAcitivity;
import com.cn.aixiyi.utils.common.SlidBackActivity;
import com.ypy.eventbus.EventBus;

import org.ddq.common.util.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/11.
 */
public class AdsRevenueActivity extends SlidBackActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{

    private LinearLayout mMenuLay, mRightlay;
    private TextView mTotalTxt;
    private TextView topTextView;
    private ListView mLvAds;
    private PullToRefreshView mPrfContain;
    private ArrayList<Map<String, Object>> data = new ArrayList<>();
    private AdsRevenueAdapter mAdapter;
    private ImageView mIvJl;
    private ImageView rightImg;
    private MyAdsCircleView myAdsCircleView;
    private int positions = 0;
    private int currentPage = 1;
    private String messageId = "";
    private final int TYPE_NO = 0;
    private final int TYPE_HEAD = 1;
    private final int TYPE_FOOT = 2;
    private String totalMoney = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TYPE_NO:
                    resetRefresh(-1, true);
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
        setContentView(R.layout.my_ads_lay);
        findViews();
        setListeners();
     //   getTotalMoney();
        setAdapter();
//        zeroStatus(data);
//        sendType(TYPE_NO);
    }


    private void findViews() {
        mMenuLay = (LinearLayout)findViewById(R.id.ThreeTitle2Icon_ll_back);
        topTextView= (TextView) findViewById(R.id.titleBar_tv_title);
        mRightlay = (LinearLayout) findViewById(R.id.ThreeTitle2Icon_ll_Icon);
        rightImg = (ImageView)findViewById(R.id.ThreeTitle2Icon_iv_icon);
        myAdsCircleView= (MyAdsCircleView) findViewById(R.id.adstotal_img);
        mLvAds = (ListView)findViewById(R.id.MMA_lv_msg);
        mIvJl = (ImageView) findViewById(R.id.MMA_iv_wujilu);
        mPrfContain = (PullToRefreshView) findViewById(R.id.MMA_ptr_contain);
        mPrfContain.setOnHeaderRefreshListener(this);
        mPrfContain.setOnFooterRefreshListener(this);
        mPrfContain.setHeadBackgroudResource(R.color.backgroud_f7f7f7);
        mPrfContain.setFootBackgroudResource(R.color.backgroud_f7f7f7);
        rightImg.setImageResource(R.mipmap.my_qianbao_guize);
        mTotalTxt= (TextView) findViewById(R.id.MMA_Txt_content);
        topTextView.setText("广告收益明细");
        myAdsCircleView.setProgress(80);


    }
    private void setListeners() {
        mMenuLay.setOnClickListener(this);
        mRightlay.setOnClickListener(this);
    }
    private void setAdapter() {
        mAdapter = new AdsRevenueAdapter(this, data);
        mLvAds.setAdapter(mAdapter);
        mLvAds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

    }

    private void getTotalMoney() {
        String params = getParams();
        Map map = new HashMap();
        map.put("data",params);
        MyOkHttpUtils.postRequest(Urls.ADSMONEY_URLS, map).execute(new MapCallback(AdsRevenueActivity.this) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        String money= TextUtil.getTextToString(rMap.get(""));
                        mTotalTxt.setText("￥"+money);
                    } else{
                        ToastUtil.ToastShow(getBaseContext(),getResources().getString(R.string.log_reg_pass_tf));
                    }
                }
            }
        });
    }
    public void getMyMoneyData(final int type, boolean isDialog) {
        String  orderUrls=Urls.ADSMONEY_URLS+"?p="+currentPage;
        MyOkHttpUtils.getRequest(orderUrls).execute(new MapCallback(AdsRevenueActivity.this,isDialog) {
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

    private String getParams() {
        Map<String,Object> map=new HashMap<>();

        String data= ParamsUtil.getParamsFromMap(map);
        return data;
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.ThreeTitle2Icon_ll_back:
                EventBus.getDefault().post(Contanct.MAINACTIVTTY);
                finish();
                break;
            case R.id.ThreeTitle2Icon_ll_Icon:
                intent.setClass(this, AdsExplainActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        currentPage++;
        getMyMoneyData(TYPE_FOOT, false);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        resetRefresh(TYPE_HEAD, false);
    }

    //  重置刷新
    private void resetRefresh(int type, boolean isDialog) {
        currentPage = 1;
        data.clear();
        getMyMoneyData(type, isDialog);
    }

    /**
     * 是否有数据
     *
     * @param list
     */
    private void zeroStatus(List list) {
        mIvJl.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
    }

    private void sendType(int type) {
        handler.sendEmptyMessage(type);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
