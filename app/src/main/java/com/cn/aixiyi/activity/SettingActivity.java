package com.cn.aixiyi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.network.UpdateUtil;
import com.cn.aixiyi.service.OrderMsgService;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.SharedPrefUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.BaseFragmentActivity;
import com.cn.aixiyi.utils.common.CommomAcitivity;
import com.cn.aixiyi.utils.common.SlidBackActivity;
import com.ypy.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/11.
 */
public class SettingActivity extends SlidBackActivity {
    private LinearLayout mLlOvalDot;
    private LinearLayout mMenuLay;
    private TextView topTextView;
    private ToggleButton sysTb;
    private  View helpView,aboutView,checkView;
    private  TextView versionTxt;
    private String versionName = "";
    private SharedPrefUtil sp;
    private OrderMsgService.DownloadBinder binder;
    private boolean binded;
//    private ServiceConnection conn = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            binder = (OrderMsgService.DownloadBinder) service;
//            binded = true;
//            // 开始下载
//            binder.start();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//        }
//    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_lay);
        sp = new SharedPrefUtil(this, Contanct.SP_UPDATA_APP);
        findViews();
        setListeners();
//        if (binded) {
//            binder.start();
//            return;
//        }
//        Intent intent = new Intent(fca, OrderMsgService.class);
//        fca.startService(intent);   //如果先调用startService,则在多个服务绑定对象调用unbindService后服务仍不会被销毁
//        fca.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }



    private void findViews() {
        mMenuLay= (LinearLayout) findViewById(R.id.titleBar_ll_back);
        topTextView= (TextView) findViewById(R.id.titleBar_tv_title);
        sysTb= (ToggleButton) findViewById(R.id.set_dd_tbtn);
        helpView=findViewById(R.id.help_lay);
        aboutView=findViewById(R.id.about_lay);
        checkView=findViewById(R.id.check_lay);
        versionTxt= (TextView) findViewById(R.id.version_txt);
        topTextView.setText("设置");
        //    versionTxt.setText(MyApplication.versionName+"");

    }
    private void setListeners() {
        mMenuLay.setOnClickListener(this);
        helpView.setOnClickListener(this);
        aboutView.setOnClickListener(this);
        checkView.setOnClickListener(this);
//        sysTb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if (buttonView.isChecked()) {
//                    Intent intent = new Intent(fca, OrderMsgService.class);
//                    fca.startService(intent);   //如果先调用startService,则在多个服务绑定对象调用unbindService后服务仍不会被销毁
//                    fca.bindService(intent, conn, Context.BIND_AUTO_CREATE);
//                } else {
//                    if (binded) {
//                        fca.unbindService(conn);
//                    }
//                }
//            }
//        });
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.titleBar_ll_back:
                EventBus.getDefault().post(Contanct.MAINACTIVTTY);
                finish();
                break;
            case R.id.help_lay:
                intent.setClass(SettingActivity.this, HelpCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.about_lay:
                intent.setClass(SettingActivity.this, AboutAiXiyiActivity.class);
                startActivity(intent);
                break;
            case R.id.check_lay:
             //   checkNewVersion();
                break;
        }
    }
    //检查新版本
    private void checkNewVersion() {
        //版本更新
        if (sp.getBoolean(Contanct.SP_UPDATA_APP, false)) {
            getVersionData();
        } else{
            sp.putBoolean(Contanct.SP_UPDATA_APP, true);
            sp.commit();
        }
    }
    /**
     * 获取版本号
     */
    private void getVersionData() {
        String params = getParams();
        Map map = new HashMap();
        map.put("data",params);
        MyOkHttpUtils.postRequest(Urls.VERSION_URL, map).execute(new MapCallback(SettingActivity.this) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        upDateNewVersion(versionName);
                    }
                }
            }
        });
    }
    private String  getParams() {
        Map<String,Object> map=new HashMap<>();
        String data= ParamsUtil.getParamsFromMap(map);
        return  data;
    }

    //更新新版本
    private void upDateNewVersion(String versionName) {
        if (!MyApplication.versionName.equals(versionName)) {
            String downUrl = Urls.BANBEN_UPDATE_download.replace("versionName",versionName);
            UpdateUtil updateUtil = new UpdateUtil(this, downUrl, versionName, MyApplication.versionDes);
            updateUtil.updateThread();
        } else {
            ToastUtil.ToastShow(this, getString(R.string.version_name));
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (binded) {
//            fca.unbindService(conn);
//        }
    }


}
