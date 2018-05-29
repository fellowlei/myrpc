package com.mark.im;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
/**
 * Created by lulei on 2018/5/29.
 * https://blog.csdn.net/linuu/article/details/51371595
 */
public class CustomClient {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "9999"));
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
                            ch.pipeline().addLast(new CustomEncoder());
                            ch.pipeline().addLast(new CustomClientHandler());
                        }
                    });

            ChannelFuture future = b.connect(HOST, PORT).sync();
            String msg = "Hello Netty Server ,I am a common client";
            CustomMsg customMsg = new CustomMsg((byte)0xAB, (byte)0xCD, msg.length(), msg);
            future.channel().writeAndFlush(customMsg);
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
