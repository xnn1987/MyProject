package com.cn.aixiyi.utils;

import android.util.Base64;

import com.tec.key.Ptlmaner;
import com.tec.key.Xxtea;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/11/5.
 */
public class XxteaUtil {

    private static String xxTea = "";

    /**
     * 加密
     *
     * @param content
     */

    public static String eryt(String key, String content) {
        if (content != null) {
            if (key == null) {//第一次会调用
                try {
                    Ptlmaner.requestProcess();
                    xxTea = new String(Ptlmaner.tcb.getBytes(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }else{
                xxTea=key;
            }
            if (xxTea != null) {
                byte[] xxtea = Xxtea.eryt(content.getBytes(), xxTea.getBytes());
                byte[] base64 = Base64.encode(xxtea, 0);
                return new String(base64);
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * 解密
     *
     * @param content
     * @return 如果返回的是null 表示失败
     */
    public static String dryt(String key, String content) {
        if (key == null) {//第一次会调用
            try {
                Ptlmaner.requestProcess();
                xxTea = new String(Ptlmaner.tcb.getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }else{
            xxTea=key;
        }
        if (content != null && xxTea != null) {
            byte[] arr = Base64.decode(content.getBytes(), 0);
            byte[] resArr = Xxtea.dryt(arr, xxTea.getBytes());
            return new String(resArr);
        } else {
            return null;
        }
    }

}
