package com.example.bottomtabbar;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
public class PersonalService {
    static String IP="http://10.0.2.2:8023/Personal.asmx/";

    public static String post(String target,String params)
    {
        String result="";
        URL url;
        try {
            url = new URL(target);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection(); // 创建一个HTTP连接
            urlConn.setRequestMethod("POST");// 指定使用POST请求方式
            urlConn.setDoInput(true); // 向连接中写入数据
            urlConn.setDoOutput(true); // 从连接中读取数据
            urlConn.setUseCaches(false); // 禁止缓存
            urlConn.setInstanceFollowRedirects(true); // 自动执行HTTP重定向
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // 设置内容类型
            DataOutputStream out = new DataOutputStream(urlConn.getOutputStream()); // 获取输出流
            out.writeBytes(params); //将要传递的数据写入数据输出流
            out.flush(); //输出缓存
            out.close(); //关闭数据输出流
            if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {// 判断是否响应成功
                InputStreamReader in = new InputStreamReader(urlConn.getInputStream()); // 获得读取的内容
                BufferedReader buffer = new BufferedReader(in); // 获取输入流对象
                String inputLine = null;

                while ((inputLine = buffer.readLine()) != null) {
                    result += inputLine + "\n";
                }
                in.close(); //关闭字符输入流
            }
            urlConn.disconnect(); //断开连接
        } catch (MalformedURLException e) {
            result="POST请求失败："+e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            result="POST请求失败："+e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    //获取用户信息
    public  static  String OLLGetUserInfoByUserName(String username)
    {
        String result ="";
        String targer = IP+"GetUserInfoByUserName";
        String params = "username="+username;
        return post(targer,params);
    }

    //插入用户信息
    public static String OLLInsertStudentinfo(String username, String password, String headimg, String school,String telnumber, String createby,String createTime, String remark )
    {
        String target = IP+"Insertuserinfo";
        String params = "username="+ URLEncoder.encode(username);
        params += "&password="+URLEncoder.encode(password);
        params += "&headimg="+URLEncoder.encode(headimg);
        params += "&school="+URLEncoder.encode(school);
        params += "&telnumber="+URLEncoder.encode(telnumber);
        params += "&createby="+URLEncoder.encode(createby);
        params += "&createTime="+URLEncoder.encode(createTime);
        params += "&remark="+URLEncoder.encode(remark);
        return post(target,params);
    }

    public static String OOLUpdateUserinfoByUserName(String username, String password, String headimg, String school,String telnumber, String createby,String createTime, String remark){
        String target=IP+"UpdateUserinfoByUserName";
        String params = "username="+ URLEncoder.encode(username);
        params += "&password="+URLEncoder.encode(password);
        params += "&headimg="+URLEncoder.encode(headimg);
        params += "&school="+URLEncoder.encode(school);
        params += "&telnumber="+URLEncoder.encode(telnumber);
        params += "&createby="+URLEncoder.encode(createby);
        params += "&createTime="+URLEncoder.encode(createTime);
        params += "remark"+URLEncoder.encode(remark);
        return post(target,params);
    }

    //修改头像
    public static  String OOLUpdateHeadByUserName(String username,String headimg){

        String target = IP+"UpdateHeadByUserName";
        String params = "username="+ URLEncoder.encode(username);
        params += "&headimg="+URLEncoder.encode(headimg);
        return post(target,params);
    }

    //修改昵称
    public static  String OOLUpdateNameByUserName(String username,String createby){

        String target = IP+"UpdateNameByUserName";
        String params = "username="+ URLEncoder.encode(username);
        params += "&createby="+URLEncoder.encode(createby);
        return post(target,params);
    }

    //修改学校
    public static  String OOLUpdateSchoolByUserName(String username,String school){

        String target = IP+"UpdateSchoolByUserName";
        String params = "username="+ URLEncoder.encode(username);
        params += "&school="+URLEncoder.encode(school);
        return post(target,params);
    }

    //修改电话
    public static  String OOLUpdateTelNumberByUserName(String username,String telnumber){

        String target = IP+"UpdateTelNumberByUserName";
        String params = "username="+ URLEncoder.encode(username);
        params += "&telnumber="+URLEncoder.encode(telnumber);
        return post(target,params);
    }
}
