package com.cn.aixiyi.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by sty on 2016/8/1.
 */
public class ToastUtil {
    public static void ToastShow(Context context, String toast) {
        Toast.makeText(context,toast,Toast.LENGTH_SHORT).show();
    }
}
