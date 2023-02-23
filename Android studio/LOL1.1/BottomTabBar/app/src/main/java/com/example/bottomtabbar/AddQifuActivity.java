package com.example.bottomtabbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import service.WebService;

public class AddQifuActivity extends AppCompatActivity {
    private Handler handler; // 声明一个Handler对象
    private Thread t,t2;
    private String result = ""; // 声明一个代表显示结果的字符串
    private Button btn_qifei;
    private TextView textView;
    public int j;
    public boolean isInterrupt=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_qifu);

        final EditText editText=findViewById(R.id.addcontent);
        //获取时间
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd   HH：mm");
        Date date=new Date(System.currentTimeMillis());
        final String time =simpleDateFormat.format(date);


        btn_qifei=findViewById(R.id.btn_qifei);
        btn_qifei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //抛出线程 添加祈福
                t = new Thread(new Runnable() {
                    public void run() {
                        result = WebService.wpInsertMessageInfo(login.ID, editText.getText().toString(), time);
                        Message m = handler.obtainMessage(); // 获取一个Message
                        m.what = 0;
                        handler.sendMessage(m); // 发送消息
                    }
                });
                t.start();// 开启线程
            }
        });

        //抛出线程 添加祈福
        t2 = new Thread(new Runnable() {
            public void run() {

                while (!isInterrupt) {
                    Message m = handler.obtainMessage(); // 获取一个Message
                    m.what = 1;
                    handler.sendMessage(m); // 发送消息
                    try {
                        Thread.sleep(3000);//休眠1秒钟
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();//输出异常信息
                    }
                }

            }
        });
        isInterrupt=false;
        t2.start();// 开启线程

        textView=findViewById(R.id.textView);
        j =0;
        //创建一个Handler对象
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0){
                    t.interrupt(); // 中断线程
                    t = null;


                    if (result.contains("true")) {
                        Log.i("ceshi","添加成功");
                        //解析返回结果
                    }
                }


                if(msg.what==1){
                    //给textview加变动字体

                    String arrrylist[]={"正在给您的灯笼做保养","正在添加燃料……","彩虹海号！","飞上月球","正在测试风速……"};
                    textView.setText(arrrylist[j]);
                    j++;
                    j = j %arrrylist.length;

                }
                super.handleMessage(msg);

            }
        };


    }
}