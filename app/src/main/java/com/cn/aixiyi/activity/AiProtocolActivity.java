package com.cn.aixiyi.activity;

import android.os.Bundle;

import com.cn.aixiyi.R;
import com.cn.aixiyi.utils.SetTitleBar;
import com.cn.aixiyi.utils.common.SlidBackActivity;

/**
 * Created by Administrator on 2016/11/28.
 */
public class AiProtocolActivity extends SlidBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ai_xy_protocol_lay);
        SetTitleBar.setTitleText(AiProtocolActivity.this,"爱洗衣协议");
    }
}

