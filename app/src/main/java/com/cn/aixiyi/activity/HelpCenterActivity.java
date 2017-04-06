package com.cn.aixiyi.activity;

import android.os.Bundle;

import com.cn.aixiyi.R;
import com.cn.aixiyi.utils.SetTitleBar;
import com.cn.aixiyi.utils.common.CommomAcitivity;
import com.cn.aixiyi.utils.common.SlidBackActivity;

/**
 * @date 2016/11/1
 * @author xingdapeng
 * @description 帮助中心
 */
public class HelpCenterActivity  extends SlidBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_center_lay);
        SetTitleBar.setTitleText(this,"帮助中心");
    }
}

