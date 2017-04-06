package com.cn.aixiyi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cn.aixiyi.R;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.mode.Urls;
import com.cn.aixiyi.myview.ProgressWebView;
import com.cn.aixiyi.utils.SetTitleBar;
import com.cn.aixiyi.utils.common.CommomAcitivity;
import com.cn.aixiyi.utils.common.SlidBackActivity;

/**
 * Created by Administrator on 2016/11/16.
 */
public class AiMainWebViewActivity  extends SlidBackActivity{

    private Intent intent;
    private  String webUrl="";
    private ProgressWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aimain_webview_lay);
        intent=getIntent();
        webUrl=intent.getStringExtra("Url");
        if (!webUrl.contains("http://")&& !webUrl.contains("https://")){
            webUrl="http://"+webUrl;
        }
        initWebView();
        setTitleBack();
    }
    public void setTitleBack() {
        SetTitleBar.setTitleText(this,"爱洗衣详情");
    }

    private void initWebView() {
        webView = (ProgressWebView) findViewById(R.id.LoadQuest_webview);
        webView.loadUrl(webUrl);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBlockNetworkImage(false);
        webView.getSettings().setBuiltInZoomControls(true);  //原网页基础上缩放
        webView.getSettings().setUseWideViewPort(true);      //任意比例缩放
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        onMyBackPressed(webView);
    }

    private void onMyBackPressed(WebView webView){
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            finish();
        }
    }
}