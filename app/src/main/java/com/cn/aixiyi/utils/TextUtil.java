package com.cn.aixiyi.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/4/14.
 */
public class TextUtil {

    public static SpannableStringBuilder changColor(String text,int color,int start,int end){
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        ForegroundColorSpan blackSpan = new ForegroundColorSpan(color);
        builder.setSpan(blackSpan,start,5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    //  这样可以测量宽度
    private void measureView(View view){
        int width = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int heigh = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        view.measure(width,heigh);
    }

    /**
     *  防止报空
     * @param object
     * @return
     */
    public static String getTextToString(Object object){
        return object==null?"":object.toString().trim();
    }

    /**
     * 取int类型的东西
     * @param object
     * @param defaultInt
     * @return
     */
    public static Integer getInt(Object object,int defaultInt){
        Integer defaultValue  = null;
        try {
            defaultValue = object==null?defaultInt:Integer.parseInt(object.toString());
        }catch (Exception e){
            defaultValue = defaultInt;
        }
        return defaultValue;
    }

    /**
     * 取string类型，并且赋予默认值
     * @param object
     * @param defaultStr
     * @return
     */
    public static String getString(Object object,String defaultStr){
        return object==null?defaultStr:object.toString();
    }

    /**
     * 获取 boolean值类型 ，
     * @param object
     * @param defaultBoolean
     * @return
     */
    public static boolean getBoolean(Object object,boolean defaultBoolean){
        return object==null?defaultBoolean:Boolean.parseBoolean(object.toString());
    }

    /**
     * tv 部分字体颜色改变
     * @param tv
     * @param color
     */
    public static void SetTextviewParkColor(TextView tv , int color,int star,int end){
        SpannableStringBuilder builder = new SpannableStringBuilder(tv.getText().toString());
        ForegroundColorSpan redSpan = new ForegroundColorSpan(color);
        builder.setSpan(redSpan, star, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(builder);
    }

    /**
     * tv 部分字体颜色改变
     * @param tv
     */
    public static void SetTextviewParkColor(TextView tv , int color1,int color2,int star1,int end1,int star2,int end2){
        SpannableStringBuilder builder = new SpannableStringBuilder(tv.getText().toString());
        ForegroundColorSpan redSpan1 = new ForegroundColorSpan(color1);
        ForegroundColorSpan redSpan2 = new ForegroundColorSpan(color2);
        builder.setSpan(redSpan1, star1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(redSpan2, star2, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(builder);
    }


}
