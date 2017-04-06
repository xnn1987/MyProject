package com.cn.aixiyi.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by zhoukai on 2016/1/7.
 */
public class AppUtil {

    private static ProgressDialog window;
    private static Activity mActivity;

    public static ProgressDialog showProgress(Activity activity, String hintText) {
        dismissProgress();
        if (activity.getParent() != null) {
            mActivity = activity.getParent();
            if (mActivity.getParent() != null) {
                mActivity = mActivity.getParent();
            }
        } else {
            mActivity = activity;
        }
        final Activity finalActivity = mActivity;
        window = ProgressDialog.show(finalActivity, "", hintText);
        window.getWindow().setGravity(Gravity.CENTER);

        window.setCancelable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissProgress();
            }
        }, 10000);
        return window;
    }

    /**
     * @param activity
     * @param hintText
     * @param time     如果  -1  没有实现限制让他自动销毁
     * @return
     */
    public static ProgressDialog showProgress(Activity activity, String hintText, int time) {
        dismissProgress();

        if (activity.getParent() != null) {
            mActivity = activity.getParent();
            if (mActivity.getParent() != null) {
                mActivity = mActivity.getParent();
            }
        } else {
            mActivity = activity;
        }
        final Activity finalActivity = mActivity;
        window = ProgressDialog.show(finalActivity, "", hintText);
        window.getWindow().setGravity(Gravity.CENTER);

        window.setCancelable(false);
        if (time != -1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissProgress();
                }
            }, time);
        }
        return window;
    }


    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0 || str.trim().equals("null");
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static void errodDoanload(final String str) {
        if (window != null) {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mActivity, str, Toast.LENGTH_SHORT).show();
                    window.dismiss();
                }
            }, 500);


        }
    }

    public static void dismissProgress() {
        if (window != null) {
            window.dismiss();
        }
    }

}
