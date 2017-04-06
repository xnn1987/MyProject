package com.cn.aixiyi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.myview.MyControlCricleView;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.SetTitleBar;
import com.cn.aixiyi.utils.SharedPrefUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.CommomAcitivity;
import com.cn.aixiyi.utils.common.SlidBackActivity;
import com.tec.key.Ptlmaner;

import org.ddq.common.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xingdapeng
 * @date 2016/11/1
 * @description 控制中心
 */
public class ControlCenterActivity extends SlidBackActivity {
    private Intent intent;
    private TextView mTxtName;
    private MyControlCricleView controlCricleView;
    private Button mTxtPut;
    private Button mTxtQu;
    private Button mTxtWash;
    private  String washId="";
    private  String washState="1";
    private String kcode ="";//夸克编码
    private  String snum="";//序列号
    private  String washTime="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_center_lay);
        intent = getIntent();
        washId = intent.getStringExtra("orderNo");
        kcode = intent.getStringExtra("kcode");
        snum = intent.getStringExtra("snum");
        washTime= intent.getStringExtra("washTime");
        SetTitleBar.setTitleText(ControlCenterActivity.this, "控制中心");
        initView();
        setListener();

    }

    private void initView() {
        controlCricleView= (MyControlCricleView) findViewById(R.id.control_center_img);
        mTxtName = (TextView) findViewById(R.id.washing_center_txt);
        mTxtPut = (Button) findViewById(R.id.put_btn);
        mTxtQu = (Button) findViewById(R.id.qu_btn);
        mTxtWash = (Button) findViewById(R.id.xiyi_btn);

    }

    private void setListener() {
        mTxtPut.setOnClickListener(this);
        mTxtQu.setOnClickListener(this);
        mTxtWash.setOnClickListener(this);
        washState= MyApplication.washState;
        if ("1".equals(washState)){
            mTxtPut.setClickable(true);
            mTxtQu.setClickable(false);
            mTxtWash.setClickable(false);
        }else if("2".equals(washState)){
            mTxtPut.setClickable(false);
            mTxtWash.setClickable(true);
            mTxtQu.setClickable(false);
        }else if("3".equals(washState)){
            mTxtPut.setClickable(false);
            mTxtWash.setClickable(false);
            mTxtQu.setClickable(true);
    }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.put_btn:
                putData();
                break;
            case R.id.qu_btn:
                quData();
                break;
            case R.id.xiyi_btn:
                washData();
                break;
        }
    }
    private void putData() {
        String params = getParams();
        Map map = new HashMap();
        map.put("data",params);
        MyOkHttpUtils.postRequest(Urls.WASH_URLS, map).execute(new MapCallback(ControlCenterActivity.this) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        MyApplication.washState="2";
                        washState= MyApplication.washState;
                        mTxtPut.setClickable(false);
                        mTxtWash.setClickable(true);
                        mTxtQu.setClickable(false);
                    }
                }
            }
        });
    }
    private void washData() {
        String params = getParams();
        Map map = new HashMap();
        map.put("data",params);
        MyOkHttpUtils.postRequest(Urls.WASH_URLS, map).execute(new MapCallback(ControlCenterActivity.this) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        MyApplication.washState="3";
                        washState= MyApplication.washState;
                        mTxtPut.setClickable(false);
                        mTxtWash.setClickable(false);
                        mTxtQu.setClickable(true);
                        if (!TextUtils.isEmpty(washTime)){
                            int washTimeInt=Integer.parseInt(washTime);
                            washTimeInt=washTimeInt*60*1000/100;
                            controlCricleView.setProgress(100,washTimeInt,"正在洗衣");
                        }
                    }
                }
            }
        });
    }
    private void quData() {
        String params = getParams();
        Map map = new HashMap();
        map.put("data",params);
        MyOkHttpUtils.postRequest(Urls.WASH_URLS, map).execute(new MapCallback(ControlCenterActivity.this) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        mTxtPut.setClickable(false);
                        mTxtQu.setClickable(false);
                        mTxtWash.setClickable(false);
                        controlCricleView.setProgress(0,0,"洗衣结束");
                    }
                }
            }
        });
    }
    private String  getParams() {
        Map<String,Object> map=new HashMap<>();
        map.put("a",washId);
        map.put("b",washState);
        String data= ParamsUtil.getParamsFromMap(map);
        return  data;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
