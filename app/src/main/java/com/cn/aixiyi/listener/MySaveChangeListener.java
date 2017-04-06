package com.cn.aixiyi.listener;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.cn.aixiyi.R;


/**
 * Created by sty on 2016/7/8.
 */
public class MySaveChangeListener implements ViewPager.OnPageChangeListener {
  private Context mContext;
    private View  mSecondLay, mNewLay;
   private TextView mSecondTxt,mNewTxt;

    public MySaveChangeListener(Context context, View  mSecondLay,View  mNewLay,TextView mSecondTxt, TextView mNewTxt) {
        this.mContext=context;
        this.mSecondTxt = mSecondTxt;
        this.mNewTxt = mNewTxt;
        this.mSecondLay = mSecondLay;
        this.mNewLay = mNewLay;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch(position){
            case 0:
                mSecondLay.setBackgroundResource(R.color.aixiyi_center_txt_blue);
                mSecondTxt.setTextColor(mContext.getResources().getColor(R.color.white));
                mNewLay.setBackgroundResource(R.color.aixiyi_main_backgroud);
                mNewTxt.setTextColor(mContext.getResources().getColor(R.color.aixiyi_center_txt_blue));
                break;
            case 1:
                mNewLay.setBackgroundResource(R.color.aixiyi_center_txt_blue);
                mNewTxt.setTextColor(mContext.getResources().getColor(R.color.white));
                mSecondLay.setBackgroundResource(R.color.aixiyi_main_backgroud);
                mSecondTxt.setTextColor(mContext.getResources().getColor(R.color.aixiyi_center_txt_blue));
                break;

        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

