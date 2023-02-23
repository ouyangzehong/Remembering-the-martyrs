package com.example.bottomtabbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

    int h1=0;
    int h2=0;
    private Thread t,t2;//定义三个线程，分别为t注册插入、t2获取第所有id、
    private String result = "";
    private String result2="";
    private String getResult3="";
    private TextView txtResult; // 声明一个显示结果的文本框对象
    private Handler handler; // 声明一个Handler对象
    int cout=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();//调用绑定id控件函数
        //创建一个Handler对象
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(result2!=null&&msg.what==1)
                {
                    t2.interrupt();
                    t2 = null;
                    int i=result2.indexOf("<string>");//去掉<string>前面部分
                    result2=result2.substring(i+8);
                    result2=result2.replace("<string>", "");//去掉所有的string=xtResult.setText(result); // 显示请求结果
                    String[] stulist2 = result2.split("</string>");//根据";"分割成字符串数组

                    for(int l=0;l<stulist2.length;l=l+2)
                    {
                        if(ID.equals(stulist2[l].trim()))
                        {

                            cout++;
                            //Toast.makeText(login.this,"输出结果应为true:"+cout, Toast.LENGTH_SHORT).show();
                            Toast.makeText(login.this,"登录成功！", Toast.LENGTH_SHORT).show();
                            break;
                        }

                    }
                    if(cout==0)
                    {
                        txtResult.setText("不存在该账号！！！");
                    }
                }

                if (result != null&&msg.what==0&&cout>0) {
                    t.interrupt(); // 中断线程
                    t = null;
                    txtResult.setText(result); // 显示请求结果
                    //解析结果
                    //解析返回结果
                    int k=result.indexOf("<string>");//去掉<string>前面部分
                    result=result.substring(k+8);
                    //txtResult.setText(result); // 显示请求结果
                    result=result.replace("<string>", "");//去掉所有的string=xtResult.setText(result); // 显示请求结果
                    //txtResult.setText(result); // 显示请求结果
                    Log.i("测试",result);
                    String[] userlist = result.split("</string>");//根据";"分割成字符串数组
                    if (userlist[1].trim().equals(password)&&result!="")//java判断字符串相等
                    {
                        txtResult.setText("登录成功");
                        editPassword.setText("");

                        //将账号传送至主页面  并且打开主页面
                        Intent intent = new Intent();
                        intent.setClass(login.this,MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("ID",ID);
                        intent.putExtras(bundle);
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
    }

    //绑定控件id函数
    private void init()
    {
        btnlg=findViewById(R.id.btnlogin);//找到到登录按钮控件
        txtResult=(TextView)findViewById(R.id.txtResult);
        register = findViewById(R.id.register);//找到注册按钮控件
    }

    //编写控件点击相应事件
    public void btClick(View view){
        switch (view.getId())
        {
            case R.id.btnlogin:
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
                    t2=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            result2=LOLPersonalWebService.LOLGetAll();
                            Message m=handler.obtainMessage();
                            m.what=1;
                            handler.sendMessage(m);
                        }
                    });
                    t2.start();
                    t=new Thread(new Runnable() {
                        public void run() {
                            result=LOLPersonalWebService.LOLGetUserInfoByID(ID);//发送文本内容到Web服务器
                            Message m = handler.obtainMessage(); // 获取一个Message
                            m.what=0;
                            handler.sendMessage(m); // 发送消息
                        }
                    });
                    t.start();
                }
                break;

            case R.id.register:
                Intent intent=new Intent(login.this,register.class);
                startActivity(intent);
                break;
        }
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