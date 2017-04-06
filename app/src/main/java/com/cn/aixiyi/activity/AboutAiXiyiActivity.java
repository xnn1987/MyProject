package com.cn.aixiyi.activity;

import android.os.Bundle;
import android.util.Log;

import com.cn.aixiyi.R;
import com.cn.aixiyi.utils.SetTitleBar;
import com.cn.aixiyi.utils.common.CommomAcitivity;
import com.cn.aixiyi.utils.common.SlidBackActivity;

/**
 * @date 2016/11/1
 * @author xingdapeng
 * @description 关于爱洗衣
 */
public class AboutAiXiyiActivity extends SlidBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_aixiyi_lay);
        SetTitleBar.setTitleText(AboutAiXiyiActivity.this,"关于爱洗衣");
    }
}

