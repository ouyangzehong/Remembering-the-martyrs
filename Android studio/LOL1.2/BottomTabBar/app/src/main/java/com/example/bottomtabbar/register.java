package com.example.bottomtabbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entity.RandomNumber;
import entity.SendEmail;


public class register extends AppCompatActivity {

    private static final int COUNTDOWN = 60;
    private static final int REQUEST_CODE_VERIFY = 1001;
    private static final String KEY_START_TIME = "start_time";
    private Toast toast;
    private TextView tvToast;
    private long verificationCode=0; //生成的验证码
    private String email; //邮箱
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
    EditText etInputEmail;
    EditText editverify;
    TextView tvCode;
    Button verify;
    Button register;
    private int currentSecond;
    private Handler handler;

    private Thread t,t2,t3;//定义三个线程，分别为t注册插入、t2获取第所有id、t3获取所有邮箱
    private String result = "";
    private String result2="";
    private String result3="";
    private String getResult3="";
    private TextView txtResult; // 声明一个显示结果的文本框对象
    ImageView head;

    int sumId=0;
    int sumEmail=0;
    final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000,1000);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        //handler发送消息
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {



                if(result2!=null&&msg.what==2)
                {
                    t2.interrupt();
                    t2 = null;
                    int i=result2.indexOf("<string>");//去掉<string>前面部分
                    result2=result2.substring(i+8);
                    result2=result2.replace("<string>", "");//去掉所有的string=xtResult.setText(result); // 显示请求结果
                    String[] stulist2 = result2.split("</string>");//根据";"分割成字符串数组


                    for(int l=0;l<stulist2.length;l=l+2)
                    {
                        if(id.equals(stulist2[l].trim()))
                        {

                            sumId++;
                            Toast.makeText(register.this,"输出结果应为true:"+sumId, Toast.LENGTH_SHORT).show();
                            Log.i("测试1", String.valueOf(sumId));
                            break;
                        }

                    }
                    if(sumId>0)
                    {
                        txtResult.setText("该账号已经被注册！！！！");
                    }
                }

                if(result3!=null&&msg.what==3)
                {
                    t3.interrupt();
                    t3 = null;
                    int i=result3.indexOf("<string>");//去掉<string>前面部分
                    result3=result3.substring(i+8);
                    result3=result3.replace("<string>", "");//去掉所有的string=xtResult.setText(result); // 显示请求结果
                    String[] stulist3 = result3.split("</string>");//根据";"分割成字符串数组
                    for(int l=0;l<stulist3.length;l++)
                    {
                        if(email.equals(stulist3[l].trim()))
                        {

                            sumEmail++;
                            Toast.makeText(register.this,"输出结果应为true:"+sumEmail, Toast.LENGTH_SHORT).show();
                            break;
                        }

                    }
                    if (sumEmail>0)
                    {
                        txtResult.setText("该邮箱已经被注册！");
                    }

                }


                if (msg.what==1&&result.contains("true")&&sumEmail==0&&sumId==0) {
                    t.interrupt(); // 中断线程
                    t = null;
                    Toast.makeText(register.this,"注册成功！", Toast.LENGTH_SHORT).show();
                    txtResult.setText("成功"); // 显示请求结果
                    //解析返回结果
                    Intent intent=new Intent(register.this,login.class);
                    register.this.finish();
                }
                super.handleMessage(msg);
            }
        };

        init();//调用绑定控id函数

    }

    //绑定控件id函数
    private void init(){
        editrepassword = findViewById(R.id.editPassword2);
        editPassword = findViewById(R.id.editPassword);
        txtResult = findViewById(R.id.txtResult);
        tvCode=findViewById(R.id.tvCode);//获取验证控件
        verify = findViewById(R.id.verify);
        verify.setEnabled(false);
        //tvCode.setEnabled(false);
        editverify = findViewById(R.id.editverify);
        etInputEmail=findViewById(R.id.etInputEmail);
        register = findViewById(R.id.register);
        register.setEnabled(false);//设置注册按钮默认下不可点击，达到输入规范要求后才可以点击
    }


    //编写控件点击相应事件
    public void btClick(View view){
        switch (view.getId()){
                //注册按钮相应
            case R.id.register:
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


                    t2=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            result2=LOLPersonalWebService.LOLGetAll();
                            Message m=handler.obtainMessage();
                            m.what=2;
                            handler.sendMessage(m);
                        }
                    });
                    t2.start();//开启获取账号线程

                    t3=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            result3=LOLPersonalWebService.LOLGetTelNumber();
                            Message m=handler.obtainMessage();
                            m.what=3;
                            handler.sendMessage(m);
                        }
                    });
                    t3.start();//开启获取邮箱线程

                    t=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            result=LOLPersonalWebService.LOLInsertuserinfo(id,password,petname,telnumber,
                                    headimg,school,institute,major,createtime,remark);
                            Message m=handler.obtainMessage();
                            m.what=1;
                            handler.sendMessage(m);
                        }
                    });
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd   HH:mm");
                    Date date = new Date(System.currentTimeMillis());
                    editID=(EditText) findViewById(R.id.editpetname);
                    editPassword=(EditText) findViewById(R.id.editPassword);
                    etInputEmail=(EditText)findViewById(R.id.etInputEmail);

                    id = editID.getText().toString();
                    password = editPassword.getText().toString();
                    petname = "未填写";
                    telnumber = etInputEmail.getText().toString();
                    //telnumber="未填写";
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
                    t.start();//开启注册插入线程
                }
                break;

                //获取验证码响应
            case R.id.tvCode:
                if (isEmail(etInputEmail.getText().toString().trim()) && etInputEmail.getText().toString().trim().length()<=31){
                    //如果格式正确，向该邮箱发送验证码
                    email=etInputEmail.getText().toString();
                    sendVerificationCode(email); //发送验证码
                    myCountDownTimer.start();
                    verify.setEnabled(true);//设置验证按钮为可用

                    //currentSecond=60;//60秒倒计时开始
                   /* t2=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message m=handler.obtainMessage();
                            m.what=0;
                            handler.sendMessage(m);
                        }
                    });
                    t2.start();*/

                }else {
                    //如果格式不正确，则提示用户
                    Toast.makeText(register.this,"邮箱格式错误",Toast.LENGTH_SHORT).show();
                }
                break;
                //验证码验证按钮响应
            case R.id.verify:
                judgeVerificationCode(); //判断输入的验证码是否正确
                break;

        }
    }

    //重置发送验证码控件，在60秒后可以重新点击获取
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_VERIFY) {
            editverify.setText("");
            etInputEmail.setText("");
            // 重置"获取验证码"按钮
            tvCode.setText(R.string.smssdk_get_code);
            tvCode.setEnabled(true);
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
        }
    }

    //发送验证码
    private void sendVerificationCode(final String email) {
        try {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        RandomNumber rn = new RandomNumber();
                        verificationCode = rn.getRandomNumber(6);
                        SendEmail se = new SendEmail(email);
                        se.sendHtmlEmail(verificationCode);//发送html邮件
                        Toast.makeText(register.this,"发送成功",Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断输入的验证码是否正确
    private void judgeVerificationCode() {
        if(Integer.parseInt(editverify.getText().toString())==verificationCode){ //验证码和输入一致
            Toast.makeText(register.this,"验证成功",Toast.LENGTH_LONG).show();
            register.setEnabled(true);

        }else{
            Toast.makeText(register.this, "验证失败", Toast.LENGTH_LONG).show();
        }
    }

    //验证邮箱格式
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    //处理头像格式
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

    //倒计时函数
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            tvCode.setClickable(false);
            tvCode.setText(l/1000+"秒");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            tvCode.setText("重新获取");
            //设置可点击
            tvCode.setClickable(true);
        }
    }

}