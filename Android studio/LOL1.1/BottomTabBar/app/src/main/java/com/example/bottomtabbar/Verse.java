package com.example.bottomtabbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Verse extends AppCompatActivity {
    WebView webVersee;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse);


       webVersee = findViewById(R.id.webVerse);
        initView();
        webVersee.setWebViewClient(new WebViewClient());
        //web_view.loadUrl("file:///android_asset/Demo.html");

        webVersee.loadUrl("file:///android_asset/sanhangqingshu/index.html");//指定要加载的网页
        webVersee.addJavascriptInterface(this,"android");

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initView() {
        WebSettings setting = webVersee.getSettings();
        setting.setJavaScriptEnabled(true);//支持Js
        setting.setCacheMode(WebSettings.LOAD_DEFAULT);//缓存模式
        //是否支持画面缩放，默认不支持
        setting.setBuiltInZoomControls(true);
        setting.setSupportZoom(true);
        //是否显示缩放图标，默认显示
        setting.setDisplayZoomControls(false);
        //设置网页内容自适应屏幕大小
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        //SINGLE_COLUMN

        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);

    }
    @JavascriptInterface
    public void turnversesubmit() {
        Intent intent = new Intent(Verse.this, Verse_submit.class);
        startActivity(intent);
    }

    @JavascriptInterface
    public void turnasp(){
        Intent intent = new Intent(Verse.this, Verse_asp.class);
        startActivity(intent);
    }
}