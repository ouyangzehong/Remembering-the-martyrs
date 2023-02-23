package com.example.bottomtabbar;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class information extends AppCompatActivity {
    String Id;
    String password;
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
    private List<Line> lineList = new ArrayList<>();
    private Thread t;
    private String result = ""; // 声明一个代表显示结果的字符串
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        //先找到控件id
        final ImageView HeadImg = (ImageView)findViewById(R.id.head);
        final TextView ID =(TextView)findViewById(R.id.ID);

/*        //获取id账号
        Intent intent = getIntent();
        if(intent != null)
        {
            byte[] bis = intent.getByteArrayExtra("my_headimg");
            Bitmap bitmap= BitmapFactory.decodeByteArray(bis, 0, bis.length);
            HeadImg.setImageBitmap(bitmap);
            //ID.setText(intent.getStringExtra("ID"));
        }*/

        Id=login.ID;
        //Id = ID.getText().toString();//从隐藏的文本控件获取id账号
        t=new Thread(new Runnable() {
            public void run() {
                result=LOLPersonalWebService.LOLGetUserInfoByID(Id);//发送文本内容到Web服务器

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

                    ImageView headimg=(ImageView) findViewById(R.id.head);
                    headimg.setDrawingCacheEnabled(true);
                    //设置为true才能获取imagePhoto的图片
                    String picturestr=stulist[4];//图片字符串
                    byte[] picturebyte = Base64.decode(picturestr, Base64.DEFAULT);//图片字符串转成字节
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    Bitmap picturebmp = BitmapFactory.decodeByteArray(picturebyte, 0, picturebyte.length);
                    headimg.setImageBitmap(picturebmp);
                    //headimg.setImageBitmap(getRoundedCornerBitmap(picturebmp));

                    petname = stulist[2].trim();
                    telnumber = stulist[3].trim();
                    school=stulist[5].trim();
                    institute = stulist[6].trim();
                    major = stulist[7].trim();


                    Line myId = new Line("账号:",R.drawable.ic_right_arrow,Id);
                    lineList.add(myId);
                    Line myname = new Line("昵称:",R.drawable.ic_right_arrow,petname);
                    lineList.add(myname);
                    Line number = new Line("邮箱:",R.drawable.ic_right_arrow,telnumber);
                    lineList.add(number);
                    Line myschool = new Line("学校:",R.drawable.ic_right_arrow,school);
                    lineList.add(myschool);
                    Line myinstitute = new Line("学院:",R.drawable.ic_right_arrow,institute);
                    lineList.add(myinstitute);
                    Line mymajor = new Line("专业:",R.drawable.ic_right_arrow,major);
                    lineList.add(mymajor);


               /*   School.setText(stulist[3].trim());
                    Number.setText(stulist[4].trim());
                    time.setText(stulist[6].replace("0:00:00", "").trim());
                    CreateBy.setText(stulist[5].trim());*/
                }
                super.handleMessage(msg);

            }
        };

        //适配器的设置
        LineAdapter adapter = new LineAdapter(information.this,
                R.layout.line_item, lineList);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        //注册监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://账号
                        Toast.makeText(information.this,"账号不能修改哦！！", Toast.LENGTH_SHORT).show();
                        break;
                    case 1://昵称
                        Intent intent =new  Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("mypetname",petname);
                        bundle.putString("ID",Id);
                        intent.putExtras(bundle);
                        intent.setClass(information.this,up_mypetname.class);
                        startActivity(intent);
                        break;
                    case 2://电话
                        Intent intent2 =new  Intent();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("mytelnumber",telnumber);
                        bundle2.putString("ID",Id);
                        intent2.putExtras(bundle2);
                        intent2.setClass(information.this,up_mytelnumber.class);
                        startActivity(intent2);

                        break;
                    case 3://学校
                        Intent intent3 =new  Intent();
                        Bundle bundle3 = new Bundle();
                        bundle3.putString("myschool",school);
                        bundle3.putString("ID",Id);
                        intent3.putExtras(bundle3);
                        intent3.setClass(information.this,up_myschool_info.class);
                        startActivity(intent3);
                        break;
                    case 4://学院
                        Intent intent4 =new  Intent();
                        Bundle bundle4 = new Bundle();
                        bundle4.putString("myinstitute",institute);
                        bundle4.putString("ID",Id);
                        intent4.putExtras(bundle4);
                        intent4.setClass(information.this,up_Institute.class);
                        startActivity(intent4);
                        break;
                    case 5://专业

                        Intent intent5 =new  Intent();
                        Bundle bundle5 = new Bundle();
                        bundle5.putString("mymajor",major);
                        bundle5.putString("ID",Id);
                        intent5.putExtras(bundle5);
                        intent5.setClass(information.this,up_Major.class);
                        startActivity(intent5);
                        break;
                }

            }
        });

        //点击头像的响应事件
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
                bundle.putString("ID",Id);
                intent.putExtras(bundle);
                intent.setClass(information.this,myhead_imge.class);
                startActivity(intent); //启动Activity

            }
        });
    }

    //重写返回键
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 按下键盘上返回按钮
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(information.this,MainActivity.class);
            startActivity(intent);
            information.this.finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}