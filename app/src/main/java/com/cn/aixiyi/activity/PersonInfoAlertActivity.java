package com.cn.aixiyi.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.aixiyi.R;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.myview.MyHeaderCricleView;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.SlidBackActivity;
import com.ypy.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/5.
 */
public class PersonInfoAlertActivity extends SlidBackActivity {

    private Intent intent;
    private LinearLayout backView;
    private TextView topTextView;
    private  TextView rightTextView;
    private  TextView leftTxt;
    private EditText rightEdt;
    private  String leftStr="";
    private  String type="";
    private  String content="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_info_update_lay);
        intent=getIntent();
        leftStr=intent.getStringExtra("title");
        type=intent.getStringExtra("type");
        findViews();
        setListeners();
    }
    private void findViews() {
        backView = (LinearLayout) findViewById(R.id.ThreeTitleBar_ll_back);
        topTextView = (TextView) findViewById(R.id.titleBar_tv_title);
        rightTextView = (TextView) findViewById(R.id.ThreeTitleBar_tv_click);
        leftTxt= (TextView) findViewById(R.id.person_info_txt);
        rightEdt= (EditText) findViewById(R.id.person_info_edt);
        leftTxt.setText(leftStr);
        topTextView.setText("修改信息");
        rightTextView.setText("确定");
    }

    private void setListeners() {
        backView.setOnClickListener(this);
        rightTextView.setOnClickListener(this);
        rightTextView.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ThreeTitleBar_ll_back:
                finish();
                break;
            case R.id.ThreeTitleBar_tv_click:
                uploadPersonInfo();
                break;

        }
    }
    private void uploadPersonInfo() {
        if (TextUtils.isEmpty(rightEdt.getText().toString().trim())) {
            ToastUtil.ToastShow(PersonInfoAlertActivity.this, getResources().getString(R.string.personinfo));
            return;
        }
        content = rightEdt.getText().toString().trim();
        String params = getParams(content);
        Map map = new HashMap();
        map.put("data",params);
        MyOkHttpUtils.postRequest(Urls.USER_INFO_UPDATE, map).execute(new MapCallback(PersonInfoAlertActivity.this) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                DebugLog.e( "Response" ,"rMap="+ rMap.toString() ) ;
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        Intent intent=new Intent();
                        intent.putExtra("content",content);
                        setResult(RESULT_OK,intent);
                        finish();
                        DebugLog.e( "Response" ,"RESULT_OK=") ;
                    }else{
                        ToastUtil.ToastShow(getBaseContext(),"更新失败");
                        finish();
                    }
                }
            }
        });
    }
    private String getParams(String content) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("info", content);
        String data = ParamsUtil.getParamsFromMap(map);
        return data;
    }
}
