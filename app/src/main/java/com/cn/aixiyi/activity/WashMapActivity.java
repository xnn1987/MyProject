package com.cn.aixiyi.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.cn.aixiyi.BaseMapDemo;
import com.cn.aixiyi.DistrictSearchDemo;
import com.cn.aixiyi.GeoCoderDemo;
import com.cn.aixiyi.LocationDemo;
import com.cn.aixiyi.NearByDemo;
import com.cn.aixiyi.R;
import com.cn.aixiyi.utils.SetTitleBar;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.BaseFragmentActivity;
import com.cn.aixiyi.utils.common.CommomAcitivity;
import com.cn.aixiyi.utils.common.SlidBackActivity;

/**
 * Created by Administrator on 2016/11/1.
 */
public class WashMapActivity extends SlidBackActivity implements View.OnClickListener{
    private static final String LTAG = WashMapActivity.class.getSimpleName();

    private Button btn1,btn2,btn3,btn4;


    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();

            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                ToastUtil.ToastShow(WashMapActivity.this,"key 验证出错! 错误码 :" + intent.getIntExtra
                        (SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, 0)
                        +  " ; 请在 AndroidManifest.xml 文件中检查 key 设置");
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                ToastUtil.ToastShow(WashMapActivity.this,"key 验证成功! 功能可以正常使用");
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                ToastUtil.ToastShow(WashMapActivity.this,"网络出错");
            }
        }
    }

    private SDKReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wash_map_lay);
        SetTitleBar.setTitleText(this,"附近洗衣机");
        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);
        btn1= (Button) findViewById(R.id.btn1);
        btn2= (Button) findViewById(R.id.btn2);
        btn3= (Button) findViewById(R.id.btn3);
        btn4= (Button) findViewById(R.id.btn4);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        int id=v.getId();
        switch (id){
            case R.id.btn1:
                intent.setClass(WashMapActivity.this, BaseMapDemo.class);
                startActivity(intent);
                overridePendingTransition(R.anim.out_to_left, R.anim.in_from_right);
                break;
            case R.id.btn2:
                intent.setClass(WashMapActivity.this, LocationDemo.class);
                startActivity(intent);
                overridePendingTransition(R.anim.out_to_left, R.anim.in_from_right);
                break;
            case R.id.btn3:
                intent.setClass(WashMapActivity.this, DistrictSearchDemo.class);
                startActivity(intent);
                overridePendingTransition(R.anim.out_to_left, R.anim.in_from_right);
                break;
            case R.id.btn4:
                intent.setClass(WashMapActivity.this, NearByDemo.class);
                startActivity(intent);
                overridePendingTransition(R.anim.out_to_left, R.anim.in_from_right);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消监听 SDK 广播
        unregisterReceiver(mReceiver);
    }
}

