package com.example.bottomtabbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class up_myname extends AppCompatActivity {

    private Thread t;
    private Handler handler;
    private String result="";
    String password,headimg="",school,telnumber,createby,createTime,remark;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_myname);

        final TextView id = findViewById(R.id.id);
        final EditText upname = findViewById(R.id.editmyname);//找到编辑控件
        Intent intent =getIntent();//获取information界面传来的信息
        if(intent!=null){
            upname.setText(intent.getStringExtra("myname"));//获取的昵称显示到编辑控件
            id.setText(intent.getStringExtra("id"));//获取id账号放入隐藏控件，用于提交数据库  从而对内容进行修改
        }

        final Button  serve = findViewById(R.id.serve);//找到保存控件
        username =id.getText().toString();//将隐藏控件下的账号 存入字符username  方便线程调用服务
        //编写保存点击事件
        serve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个新线程，用于读取网站信息
                t=new Thread(new Runnable() {
                    public void run() {
                        result=PersonalService.OOLUpdateNameByUserName(username,createby);
                        Message m = handler.obtainMessage(); // 获取一个Message
                        m.what=1;
                        handler.sendMessage(m); // 发送消息
                    }
                });

                createby = upname.getText().toString();
                t.start();
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                t.interrupt(); // 中断线程
                t = null;
                super.handleMessage(msg);
                if (msg.what==1 && result.contains("true"))
                {
                    Toast.makeText(up_myname.this,"修改成功！", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent();
                }
                else {
                    Toast.makeText(up_myname.this, "修改失败！", Toast.LENGTH_SHORT).show();
                }
            }
        };


    }
}