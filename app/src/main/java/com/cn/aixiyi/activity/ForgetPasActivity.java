package com.cn.aixiyi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.SetTitleBar;
import com.cn.aixiyi.utils.SharedPrefUtil;
import com.cn.aixiyi.utils.TimeWatch;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.CommomAcitivity;

import org.ddq.common.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * @date 2016/11/1
 * @author xingdapeng
 * @description 忘记密码
 */
public class ForgetPasActivity extends CommomAcitivity {

    private  Intent intent;
    private EditText mEdttel,mEdtrand,mEdtnewpass,mEtRepeatPsw;
    private Button eBtnMsg;
    private Button eBtnEnsure;
    private  String tel="";
    private SharedPrefUtil sharedUserUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_pass_lay);
        intent=getIntent();
        tel=intent.getStringExtra("tel");
        SetTitleBar.setTitleText(this,"重置密码");
        initView();
        setListener();
        getUserSp();
    }


    private void initView() {
        mEdttel= (EditText) findViewById(R.id.tel_edt);
        mEdtrand= (EditText) findViewById(R.id.yanzheng_edt);
        mEdtnewpass= (EditText) findViewById(R.id.pass_edt);
        mEtRepeatPsw = (EditText) findViewById(R.id.repass_edt);
        eBtnMsg= (Button) findViewById(R.id.have_yangz_btn);
        eBtnEnsure= (Button) findViewById(R.id.gz_result_btn);
        mEdttel.setText(tel);
    }

    private void setListener() {
        eBtnMsg.setOnClickListener(this);
        eBtnEnsure.setOnClickListener(this);
    }
    public void getUserSp() {
        sharedUserUtil = new SharedPrefUtil(getBaseContext(), Contanct.SP_USER);
    }
    @Override
    public void onClick(View v) {
        int vId=v.getId();
      switch (vId){
          case  R.id.have_yangz_btn:
           getMsgCodeData();
          break;
          case  R.id.gz_result_btn:
              setForgetPassData();
              break;
      }
    }
    private void getMsgCodeData() {
        if (TextUtils.isEmpty(mEdttel.getText().toString())){
            ToastUtil.ToastShow(this,getResources().getString(R.string.log_reg_tel));
            return;
        }
        String telphone=mEdttel.getText().toString().trim();
        String randsUrl=Urls.USER_RANDOM+"?phone="+telphone;
        MyOkHttpUtils.getRequest(randsUrl).execute(new MapCallback(ForgetPasActivity.this) {
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
    private void setForgetPassData() {
        if (TextUtils.isEmpty(mEdttel.getText().toString())) {
            ToastUtil.ToastShow(this, getResources().getString(R.string.log_reg_tel));
            return;
        } else if (TextUtils.isEmpty(mEdtrand.getText().toString())) {
            ToastUtil.ToastShow(this, getResources().getString(R.string.reg_yzm));
            return;
        } else if (TextUtils.isEmpty(mEdtnewpass.getText().toString())) {
            ToastUtil.ToastShow(this, getResources().getString(R.string.log_reg_pass));
            return;
        } else {
            if (!mEtRepeatPsw.getText().toString().equals(mEdtnewpass.getText().toString())) {
                ToastUtil.ToastShow(this, getResources().getString(R.string.reg_pass));
                return;
            }else{
                ForgetPassData();
            }

        }
    }

    private void ForgetPassData() {
        String params = getParams();
        Map map = new HashMap();
        map.put("data",params);
        MyOkHttpUtils.postRequest(Urls.USER_FORGET, map).execute(new MapCallback(ForgetPasActivity.this,Contanct.DIALOG_UPLOAD) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode= (int) rMap.get("rcode");
                    if (rcode==0){
                        sharedUserUtil.putString(Contanct.SP_USERNAME, mEdttel.getText().toString().trim());
                        sharedUserUtil.putString(Contanct.SP_USERPASS, mEdtnewpass.getText().toString().trim());
                        sharedUserUtil.commit();
                        finish();
                    }else{
                        ToastUtil.ToastShow(getBaseContext(), Contanct.ForgetPassFail+",验证码超时或不正确");
                    }
                }
            }
        });
    }

    private String  getParams() {
        Map<String,Object> map=new HashMap<>();
        String tel=mEdttel.getText().toString().trim();
        String pas=mEdtnewpass.getText().toString().trim();
        String passa=mEtRepeatPsw.getText().toString().trim();
        String code=mEdtrand.getText().toString().trim();
        map.put("phone",tel);
        map.put("userPass",pas);
        map.put("userPassAgain",passa);
        map.put("validCode",code);
        String data= ParamsUtil.getParamsFromMap(map);
        return  data;
    }


    /**
     * 定时器
     */
    private void setWaitTxt() {
        TimeWatch timewatch = new TimeWatch(60000, 1000, ForgetPasActivity.this, eBtnMsg);
        timewatch.start();
    }

    /**
     * 存储用户信息
     */
    private void saveUserInfo(String data) {
        SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(getBaseContext(), Contanct.SP_USER_NAME);
        sharedPrefUtil.putString(Contanct.SP_USERINFO,data);
        sharedPrefUtil.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
