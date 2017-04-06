package com.cn.aixiyi.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.SetTitleBar;
import com.cn.aixiyi.utils.SharedPrefUtil;
import com.cn.aixiyi.utils.TextUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.BaseFragmentActivity;
import com.cn.aixiyi.utils.common.SlidBackActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import alipay.PayResult;

/**
 * @date 2016/11/1
 * @author xingdapeng
 * @description 钱包充值
 */
public class MyQBRechargeActivity  extends SlidBackActivity {
    private View  oneView;
    private View  twoView;
    private View  threeView;
    private View  fourView;
    private View  fiveView;
    private View  sixView;

    private TextView oneTxt;
    private TextView twoTxt;
    private TextView threeTxt;
    private TextView fourTxt;
    private TextView fiveTxt;
    private TextView sixTxt;

    private RadioButton weixinBtn;
    private  RadioButton zhifubaoBtn;

    private Button ensureBtn;

    private int payNum=0;
    private String warterNumStr="sno123912318231283123";
    private  int isWeixinOrZhifubao=0;
    private IWXAPI api;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        rechargeResultData();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(MyQBRechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                default:
                    break;
            }
        };
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");
        setContentView(R.layout.my_qianbao_recharge_lay);
        SetTitleBar.setTitleText(this,"充值");
        initView();
        setListener();
    }


    private void initView() {
        oneView=findViewById(R.id.one_lay);
        twoView=findViewById(R.id.two_lay);
        threeView=findViewById(R.id.three_lay);
        fourView=findViewById(R.id.four_lay);
        fiveView=findViewById(R.id.five_lay);
        sixView=findViewById(R.id.six_lay);
        oneTxt= (TextView) findViewById(R.id.one_txt);
        twoTxt= (TextView) findViewById(R.id.two_txt);
        threeTxt= (TextView) findViewById(R.id.three_txt);
        fourTxt= (TextView) findViewById(R.id.four_txt);
        fiveTxt= (TextView) findViewById(R.id.five_txt);
        sixTxt= (TextView) findViewById(R.id.six_txt);
        weixinBtn= (RadioButton) findViewById(R.id.weixin_rtn);
        zhifubaoBtn= (RadioButton) findViewById(R.id.zhifubao_rtn);
        ensureBtn= (Button) findViewById(R.id.gz_result_btn);
        weixinBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    isWeixinOrZhifubao=0;//0代表微信
                    zhifubaoBtn.setChecked(false);
                }
            }
        });

        zhifubaoBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                 if (compoundButton.isChecked()){
                     isWeixinOrZhifubao=1;//1代表支付宝
                     weixinBtn.setChecked(false);
                 }

            }
        });
    }


    private void setListener() {
        oneView.setOnClickListener(this);
        twoView.setOnClickListener(this);
        threeView.setOnClickListener(this);
        fourView.setOnClickListener(this);
        fiveView.setOnClickListener(this);
        sixView.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);


    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.one_lay:
                payNum=20;
                break;
            case R.id.two_lay:
                payNum=50;
                break;
            case R.id.three_lay:
                payNum=100;
                break;
            case R.id.four_lay:
                payNum=200;
                break;
            case R.id.five_lay:
                payNum=300;
                break;
            case R.id.six_lay:
                payNum=500;
                break;
            case R.id.gz_result_btn:
                  rechargeData();
                break;
        }
    }

    /**
     * 从后台获取支付信息
     */
    private void rechargeData() {
        String params = getParams();
        Map map = new HashMap();
        map.put("data",params);
        MyOkHttpUtils.postRequest(Urls.RECHARE_URLS, map).execute(new MapCallback(MyQBRechargeActivity.this) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        float totalPayNum=MyApplication.tianCaiMoneyF+payNum;
                        MyApplication.tianCaiMoneyF= totalPayNum;
                        MyApplication.tianCaiMoney= TextUtil.getTextToString(totalPayNum);
                    }else{
                        ToastUtil.ToastShow(getBaseContext(), "充值失败！");
                    }
                }
            }
        });
    }

    private String  getParams() {
        Map<String,Object> map=new HashMap<>();
        map.put("a",isWeixinOrZhifubao+"");
        map.put("b",payNum+"");
        map.put("c",warterNumStr);
        String data= ParamsUtil.getParamsFromMap(map);
        return  data;
    }

//    String productInfo=response.toString();
//    payTaiCaiMoneyData(productInfo);
    private void payTaiCaiMoneyData(String productInfo) {
        if (isWeixinOrZhifubao==0){
            initWeiXinZhifu(productInfo);
        }else if(isWeixinOrZhifubao==1){
            initZhifubaoZhifu(productInfo);
        }
    }
   //调微信支付
    private void initWeiXinZhifu(String productInfo) {
						if(null != productInfo && !productInfo.isEmpty() ){
							PayReq req = new PayReq();
							//req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
//							req.appId			= productInfo.getString("appid");
//							req.partnerId		= productInfo.getString("partnerid");
//							req.prepayId		= productInfo.getString("prepayid");
//							req.nonceStr		= productInfo.getString("noncestr");
//							req.timeStamp		= productInfo.getString("timestamp");
//							req.packageValue	= productInfo.getString("package");
//							req.sign			= productInfo.getString("sign");
							req.extData			= "app data"; // optional
							Toast.makeText(MyQBRechargeActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
							// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
							api.sendReq(req);
						}else{
							Toast.makeText(MyQBRechargeActivity.this, "返回错误", Toast.LENGTH_SHORT).show();
						}

    }
    //调支付宝
    private void initZhifubaoZhifu(final  String productInfo) {
        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(MyQBRechargeActivity.this);
                Map<String, String> result = alipay.payV2(productInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 从后台获取支付结果
     */
    private void rechargeResultData() {
        String params = getParams();
        Map map = new HashMap();
        map.put("data",params);
        MyOkHttpUtils.postRequest(Urls.PAYRESULT_URLS, map).execute(new MapCallback(MyQBRechargeActivity.this) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        finish();
                        Toast.makeText(MyQBRechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    }else{
                        ToastUtil.ToastShow(getBaseContext(), "支付失败！");
                    }
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
