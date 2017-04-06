package com.cn.aixiyi.Interface;

import android.app.Activity;
import android.text.TextUtils;

import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.network.AppUtil;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.ShowDialogUtil;


/**
 * Created by Administrator on 2016/5/13.
 */
//public abstract class MapCallback extends Callback<String> {
//    private Activity activity;
//    private String dialogContent;
//    private boolean isDialog = true;
//    private String errorMsg = Contanct.DIALOG_ERROR;
//    private String errorCode ;
//
//    @Override
//    public void onResponse(String string) {
//        if (string == null ) {
//            AppUtil.dismissProgress();
//            ShowDialogUtil showDialogUtil = new ShowDialogUtil(activity);
//            showDialogUtil.startWaitingDialog(activity,errorMsg);
//        }
//    }
//
//    @Override
//    public String parseNetworkResponse(Response response) throws Exception {
//        String string = response.body().string();
//         DebugLog.e("Response", string) ;
//        return string;
//    }
//
//    /**
//     * 获取Cookie
//     * @param response
//     */
//    private void getCookie(Response response){
//        String rawCookies =  response.headers().get("Set-Cookie");
//        if(!TextUtils.isEmpty(rawCookies)){
//            MyApplication.cookies = rawCookies.substring(0, rawCookies.indexOf(";"));
//        }
//    }
//
//    public MapCallback(Activity activity) {
//        super();
//        this.activity = activity;
//        this.dialogContent = Contanct.DIALOG_SHOW;
//    }
//
//    /**
//     * @param activity
//     * @param isDialog 默认要，
//     */
//    public MapCallback(Activity activity, boolean isDialog) {
//        super();
//        this.activity = activity;
//        this.isDialog = isDialog;
//        this.dialogContent = Contanct.DIALOG_SHOW;
//    }
//
//    public MapCallback(Activity activity, String dialogContent) {
//        super();
//        this.activity = activity;
//        this.dialogContent = dialogContent;
//    }
//
//    @Override
//    public void onBefore(Request request) {
//        super.onBefore(request);
//        if (isDialog)
//            AppUtil.showProgress(activity, dialogContent);
//    }
//
//    @Override
//    public void onAfter() {
//        super.onAfter();
//        AppUtil.dismissProgress();
//    }
//
//    @Override
//    public void onError(Call call, Exception exception) {
//        DebugLog.Toast(activity, errorMsg+"==="+exception.toString());
//    }
//
//    @Override
//    public void inProgress(float progress) {
//        super.inProgress(progress);
//    }
//
//    public void setDialogContent(String dialogContent) {
//        this.dialogContent = dialogContent;
//    }
//
//    public String getErrorMsg() {
//        return errorMsg;
//    }
//
//    public void setErrorMsg(String errorMsg) {
//        this.errorMsg = errorMsg;
//    }
//
//    public String getErrorCode() {
//        return errorCode;
//    }
//}
