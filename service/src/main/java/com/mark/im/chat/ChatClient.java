package com.mark.im.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.util.Scanner;

public class ChatClient {
    
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    public static void main(String[] args) throws Exception {

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();
                     p.addLast(new LineBasedFrameDecoder(1024));
                     p.addLast(new StringDecoder());
                     p.addLast(new ChatClientHandler());
                 }
             });

            ChannelFuture future = b.connect(HOST, PORT).sync();
            Scanner s = new Scanner(System.in);

            while(true) {
                System.out.println("请输入消息：格式 type:from:to:content");
                String lin = s.nextLine();
                if(lin.equals("exit")) break;
                System.out.println(">>>" + lin);
                ByteBuf message = ChatUtil.genMsg(lin);
                future.channel().writeAndFlush(message);

            }
            future.channel().closeFuture().sync();


        } finally {
            group.shutdownGracefully();
        }
    }



}
