package com.mark.im.chat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServerHandler extends ChannelInboundHandlerAdapter {
    
    
    private int counter;
    private static Map<String,Channel> userMap = new ConcurrentHashMap<String, Channel>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String)msg;


        System.out.println("server receive order : " + body + ";the counter is: " + ++counter);
        String[] arr = body.split(":");
        if(arr.length < 3) return;
        String type = arr[0];
        String from = arr[1];
        String to = arr[2];
        if(type.equals("1")){
            // login
            if(!userMap.containsKey(from)){
                userMap.put(from,ctx.channel());
            }
        }else if(type.equals("2")){
            // send msg
            Channel channel = userMap.get(to);
            channel.writeAndFlush(ChatUtil.genMsg(body));
        }else if(type.equals("3")){
            // send to all msg
            for (String key : userMap.keySet()) {
                userMap.get(key).writeAndFlush(ChatUtil.genMsg(body));
            }
        }else{
            System.out.println("bad type " + type + ",detail:" + body);
        }
        System.out.println("##########login users:" + userMap.keySet() + "###############");
        ByteBuf resp = Unpooled.copiedBuffer(ChatUtil.genMsg("ok!"));
        ctx.writeAndFlush(resp);
    }
    
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
