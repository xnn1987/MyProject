package com.cn.aixiyi.listener;

import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.cn.aixiyi.R;


/**
 * Created by sty on 2016/7/4.
 */
public class MyPageChangeListener implements ViewPager.OnPageChangeListener {
    private RadioGroup mRadioGroup;

    public MyPageChangeListener(RadioGroup mRadioGroup) {
        this.mRadioGroup = mRadioGroup;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch(position){
//            case 0:
//                mRadioGroup.check(R.id.jsq_house_busy_rg);
//                break;
//            case 1:
//                mRadioGroup.check(R.id.jsq_house_gjj_rg);
//                break;
//            case 2:
//                mRadioGroup.check(R.id.jsq_house_zh_rg);
//                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
