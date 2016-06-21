package com.jinzht.pro.bean;

/**
 * 通用返回信息，仅data为空时可用
 */
public class CommonBean {

    /**
     * data : “”
     * message : 验证码发送成功，请注意查收!
     * status : 200
     */

    private String data;
    private String message;
    private int status;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CommonBean{" +
                "data='" + data + '\'' +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
