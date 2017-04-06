package com.cn.aixiyi.listener;

import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;



/**
 * Created by sty on 2016/7/4.
 */
public class MyCheckChangeListener  implements RadioGroup.OnCheckedChangeListener{

    private ViewPager viewPager=null;

    public MyCheckChangeListener(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
//            case R.id.jsq_house_busy_rg:
//                //ViewPager显示第一个Fragment且关闭页面切换动画效果
//                viewPager.setCurrentItem(0,false);
//                break;
//            case R.id.jsq_house_gjj_rg:
//                viewPager.setCurrentItem(1,false);
//                break;
//            case R.id.jsq_house_zh_rg:
//                viewPager.setCurrentItem(2,false);
//                break;
        }

    }
}
