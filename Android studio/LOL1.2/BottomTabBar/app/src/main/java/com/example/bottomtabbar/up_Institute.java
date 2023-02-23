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

public class up_Institute extends AppCompatActivity {

    private Thread t;
    private Handler handler;
    private String result="";
    String Id;
    String institute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up__institute);

        //找到编辑控件
        final EditText editInstitute=findViewById(R.id.editInstitute);
        //找到隐藏的ID控件
        final TextView ID =findViewById(R.id.ID);

        Intent intent4 =getIntent();//获取information界面传来的信息
        if(intent4!=null){
            editInstitute.setText(intent4.getStringExtra("myinstitute"));//获取的昵称显示到编辑控件
            ID.setText(intent4.getStringExtra("ID"));//获取id账号放入隐藏控件，用于提交数据库  从而对内容进行修改
        }



        final Button serve = findViewById(R.id.serve);//找到保存控件
        Id=ID.getText().toString();//获取隐藏的id控件下的账号  存入Id字符串 方便线程调用服务

        //创建线程用于获取信息
        t=new Thread(new Runnable() {
            @Override
            public void run() {
                result=LOLPersonalWebService.LOLGetUserInfoByID(Id);
                Message m = handler.obtainMessage(); // 获取一个Message
                m.what=0;
                handler.sendMessage(m); // 发送消息
            }
        });
        serve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个新线程，用于读取网站信息
                t=new Thread(new Runnable() {
                    public void run() {
                        result=LOLPersonalWebService.LOLUpdateInstituteByID(Id,institute);
                        Message m = handler.obtainMessage(); // 获取一个Message
                        m.what=1;
                        handler.sendMessage(m); // 发送消息
                    }
                });

                institute = editInstitute.getText().toString();  //获取编辑控件下的新昵称  存放在petname字符内
                t.start();
            }
        });

        //handler发送消息
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                t.interrupt(); // 中断线程
                t = null;
                super.handleMessage(msg);
                if (msg.what==1 && result.contains("true"))
                {
                    Toast.makeText(up_Institute.this,"修改成功！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(up_Institute.this,information.class);
                    startActivity(intent);
                    up_Institute.this.finish();
                }
                else {
                    Toast.makeText(up_Institute.this, "修改失败！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}