package com.example.bottomtabbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AFragment extends Fragment {
    private WebView wb1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AFragment newInstance(String param1, String param2) {
        AFragment fragment = new AFragment();
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
        View view= inflater.inflate(R.layout.fragment_a, container, false);
        wb1=view.findViewById(R.id.fawb);
        //按键监听
        wb1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && wb1.canGoBack()) {
                    /* handler.sendEmptyMessage(1);*/
                    wb1.goBack();
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
        wb1.setWebViewClient(new WebViewClient());
        //web_view.loadUrl("file:///android_asset/Demo.html");
        wb1.setWebViewClient(new WebViewClient());
        wb1.loadUrl("file:///android_asset/Demoturn/begin.html");
        wb1.addJavascriptInterface(this,"android");
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initView() {
        WebSettings setting = wb1.getSettings();
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
    public void turnnote() {
        Intent intent=new Intent(getActivity(),MainActivityNote.class);
        startActivity(intent);
    }


}