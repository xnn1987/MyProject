package com.cn.aixiyi.boradcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Administrator on 2016/3/4.
 */
public class JPushReceive extends BroadcastReceiver {
    private Bundle bundle = new Bundle();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("xiangdengma", intent.getAction());
//        if (intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
//
//            Bundle bundle = intent.getExtras();
//            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            Log.e("jPush mes ", title + "  msg  " + message);
//        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
//                .getAction())) {
//            Log.i("jPush", "接受到推送下来的自定义消息");
//            receivingNotification(context, bundle);
//        }
    }

    private void receivingNotification(Context context, Bundle bundle) {
//        NotificationManager manager = (NotificationManager) context
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//        // 使用notification
//        // 使用广播或者通知进行内容的显示
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(
//                context);
//        builder.setContentText(message).setSmallIcon(R.mipmap.xxjr_logo)
//                .setContentTitle(JPushInterface.EXTRA_TITLE);
//        builder.setDefaults(Notification.DEFAULT_SOUND);
//        manager.notify(1, builder.build());
    }
}
