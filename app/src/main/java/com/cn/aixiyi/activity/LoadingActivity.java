package com.cn.aixiyi.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.network.AppUtil;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.MathCommon;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.SharedPrefUtil;
import com.cn.aixiyi.utils.ShowDialogUtil;
import com.cn.aixiyi.utils.TextUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.CommomAcitivity;
import com.cn.aixiyi.utils.permissionshelper.PermissionsHelper;
import com.cn.aixiyi.utils.permissionshelper.permission.DangerousPermissions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.ddq.common.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


/**
 * @author xingdapeng
 * @date 2016/11/1
 * @description 引导页
 */
public class LoadingActivity extends CommomAcitivity {
    private ImageView mLoadImg;
    private Handler handler = new Handler();
    private SharedPrefUtil sharedUserUtil;
    private String userName = "";
    private String passWord = "";
    private  static final String[] PERMISSIONS = new String[]{
            DangerousPermissions.READ_PHONE_STATE,
            DangerousPermissions.CALL_PHONE,
            DangerousPermissions.CAMERA,
            DangerousPermissions.ACCESS_FINE_LOCATION,
            DangerousPermissions.ACCESS_COARSE_LOCATION,
            DangerousPermissions.SEND_SMS,
            DangerousPermissions.READ_EXTERNAL_STORAGE,
            DangerousPermissions.WRITE_EXTERNAL_STORAGE,
            DangerousPermissions.MOUNT_UNMOUNT_FILESYSTEMS,
            DangerousPermissions.WRITE_SETTINGS
    };
    private PermissionsHelper permissionsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_lay);
       // checkPermissions();
        handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getUserSp();
                    }
                },100);
    }

    private void getUserSp() {
        sharedUserUtil = new SharedPrefUtil(getBaseContext(), Contanct.SP_USER);
        userName = sharedUserUtil.getString(Contanct.SP_USERNAME, "");
        passWord = sharedUserUtil.getString(Contanct.SP_USERPASS, "");
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(passWord)) {
            loginUpload();
        } else {
            Intent intent = new Intent(LoadingActivity.this, UserLoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    /**
     * 登录
     */
    private void loginUpload() {
        String params = getParams();
        Map map = new HashMap();
        map.put("data",params);
        MyOkHttpUtils.postRequest(Urls.USER_LOGIN, map).execute(new MapCallback(LoadingActivity.this,false) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        intentToMainActivity();
                    } else {
                        ToastUtil.ToastShow(LoadingActivity.this,getResources().getString(R.string.log_reg_pass_tf));
                    }
                }
            }

            @Override
            public void onError(Call call, Exception exception) {
                super.onError(call, exception);
                Intent intent = new Intent(LoadingActivity.this, UserLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private String getParams() {
        Map<String, Object> map = new HashMap<>();
        map.put("amount", userName);
        map.put("password", passWord);
        String data = ParamsUtil.getParamsFromMap(map);
        DebugLog.e("Response", "data=" + data);
        return data;
    }
    /**
     * 跳转到mainActivity
     */
    private void intentToMainActivity() {
        Intent intent = new Intent(LoadingActivity.this, AiMainActivity.class);
        startActivity(intent);
        finish();
        DebugLog.e("Response", "333333333333");
    }
    //23版本以上检查权限
    private void checkPermissions() {
        permissionsHelper = new PermissionsHelper(this,PERMISSIONS);
        if (permissionsHelper.checkAllPermissions(PERMISSIONS)){
            permissionsHelper.onDestroy();
            //do nomarl
        }else {
            //申请权限
            permissionsHelper.startRequestNeedPermissions();
        }
        permissionsHelper.setonAllNeedPermissionsGrantedListener(new PermissionsHelper.onAllNeedPermissionsGrantedListener() {


            @Override
            public void onAllNeedPermissionsGranted() {
                Log.d("test","onAllNeedPermissionsGranted");
            }

            @Override
            public void onPermissionsDenied() {
                Log.d("test","onPermissionsDenied");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissionsHelper.onActivityResult(requestCode, resultCode, data);
    }

}
