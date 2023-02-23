package com.example.bottomtabbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class Verse_asp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse_asp);
        WebView webAsp = findViewById(R.id.webAsp);
        WebSettings webSettings = webAsp.getSettings();
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//设置webview推荐使用的窗口
        webSettings.setLoadWithOverviewMode(true);//设置webview加载的页面的模式
//webSettings.setSupportZoom(true); // 支持缩放
//webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webAsp.loadUrl("http://10.101.136.62:8022/");//指定要加载的网页
    }
}