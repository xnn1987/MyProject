package com.cn.aixiyi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import com.cn.aixiyi.myAdapter.MyQianBaoAdapter;
import com.cn.aixiyi.myview.PullToRefreshView;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.MathCommon;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.TextUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.CommomAcitivity;
import com.ypy.eventbus.EventBus;

import org.ddq.common.util.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/11.
 */
public class MyQianBaoActivity extends CommomAcitivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {


    private LinearLayout mLlOvalDot;
    private LinearLayout mMenuLay, mRightlay;
    private TextView topTextView;
    private ImageView rightImg;
    private  View leftView,middleView,rightView;
    private  TextView leftTxt,middleTxt,rightTxt;
    private  View leftLineView,middleLineView,rightLineView;
    private TextView mMoneyTxt;
    private Button mChongzBtn;
    private ListView mLvWashing;
    private PullToRefreshView mPrfContain;
    private ArrayList<Map<String, Object>> data = new ArrayList<>();
    private MyQianBaoAdapter mAdapter;
    private ImageView mIvJl;
    private ImageView mIvRIcon;
    private int positions = 0;
    private int currentPage = 1;
    private String messageId = "";
    private final int TYPE_NO = 0;
    private final int TYPE_HEAD = 1;
    private final int TYPE_FOOT = 2;
    private String totalMoney = "";
    private  int  checkType=0;
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
        setContentView(R.layout.my_qianbao_lay);
        findViews();
        setListeners();
        setAdapter();

    }
    @Override
    protected void onResume() {
        super.onResume();
        zeroStatus(data);
        sendType(TYPE_NO);
    }

    private void findViews() {
        mMenuLay = (LinearLayout) findViewById(R.id.ThreeTitle2Icon_ll_back);
        topTextView = (TextView)findViewById(R.id.titleBar_tv_title);
        mMoneyTxt = (TextView) findViewById(R.id.my_qianbao_content_txt);
        mChongzBtn = (Button)findViewById(R.id.chongz_btn);
        mRightlay = (LinearLayout) findViewById(R.id.ThreeTitle2Icon_ll_Icon);
        rightImg = (ImageView)findViewById(R.id.ThreeTitle2Icon_iv_icon);
        leftView= findViewById(R.id.time_one_lay);
        middleView= findViewById(R.id.time_two_lay);
        rightView= findViewById(R.id.time_three_lay);
        leftTxt= (TextView) findViewById(R.id.time_one_btn);
        leftLineView= findViewById(R.id.time_one_view);
        middleTxt= (TextView) findViewById(R.id.time_two_btn);
        middleLineView= findViewById(R.id.time_two_view);
        rightTxt= (TextView) findViewById(R.id.time_three_btn);
        rightLineView= findViewById(R.id.time_three_view);
        mLvWashing = (ListView) findViewById(R.id.MMA_lv_msg);
        mIvJl = (ImageView) findViewById(R.id.MMA_iv_wujilu);
        mPrfContain = (PullToRefreshView) findViewById(R.id.MMA_ptr_contain);
        mPrfContain.setOnHeaderRefreshListener(this);
        mPrfContain.setOnFooterRefreshListener(this);
        mPrfContain.setHeadBackgroudResource(R.color.backgroud_f7f7f7);
        mPrfContain.setFootBackgroudResource(R.color.backgroud_f7f7f7);


    }

    private void setListeners() {
        mMenuLay.setOnClickListener(this);
        mRightlay.setOnClickListener(this);
        mChongzBtn.setOnClickListener(this);
        leftView.setOnClickListener(this);
        middleView.setOnClickListener(this);
        rightView.setOnClickListener(this);
    }

    private void setAdapter() {
        mAdapter = new MyQianBaoAdapter(MyQianBaoActivity.this, data);
        mLvWashing.setAdapter(mAdapter);

    }
    public void getMyMoneyData(final int type, boolean isDialog) {
        String  moneyUrls=Urls.MONEY_URLS+"?type="+checkType+"&p="+currentPage;
        MyOkHttpUtils.getRequest(moneyUrls).execute(new MapCallback(MyQianBaoActivity.this,isDialog) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap!=null){
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        Map<String, Object> attrMap = (Map<String, Object>) rMap.get("data");
                        if (!TextUtils.isEmpty( TextUtil.getTextToString(attrMap.get("balance")))){
                            MyApplication.tianCaiMoneyF= Float.parseFloat( TextUtil.getTextToString(attrMap.get("balance")));
                            MyApplication.tianCaiMoney= TextUtil.getTextToString(attrMap.get("balance"));
                            mMoneyTxt.setText(MyApplication.tianCaiMoney + "");
                        }
                        List<Map<String, Object>> bannersList = (List<Map<String, Object>>) attrMap.get("funds");
                        if (bannersList!=null){
                            if (bannersList.size() == 0) {
                                currentPage--;
                            } else {
                                data.addAll(bannersList);
                                mAdapter.notifyDataSetChanged();
                                zeroStatus(data);
                            }
                        }
                        sendType(type);
                    }

                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.ThreeTitle2Icon_ll_back:
                EventBus.getDefault().post(Contanct.MAINACTIVTTY);
                finish();
                break;
            case R.id.chongz_btn:
                if (MathCommon.isLogin(MyQianBaoActivity.this)){
                    intent.setClass(this, MyQBRechargeActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ThreeTitle2Icon_ll_Icon:
                intent.setClass(this, MyQBExplainActivity.class);
                startActivity(intent);
                break;
            case R.id.time_one_lay:
                checkType=0;
                setViewOnClickCheck(true, false, false);
                resetRefresh(-1, true);
                break;
            case R.id.time_two_lay:
                checkType=1;
                setViewOnClickCheck(false, true, false);
                resetRefresh(-1, true);
                break;
            case R.id.time_three_lay:
                checkType=2;
                setViewOnClickCheck(false, false, true);
                resetRefresh(-1, true);
                break;

        }
    }
    private void setViewOnClickCheck(boolean left, boolean middle, boolean right) {
        if (left == true) {
            leftTxt.setTextColor(getResources().getColor(R.color.aixiyi_center_txt_blue));
            leftLineView.setBackgroundColor(getResources().getColor(R.color.aixiyi_center_txt_blue));
        } else {
            leftTxt.setTextColor(getResources().getColor(R.color.aixiyi_setting_gray));
            leftLineView.setBackgroundColor(getResources().getColor(R.color.white));
        }
        if (middle == true) {
            middleTxt.setTextColor(getResources().getColor(R.color.aixiyi_center_txt_blue));
            middleLineView.setBackgroundColor(getResources().getColor(R.color.aixiyi_center_txt_blue));
        } else {
            middleTxt.setTextColor(getResources().getColor(R.color.aixiyi_setting_gray));
            middleLineView.setBackgroundColor(getResources().getColor(R.color.white));
        }
        if (right == true) {
            rightTxt.setTextColor(getResources().getColor(R.color.aixiyi_center_txt_blue));
            rightLineView.setBackgroundColor(getResources().getColor(R.color.aixiyi_center_txt_blue));
        } else {
            rightTxt.setTextColor(getResources().getColor(R.color.aixiyi_setting_gray));
            rightLineView.setBackgroundColor(getResources().getColor(R.color.white));
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
