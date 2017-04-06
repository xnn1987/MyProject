package com.cn.aixiyi.myAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by sty on 2016/7/8.
 */
public class MySaveVpAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> datas;

    public MySaveVpAdapter(FragmentManager fm, ArrayList<Fragment> datas) {
        super(fm);
        this.datas = datas;
    }
    @Override
    public Fragment getItem(int position) {
        return datas.get(position);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

}
