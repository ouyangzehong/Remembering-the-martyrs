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

public class login extends AppCompatActivity {
    private Button btnlg;
    private Button register;
    String username,password;
    String headimg="",school,telnumber,createby,createTime,remark;
    EditText editUsername;
    EditText editPassword;
    private Thread t;
    private  String result = "";
    private TextView txtResult; // 声明一个显示结果的文本框对象
    private Handler handler; // 声明一个Handler对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register=findViewById(R.id.register);
        btnlg=findViewById(R.id.btnlogin);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this,register.class);
                startActivity(intent);
            }
        });
        btnlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //创建一个一个线程，用于读取服务网站信息
                t=new Thread(new Runnable() {
                    public void run() {
                        result=PersonalService.OLLGetUserInfoByUserName(username);//发送文本内容到Web服务器
                        Message m = handler.obtainMessage(); // 获取一个Message
                        m.what=0;
                        handler.sendMessage(m); // 发送消息
                    }
                });
                editUsername=(EditText) findViewById(R.id.editUsername);
                editPassword=(EditText) findViewById(R.id.editPassword1);
                username = editUsername.getText().toString(); // 获取输入文本内容的EditText组件
                password = editPassword.getText().toString(); // 获取输入文本内容的EditText组件
                t.start();// 开启线程



            }
        });

        txtResult=(TextView) findViewById(R.id.txtResult);

        //创建一个Handler对象
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                t.interrupt(); // 中断线程
                t = null;
                if (result != null) {
                    txtResult.setText(result); // 显示请求结果
        //解析结果
        //解析返回结果
                    int i=result.indexOf("<string>");//去掉<string>前面部分
                    result=result.substring(i+8);
                    txtResult.setText(result); // 显示请求结果
                    result=result.replace("<string>", "");//去掉所有的string=xtResult.setText(result); // 显示请求结果
                    txtResult.setText(result); // 显示请求结果
                    String[] userlist = result.split("</string>");//根据";"分割成字符串数组
                    if (userlist[1].trim().equals(password))//java判断字符串相等
                    {
                        txtResult.setText("登录成功");
                        editPassword.setText("");

                        Intent intent = new Intent();
                        intent.setClass(login.this,MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username",username);
/*                        bundle.putString("school",userlist[3].trim());
                        bundle.putString("telnumber",userlist[4].trim());
                        bundle.putString("createby",userlist[5].trim());
                        bundle.putString("createTime",userlist[6].replace("0:00:00", "").trim());*/
                        intent.putExtras(bundle);
                        startActivityForResult(intent,0);

                    }
                    else
                    {
                        txtResult.setText("登录失败,账号或者密码错误！");
                    }
                }
                super.handleMessage(msg);
            }};
    }
}