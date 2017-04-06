package com.cn.aixiyi.utils;

import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by sty on 2016/9/23.
 */
public class PictureUtil {


    /**
     *
     * @param picRes
     * @return
     */
    public static DisplayImageOptions setImageOptions(int picRes) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(picRes) //加载时显示的图片
                .showImageForEmptyUri(picRes)// 加载数据为空时显示的图片
                .showImageOnFail(picRes)// 加载失败时显示的图片
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        return options;
    }


    /**
     * 圆角的imageload缓存
     * @param mContext
     * @param picRes
     * @param count
     * @return
     */
    public static DisplayImageOptions setImageOptions(Context mContext, int picRes, int count) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(picRes) //加载时显示的图片
                .showImageForEmptyUri(picRes)// 加载数据为空时显示的图片
                .showImageOnFail(picRes)// 加载失败时显示的图片
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(DensityUtil.px2dip(mContext, count)))
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        return options;
    }

}
