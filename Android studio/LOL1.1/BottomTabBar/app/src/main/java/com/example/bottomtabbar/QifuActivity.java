package com.example.bottomtabbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import adapter.madapter;
import entity.MessageInfo;
import service.WebService;

public class QifuActivity extends AppCompatActivity {
    //定义全局变量
    ListView listView;
    LayoutInflater layoutInflater;
    ArrayList<MessageInfo> arrayList=new ArrayList<>();
    private Handler handler; // 声明一个Handler对象
    private String result = ""; // 声明一个代表显示结果的字符串
    private String result2 = ""; // 声明第二个代表显示结果的字符串
    private Thread t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qifu);
        layoutInflater=getLayoutInflater();
        listView=findViewById(R.id.message_listview);


        t=new Thread(new Runnable() {
            public void run() {

               /* result= WebService.wpGetAllMessageInfo();//发送文本内容到Web服务器
                result2=WebService.wpGetsomeUserInfo();*/
                result= WebService.wpGetqifu();
                Message m = handler.obtainMessage(); // 获取一个Message
                m.what=0;
                handler.sendMessage(m); // 发送消息
            }
        });
        t.start();// 开启线程
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                t.interrupt(); // 中断线程
                t = null;
                if (result != null) {
                    int i=result.indexOf("<string>");//去掉<string>前面部分
                    result=result.substring(i+8);
                    result=result.replace("<string>", "");//去掉所有的string
                    String[] stulist = result.split("</string>");//根据"</string>"分割成字符串数组
                    int j=stulist.length/6;
                    Log.i("测试",result);
                    Toast.makeText(QifuActivity.this,result, Toast.LENGTH_SHORT).show();
                    int k = 0;
                    MessageInfo messageInfo=new MessageInfo();
                    for(int h=0;h<j;h++){

                        messageInfo.photo=stulist[k].trim();
                        messageInfo.name=stulist[k+1].trim();
                        messageInfo.time=stulist[k+2].trim();
                        messageInfo.content=stulist[k+3].trim();
                        messageInfo.university=stulist[k+4].trim();
                        messageInfo.institute=stulist[k+5];
                        arrayList.add(messageInfo);
                        k+=6;
                    }
                    //适配listview
                    madapter adapter = new madapter(layoutInflater,arrayList);
                    listView.setAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };









    }
}