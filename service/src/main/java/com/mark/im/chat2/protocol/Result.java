package com.mark.im.chat2.protocol;

/**
 * Created by lulei on 2018/5/30.
 */
public class Result {
    private String type;
    private String message;

    public Result(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Result{" +
                "type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
