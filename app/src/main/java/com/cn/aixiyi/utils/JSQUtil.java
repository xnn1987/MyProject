package com.cn.aixiyi.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by sty on 2016/7/13.
 */
public class JSQUtil {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void ToastShow(Context context, String toast) {
        Toast.makeText(context,toast,Toast.LENGTH_LONG).show();
    }
    //正数
    public static boolean ZZSFilter(String str)throws PatternSyntaxException {
        Log.i("ZZSFilter",isMatch("^\\+{0,1}[1-9]\\d*", str)+"========="+isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", str));
        return isMatch("^\\+{0,1}[1-9]\\d*", str)||isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", str);
    }
    private static boolean isMatch(String regex, String orginal){
        if (orginal == null || orginal.trim().equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(orginal);
        return isNum.matches();
    }
}
