package com.cn.aixiyi.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.aixiyi.R;


/**
 * Created by sty on 2016/8/14.
 */
public class ShowDialogUtil {
    private  Activity mActivity;
    private  Dialog mDialog;
    private  TextView noticeTv;

    public ShowDialogUtil(Activity activity){
        this.mActivity = activity;
    }


    public  void startWaitingDialog(Activity mActivity, String notice) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View v = inflater.inflate(R.layout.show_dialog_lay, null);

        noticeTv = (TextView) v.findViewById(R.id.notice_tv);
        noticeTv.setText(notice);
        mDialog = new Dialog(mActivity,R.style.loading_dialog);
        mDialog.getWindow().setGravity(Gravity.CENTER);
        mDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mDialog.setCancelable(true);
        if (!mDialog.isShowing())
            mDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stop_WaitingDialog();
            }
        }, 2000);

    }

    public void stop_WaitingDialog() {
        if (mDialog != null ) {
            mDialog.dismiss();
        }
    }
}
