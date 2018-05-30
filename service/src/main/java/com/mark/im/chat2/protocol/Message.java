package com.mark.im.chat2.protocol;

/**
 * Created by lulei on 2018/5/30.
 */
public class Message {
    private String type;
    private String from;
    private String to;
    private String content;

    public Message(String type, String from, String to, String content) {
        this.type = type;
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
