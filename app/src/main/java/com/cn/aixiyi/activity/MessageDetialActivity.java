package com.cn.aixiyi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.R;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.network.MapCallback;
import com.cn.aixiyi.network.MyOkHttpUtils;
import com.cn.aixiyi.network.MyYingShiVolleyUtils;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.MathCommon;
import com.cn.aixiyi.utils.ParamsUtil;
import com.cn.aixiyi.utils.SetTitleBar;
import com.cn.aixiyi.utils.TextUtil;
import com.cn.aixiyi.utils.ToastUtil;
import com.cn.aixiyi.utils.common.CommomAcitivity;
import com.cn.aixiyi.utils.common.SlidBackActivity;

import org.ddq.common.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @date 2016/11/1
 * @author xingdapeng
 * @description 消息详情
 */
public class MessageDetialActivity extends SlidBackActivity {
    private Intent intent;
     private TextView msgTitleTxt;
    private TextView msgTimeTxt;
    private TextView msgContentTxt;
    private  String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_detail_lay);
        SetTitleBar.setTitleText(this,"消息详情");
        intent=getIntent();
        id=intent.getStringExtra("id");
        initView();
        getExplainData();
    }
    private void initView() {
        msgTitleTxt= (TextView) findViewById(R.id.message_title);
        msgTimeTxt= (TextView) findViewById(R.id.message_time);
        msgContentTxt= (TextView) findViewById(R.id.message_content);
    }

    /**
     * 获取钱包说明
     */

    private void getExplainData() {
        String  messageDetialUrls=Urls.MESSAGEDETAIL_URLS+"?id="+id;
        DebugLog.e("Response", "messageDetialUrls=" + messageDetialUrls);
        MyOkHttpUtils.getRequest(messageDetialUrls).execute(new MapCallback(MessageDetialActivity.this) {
            @Override
            public void onResponse(Map rMap) {
                super.onResponse(rMap);
                if (rMap != null) {
                    int rcode = (int) rMap.get("rcode");
                    if (rcode == 0) {
                        Map<String, Object> data = (Map<String, Object>) rMap.get("data");
                        msgTitleTxt.setText(TextUtil.getTextToString(data.get("title")));
                        msgTimeTxt.setText(TextUtil.getTextToString(data.get("createTime")));
                        msgContentTxt.setText(TextUtil.getTextToString(data.get("content")));
                    }
                }
            }
        });
    }
}

