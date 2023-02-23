package com.example.bottomtabbar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class register extends AppCompatActivity {

    private Handler handler; // 声明一个Handler对象
    String username,password,headimg,school,telnumber,createby,createtime,remark;
    EditText editUsername;
    EditText editPassword;
    private Thread t;
    private  String result = "";
    ImageView head;
    private TextView txtResult; // 声明一个显示结果的文本框对象
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
                if (result.contains("true")) {
                    txtResult.setText("成功"); // 显示请求结果
                    //解析返回结果
                }
                super.handleMessage(msg);
            }
        };

        Button register =findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        result=PersonalService.OLLInsertStudentinfo(username,password,headimg,school,telnumber,createby,createtime,remark);
                        Message m=handler.obtainMessage();
                        m.what=0;
                        handler.sendMessage(m);
                    }
                });
                editUsername=(EditText) findViewById(R.id.editUsername);
                editPassword=(EditText) findViewById(R.id.editPassword);
                username = editUsername.getText().toString();
                password = editPassword.getText().toString();
                headimg = "aaa";
                head =findViewById(R.id.head);
                head.setDrawingCacheEnabled(true); //设置为true才能获取imagePhoto的图片
                byte[] photobyte = null;
                Bitmap photobmp = Bitmap.createBitmap(head.getDrawingCache()); //获取imageView1的图片
                if (photobmp != null) {
                    photobyte=getByteByBitmap(photobmp); //对图片以以png格式压缩并转换成字节数组
                    headimg= Base64.encodeToString(photobyte, Base64.DEFAULT);//对图片字节数组进行Base64编码病转成字符串
                }

                school = "未填写";
                telnumber = "未填写";
                createby = "未填写";
                createtime = "";
                remark = "";
                t.start();
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