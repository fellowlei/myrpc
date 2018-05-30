package com.mark.im.chat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by lulei on 2018/5/30.
 */
public class ChatUtil {
    public static ByteBuf genMsg(String  msg){
        byte[] req = (msg + System.getProperty("line.separator")).getBytes();
        ByteBuf message = Unpooled.buffer(req.length);
        message.writeBytes(req);
        return message;
    }

}
