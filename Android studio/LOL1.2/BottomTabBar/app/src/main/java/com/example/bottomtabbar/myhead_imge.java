package com.example.bottomtabbar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class myhead_imge extends AppCompatActivity {


    String headimg = "";//将图片转换成字符串
    private Thread t;
    private String result = ""; // 声明一个代表显示结果的字符串
    private Handler handler; // 声明一个Handler对象
    String id;
    ImageView HeadImg;
    TextView ID;
    Button btnPicture2;
    Button btnPicture;
    Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myhead_imge);


        init();

        //获取传送过来的头像
        Intent intent =getIntent();
        if(intent != null)
        {
            byte[] bis = intent.getByteArrayExtra("my_headimg");
            Bitmap bitmap= BitmapFactory.decodeByteArray(bis, 0, bis.length);
            HeadImg.setImageBitmap(bitmap);
            ID.setText(intent.getStringExtra("ID"));

        }
        //hander发送消息
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                t.interrupt(); // 中断线程
                t = null;
                super.handleMessage(msg);
                if (msg.what==1 && result.contains("true"))
                {
                    Toast.makeText(myhead_imge.this,"修改成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(myhead_imge.this,information.class);
                    startActivity(intent);
                    myhead_imge.this.finish();
                }
                else {
                    Toast.makeText(myhead_imge.this, "修改失败！", Toast.LENGTH_SHORT).show();
                }
            }
        };


    }



    //绑定控件id函数
    private void init()
    {

        //获取用户账号以及头像
        HeadImg =findViewById(R.id.head);//绑定头像控件
        ID=findViewById(R.id.ID);//绑定隐藏的文本框控件 用于存储账号

        btnPicture2=(Button)findViewById(R.id.btnPhoto2);        //相册获取照片按钮
        btnPicture = (Button) findViewById(R.id.btnPhoto);        //相机获取照片按钮...........................
        btnSubmit=(Button)findViewById(R.id.btnSubmit);         //保存按钮

    }

    //编写控件点击相应事件
    public void btClick(View view){
        switch (view.getId())
        {
            case R.id.btnPhoto://相机界面的控件响应事件
                Intent intent=new Intent(); //创建Intent对象
                Button button=(Button)view; //将View强制转换为Button对象
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置动作照相机
                startActivityForResult(intent,1); //启动Activity
                break;
            case R.id.btnPhoto2://相册获取
                Intent intent2 = new Intent(Intent.ACTION_PICK, null);
                intent2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent2, 2);
                break;
            case R.id.btnSubmit://保持头像
                // 创建一个新线程，用于读取网站信息
                t=new Thread(new Runnable() {
                    public void run() {
                        result=LOLPersonalWebService.LOLUpdateHeadByID(id,headimg);
                        Message m = handler.obtainMessage(); // 获取一个Message
                        m.what=1;
                        handler.sendMessage(m); // 发送消息
                    }
                });
                id=ID.getText().toString();
                headimg="aaaa";//将图片转换成字符串
                HeadImg.setDrawingCacheEnabled(true); //设置为true才能获取imagePhoto的图片
                byte[] photobyte = null;
                Bitmap photobmp = Bitmap.createBitmap(HeadImg.getDrawingCache()); //获取imageView1的图片
                if (photobmp != null) {
                    photobyte=getByteByBitmap(photobmp); //对图片以以png格式压缩并转换成字节数组
                    headimg= Base64.encodeToString(photobyte, Base64.DEFAULT);//对图片字节数组进行Base64编码病转成字符串
                }
                t.start();// 开启线程
                break;

        }
    }



    @Override//获取照相机返回的相片并在imagePhoto显示
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK&&requestCode == 1 ){
            Bundle extras=data.getExtras();
            Bitmap bitmap=(Bitmap)extras.get("data");
            HeadImg=(ImageView)findViewById(R.id.head);
            HeadImg.setImageBitmap(bitmap);
        }
        else if(requestCode==2){
            if (data != null)
            {
                // 得到图片的全路径
                Uri uri = data.getData();
                HeadImg = findViewById(R.id.head);
                HeadImg.setImageURI(uri);
            }
        }
    }

    //图片处理.......
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

    //返回键的重写
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 按下键盘上返回按钮
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(myhead_imge.this,information.class);
            startActivity(intent);
            myhead_imge.this.finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}