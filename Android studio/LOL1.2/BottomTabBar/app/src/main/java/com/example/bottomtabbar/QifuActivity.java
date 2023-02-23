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
                
                //result2=LOLPersonalWebService.LOLGetsomeUserInfo();
                result= LOLPersonalWebService.LOLGetQifu();//发送文本内容到Web服务器

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
                    long startTime = System.currentTimeMillis(); // 获取开始时间
// doThing(); // 测试的代码段
                    for(int h=0;h<j;h++){
                        MessageInfo messageInfo=new MessageInfo();
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
                    long endTime = System.currentTimeMillis(); // 获取结束时间
                    Log.e("wsy","代码运行时间： " + (endTime - startTime) + "ms");
                }
                super.handleMessage(msg);
            }
        };

   /*     handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                t.interrupt(); // 中断线程
                t = null;
                if (result != null&&result2 != null) {
//txtResult.setText(result); // 显示请求结果
//解析返回结果
                    int x=result2.indexOf("<string>");//去掉<string>前面部分
                    result2=result2.substring(x+8);
                    result2=result2.replace("<string>", "");//去掉所有的string
                    String[] stulist2 = result2.split("</string>");//根据"</string>"分割成字符串数组
                    ///////////////////////////////////////
                    int i=result.indexOf("<string>");//去掉<string>前面部分
                    result=result.substring(i+8);
                    result=result.replace("<string>", "");//去掉所有的string
                    String[] stulist = result.split("</string>");//根据"</string>"分割成字符串数组
                    int j=stulist.length/4;

                    Log.i("测试",result);
                    int k=0;
                    for(int h=0;h<j;h++){
                        MessageInfo messageInfo=new MessageInfo();
                        messageInfo.ID=stulist[k+1].trim();
                        messageInfo.content=stulist[k+2].trim();
                        messageInfo.time=stulist[k+3].trim();
                        messageInfo.institute="";
                        messageInfo.name="";
                        messageInfo.photo="";
                        messageInfo.university="";
                        for (int l = 0;  l< stulist2.length;l ++) {
                            if(messageInfo.ID.equals(stulist2[l].trim())){
                                messageInfo.institute=stulist2[l+4].trim();
                                messageInfo.name=stulist2[l+1].trim();
                                messageInfo.photo=stulist2[l+2].trim();
                                messageInfo.university=stulist2[l+3].trim();
                                break;
                            }
                        }

                        arrayList.add(messageInfo);
                        k+=4;
                    }
                    //适配listview
                    madapter adapter = new madapter(layoutInflater,arrayList);
                    listView.setAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };*/









    }
}