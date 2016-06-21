package com.jinzht.pro.bean;

/**
 * EventBus的消息
 */
public class EventMsg {
    private String msg;

    public EventMsg() {
    }

    public EventMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
