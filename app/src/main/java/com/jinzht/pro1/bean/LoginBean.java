package com.jinzht.pro1.bean;

/**
 * 登录接口的返回信息
 */
public class LoginBean {

    private String msg;
    private DataEntity data;
    private int code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class DataEntity {

        private boolean info;
        private Object auth;

        public boolean getInfo() {
            return info;
        }

        public void setInfo(boolean info) {
            this.info = info;
        }

        public Object getAuth() {
            return auth;
        }

        public void setAuth(Object auth) {
            this.auth = auth;
        }
    }
}
