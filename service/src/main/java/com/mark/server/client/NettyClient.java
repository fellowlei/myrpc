package com.mark.server.client;

import com.mark.server.domain.RpcDecoder;
import com.mark.server.domain.RpcEncoder;
import com.mark.server.domain.RpcRequest;
import com.mark.server.domain.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by lulei on 2018/5/22.
 */
public class NettyClient {

    private String remoteAddr = "localhost:9999";
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);

    public void start(final RpcRequest rpcRequest) {
        Bootstrap b = new Bootstrap();
        b.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline cp = socketChannel.pipeline();
                        cp.addLast(new RpcEncoder(RpcRequest.class));
                        cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
                        cp.addLast(new RpcDecoder(RpcResponse.class));
                        cp.addLast(new RpcClientHandler());
                    }
                });

        final InetSocketAddress remoteAddr = new InetSocketAddress("localhost", 9999);
        ChannelFuture channelFuture = b.connect(remoteAddr);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    logger.debug("Successfully connect to remote server. remote peer = " + remoteAddr);
                    RpcClientHandler handler = channelFuture.channel().pipeline().get(RpcClientHandler.class);
                    handler.sendRequest(rpcRequest);
                }
            }
        });
    }
}
