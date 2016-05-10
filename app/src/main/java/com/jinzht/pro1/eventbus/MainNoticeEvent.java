package com.jinzht.pro1.eventbus;

/**
 * 主页的推送消息
 */
public class MainNoticeEvent {
    private String msg;

    public MainNoticeEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
