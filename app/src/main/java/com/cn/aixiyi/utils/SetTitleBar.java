package com.cn.aixiyi.utils;

import android.app.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.aixiyi.R;
import com.cn.aixiyi.app.MyApplication;


/**
 * Created by Administrator on 2016/1/6.
 */

public class SetTitleBar {

    /**
     * 标题只有2个控件， 一个返回一个标题文本，  但是不对返回按钮设置监听
     * @param activity
     * @param title
     */
    public static void setTitleTextNoListener(final Activity activity, String title) {
        putTitleName(activity, title);
        LinearLayout mLlBack = (LinearLayout) activity.findViewById(R.id.titleBar_ll_back);
        TextView mTvTitle = (TextView) activity.findViewById(R.id.titleBar_tv_title);
        mTvTitle.setText(title);
    }

    /**
     * 标题 只有两个控件   一个返回一个标题文本，  对返回设置了监听
     * @param activity
     * @param title
     */
    public static void setTitleText(final Activity activity, String title) {
        putTitleName(activity, title);
        LinearLayout mLlBack = (LinearLayout) activity.findViewById(R.id.titleBar_ll_back);
        TextView mTvTitle = (TextView) activity.findViewById(R.id.titleBar_tv_title);
        mTvTitle.setText(title);
        mLlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }


    /**
     * 三个标题   ---->>  局限 跳转的东西
     *
     * @param activity
     */
    public static <T> void setTitleText(final Activity activity, String title, String rightTitle,
                                        final Class<T> tClass, final String[] params, final String[] paramsValue) {
        putTitleName(activity,title);
        LinearLayout mLlBack = (LinearLayout) activity.findViewById(R.id.ThreeTitleBar_ll_back);
        LinearLayout mLlRightTitle = (LinearLayout) activity.findViewById(R.id.ThreeTitleBar_ll_click);
        TextView mTvTitle = (TextView) activity.findViewById(R.id.titleBar_tv_title);
        TextView mTvRight = (TextView) activity.findViewById(R.id.ThreeTitleBar_tv_click);
        mTvTitle.setText(title);
        mTvRight.setText(rightTitle);
        mLlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        mLlRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getApplicationContext(), tClass);
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        intent.putExtra(params[i], paramsValue[i]);
                    }
                }
                activity.startActivity(intent);
            }
        });

    }

    /**
     * 手动扩展，    需要监听的 右边的小标题
     *
     * @param activity
     * @param title
     * @param rightTitle
     */
    public static void setTitleText(final Activity activity, String title, String rightTitle) {
        putTitleName(activity,title);
        LinearLayout mLlBack = (LinearLayout) activity.findViewById(R.id.ThreeTitleBar_ll_back);
        LinearLayout mLLRight = (LinearLayout) activity.findViewById(R.id.ThreeTitleBar_ll_click);
        TextView mTvTitle = (TextView) activity.findViewById(R.id.titleBar_tv_title);
        TextView mTvRight = (TextView) activity.findViewById(R.id.ThreeTitleBar_tv_click);
        mTvTitle.setText(title);
        mTvRight.setText(rightTitle);
        mLlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

    }


    /**
     *  标题三个控件， 右边是小图标，点击可以跳转到相对应的activity
     * @param activity
     * @param title
     * @param mipmapImage  图片
     * @param tClass       即将跳转的activity
     * @param params        intent.put（params,paramsValue）
     * @param paramsValue
     * @param <T>
     */
    public static <T> void setThreeTitleHasIcon(final Activity activity, String title, int mipmapImage, final Class<T> tClass, final String[] params, final String[] paramsValue) {
        putTitleName(activity,title);
        LinearLayout mLlBack = (LinearLayout) activity.findViewById(R.id.ThreeTitle2Icon_ll_back);
        LinearLayout mLlRightIcon = (LinearLayout) activity.findViewById(R.id.ThreeTitle2Icon_ll_Icon);
        TextView mTvTitle = (TextView) activity.findViewById(R.id.titleBar_tv_title);
        ImageView mIvRight = (ImageView) activity.findViewById(R.id.ThreeTitle2Icon_iv_icon);
        mIvRight.setImageResource(mipmapImage);
        mTvTitle.setText(title);
        mLlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        mLlRightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getApplicationContext(), tClass);
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        intent.putExtra(params[i], paramsValue[i]);
                    }
                }
                activity.startActivity(intent);
            }
        });
    }

    /**
     * 标题三个控件， 右边是小图标，  右边没有设置点击事件
     * @param activity
     * @param title
     * @param mipmapImage       图片
     * @param <T>
     */
    public static <T> void setThreeTitleHasIcon(final Activity activity, String title, int mipmapImage) {
        putTitleName(activity,title);
        LinearLayout mLlBack = (LinearLayout) activity.findViewById(R.id.ThreeTitle2Icon_ll_back);
        LinearLayout mLlRightIcon = (LinearLayout) activity.findViewById(R.id.ThreeTitle2Icon_ll_Icon);
        ImageView mIvRight = (ImageView) activity.findViewById(R.id.ThreeTitle2Icon_iv_icon);
        TextView mTvTitle = (TextView) activity.findViewById(R.id.titleBar_tv_title);
        mIvRight.setImageResource(mipmapImage);
        mTvTitle.setText(title);
        mLlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    /**
     * 对应的  类名存放，用于统计
     *
     * @param activity
     * @param title
     */
    private static void putTitleName(Activity activity, String title) {
        ComponentName componentName = activity.getComponentName();
        MyApplication.activityTitleMap.put(componentName.getShortClassName(), title);
    }


}
