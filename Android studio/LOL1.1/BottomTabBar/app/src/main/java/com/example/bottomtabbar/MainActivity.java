package com.example.bottomtabbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.hjm.bottomtabbar.BottomTabBar;

import java.io.ByteArrayOutputStream;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MainActivity extends AppCompatActivity {


    private Thread t;
    private TextView txtResult; // 声明一个显示结果的文本框对象
    private Handler handler;
    private String result = ""; // 声明一个代表显示结果的字符串
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.drawer_layout);


        BottomTabBar mBottomTabBar=findViewById(R.id.bottom_tab_bar);
        mBottomTabBar.init(getSupportFragmentManager())
                .addTabItem("铭记", R.drawable.ic_baseline_book_stroke, AFragment.class)
                .addTabItem("传承", R.drawable.ic_baseline_star_fill, BFragment.class)
                .addTabItem("祈福", R.drawable.ic_baseline_people_24, CFragment.class)
                .isShowDivider(false);

        mBottomTabBar.setOnTabChangeListener(new BottomTabBar.OnTabChangeListener() {
            @Override
            public void onTabChange(int position, String name, View view) {
                if(position==0){
                    setTitle("铭记历史");
                }
                if (position==1){
                    setTitle("精神传承");
                }
                if(position==2){
                    setTitle("许愿祈福");
                }
            }
        });


        //先绑定所有的控件
        final TextView ID = findViewById(R.id.ID);
        final TextView PetName=findViewById(R.id.petname);
        final TextView TelNumber=findViewById(R.id.TelNumber);
        final ImageView HeadImg=findViewById(R.id.head);
        final ImageView back =findViewById(R.id.h_back);
        final Button quit=findViewById(R.id.quit);
        Glide.with(this).load(R.drawable.appicon)
                .bitmapTransform(new BlurTransformation(this, 25), new CenterCrop(this))
                .into(back);
        final TextView School=findViewById(R.id.School);
        final TextView Institute=findViewById(R.id.Institute);
        final TextView Major =findViewById(R.id.Major);
        final TextView CreateTime=findViewById(R.id.CreateTime);
        final Button updat_information_bu=findViewById(R.id.updat_information_bu);

        txtResult = findViewById(R.id.textView);

        t=new Thread(new Runnable() {
            public void run() {
                result=LOLPersonalWebService.LOLGetUserInfoByID(id);//发送文本内容到Web服务器
                Message m = handler.obtainMessage(); // 获取一个Message
                m.what=0;
                handler.sendMessage(m); // 发送消息
            }
        });

        id=login.ID;
        //id=ID.getText().toString();
        t.start();
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

                    ID.setText(stulist[0]);
                    PetName.setText(stulist[2].trim());
                    TelNumber.setText(stulist[3].trim());

                    ImageView headimg=(ImageView) findViewById(R.id.head);
                    headimg.setDrawingCacheEnabled(true);
                    //设置为true才能获取imagePhoto的图片
                    String picturestr=stulist[4];//图片字符串
                    byte[] picturebyte = Base64.decode(picturestr, Base64.DEFAULT);//图片字符串转成字节
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    Bitmap picturebmp = BitmapFactory.decodeByteArray(picturebyte, 0, picturebyte.length);
                    headimg.setImageBitmap(picturebmp);

                    School.setText(stulist[5].trim());
                    Institute.setText(stulist[6].replace("0:00:00", "").trim());
                    Major.setText(stulist[7].trim());
                    CreateTime.setText(stulist[8].trim());
                }
                super.handleMessage(msg);
            }
        };

        //编写修改资料按钮控件的响应事件
        updat_information_bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();

                //将图片存入byte数组
                Bitmap bitmap = ((BitmapDrawable)HeadImg.getDrawable()).getBitmap();
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte [] bitmapByte =baos.toByteArray();

                //打包至Bundle
                Bundle bundle=new Bundle();
                bundle.putByteArray("my_headimg",bitmapByte);
                bundle.putString("ID",id);
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this,information.class);
                startActivity(intent); //启动Activity
            }
        });



        //编写点击头像事件
        HeadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();

                //将图片存入byte数组
                Bitmap bitmap = ((BitmapDrawable)HeadImg.getDrawable()).getBitmap();
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte [] bitmapByte =baos.toByteArray();

                //打包至Bundle
                Bundle bundle=new Bundle();
                bundle.putByteArray("my_headimg",bitmapByte);
                bundle.putString("ID",id);
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this,myhead_imge.class);
                startActivity(intent); //启动Activity

            }
        });


        //编辑退出软件按钮
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);

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
                                    System.exit(0);
                                }
                            }).show();

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

}