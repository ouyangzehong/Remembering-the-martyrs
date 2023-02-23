package com.example.bottomtabbar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjm.bottomtabbar.BottomTabBar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private Thread t;
    private TextView txtResult; // 声明一个显示结果的文本框对象
    private Handler handler;
    private String result = ""; // 声明一个代表显示结果的字符串
    String username;


    private Thread t1;
    private Handler handler1;
    String password,headimg="",school,telnumber,createby,createTime,remark;
    private String result1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.drawer_layout);
        BottomTabBar mBottomTabBar=findViewById(R.id.bottom_tab_bar);
        mBottomTabBar.init(getSupportFragmentManager())
                .addTabItem("主页", R.drawable.ic_baseline_star_fill2, AFragment.class)
                .addTabItem("数据", R.drawable.ic_outline_bar_chart_24, BFragment.class)
                .addTabItem("留言", R.drawable.ic_baseline_people_24, CFragment.class)
                .isShowDivider(false);

        final TextView name = findViewById(R.id.username);
        final TextView School = findViewById(R.id.School);
        final TextView Number = findViewById(R.id.TelNumber);
        final TextView CreateBy = findViewById(R.id.CreateBy);
        final TextView time =findViewById(R.id.time);
        txtResult=(TextView) findViewById(R.id.txtResult);
        final  ImageView head =findViewById(R.id.h_head);

        //点击头像跳转至我的头像界面
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();

                //将图片存入byte数组
                Bitmap bitmap = ((BitmapDrawable)head.getDrawable()).getBitmap();
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte [] bitmapByte =baos.toByteArray();

                //打包至Bundle
                Bundle bundle=new Bundle();
                bundle.putByteArray("myhead",bitmapByte);
                bundle.putString("username",username);
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this,myhead_imge.class);
                startActivity(intent); //启动Activity

            }
        });

        //获取登录页面的账号并且显示
        Intent intent =getIntent();//
        name.setText(intent.getStringExtra("username"));
/*        School.setText(intent.getStringExtra("school"));
        CreateBy.setText(intent.getStringExtra("createby"));
        Number.setText(intent.getStringExtra("telnumber"));
        time.setText(intent.getStringExtra("createTime"));*/
        Bundle bundle = intent.getExtras();

        t=new Thread(new Runnable() {
            public void run() {
                result=PersonalService.OLLGetUserInfoByUserName(username);//发送文本内容到Web服务器
                Message m = handler.obtainMessage(); // 获取一个Message
                m.what=0;
                handler.sendMessage(m); // 发送消息
            }
        });
        username = name.getText().toString();
        t.start();// 开启线程
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                t.interrupt(); // 中断线程
                t = null;
                if (result != null) {
                    int i=result.indexOf("<string>");//去掉<string>前面部分
                    result=result.substring(i+8);
                    result=result.replace("<string>", "");//去掉所有的string=xtResult.setText(result); // 显示请求结果
                    String[] stulist = result.split("</string>");//根据";"分割成字符串数组

                    ImageView head=(ImageView) findViewById(R.id.h_head);
                    head.setDrawingCacheEnabled(true);
                    //设置为true才能获取imagePhoto的图片
                    String picturestr=stulist[2];//图片字符串
                    byte[] picturebyte = Base64.decode(picturestr, Base64.DEFAULT);//图片字符串转成字节
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    Bitmap picturebmp = BitmapFactory.decodeByteArray(picturebyte, 0, picturebyte.length);
                    head.setImageBitmap(picturebmp);

                    School.setText(stulist[3].trim());
                    Number.setText(stulist[4].trim());
                    time.setText(stulist[6].replace("0:00:00", "").trim());
                    CreateBy.setText(stulist[5].trim());
                }
                super.handleMessage(msg);
            }
        };

        school = School.getText().toString();
        //修改按钮
        final  Button updata_user =findViewById(R.id.updat_information_bu);
        updata_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();

                //将图片存入byte数组
                Bitmap bitmap = ((BitmapDrawable)head.getDrawable()).getBitmap();
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte [] bitmapByte =baos.toByteArray();

                //打包至Bundle
                Bundle bundle=new Bundle();
                bundle.putByteArray("myhead",bitmapByte);
                bundle.putString("username",username);
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this,Information.class);
                startActivity(intent); //启动Activity

            }
        });
    }






}