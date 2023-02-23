package com.example.bottomtabbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class register extends AppCompatActivity {

    String id;
    String password;
    String repassword;//二次密码
    String petname;
    String telnumber;
    String headimg;
    String school;
    String institute;
    String major;
    String createtime;
    String remark;
    EditText editID;
    EditText editPassword;
    EditText editrepassword;
    private Handler handler;
    private Thread t;
    private String result = "";
    private TextView txtResult; // 声明一个显示结果的文本框对象
    ImageView head;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                t.interrupt(); // 中断线程
                t = null;
                txtResult.setText(result); // 显示请求结果
                if (msg.what==0&&result.contains("true")) {
                    Toast.makeText(register.this,"注册成功！", Toast.LENGTH_SHORT).show();
                    txtResult.setText("成功"); // 显示请求结果
                    //解析返回结果

                    Intent intent=new Intent(register.this,login.class);
                    register.this.finish();
                }
                super.handleMessage(msg);
            }
        };

        editrepassword = findViewById(R.id.editPassword2);
        editPassword = findViewById(R.id.editPassword);

        txtResult = findViewById(R.id.txtResult);
        final Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                repassword = editrepassword.getText().toString();
                password = editPassword.getText().toString();


                if(password == ""||repassword == ""||password.length()==0||repassword.length()==0){
                    Toast.makeText(register.this,"请全部输入密码！", Toast.LENGTH_SHORT).show();
                    txtResult.setText("请全部输入密码！");
                }
                else if(!password.equals(repassword))
                {
                    Toast.makeText(register.this,"两次密码不一致！", Toast.LENGTH_SHORT).show();
                    txtResult.setText("两次密码不一致！");
                }
                else{

                    t=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            result=LOLPersonalWebService.LOLInsertuserinfo(id,password,petname,telnumber,
                                    headimg,school,institute,major,createtime,remark);
                            Message m=handler.obtainMessage();
                            m.what=0;
                            handler.sendMessage(m);
                        }
                    });

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd   HH:mm");
                    Date date = new Date(System.currentTimeMillis());
                    editID=(EditText) findViewById(R.id.editpetname);
                    editPassword=(EditText) findViewById(R.id.editPassword);
                    id = editID.getText().toString();
                    password = editPassword.getText().toString();
                    petname = "未填写";
                    telnumber = "未填写";
                    headimg = "aaa";
                    head =(ImageView) findViewById(R.id.head);
                    head.setDrawingCacheEnabled(true); //设置为true才能获取imagePhoto的图片
                    byte[] photobyte = null;
                    Bitmap photobmp = Bitmap.createBitmap(head.getDrawingCache()); //获取imageView1的图片
                    if (photobmp != null) {
                        photobyte=getByteByBitmap(photobmp); //对图片以以png格式压缩并转换成字节数组
                        headimg= Base64.encodeToString(photobyte, Base64.DEFAULT);//对图片字节数组进行Base64编码病转成字符串
                    }
                    school = "未填写";
                    institute="未填写";
                    major = "未填写";
                    createtime=simpleDateFormat.format(date);
                    remark="";
                    t.start();
                }

            }
        });


    }

    private byte[] getByteByBitmap(Bitmap bmp){
        byte[] compressData = null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);//100表示不压缩
        //此处必须以png格式压缩，若以JPG压缩，会有黑背景
        compressData = outStream.toByteArray();
        try {
            outStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return compressData;
    }
}