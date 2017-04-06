package com.cn.aixiyi.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.cn.aixiyi.activity.LoadingActivity;
import com.cn.aixiyi.activity.UserLoginActivity;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.mode.Contanct;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.ddq.common.util.JsonUtil;

/**
 * Created by Administrator on 2016/8/25.
 */
public class MathCommon {
    /**
     * 判断有没有登录
     *
     * @param activity
     * @return
     */
    public static boolean isLogin(Activity activity) {
        if (MyApplication.userInfo == null||TextUtils.isEmpty(MyApplication.cookies)) {
            Intent intent = new Intent(activity, UserLoginActivity.class);
            activity.startActivity(intent);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取用户信息
     * @param activity
     * @param handler
     */
    public static void getUserInfo(final Activity activity, final Handler handler){
//        MyOkHttpUtils.postRequest(Urls.USER_INFO, null).execute(new MapCallback(activity) {
//            @Override
//            public void onResponse(Map rMap) {
//                super.onResponse(rMap);
//                if (rMap != null){
//                    List<Map<String, Object>> infoList = (List<Map<String, Object>>) rMap.get("rows");
//                    MyApplication.userInfo = infoList.get(0);
//                    saveUserInfo(activity);
//                    handler.sendEmptyMessage( Contanct.LOAD_SUCCESS);
//                }
//            }
//
//            @Override
//            public void onError(Call call, Exception exception) {
//                handler.sendEmptyMessage( Contanct.LOAD_ERROR);
//            }
//        });
    }

    /**
     * 存储用户信息
     */
    public static void saveUserInfo(Activity activity) {
        String userInfo = JsonUtil.getInstance().object2JSON(MyApplication.userInfo);
        SharedPrefUtil sharedPrefUtil = new SharedPrefUtil(activity, Contanct.SP_USER_NAME);
        sharedPrefUtil.putString(Contanct.SP_USERINFO, userInfo);
        sharedPrefUtil.putString(Contanct.SP_USER_UID, MyApplication.uid);
        sharedPrefUtil.commit();
    }

    /**
     * 对于  imageload  自定义的option  请求失败的图�?
     * @param resImg
     * @return
     */
    public static DisplayImageOptions custImageOptions(int resImg) {
        DisplayImageOptions custOptions = new DisplayImageOptions.Builder()
                .showStubImage(resImg)// 加载等待 时显示的图片
                .showImageForEmptyUri(resImg)// 加载数据为空时显示的图片
                .showImageOnFail(resImg)// 加载失败时显示的图片
                .cacheInMemory().cacheOnDisc() /**
         * .displayer(new
         * RoundedBitmapDisplayer(20))
         **/
                .build();
        return custOptions;
    }
}
