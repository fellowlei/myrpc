package com.mark.im.chat2;

import com.mark.im.chat2.protocol.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class ChatClientHandler extends SimpleChannelInboundHandler<Message> {
    

    private int counter;
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(ChatUtil.genMsg("1:mark:mark2:hello"));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        System.out.println("Now is : " + message + " ; the counter is : "+ ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
    
    

}
