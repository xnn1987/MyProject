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
import com.cn.aixiyi.utils.common.SlidBackActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/7.
 */
public class MyQBExplainActivity extends SlidBackActivity {
  private TextView mQbExplianTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_qb_explain_lay);
        SetTitleBar.setTitleText(this,"钱包说明");
        initView();
    }

    private void initView() {
        mQbExplianTxt= (TextView) findViewById(R.id.qiangbao_explain_txt);
    }

}