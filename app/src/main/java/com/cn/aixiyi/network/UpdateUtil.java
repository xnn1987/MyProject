package com.cn.aixiyi.network;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


import com.cn.aixiyi.R;
import com.cn.aixiyi.utils.DebugLog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * Created by Administrator on 2016/2/25.
 */
public class UpdateUtil {

    private Context context;
    private ProgressDialog pBar;
    private String url;
    private String versionName;//版本名称
    private String des ; //版本描述
    private String currentVersion;//  当前版本

    public UpdateUtil(Context context, String url, String versionName, String des) {
        this.context = context;
        this.versionName = versionName;
        this.url = url;
        this.des = des;
    }

    public void updateThread(){
        // 自动检查有没有新版本 如果有新版本就提示更新
        new Thread() {
            public void run() {
                try {
                    handler1.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();
    }

    private Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            // 如果有更新就提示
            if (isNeedUpdate()) {   //在下面的代码段
                showUpdateDialog();  //下面的代码段
            }
        }

        ;
    };

    /**
     * 判断是否是最新版本，如果不是，跳出对话框选择是否更新：
     */
    private void showUpdateDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("请升级APP至版本" + versionName);
        builder.setMessage(des );
        builder.setCancelable(false);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    downFile(url);     //在下面的代码段
                } else {
                    Toast.makeText(context, "SD卡不可用，请插入SD卡",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        String[] LastVersionSplit = versionName.split("\\.");
        String[] currentVersionSplit = currentVersion.split("\\.");
        if (LastVersionSplit[1].equals(currentVersionSplit[1])) {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }

            });
        }
        builder.create().show();
    }

    private boolean isNeedUpdate() {
        String v = versionName; // 最新版本的版本号
        if (v.equals(getVersion())) {
            return false;
        } else {
            return true;
        }
    }

    // 获取当前版本的版本号
    private String getVersion() {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            currentVersion = packageInfo.versionName;

            //  多渠道打包之后可能会出现  -360等
            int indexOf = currentVersion.indexOf("-");//  没有返回-1
            if (indexOf!=-1){
                currentVersion = currentVersion.substring(0,indexOf);
                return currentVersion;
            }else {
                return currentVersion;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }

    void downFile(final String url1) {
            pBar = new ProgressDialog(context);    //进度条，在下载的时候实时更新进度，提高用户友好度
            pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pBar.setTitle("正在下载");
            pBar.setMessage("请稍候...");
            pBar.setProgress(0);
            pBar.setCanceledOnTouchOutside(false);
            pBar.setCancelable(false);
            pBar.show();
        new Thread() {
            public void run() {
                URL url = null;
                try {
                    url = new URL(url1);
                    if (url1.contains("http")){
                        HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.connect();
                        //获取到文件的大小
                        int length =conn.getContentLength();
                        pBar.setMax(length);
                        InputStream is = conn.getInputStream();
                        File file = new File(Environment.getExternalStorageDirectory(), "AiXiYi.apk");
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        byte[] buffer = new byte[1024];
                        int len ;
                        int total=0;
                        while((len =bis.read(buffer))!=-1){
                            fos.write(buffer, 0, len);
                            total+= len;
                            //获取当前下载量
                            DebugLog.e("tatal-->", total + "");
                            pBar.setProgress(total);
                        }

                        fos.close();
                        bis.close();
                        is.close();
                    }else if(url1.contains("https")){
                        TrustManager[] tm = { new MyX509TrustManager() };
                        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
                        sslContext.init(null, tm, new java.security.SecureRandom());
                        // 从上述SSLContext对象中得到SSLSocketFactory对象
                        SSLSocketFactory ssf = sslContext.getSocketFactory();
                        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                        conn.setSSLSocketFactory(ssf);
                        conn.setConnectTimeout(5000);
                        conn.connect();
                        //获取到文件的大小
                        int length =conn.getContentLength();
                        pBar.setMax(length);
                        InputStream is = conn.getInputStream();
                        File file = new File(Environment.getExternalStorageDirectory(), "AiXiYi.apk");
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        byte[] buffer = new byte[1024];
                        int len ;
                        int total=0;
                        while((len =bis.read(buffer))!=-1){
                            fos.write(buffer, 0, len);
                            total+= len;
                            //获取当前下载量
                            DebugLog.e("tatal-->", total + "");
                            pBar.setProgress(total);
                        }
                        fos.close();
                        bis.close();
                        is.close();
                    }

                    down();
                } catch (MalformedURLException e) {


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }

        }.start();
    }
    void down() {
        handler1.post(new Runnable() {
            public void run() {
                pBar.cancel();
                update();
            }
        });
    }
    //安装文件，一般固定写法
    void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "AiXiYi.apk")),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
