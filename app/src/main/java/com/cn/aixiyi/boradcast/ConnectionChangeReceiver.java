package com.cn.aixiyi.boradcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.cn.aixiyi.R;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.utils.CommonNetGetData;


public class ConnectionChangeReceiver extends BroadcastReceiver {
    public ConnectionChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
       CommonNetGetData commonNetGetData = new CommonNetGetData();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()){
            Toast.makeText(context, "无法连接到网络，请检查网络", Toast.LENGTH_SHORT).show();
            if (((Activity)context).findViewById(R.id.cannot_network)!=null)
                ((Activity)context).findViewById(R.id.cannot_network).setVisibility(View.VISIBLE);
        }else {
            if (((Activity)context).findViewById(R.id.cannot_network)!=null)
                ((Activity)context).findViewById(R.id.cannot_network).setVisibility(View.GONE);

        }

        if (MyApplication.uid !=null && !TextUtils.isEmpty(MyApplication.uid)) {
            if (MyApplication.userInfo == null)
                commonNetGetData.getUserInfoData((Activity) context);
            if (MyApplication.userQuanJu == null)
                commonNetGetData.getQuanJuData((Activity) context);
        }
    }
}
