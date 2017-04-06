package com.cn.aixiyi.Interface;

import android.app.Activity;

import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.network.AppUtil;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.ToastUtil;

/**
 * Created by xingdapeng on 2016/10/26.
 */
public class MyXiaoYaCallBack implements MyCallBack {
    private Activity activity;
    private String dialogContent;
    public MyXiaoYaCallBack(Activity activity) {
        super();
        this.activity = activity;
        this.dialogContent = Contanct.DIALOG_SHOW;
    }

    @Override
    public void onBefore() {
        AppUtil.showProgress(activity, dialogContent);
    }

    @Override
    public void onAfter() {
        AppUtil.dismissProgress();
    }

    @Override
    public void onResponse(Object response) {

    }

    @Override
    public void onError( Exception e) {
        DebugLog.Toast(activity,  "===" +  e.toString());
        ToastUtil.ToastShow(activity, Contanct.DIALOG_ERROR);
    }
}
