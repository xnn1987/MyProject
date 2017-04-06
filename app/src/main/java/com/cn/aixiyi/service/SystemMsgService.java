package com.cn.aixiyi.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;

import com.cn.aixiyi.R;

/**
 * Created by sty on 2016/8/2.
 */
public class SystemMsgService extends Service {
    private MyThread myThread;
    private NotificationManager manager;
    private Notification notification;
    private PendingIntent pi;
    //private AsyncHttpClient client;
    private boolean flag = true;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onCreate() {
        System.out.println("oncreate()");
     //   this.client = new AsyncHttpClient();
        this.myThread = new MyThread();
        this.myThread.start();
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        this.flag = false;
        super.onDestroy();
    }

    private class MyThread extends Thread {
        @Override
        public void run() {
            String url = "你请求的网络地址";
            while (flag) {
                System.out.println("发送请求");
                try {
                    // 每个10秒向服务器发送一次请求
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 采用get方式向服务器发送请求
//                client.get(url, new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers,
//                                          byte[] responseBody) {
//                        try {
//                            JSONObject result = new JSONObject(new String(
//                                    responseBody, "utf-8"));
//                            int state = result.getInt("state");
//                            // 假设偶数为未读消息
//                            if (state % 2 == 0) {
//                                String content = result.getString("content");
//                                String date = result.getString("date");
//                                String number = result.getString("number");
//                                notification(content, number, date);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers,
//                                          byte[] responseBody, Throwable error) {
//                        Toast.makeText(getApplicationContext(), "数据请求失败", 0)
//                                .show();
//                    }
//                });
            }
        }
    }

    private void notification(String content, String number, String date) {
        // 获取系统的通知管理器
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(SystemMsgService.this);
        Intent intent = new Intent(getApplicationContext(),
                MessageTestActivity.class);
        intent.putExtra("content", content);
        intent.putExtra("number", number);
        intent.putExtra("date", date);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                SystemMsgService.this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_launcher);// 设置图标
        builder.setWhen(System.currentTimeMillis());// 设置通知来到的时间
        // builder.setAutoCancel(true);
        builder.setContentTitle( "ContentTitle");// 设置通知的标题
        builder.setContentText(content);// 设置通知的内容
        builder.setTicker("状态栏上显示");// 状态栏上显示
        builder.setOngoing(true);
            /*
             * // 设置声音(手机中的音频文件) String path =
             * Environment.getExternalStorageDirectory() .getAbsolutePath() +
             * "/Music/a.mp3"; File file = new File(path);
             * builder.setSound(Uri.fromFile(file));
            */
        // 获取Android多媒体库内的铃声
        builder.setSound(Uri.withAppendedPath(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "5"));
        // builder.setVibrate(new long[]{2000,1000,4000}); //需要真机测试
         notification = builder.build();
        notification.defaults = Notification.DEFAULT_ALL; // 使用默认设置，比如铃声、震动、闪灯
        notification.flags = Notification.FLAG_AUTO_CANCEL; // 但用户点击消息后，消息自动在通知栏自动消失
        notification.flags |= Notification.FLAG_NO_CLEAR;// 点击通知栏的删除，消息不会依然不会被删除
        manager.notify(0, notification);
    }
}
