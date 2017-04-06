package com.cn.aixiyi.utils;

import android.app.Activity;
import android.widget.Toast;

import com.cn.aixiyi.activity.LoadingActivity;
import com.google.gson.Gson;
import com.tec.key.Ptlmaner;



import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/11/8.
 * 传入的参数转化为加密串工具类
 */
public class ParamsUtil {
    private static String data = "";
    private static String key = "";
    private static Gson gson=new Gson();
    private static  String jsonString="";
    /*
* map转化为加密串
* */
    public static String getParamsFromMap(Activity activity,Map<String, Object> map) {

        if (map == null) {
            ToastUtil.ToastShow(activity,"Map不存在");
            Toast.makeText(activity,"Map不存在",Toast.LENGTH_LONG).show();
            return null;
        }
        ToastUtil.ToastShow(activity,"封装成json");
        jsonString=gson.toJson(map);
        ToastUtil.ToastShow(activity,"json="+jsonString);
        DebugLog.e("Response", "jsonString=" + jsonString);
        try {
            ToastUtil.ToastShow(activity,"开始加密");
            Ptlmaner.requestProcess();
            key = new String(Ptlmaner.tcb.getBytes(), "UTF-8");
            ToastUtil.ToastShow(activity,"加密结束==");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            ToastUtil.ToastShow(activity,"加密失败==");
        }
        data = XxteaUtil.eryt(key, jsonString);
        ToastUtil.ToastShow(activity,"加密结果=="+data);
        return data;

    }

   /*
   * map转化为加密串
   * */
    public static String getParamsFromMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
         jsonString=gson.toJson(map);
        DebugLog.e("Response", "jsonString=" + jsonString);
        try {
            Ptlmaner.requestProcess();
            key = new String(Ptlmaner.tcb.getBytes(), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        data = XxteaUtil.eryt(key, jsonString);
        return data;

    }
    /*
    * list转化为加密串
    * */
    public static String getParamsFromArray(List<Map<String, Object>> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
         jsonString=gson.toJson(list);
        try {
            Ptlmaner.requestProcess();
            key = new String(Ptlmaner.tcb.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        data = XxteaUtil.eryt(key, jsonString);
        return data;

    }
    /*
       * String 转化为加密串
       * */
    public static String getParamsFromString(String str) {
        if (str.isEmpty()) {
            return null;
        }
        try {
            Ptlmaner.requestProcess();
            key = new String(Ptlmaner.tcb.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        data = XxteaUtil.eryt(key, str);
        return data;

    }
}
