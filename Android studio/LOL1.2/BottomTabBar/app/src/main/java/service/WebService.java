package service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class WebService {
    //static String IP="http://124.71.168.141:8005/WebService1.asmx/";
    static  String IP = "http://10.0.2.2:8033/PersonalWebService.asmx/";
    public static String post(String target,String  params)
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
    //插入祈福
    public static String wpInsertMessageInfo(String ID, String MessageContent, String CreateTime) {
        //要提交的目标地址
        String target=IP+"InsertMessageInfo";
        String params = "ID="+ URLEncoder.encode(ID);
        params += "&MessageContent="+URLEncoder.encode(MessageContent);
        params += "&CreateTime="+URLEncoder.encode(CreateTime);
        return post(target,params);
    }
    //根据ID获取最新祈福
    public static String wpGetMessageInfoByID(String ID) {
        String result="";
        //要提交的目标地址
        String target=IP+"GetMessageInfoByID";
        String params = "ID="+ID;
        return post(target,params);
    }
    //获取所有人的祈福
    public static String wpGetAllMessageInfo() {
        String result="";
        //要提交的目标地址
        String target=IP+"GetAllMessageInfo";
        String params = "";
        return post(target,params);
    }
    //通过id获取个人信息表的部分信息
    public static String wpGetsomeUserInfoByID(String ID) {
        String result="";
        //要提交的目标地址
        String target=IP+"GetsomeUserInfoByID";
        String params = "ID="+ID;
        return post(target,params);
    }
    //不通过id获取个人信息表的部分信息
    public static String wpGetsomeUserInfo() {
        String result="";
        //要提交的目标地址
        String target=IP+"GetsomeUserInfo";
        String params = "";
        return post(target,params);
    }


}
