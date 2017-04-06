package com.cn.aixiyi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.MathCommon;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.SetTitleBar;
import com.cn.aixiyi.utils.SharedPrefUtil;
import com.cn.aixiyi.utils.TextUtil;
import com.cn.aixiyi.utils.TimeWatch;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.CommomAcitivity;

import org.ddq.common.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * @date 2016/11/1
 * @author xingdapeng
 * @description 用户注册
 */
public class UserRegisterActivity extends CommomAcitivity {
    private static final String TAG = "LoginRegiesterActivity";
    private Intent intent;
    private EditText mEtTel, mEtPsw, mEtRepeatPsw, mEtRandNo;
    private Button mBtnRandNo, mBtnComplect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register_lay);
        SetTitleBar.setTitleText(this,"");
        initView();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private void initView() {
        mEtTel = (EditText) findViewById(R.id.tel_edt);
        mEtPsw = (EditText) findViewById(R.id.pass_edt);
        mEtRepeatPsw = (EditText) findViewById(R.id.repass_edt);
        mEtRandNo = (EditText) findViewById(R.id.yanzheng_edt);
        mBtnRandNo = (Button) findViewById(R.id.have_yangz_btn);
        mBtnComplect = (Button) findViewById(R.id.gz_result_btn);

    }

    private void setListener() {
        mBtnRandNo.setOnClickListener(this);
        mBtnComplect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        Intent intent = null;
        switch (vId) {
            case R.id.have_yangz_btn:
                sendRandNo();
                break;
            case R.id.gz_result_btn:
                LoginOrRegin();
                break;

        }
    }
    //发送请求获取验证码
    private void sendRandNo() {
        if (TextUtils.isEmpty(mEtTel.getText().toString())){
            ToastUtil.ToastShow(this,getResources().getString(R.string.log_reg_tel));
            return;
        }
        String telphone=mEtTel.getText().toString().trim();
        String randsUrl=Urls.USER_RANDOM+"?phone="+telphone;
        MyOkHttpUtils.getRequest(randsUrl).execute(new MapCallback(UserRegisterActivity.this) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode= (int) rMap.get("rcode");
                    if (rcode==0){
                        setWaitTxt();
                    }else {
                        ToastUtil.ToastShow(getBaseContext(), Contanct.RandomFail);
                    }
                }else {
                    ToastUtil.ToastShow(getBaseContext(), Contanct.RandomFail);
                }
            }
        });
    }
    /**
     * 定时器
     */
    private void setWaitTxt() {
        TimeWatch timewatch = new TimeWatch(60000, 1000, this, mBtnRandNo);
        timewatch.start();
    }

    //点击完成之后选择登陆或者注册
    private void LoginOrRegin() {
        if (TextUtils.isEmpty(mEtTel.getText().toString())) {
            ToastUtil.ToastShow(this, getResources().getString(R.string.log_reg_tel));
            return;
        } else if (TextUtils.isEmpty(mEtPsw.getText().toString())) {
            ToastUtil.ToastShow(this, getResources().getString(R.string.log_reg_pass));
            return;
        }  else {
            if (!mEtRepeatPsw.getText().toString().equals(mEtPsw.getText().toString())) {
                ToastUtil.ToastShow(this, getResources().getString(R.string.reg_pass));
                return;
            } else if (TextUtils.isEmpty(mEtRandNo.getText().toString())) {
                ToastUtil.ToastShow(this, getResources().getString(R.string.reg_yzm));
                return;
            }  else {
                registerUpload();
            }
        }
    }
//    /**
//     * 注册
//     */
//    private void registerUpload() {
//        String params= getParams();
//        //https请求
//        MyYingShiVolleyUtils.getIntance(this).OkHttpPostStringTypeOne(Urls.USER_REGISTER,params, new MyXiaoYaCallBack(this) {
//            @Override
//            public void onResponse(Object response) {
//                if (!TextUtils.isEmpty(response.toString())) {
//                    DebugLog.e("Response", "MainActivity=" + response);
//                    Map<String,Object> loginMap= JsonUtil.getInstance().json2Object(response.toString(), Map.class);
//                    int rcode= (int) loginMap.get("rcode");
//                    String rinfo= (String) loginMap.get("rinfo");
//                    if (rcode==0){
//                        finish();
//                    }else{
//                        ToastUtil.ToastShow(getBaseContext(),Contanct.RegisterFail);
//                        finish();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onError(Exception e) {
//            }
//        });
//    }
    private void registerUpload() {
        String params = getParams();
        Map map = new HashMap();
        map.put("data",params);
        MyOkHttpUtils.postRequest(Urls.USER_REGISTER, map).execute(new MapCallback(UserRegisterActivity.this,Contanct.DIALOG_UPLOAD) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode= (int) rMap.get("rcode");
                    if (rcode==0){
                        finish();
                    }else{
                        ToastUtil.ToastShow(getBaseContext(),Contanct.RegisterFail);
                        finish();
                    }
                }
            }
        });
    }
    private String  getParams() {
        Map<String,Object> map=new HashMap<>();
        String tel=mEtTel.getText().toString().trim();
        String pas=mEtPsw.getText().toString().trim();
        String passa=mEtRepeatPsw.getText().toString().trim();
        String code=mEtRandNo.getText().toString().trim();
        map.put("phone",tel);
        map.put("userPass",pas);
        map.put("userPassAgain",passa);
        map.put("validCode",code);
        String data= ParamsUtil.getParamsFromMap(map);
        return  data;
    }


}

