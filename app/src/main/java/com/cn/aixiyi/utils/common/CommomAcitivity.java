package com.cn.aixiyi.utils.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cn.aixiyi.R;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.boradcast.ConnectionChangeReceiver;
import com.cn.aixiyi.utils.SystemBarTintManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;



/**
 * @date 2016/11/1
 * @author xingdapeng
 * @description 普通Activity
 */
public class CommomAcitivity extends Activity implements View.OnClickListener{
    private ConnectionChangeReceiver receiver;
    private SystemBarTintManager tintManager;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(this,true);
            setMiuiStatusBarDarkMode(this,true);
        }
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(false);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintResource(R.color.white);
        tintManager.setNavigationBarTintResource(R.color.white);
        tintManager.setStatusBarTintResource(R.color.white);
        receiverNetWork();
        MyApplication.addActivity(this);
    }

    public void  setNavigationBarTintEnabled(boolean flag){
        tintManager.setNavigationBarTintEnabled(flag);
    }

    public void setStatusBarTintEnabled(boolean flag){
        tintManager.setStatusBarTintEnabled(flag);
    }

    public void setTintResource(int resColor){
        tintManager.setTintResource(resColor);
    }

    public void setStatusBarTintResource(int resColor){
        tintManager.setStatusBarTintResource(resColor);
    }

    public void setNavigationBarTintResource(int resColor){
        tintManager.setNavigationBarTintResource(resColor);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
    }

    private void receiverNetWork(){
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new ConnectionChangeReceiver();
        registerReceiver(receiver,filter);
    }
    @TargetApi(19)
    private void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public  boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}
