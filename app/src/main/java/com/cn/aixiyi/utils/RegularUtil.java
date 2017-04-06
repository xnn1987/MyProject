package com.cn.aixiyi.utils;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by sty on 2016/8/1.
 */
public class RegularUtil {
    //手机号码
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    //正数
    public static boolean ZZSFilter(String str) throws PatternSyntaxException {
        Log.i("ZZSFilter", isMatch("^\\+{0,1}[1-9]\\d*", str) + "=========" + isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", str));
        return isMatch("^\\+{0,1}[1-9]\\d*", str) || isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", str);
    }

    private static boolean isMatch(String regex, String orginal) {
        if (orginal == null || orginal.trim().equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(orginal);
        return isNum.matches();
    }

    public static boolean checkPassAndRepass(String pass, String repass) {
        if (pass.isEmpty() || repass.isEmpty())
            return false;
        if (pass.equals(repass))
            return true;
        return false;
    }
}
