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

import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.myAdapter.MyMessageAdapter;
import com.cn.aixiyi.myview.PullToRefreshView;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.MathCommon;
import com.cn.aixiyi.utils.ParamsUtil;
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
public class MessageCenterActivity extends SlidBackActivity implements  PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    private TextView topTextView;
    private ListView mLvMassage;
    private LinearLayout mMenuLay;
    private PullToRefreshView mPrfContain;
    private ArrayList<Map<String, Object>> data=new ArrayList<>();
    private MyMessageAdapter mAdapter;
    private ImageView mIvJl;
    private int currentPage = 1;
    private final int TYPE_NO = 0;
    private final int TYPE_HEAD = 1;
    private final int TYPE_FOOT = 2;
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
        setContentView(R.layout.my_message_lay);
        findViews();
        setListener();
        setAdapter();
        zeroStatus(data);
        sendType(TYPE_NO);
    }



    private void findViews() {
        mMenuLay = (LinearLayout) findViewById(R.id.titleBar_ll_back);
        topTextView= (TextView) findViewById(R.id.titleBar_tv_title);
        mLvMassage= (ListView)findViewById(R.id.MMA_lv_msg);
        mIvJl = (ImageView) findViewById(R.id.MMA_iv_wujilu);
        mPrfContain = (PullToRefreshView)findViewById(R.id.MMA_ptr_contain);
        mPrfContain.setOnHeaderRefreshListener(this);
        mPrfContain.setOnFooterRefreshListener(this);
        mPrfContain.setHeadBackgroudResource(R.color.backgroud_f7f7f7);
        mPrfContain.setFootBackgroudResource(R.color.backgroud_f7f7f7);
        topTextView.setText("消息");
    }
    private void setListener() {
        mMenuLay.setOnClickListener(this);
    }
    private void setAdapter() {
        mAdapter=new MyMessageAdapter(this,data);
        mLvMassage.setAdapter(mAdapter);
        mLvMassage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map=data.get(i);
                String id= TextUtil.getTextToString(map.get("id"));
                if (MathCommon.isLogin(MessageCenterActivity.this)){
                    Intent intent = new Intent();
                    intent.setClass(MessageCenterActivity.this, MessageDetialActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }
            }
        });
    }

    public void getMyMessageData(final int type, boolean isDialog) {
        String  messageUrls=Urls.MYMASSGE_URLS+"?p="+currentPage;
        MyOkHttpUtils.getRequest(messageUrls).execute(new MapCallback(MessageCenterActivity.this,isDialog) {
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
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.titleBar_ll_back:
                EventBus.getDefault().post(Contanct.MAINACTIVTTY);
                finish();
                break;

        }
    }
    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        currentPage++;
        getMyMessageData(TYPE_FOOT,false);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        resetRefresh(TYPE_HEAD,false);
    }

    //  重置刷新
    private void resetRefresh(int type,boolean isDialog){
        currentPage = 1;
        data.clear();
        getMyMessageData(type,isDialog);
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
