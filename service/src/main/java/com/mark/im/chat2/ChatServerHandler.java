package com.mark.im.chat2;


import com.mark.im.chat2.protocol.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServerHandler extends SimpleChannelInboundHandler<Message> {
    
    
    private int counter;
    private static Map<String,Channel> userMap = new ConcurrentHashMap<String, Channel>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        System.out.println("server receive order : " + message + ";the counter is: " + ++counter);
        String type = message.getType();
        String from = message.getFrom();
        String to = message.getTo();
        if(type.equals("1")){
            // login
            if(!userMap.containsKey(from)){
                userMap.put(from,ctx.channel());
            }
        }else if(type.equals("2")){
            // send msg
            Channel channel = userMap.get(to);
            channel.writeAndFlush(message);
        }else if(type.equals("3")){
            // send to all msg
            for (String key : userMap.keySet()) {
                userMap.get(key).writeAndFlush(message);
            }
        }else{
            System.out.println("bad type " + type + ",detail:" + message);
        }
        System.out.println("##########login users:" + userMap.keySet() + "###############");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
