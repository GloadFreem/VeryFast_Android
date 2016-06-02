package com.jinzht.pro1.bean;

/**
 * 登录接口的返回信息
 */
public class LoginBean {

    /**
     * userId : 1
     */

    private DataBean data;
    /**
     * data : {"userId":1}
     * message : 登录成功！
     * status : 200
     */

    private String message;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
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

    public static class DataBean {
        private long userId;

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "userId=" + userId +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
