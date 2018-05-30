package com.mark.im.chat2;

import com.mark.im.chat2.protocol.Message;
import com.mark.im.chat2.protocol.RpcDecoder;
import com.mark.im.chat2.protocol.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

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
                     ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
                     ch.pipeline().addLast(new RpcEncoder(Message.class));
                     ch.pipeline().addLast(new RpcDecoder(Message.class));
                     ch.pipeline().addLast(new ChatClientHandler());
                 }
             });

            ChannelFuture future = b.connect(HOST, PORT).sync();
            Scanner s = new Scanner(System.in);

            while(true) {
                System.out.println("请输入消息：格式 type:from:to:content");
                String lin = s.nextLine();
                if(lin.equals("exit")) break;
                System.out.println(">>>" + lin);
                Message message = ChatUtil.genMessage(lin);
                future.channel().writeAndFlush(message);

            }
            future.channel().closeFuture().sync();


        } finally {
            group.shutdownGracefully();
        }
    }



}
