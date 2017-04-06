package com.cn.aixiyi.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.aixiyi.R;
import com.cn.aixiyi.fragment.MyXyAddressFragment;
import com.cn.aixiyi.fragment.MyXyMachineFragment;
import com.cn.aixiyi.listener.MySaveChangeListener;
import com.cn.aixiyi.myAdapter.MySaveVpAdapter;
import com.cn.aixiyi.utils.SetTitleBar;
import com.cn.aixiyi.utils.common.BaseFragmentActivity;
import com.cn.aixiyi.utils.common.SlidBackActivity;

import java.util.ArrayList;


/**
 * @date 2016/11/1
 * @author xingdapeng
 * @description 我的收藏
 */
public class MySaveActivity extends SlidBackActivity implements View.OnClickListener{
    private ViewPager mTViewPager;
    private TextView mSecondTv,mNewTv;
    private LinearLayout mLlSecond,mLlNew;
    private ArrayList<Fragment> fmList=new ArrayList<Fragment>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_save_lay);
        initView();
        //初始化ViewPager
        initViewPager();
        //设置View监听事件
        setListener();
    }
    private void initView() {
        mTViewPager = (ViewPager) findViewById(R.id.jsq_taxes_vp);
        mSecondTv= (TextView) findViewById(R.id.taxes_second_txt);
        mNewTv= (TextView) findViewById(R.id.taxes_new_txt);
        mLlSecond= (LinearLayout) findViewById(R.id.xiyi_dian_lay);
        mLlNew= (LinearLayout) findViewById(R.id.xiyi_ji_lay);
    }
    private void initViewPager() {
        MyXyAddressFragment myXyAddressFragment=new MyXyAddressFragment();
        MyXyMachineFragment myXyMachineFragment=new MyXyMachineFragment();
        fmList.add(myXyAddressFragment);
        fmList.add(myXyMachineFragment);
        mTViewPager.setAdapter(new MySaveVpAdapter(getSupportFragmentManager(),fmList));
    }

    private void setListener() {
        SetTitleBar.setTitleText(this,"我的收藏");
        mLlNew.setOnClickListener(this);
        mLlSecond.setOnClickListener(this);
        mTViewPager.setOnPageChangeListener(new MySaveChangeListener(this,mLlSecond,mLlNew,mSecondTv,mNewTv));

    }

    @Override
    public void onClick(View v) {
        int vID = v.getId();
        switch (vID) {
            case R.id.xiyi_dian_lay:
                setViewOnClickCheck(true, false);
                mTViewPager.setCurrentItem(0);
                break;
            case R.id.xiyi_ji_lay:
                setViewOnClickCheck(false, true);
                mTViewPager.setCurrentItem(1);
                break;
        }
    }


    private void setViewOnClickCheck(boolean left, boolean right) {
        if (left == true) {
            mLlSecond.setBackgroundResource(R.color.aixiyi_center_txt_blue);
            mSecondTv.setTextColor(getResources().getColor(R.color.white));

        } else {
            mLlSecond.setBackgroundResource(R.color.aixiyi_main_backgroud);
            mSecondTv.setTextColor(getResources().getColor(R.color.aixiyi_center_txt_blue));
        }
        if (right == true) {
            mLlNew.setBackgroundResource(R.color.aixiyi_center_txt_blue);
            mNewTv.setTextColor(getResources().getColor(R.color.white));

        } else {
            mLlNew.setBackgroundResource(R.color.aixiyi_main_backgroud);
            mNewTv.setTextColor(getResources().getColor(R.color.aixiyi_center_txt_blue));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
