package com.example.bottomtabbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends AppCompatActivity {
    //声明按钮控件
    private Button btnlg;//登录按钮
    private Button register;//注册按钮
    String password;//账号及密码
    public static String ID;

    EditText editID;//账号编辑框
    EditText editPassword;//密码编辑框

    private Thread t;
    private  String result = "";
    private TextView txtResult; // 声明一个显示结果的文本框对象
    private Handler handler; // 声明一个Handler对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnlg=findViewById(R.id.btnlogin);//找到到登录按钮控件
        btnlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editID = (EditText)findViewById(R.id.editpetname);
                editPassword = (EditText)findViewById(R.id.editPassword);
                ID=editID.getText().toString();
                password=editPassword.getText().toString();
                if(ID.length()==0&&password.length()==0){
                    Toast.makeText(login.this,"请输入账号和密码！！",Toast.LENGTH_LONG);
                    txtResult.setText("请输入账号和密码!!");
                }
                else if(password.length()==0){
                    Toast.makeText(login.this,"请输入密码！！",Toast.LENGTH_LONG);
                    txtResult.setText("请输入密码!!");
                }
                else if(ID.length()==0){
                    Toast.makeText(login.this,"请输入账号！！",Toast.LENGTH_LONG);
                    txtResult.setText("请输入账号!!");
                }
                else {
                    t=new Thread(new Runnable() {
                        public void run() {
                            result=LOLPersonalWebService.wpGetUserInfoByUsername(ID);//发送文本内容到Web服务器
                            Message m = handler.obtainMessage(); // 获取一个Message
                            m.what=0;
                            handler.sendMessage(m); // 发送消息
                        }
                    });
                    t.start();
                }

            }
        });

        txtResult=(TextView)findViewById(R.id.txtResult);


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
                    if (userlist[1].trim().equals(password)&&result!="")//java判断字符串相等
                    {
                        txtResult.setText("登录成功");
                        editPassword.setText("");

                        //将账号传送至主页面  并且打开主页面
                        Intent intent = new Intent();
                        intent.setClass(login.this,MainActivity.class);
                       /* Bundle bundle = new Bundle();
                        bundle.putString("ID",ID);
                        intent.putExtras(bundle);*/
                        startActivity(intent);
                        login.this.finish();

                    }
                    else
                    {
                        txtResult.setText("登录失败,密码错误！");
                    }
                }
                super.handleMessage(msg);
            }};


        register = findViewById(R.id.register);//找到注册按钮控件
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this,register.class);
                startActivity(intent);
            }
        });
    }

    // 返回按键的重写
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 按下键盘上返回按钮
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            new AlertDialog.Builder(this)
                    .setMessage("确定退出系统吗？")
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            })
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    login.this.finish();
                                    finish();
                                }
                            }).show();

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}