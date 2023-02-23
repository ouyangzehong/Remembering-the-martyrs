package com.example.bottomtabbar;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LOLPersonalWebService {
    static String IP="http://124.71.168.141:8005/WebService1.asmx/";
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
    public  static  String LOLGetUserInfoByID(String ID)
    {
        String result = "";
        String targer = IP+"GetUserInfoByID";
        String params = "ID=" + ID;
        return post(targer,params);
    }
    public static String wpGetUserInfoByUsername(String ID) {
        String result="";
        //要提交的目标地址
        String target=IP+"GetUserInfoByUsername";
        String params = "ID="+ID;
        return post(target,params);
    }

    //插入用户信息
    public  static String LOLInsertuserinfo(String id, String password, String petname, String telnumber,
                                            String headimg, String school, String institute, String major,String createtime,String remark)
    {
        String target = IP+"Insertuserinfo";
        String params = "id="+ URLEncoder.encode(id);
        params += "&password="+URLEncoder.encode(password);
        params += "&petname="+URLEncoder.encode(petname);
        params += "&telnumber="+URLEncoder.encode(telnumber);
        params += "&headimg="+URLEncoder.encode(headimg);
        params += "&school="+URLEncoder.encode(school);
        params += "&institute="+URLEncoder.encode(institute);
        params += "&major="+URLEncoder.encode(major);
        params += "&createtime="+URLEncoder.encode(createtime);
        params += "&remark="+URLEncoder.encode(remark);
        return post(target,params);

    }

    //修改头像
    public static String LOLUpdateHeadByID(String ID,String headimg)
    {
        String target = IP+"UpdateHeadByID";
        String params = "ID="+URLEncoder.encode(ID);
        params += "&headimg="+URLEncoder.encode(headimg);
        return post(target,params);
    }

    //修改昵称
    public static  String LOLUpdateNameByID(String ID,String petname)
    {
        String target = IP+"UpdateNameByID";
        String params = "ID="+ URLEncoder.encode(ID);
        params += "&petname="+URLEncoder.encode(petname);
        return post(target,params);
    }

    //修改学校
    public static  String LOLUpdateSchoolByID(String ID,String school)
    {
        String target = IP+"UpdateSchoolByID";
        String params = "ID="+ URLEncoder.encode(ID);
        params += "&school="+URLEncoder.encode(school);
        return post(target,params);
    }

    //修改电话

    public static  String LOLUpdateTelNumberByID(String ID,String telnumber)
    {
        String target = IP+"UpdateTelNumberByID";
        String params = "ID="+ URLEncoder.encode(ID);
        params += "&telnumber="+URLEncoder.encode(telnumber);
        return post(target,params);
    }


    //修改学院
    public  static  String LOLUpdateInstituteByID(String ID,String Institute)
    {
        String target = IP+"UpdateInstituteByID";
        String params = "ID="+ URLEncoder.encode(ID);
        params += "&Institute="+URLEncoder.encode(Institute);
        return post(target,params);
    }

    //修改专业
    public static String LOLUpdateMajorByID(String ID,String Major)
    {
        String target = IP+"UpdateMajorByID";
        String params = "ID="+ URLEncoder.encode(ID);
        params += "&Major="+URLEncoder.encode(Major);
        return post(target,params);
    }


}
