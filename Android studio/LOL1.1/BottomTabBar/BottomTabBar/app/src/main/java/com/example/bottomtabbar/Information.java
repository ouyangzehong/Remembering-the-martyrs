package com.example.bottomtabbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IInterface;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Information extends AppCompatActivity {

    private List<Line> lineList = new ArrayList<>();
    String username;
    private Thread t;
    private String result = ""; // 声明一个代表显示结果的字符串
    private Handler handler;
    private String nn2;
    String school,telnumber,createby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);


        final ImageView myhead = (ImageView)findViewById(R.id.myhead);
        final TextView myid = (TextView)findViewById(R.id.username);
        final TextView nn = (TextView)findViewById(R.id.nn);
        //获取用户账号
        //获取用户账号
        final Intent intent=getIntent();//创建Intent对象
        if(intent !=null)
        {
            byte[] bis = intent.getByteArrayExtra("myhead");
            Bitmap bitmap= BitmapFactory.decodeByteArray(bis, 0, bis.length);
            myhead.setImageBitmap(bitmap);
            myid.setText(intent.getStringExtra("username"));

        }
        username = myid.getText().toString(); // 获取输入文本内容的EditText组件
        //t线程用于获取数据库信息
        t=new Thread(new Runnable() {
            public void run() {
                result=PersonalService.OLLGetUserInfoByUserName(username);//发送文本内容到Web服务器
                Message m = handler.obtainMessage(); // 获取一个Message
                m.what=0;
                handler.sendMessage(m); // 发送消息
            }
        });
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

                    school=stulist[3].trim();
                    createby=stulist[5].trim();
                    telnumber=stulist[4].trim();

                    LineAdapter adapter = new LineAdapter(Information.this,
                            R.layout.line_item, lineList);
                    Line myId = new Line("我的账号:",R.drawable.ic_right_arrow,username);
                    lineList.add(myId);
                    Line myname = new Line("我的昵称:",R.drawable.ic_right_arrow,createby);
                    lineList.add(myname);
                    Line myschool = new Line("我的学校:",R.drawable.ic_right_arrow,school);
                    lineList.add(myschool);
                    Line number = new Line("我的电话:",R.drawable.ic_right_arrow,telnumber);
                    lineList.add(number);

                    ListView listView = findViewById(R.id.list_view);
                    listView.setAdapter(adapter);
                    //注册监听器
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                            switch (position){
                                case 0:
                                    Toast.makeText(Information.this,"账号不能修改哦！！", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Intent intent =new  Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("myname",createby);
                                    bundle.putString("id",username);
                                    intent.putExtras(bundle);
                                    intent.setClass(Information.this,up_myname.class);
                                    startActivity(intent);
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    break;
                            }

                        }
                    });
/*                  School.setText(stulist[3].trim());
                    Number.setText(stulist[4].trim());
                    time.setText(stulist[6].replace("0:00:00", "").trim());
                    CreateBy.setText(stulist[5].trim());*/
                }
                super.handleMessage(msg);

            }
        };

        nn2 = nn.getText().toString();




        //点击头像事件
        myhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                //将图片存入byte数组
                Bitmap bitmap = ((BitmapDrawable)myhead.getDrawable()).getBitmap();
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte [] bitmapByte =baos.toByteArray();

                //打包至Bundle
                Bundle bundle=new Bundle();
                bundle.putByteArray("myhead",bitmapByte);
                bundle.putString("username",username);
                intent.putExtras(bundle);
                intent.setClass(Information.this,myhead_imge.class);
                startActivity(intent);
            }
        });
    }
}