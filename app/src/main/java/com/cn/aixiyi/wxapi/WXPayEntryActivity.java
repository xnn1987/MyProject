package com.cn.aixiyi.wxapi;

import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.ParamsUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = MyApplication.api;
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
					switch (resp.errCode) {
				case BaseResp.ErrCode.ERR_OK:
					rechargeData();
					break;
			}
		}
	}
	/**
	 * 从后台获取支付结果
	 */
	private void rechargeData() {
		String params= getParams();
		//https请求
		MyYingShiVolleyUtils.getIntance(this, "").OkHttpPostStringTypeOne(Urls.PAYRESULT_URLS,params, new MyXiaoYaCallBack(this) {
			@Override
			public void onResponse(Object response) {
				if (!TextUtils.isEmpty(response.toString())) {
					DebugLog.e("Response", "MainActivity=" + response);
					AlertDialog.Builder builder = new AlertDialog.Builder(WXPayEntryActivity.this);
					builder.setTitle(R.string.app_tip);
					builder.setMessage("支付成功");
					builder.show();
					finish();
				}
			}

			@Override
			public void onError(Exception e) {
				AlertDialog.Builder builder = new AlertDialog.Builder(WXPayEntryActivity.this);
				builder.setTitle(R.string.app_tip);
				builder.setMessage("支付失败");
				builder.show();
				finish();
			}
		});
	}


	private String  getParams() {
		Map<String,Object> map=new HashMap<>();
//        String tel=mEtTel.getText().toString().trim();
//        String pas=mEtPsw.getText().toString().trim();
//        map.put("tel",tel);
//        map.put("pass",pas);
		String data= ParamsUtil.getParamsFromMap(map);
		return  data;
	}
}