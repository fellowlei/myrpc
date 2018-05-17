package com.mark.server;

import com.alibaba.fastjson.JSONObject;
import com.mark.service.MyProxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

public class SocketClient {
    public static void main(String args[]) throws Exception {
        // 要连接的服务端IP地址和端口
        String host = "127.0.0.1";
        int port = 55533;
        // 与服务端建立连接
        Socket socket = new Socket(host, port);

        // ##################output #######################################
        // 建立连接后获得输出流
        OutputStream outputStream = socket.getOutputStream();
        String message = "你好  yiwangzhibujian";
        //首先需要计算得知消息的长度
        byte[] sendBytes = message.getBytes("UTF-8");
        //然后将消息的长度优先发送出去
        outputStream.write(sendBytes.length >>8);
        outputStream.write(sendBytes.length);
        //然后将消息再次发送出去
        outputStream.write(sendBytes);
        outputStream.flush();


        // ##################input #######################################
        // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
        InputStream inputStream = socket.getInputStream();
        byte[] bytes;
        // 因为可以复用Socket且能判断长度，所以可以一个Socket用到底
//        while (true) {
            // 首先读取两个字节表示的长度
            int first = inputStream.read();
            //如果读取的值为-1 说明到了流的末尾，Socket已经被关闭了，此时将不能再去读取
            if(first==-1){
               return;
            }
            int second = inputStream.read();
            int length = (first << 8) + second;
            // 然后构造一个指定长的byte数组
            bytes = new byte[length];
            // 然后读取指定长度的消息即可
            inputStream.read(bytes);
            System.out.println("get message from client: " + new String(bytes, "UTF-8"));
//        }
        inputStream.close();

        outputStream.close();
        socket.close();
    }

    public static Object remoteExecute(Object obj, Method method, Object[] args) throws Exception {
        // 要连接的服务端IP地址和端口
        String host = "127.0.0.1";
        int port = 55533;
        // 与服务端建立连接
        Socket socket = new Socket(host, port);

        // ##################output #######################################
        // 建立连接后获得输出流
        OutputStream outputStream = socket.getOutputStream();
        JSONObject jsonObject = MyProxy.genRemoteObj(obj, method, args);
        String message = jsonObject.toJSONString();
//        String message = "你好  yiwangzhibujian";
        //首先需要计算得知消息的长度
        byte[] sendBytes = message.getBytes("UTF-8");
        //然后将消息的长度优先发送出去
        outputStream.write(sendBytes.length >>8);
        outputStream.write(sendBytes.length);
        //然后将消息再次发送出去
        outputStream.write(sendBytes);
        outputStream.flush();


        // ##################input #######################################
        // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
        InputStream inputStream = socket.getInputStream();
        byte[] bytes;
        // 因为可以复用Socket且能判断长度，所以可以一个Socket用到底
//        while (true) {
        // 首先读取两个字节表示的长度
        int first = inputStream.read();
        //如果读取的值为-1 说明到了流的末尾，Socket已经被关闭了，此时将不能再去读取
        if(first==-1){
            return null;
        }
        int second = inputStream.read();
        int length = (first << 8) + second;
        // 然后构造一个指定长的byte数组
        bytes = new byte[length];
        // 然后读取指定长度的消息即可
        inputStream.read(bytes);
        String jsonResult = new String(bytes, "UTF-8");
        Object result = MyProxy.parseRemoteResult(JSONObject.parseObject(jsonResult));
        System.out.println("get message from client: " + new String(bytes, "UTF-8"));
//        }
        inputStream.close();

        outputStream.close();
        socket.close();
        return result;
    }
}