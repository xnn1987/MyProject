package com.cn.aixiyi.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.SetTitleBar;
import com.cn.aixiyi.utils.common.CommomAcitivity;

import java.util.HashMap;
import java.util.Map;

/**
 * @date 2016/11/1
 * @author xingdapeng
 * @description 广告说明
 */
public class AdsExplainActivity  extends CommomAcitivity {
    private TextView madsExplianTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ads_explain_lay);
        SetTitleBar.setTitleText(this,"广告收益说明");
        initView();
    //    getExplainData();
    }

    private void initView() {
        madsExplianTxt= (TextView) findViewById(R.id.ads_explain_txt);
    }

    /**
     * 获取钱包说明
     */
    private void getExplainData() {
        String params= getParams();
        //https请求
        MyYingShiVolleyUtils.getIntance(this, "").OkHttpPostStringTypeOne(Urls.ADSEXPLAIN_URLS,params, new MyXiaoYaCallBack(this) {
            @Override
            public void onResponse(Object response) {
                if (!TextUtils.isEmpty(response.toString())) {
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private String  getParams() {
        Map<String,Object> map=new HashMap<>();
        String data= ParamsUtil.getParamsFromMap(map);
        return  data;
    }

}
