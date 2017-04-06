package com.cn.aixiyi.utils;

import android.content.Context;
import android.content.Intent;

import android.widget.ImageView;



/**
 * Created by Administrator on 2016/3/15.
 */
public class ViewMathUtils {
    /**
     * 首页的消息通知   有或者五
     * @param mIvMsg
     */
    public static void mIvMsgNotifySee(ImageView mIvMsg){
//        if (MainActivity.allMsgCount==0){
//            mIvMsg.setImageResource(R.mipmap.xiaoxiwu);
//        }else {
//            mIvMsg.setImageResource(R.mipmap.xiaoxiyou);
//        }
    }

    /**
     * 跳转到消息通知里面的界面
     * @param context
     */
    public static void intent2MsgActivity(Context context) {
        Intent intent = new Intent();
//        if (MyApplication.userInfo == null) {
//            intent.setClass(context, LoginActivity.class);
//        } else{
//            intent.setClass(context, MsgCenterActivity.class);
//         }
        context.startActivity(intent);
    }

    /**
     * 判断是否需要登录
     * @param context
     * @param actClassName
     * @param <T>
     */
    public static  <T> void intentIsLogin(Context context,Class<T> actClassName){
//        if (MyApplication.uid==null){
//            Intent intent = new Intent(context,LoginActivity.class);
//            context.startActivity(intent);
//        }else {
//            if (MyApplication.userInfo==null){
//                new CommonNetGetData().getUserInfoData((Activity) context);
//            }else {
//                Intent intent = new Intent(context, actClassName);
//                context.startActivity(intent);
//            }
//
//        }
    }

    public static <T> void intentIsLogig(Context context,Class<T> actClassName){
//        if (MyApplication.uid==null){
//            Intent intent = new Intent(context,LoginActivity.class);
//            context.startActivity(intent);
//        }else {
//            if (MyApplication.userInfo==null){
//                new CommonNetGetData().getUserInfoData((Activity) context);
//            }else {
//                Intent intent = new Intent(context, actClassName);
//                context.startActivity(intent);
//            }
//
//        }
    }

    /**
     * 跳转
     * @param context
     * @param actClassName
     * @param params
     * @param values
     * @param <T>
     */
    public static <T> void intentToClass(Context context,Class<T> actClassName,String[] params,String[] values){
        Intent intent = new Intent(context,actClassName);
        if (params!=null){
            for (int i=0; i<values.length; i++){
                intent.putExtra(params[i],values[i]);
            }
        }
        context.startActivity(intent);
    }

    /**
     * 沉浸式
     * @param activity
     */
   /* public static void chenJinShi(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }*/
}
