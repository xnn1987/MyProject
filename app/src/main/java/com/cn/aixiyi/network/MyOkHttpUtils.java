package com.cn.aixiyi.network;

import android.app.Activity;
import android.widget.Toast;

import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.utils.DebugLog;
import com.cn.aixiyi.utils.TextUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.util.Map;


/**
 * Created by Administrator on 2016/5/13.
 */
public class MyOkHttpUtils {

    /**
     * 普通数据请求
     *
     * @param url
     * @return
     */
    public static RequestCall getRequest(String url) {
        RequestCall build = OkHttpUtils
                .get()
                .url(url)
                .addHeader("Cookie", MyApplication.cookies)
                .build().connTimeOut(20000);
        DebugLog.e( "response--> " ,  "Cookie"+MyApplication.cookies ) ;
        return build;
    }

    /**
     * 普通数据请求
     *
     * @param url
     * @param params
     * @return
     */
    public static RequestCall postRequest(String url, Map<String, String> params) {
        RequestCall build = OkHttpUtils
                .post()
                .url(url)
                .params(params)
                .addHeader("Cookie", MyApplication.cookies)
                .build().connTimeOut(20000);
        DebugLog.e( "response--> " ,  "Cookie"+MyApplication.cookies ) ;
        return build;
    }

    /**
     * 上传文件
     *
     * @param activity 吐司
     * @param url
     * @param fileUrl
     * @return
     */
    public static RequestCall postFile(Activity activity, String url,
                                       Map<String, String> params, String fileUrl) {
        File file = new File(fileUrl);
        if (!file.exists()) {
            Toast.makeText(activity, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return null;
        }
        RequestCall build = OkHttpUtils
                .post()
                .url(url)
                .params(params)
                .addFile("ftpfile", fileUrl, file)
                .addHeader("Cookie", MyApplication.cookies)
                .build().connTimeOut(20000).readTimeOut(20000).writeTimeOut(20000);
        return build;
    }
}
