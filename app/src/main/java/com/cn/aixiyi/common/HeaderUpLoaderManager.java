package com.cn.aixiyi.common;

import android.app.Activity;
import android.content.Context;

import com.cn.aixiyi.Interface.MyCallBack;
import com.cn.aixiyi.Interface.MyXiaoYaCallBack;
import com.cn.aixiyi.common.manager.OKHttpClientManager;

import com.cn.aixiyi.mode.BusEvent;
import com.squareup.okhttp.Request;
import com.ypy.eventbus.EventBus;

import java.io.File;

/**
 * Created by sty on 2016/8/1.
 */
public class HeaderUpLoaderManager {
    private static final String TAG = "HeaderUpLoaderManager";
    private BusEvent pCenterEvent=new BusEvent();
    //私有化构造函数，防止被实例化
    private HeaderUpLoaderManager(){

    }
    private static class SingletonHodler {
        private static final HeaderUpLoaderManager INSTANCE = new HeaderUpLoaderManager();
    }

    public static final HeaderUpLoaderManager getInstance() {
        return SingletonHodler.INSTANCE;
    }
   //上传头像
    public  void setData(final Activity context,String fileStr){
        String uploadUrl="";//上传路径
        String param="upload header";//上传说明
        File file =new File(fileStr);
//        OKHttpClientManager.getInstance().OkHttpFilePost(uploadUrl, file, param, new MyXiaoYaCallBack(context) {
//            @Override
//            public void onResponse(String response) {
//
//                EventBus.getDefault().post(new BusEvent(BusEvent.UPLOAD_SUCCESS,response));
//            }
//
//
//        });

    }
    //下载头像

    public  void downLoadHeader(final Activity context,File file){
        String downLoadUrl="";//下载路径
//        OKHttpClientManager.getInstance().OkHttpDownLoadPost(downLoadUrl, file,  new MyXiaoYaCallBack(context) {
//            @Override
//            public void onResponse(String response) {
//                EventBus.getDefault().post(new BusEvent(BusEvent.DOWNLOAD_SUCCESS,response));
//            }
//
//        });

    }

}
