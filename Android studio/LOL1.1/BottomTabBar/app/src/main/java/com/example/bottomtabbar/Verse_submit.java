package com.example.bottomtabbar;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import service.WebService;

public class Verse_submit extends AppCompatActivity {

    //全局变量
    private Handler handler; // 声明一个Handler对象
    private String result = ""; // 声明一个代表显示结果的字符串
    private TextView txtResult; // 声明一个显示结果的文本框对象
    private Thread t;
    String ID;
    String Tel;
    String Adress;
    String TheOne;
    String TheTwo;
    String TheThree;
    String photostring="";//将图片转换成字符串
    //控件声明
    EditText editID;
    EditText editTel;
    EditText editAdress;
    EditText editTheOne;
    EditText editTheTwo;
    EditText editTheThree;
    ImageView imagePhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse_submit);
        //版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.INTERNET}, 1);
        }
        //控件初始化
        editID=findViewById(R.id.editID);
        editTel=findViewById(R.id.editTel);
        editAdress=findViewById(R.id.editAdress);
        editTheOne=findViewById(R.id.editTheOne);
        editTheTwo=findViewById(R.id.editTheTwo);
        editTheThree=findViewById(R.id.editTheThree);
        //拍照按钮
        Button btnPicture=(Button)findViewById(R.id.btnPhoto);
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(); //创建Intent对象
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置动作照相机
                startActivityForResult(intent,1); //启动Activity
            }
        });
      //相册按钮
        Button btnXiangce=(Button)findViewById(R.id.btnXiangce);
        btnXiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
            }
        });
        //返回
        Button buttonReturn=(Button)findViewById(R.id.buttonReturn);
        buttonReturn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Verse_submit.this,MainActivity.class);
                startActivity(intent);
                Verse_submit.this.finish();
            }});
        //提交按钮
        Button btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个新线程，用于读取网站信息
                t=new Thread(new Runnable() {
                    public void run() {
                        result= WebService.wpInsertVerseInfo(ID,Tel,TheOne,TheTwo,TheThree,Adress,photostring);
                        Message m = handler.obtainMessage(); // 获取一个Message
                        m.what=1;
                        handler.sendMessage(m); // 发送消息
                    }
                });
                ID=editID.getText().toString();
                Tel=editTel.getText().toString();
                TheOne=editTheOne.getText().toString();
                TheTwo=editTheTwo.getText().toString();
                TheThree=editTheThree.getText().toString();
                Adress=editAdress.getText().toString();
                photostring="aaaa";//将图片转换成字符串
                imagePhoto.setDrawingCacheEnabled(true); //设置为true才能获取imagePhoto的图片
                byte[] photobyte = null;
                Bitmap photobmp = Bitmap.createBitmap(imagePhoto.getDrawingCache()); //获取imageView1的图片
                if (photobmp != null) {
                    photobyte=getByteByBitmap(photobmp); //对图片以以png格式压缩并转换成字节数组
                    photostring= Base64.encodeToString(photobyte, Base64.DEFAULT);//对图片字节数组进行Base64编码病转成字符串
                }
                t.start();// 开启线程
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                t.interrupt(); // 中断线程
                t = null;
                if (result.contains("true")) {
                    Toast.makeText(Verse_submit.this,"提交成功！", Toast.LENGTH_SHORT).show();
                    //解析返回结果
                }
                super.handleMessage(msg);

            }
        };
    }
    @Override//获取照相机返回的相片并在imagePhoto显示
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK&&requestCode == 1 ){
            Bundle extras=data.getExtras();
            Bitmap bitmap=(Bitmap)extras.get("data");
            imagePhoto=(ImageView)findViewById(R.id.imagePhoto);
            imagePhoto.setImageBitmap(bitmap);
        }
        else if(requestCode==2){
            if (data != null)
            {
                // 得到图片的全路径
                Uri uri = data.getData();
                imagePhoto=(ImageView)findViewById(R.id.imagePhoto);
                imagePhoto.setImageURI(uri);
            }
        }
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