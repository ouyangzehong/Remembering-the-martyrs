package com.example.bottomtabbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class fg_left extends AppCompatActivity {
    private Thread t;
    private TextView txtResult; // 声明一个显示结果的文本框对象
    private Handler handler;
    private String result = ""; // 声明一个代表显示结果的字符串
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fg_left);

        final TextView name = findViewById(R.id.username);
        final TextView School = findViewById(R.id.School);
        final TextView Number = findViewById(R.id.TelNumber);
        final TextView CreateBy = findViewById(R.id.CreateBy);
        final TextView time =findViewById(R.id.time);
        txtResult=(TextView) findViewById(R.id.txtResult);


    }
}