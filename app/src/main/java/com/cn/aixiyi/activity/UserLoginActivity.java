package com.cn.aixiyi.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
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
import com.cn.aixiyi.utils.SharedPrefUtil;
import com.cn.aixiyi.utils.TextUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.CommomAcitivity;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.ddq.common.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * @author xingdapeng
 * @date 2016/11/1
 * @description 用户登录
 */
public class UserLoginActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "UserLoginActivity";
    private Intent intent;
    private Button mSkipBtn;
    private EditText mEtTel, mEtPsw;
    private Button mBtnComplect;
    private Button mRigsterBtn;
    private TextView mFrogetTxt;
    private SharedPrefUtil sharedUserUtil;
    private  TextView xieyiTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_lay);
        initView();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        mSkipBtn = (Button) findViewById(R.id.skip_btn);
        mEtTel = (EditText) findViewById(R.id.tel_edt);
        mEtPsw = (EditText) findViewById(R.id.pass_edt);
        xieyiTxt= (TextView) findViewById(R.id.aixiyi_xieyi_txt);
        mBtnComplect = (Button) findViewById(R.id.gz_result_btn);
        mRigsterBtn = (Button) findViewById(R.id.register_btn);
        mFrogetTxt = (TextView) findViewById(R.id.forget_txt);

    }

    private void setListener() {
        mSkipBtn.setOnClickListener(this);
        mBtnComplect.setOnClickListener(this);
        mRigsterBtn.setOnClickListener(this);
        mFrogetTxt.setOnClickListener(this);
        xieyiTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        Intent intent = null;
        switch (vId) {
            case R.id.skip_btn:
                intentToMainActivity();
                break;
            case R.id.gz_result_btn:
                gotoLogin();
                break;
            case R.id.register_btn:
                intent = new Intent(UserLoginActivity.this, UserRegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.forget_txt:
                intent = new Intent(UserLoginActivity.this, ForgetPasActivity.class);
                intent.putExtra("tel", mEtTel.getText().toString());
                startActivity(intent);
                break;
            case R.id.aixiyi_xieyi_txt:
                intent = new Intent(UserLoginActivity.this, AiProtocolActivity.class);
                startActivity(intent);
                break;

        }
    }


    //点击完成之后选择登陆或者注册
    private void gotoLogin() {
        if (TextUtils.isEmpty(mEtTel.getText().toString())) {
            ToastUtil.ToastShow(this, getResources().getString(R.string.log_reg_tel));
            return;
        } else if (TextUtils.isEmpty(mEtPsw.getText().toString())) {
            ToastUtil.ToastShow(this, getResources().getString(R.string.log_reg_pass));
            return;
        } else {
            loginUpload();
        }
    }
    /**
     * 登录
     */
    private void loginUpload() {
        String params = getParams();
        Map map = new HashMap();
        map.put("data",params);
        MyOkHttpUtils.postRequest(Urls.USER_LOGIN, map).execute(new MapCallback(UserLoginActivity.this,Contanct.DIALOG_UPLOAD) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        sharedUserUtil = new SharedPrefUtil(getBaseContext(), Contanct.SP_USER);
                        sharedUserUtil.putString(Contanct.SP_USERNAME, mEtTel.getText().toString().trim());
                        sharedUserUtil.putString(Contanct.SP_USERPASS, mEtPsw.getText().toString().trim());
                        sharedUserUtil.commit();
                        intentToMainActivity();
                    } else{
                        ToastUtil.ToastShow(UserLoginActivity.this,getResources().getString(R.string.log_reg_pass_tf));
                    }
                }
            }
        });
    }
    private String getParams() {
        Map<String, Object> map = new HashMap<>();
        String tel = mEtTel.getText().toString().trim();
        String pas = mEtPsw.getText().toString().trim();
        map.put("amount", tel);
        map.put("password", pas);
        String data = ParamsUtil.getParamsFromMap(map);
        DebugLog.e("Response", "data=" + data);
        return data;
    }

    /**
     * 跳转到mainActivity
     */
    private void intentToMainActivity() {
        Intent intent = new Intent(UserLoginActivity.this, AiMainActivity.class);
        startActivity(intent);
        finish();
        DebugLog.e("Response", "333333333333");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }


}
