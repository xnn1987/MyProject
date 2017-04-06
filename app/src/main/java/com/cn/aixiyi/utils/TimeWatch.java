package com.cn.aixiyi.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;

import com.cn.aixiyi.R;


/**
 * Created by sty on 2016/8/1.
 */
public class TimeWatch extends CountDownTimer {
    private Context context;
    private  Button yzmBtn;

    public TimeWatch(long millisInFuture, long countDownInterval, Context context, Button yzmBtn) {
        super(millisInFuture, countDownInterval);
        this.context = context;
        this.yzmBtn = yzmBtn;
    }

    @Override
    public void onFinish() {// 计时完毕
        yzmBtn.setText(context.getResources().getString(R.string.lr_have_yanzheng));
        yzmBtn.setClickable(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程
        yzmBtn.setClickable(false);//防止重复点击
        yzmBtn.setText(millisUntilFinished / 1000 + "s");
    }
}
