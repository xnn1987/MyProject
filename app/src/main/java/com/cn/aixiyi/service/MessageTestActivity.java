package com.cn.aixiyi.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.cn.aixiyi.R;
import com.cn.aixiyi.utils.common.CommomAcitivity;

/**
 * Created by Administrator on 2016/11/1.
 */
public class MessageTestActivity extends CommomAcitivity {
    private OrderMsgService.DownloadBinder binder;
    private TextView text;

    private boolean binded;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int progress = msg.arg1;
            text.setText("downloading..." + progress + "%");
        };
    };

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (OrderMsgService.DownloadBinder) service;
            binded = true;
            // 开始下载
            binder.start();
            // 监听进度信息
            listenProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binded) {
            unbindService(conn);
        }
    }

    public void start(View view) {
        if (binded) {
            binder.start();
            listenProgress();
            return;
        }
        Intent intent = new Intent(this, OrderMsgService.class);
        startService(intent);   //如果先调用startService,则在多个服务绑定对象调用unbindService后服务仍不会被销毁
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    /**
     * 监听进度
     */
    private void listenProgress() {
        new Thread() {
            public void run() {
                while (!binder.isCancelled() && binder.getProgress() <= 100) {
                    int progress = binder.getProgress();
                    Message msg = handler.obtainMessage();
                    msg.arg1 = progress;
                    handler.sendMessage(msg);
                    if (progress == 100) {
                        break;
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }.start();
    }
}
