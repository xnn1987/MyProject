package com.cn.aixiyi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.myAdapter.MySuggestAdapter;
import com.cn.aixiyi.myAdapter.MySuggestDetailAdapter;
import com.cn.aixiyi.myview.PullToRefreshView;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.SetTitleBar;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.CommomAcitivity;
import com.cn.aixiyi.utils.common.SlidBackActivity;

import org.ddq.common.util.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date 2016/11/1
 * @author xingdapeng
 * @description 意见反馈详情
 */
public class SuggestRkDetialActivity  extends SlidBackActivity implements  PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{
    private  Intent intent;
    private ListView mLvWashing;
    private PullToRefreshView mPrfContain;
    private ArrayList<Map<String, Object>> data=new ArrayList<>();
    private MySuggestDetailAdapter mAdapter;
    private ImageView mIvJl;
    private int currentPage = 1;
    private final int TYPE_NO = 0;
    private final int TYPE_HEAD = 1;
    private final int TYPE_FOOT = 2;
    private EditText mSgtEdt;
    private TextView mSendTxt;
    private  String id="";
    private  String content="";
    private  String title="";

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
        setContentView(R.layout.suggest_back_detail_lay);
        SetTitleBar.setTitleText(this,"意见反馈详情");
        intent=getIntent();
        id=intent.getStringExtra("id");
        title=intent.getStringExtra("title");
        initView();
        setAdapter();
        setListener();
        zeroStatus(data);
        sendType(TYPE_NO);
    }

    private void initView() {
        mLvWashing= (ListView) findViewById(R.id.MMA_lv_msg);
        mIvJl = (ImageView) findViewById(R.id.MMA_iv_wujilu);
        mPrfContain = (PullToRefreshView) findViewById(R.id.MMA_ptr_contain);
        mPrfContain.setOnHeaderRefreshListener(this);
        mPrfContain.setOnFooterRefreshListener(this);
        mPrfContain.setHeadBackgroudResource(R.color.backgroud_f7f7f7);
        mPrfContain.setFootBackgroudResource(R.color.backgroud_f7f7f7);
        mSgtEdt= (EditText) findViewById(R.id.suggest_edt);
        mSendTxt= (TextView) findViewById(R.id.send_txt);

    }

    private void setListener() {
        mSendTxt.setOnClickListener(this);
    }

    private void setAdapter() {
        mAdapter=new MySuggestDetailAdapter(SuggestRkDetialActivity.this,data, title);
        mLvWashing.setAdapter(mAdapter);

    }
    public void getMySuggestData(final int type, boolean isDialog) {
        String  suggestUrls=Urls.SUGGESTDEAILT_URLS+"?id="+id+"&p="+currentPage;
        MyOkHttpUtils.getRequest(suggestUrls).execute(new MapCallback(SuggestRkDetialActivity.this,isDialog) {
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
            case R.id.send_txt:
                sendSuggestData();
                break;

        }
    }

    private void sendSuggestData() {
        if (TextUtils.isEmpty(mSgtEdt.getText().toString())){
            ToastUtil.ToastShow(this,getResources().getString(R.string.personinfo));
            return;
        }
        content=mSgtEdt.getText().toString().trim();
        String params= getParams();
        //https请求
        MyYingShiVolleyUtils.getIntance(this).OkHttpPostStringTypeOne(Urls.SUGGESTDEAILTSEND_URLS,params, new MyXiaoYaCallBack(this) {
            @Override
            public void onResponse(Object response) {
                if (!TextUtils.isEmpty(response.toString())) {
                    DebugLog.e("Response", "MainActivity=" + response);
                    Map<String, Object> map = JsonUtil.getInstance().json2Object(response.toString(), Map.class);
                    int rcode = (int) map.get("rcode");
                    if (rcode == 0) {
                        //假如发送成功
                        resetRefresh(-1,true);
                    }
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private String  getParams() {
        Map<String,Object> map=new HashMap<>();
        map.put("content",content);
        map.put("belongId",id);
        String data= ParamsUtil.getParamsFromMap(map);
        return  data;
    }
    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        currentPage++;
        getMySuggestData(TYPE_FOOT,false);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        resetRefresh(TYPE_HEAD,false);
    }

    //  重置刷新
    private void resetRefresh(int type,boolean isDialog){
        currentPage = 1;
        data.clear();
        getMySuggestData(type,isDialog);
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
