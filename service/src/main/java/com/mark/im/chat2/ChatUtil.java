package com.mark.im.chat2;

import com.mark.im.chat2.protocol.Message;

/**
 * Created by lulei on 2018/5/30.
 */
public class ChatUtil {
    public static Message genMessage(String line){
        String[] arr = line.split(":");
        if(arr.length < 4){
            return null;
        }
        String type = arr[0];
        String from = arr[1];
        String to = arr[2];
        String content = arr[3];
        Message msg = new Message(type, from, to, content);
        return msg;
    }

}
