package com.example.bottomtabbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;

import adapter.madapter;
import entity.MessageInfo;
import service.WebService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CFragment extends Fragment {
    private WebView wb3;
    private Handler handler; // 声明一个Handler对象
    private String result = ""; // 声明一个代表显示结果的字符串
    private String result2 = ""; // 声明第二个代表显示结果的字符串
    private Thread t;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CFragment newInstance(String param1, String param2) {
        CFragment fragment = new CFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c, container, false);
        wb3 = view.findViewById(R.id.fcwb);
        //按键监听
        wb3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && wb3.canGoBack()) {
                    /* handler.sendEmptyMessage(1);*/
                    wb3.goBack();
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    @SuppressLint("JavascriptInterface")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        wb3.setWebViewClient(new WebViewClient());
        //web_view.loadUrl("file:///android_asset/Demo.html");

        wb3.loadUrl("file:///android_asset/qifu/index.html");
        wb3.addJavascriptInterface(this, "android");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initView() {
        WebSettings setting = wb3.getSettings();
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
    public void turnqifu() {
        Intent intent = new Intent(getActivity(), QifuActivity.class);
        startActivity(intent);
    }

    @JavascriptInterface
    public void turnaddqifu() {
        Intent intent = new Intent(getActivity(), AddQifuActivity.class);
        startActivity(intent);
    }



}