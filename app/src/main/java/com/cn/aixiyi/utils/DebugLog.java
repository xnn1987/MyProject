package com.cn.aixiyi.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/4/26.
 */
public class DebugLog {
    private static boolean open = true;

    public static void e(String name,String log){
        if (open) {
            Log.e(name, log);
        }
    }
    public static void Toast(Activity activity,String content){
        Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
    }

}
