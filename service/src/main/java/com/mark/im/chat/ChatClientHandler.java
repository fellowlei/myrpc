package com.mark.im.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class ChatClientHandler extends ChannelInboundHandlerAdapter {
    

    private int counter;
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(ChatUtil.genMsg("1:mark:mark2:hello"));
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
        throws Exception {
        String buf = (String) msg;
        System.out.println("Now is : " + buf + " ; the counter is : "+ ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
    
    

}
