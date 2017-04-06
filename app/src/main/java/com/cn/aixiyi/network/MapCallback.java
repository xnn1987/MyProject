package com.cn.aixiyi.network;

import android.app.Activity;
import android.text.TextUtils;

import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.SharedPrefUtil;
import com.cn.aixiyi.utils.ShowDialogUtil;
import com.cn.aixiyi.utils.TextUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.zhy.http.okhttp.callback.Callback;

import org.ddq.common.util.JsonUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Administrator on 2016/5/13.
 */
public abstract class MapCallback extends Callback<Map> {
    private Activity activity;
    private String dialogContent;
    private boolean isDialog = true;
    private String errorMsg = Contanct.DIALOG_ERROR;
    private String errorCode ;

    @Override
    public void onResponse(Map rMap) {
        if (rMap == null && !errorMsg.equals(Contanct.DIALOG_ERROR)) {
            AppUtil.dismissProgress();
            ShowDialogUtil showDialogUtil = new ShowDialogUtil(activity);
            showDialogUtil.startWaitingDialog(activity,errorMsg);
        }
    }

    @Override
    public Map parseNetworkResponse(Response response) throws Exception {
        getCookie(response);
        String string = response.body().string();
        Map map = JsonUtil.getInstance().json2Object(string, Map.class);
        return map;
    }

    /**
     * 获取Cookie
     * @param response
     */
    private void getCookie(Response response){
        String rawCookies =  response.headers().get("Set-Cookie");
        if(!TextUtils.isEmpty(rawCookies)){
            MyApplication.cookies = rawCookies.substring(0, rawCookies.indexOf(";"));
        }
    }

    public MapCallback(Activity activity) {
        super();
        this.activity = activity;
        this.dialogContent = Contanct.DIALOG_SHOW;
    }

    /**
     * @param activity
     * @param isDialog 默认要，
     */
    public MapCallback(Activity activity, boolean isDialog) {
        super();
        this.activity = activity;
        this.isDialog = isDialog;
        this.dialogContent = Contanct.DIALOG_SHOW;
    }

    public MapCallback(Activity activity, String dialogContent) {
        super();
        this.activity = activity;
        this.dialogContent = dialogContent;
    }

    @Override
    public void onBefore(Request request) {
        super.onBefore(request);
        if (isDialog)
            AppUtil.showProgress(activity, dialogContent);
    }

    @Override
    public void onAfter() {
        super.onAfter();
        AppUtil.dismissProgress();
    }

    @Override
    public void onError(Call call, Exception exception) {
        DebugLog.Toast(activity, "网络请求失败!");
    }

    @Override
    public void inProgress(float progress) {
        super.inProgress(progress);
    }

    public void setDialogContent(String dialogContent) {
        this.dialogContent = dialogContent;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
