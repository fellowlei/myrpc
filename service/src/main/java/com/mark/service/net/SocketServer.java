package com.mark.service.net;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;
import com.mark.service.MyProxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lulei on 2018/5/17.
 */
public class SocketServer {

    public boolean isRun = true;

    //如果使用多线程，那就需要线程池，防止并发过高时创建过多线程耗尽资源
    public static ExecutorService threadPool = Executors.newFixedThreadPool(100);

    public void init() throws IOException {
        // 监听指定的端口
        int port = 55533;
        ServerSocket server = new ServerSocket(port);
        // server将一直等待连接的到来
        System.out.println("server将一直等待连接的到来");

        while (isRun) {
            final Socket socket = server.accept();

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        handlerSocket(socket);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        IOUtils.close(socket);
                    }
                }
            };
            threadPool.submit(runnable);
        }

        server.close();
    }

    public void handlerSocket(Socket socket) throws IOException {
        // ##################input #######################################
        // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
        InputStream inputStream = socket.getInputStream();
        byte[] bytes;
        // 因为可以复用Socket且能判断长度，所以可以一个Socket用到底
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
        String result = new String(bytes, "UTF-8");
        System.out.println("get message from client: " + new String(bytes, "UTF-8"));

        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject jsonResult = MyProxy.invokeRemote(jsonObject);

        // ##################output #######################################
        // 建立连接后获得输出流
        OutputStream outputStream = socket.getOutputStream();
//            String message = "你好  yiwangzhibujian";
        String message = jsonResult.toJSONString();
        //首先需要计算得知消息的长度
        byte[] sendBytes = message.getBytes("UTF-8");
        //然后将消息的长度优先发送出去
        outputStream.write(sendBytes.length >>8);
        outputStream.write(sendBytes.length);
        //然后将消息再次发送出去
        outputStream.write(sendBytes);
        outputStream.flush();
        inputStream.close();
    }

    public static void main(String[] args) throws Exception {
        new SocketServer().init();
    }

    public static void demo2() throws Exception {
        // 监听指定的端口
        int port = 55533;
        ServerSocket server = new ServerSocket(port);
        // server将一直等待连接的到来
        System.out.println("server将一直等待连接的到来");

        //如果使用多线程，那就需要线程池，防止并发过高时创建过多线程耗尽资源
        ExecutorService threadPool = Executors.newFixedThreadPool(100);
        while (true) {
            final Socket socket = server.accept();

            Runnable runnable=new Runnable() {
                @Override
                public void run() {
                    try {
                        // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
                        InputStream inputStream = socket.getInputStream();
                        byte[] bytes = new byte[1024];
                        int len;
                        StringBuilder sb = new StringBuilder();
                        while ((len = inputStream.read(bytes)) != -1) {
                            // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                            sb.append(new String(bytes, 0, len, "UTF-8"));
                        }
                        System.out.println("get message from client: " + sb);
                        inputStream.close();
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            threadPool.submit(runnable);
        }

    }
}