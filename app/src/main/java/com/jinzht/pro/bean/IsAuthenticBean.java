package com.jinzht.pro.bean;

/**
 * 检查用户是否已认证
 */
public class IsAuthenticBean {

    /**
     * message :
     * status : 200
     * data : {"statusId":0,"name":"未认证"}
     */

    private String message;
    private int status;
    /**
     * statusId : 0
     * name : 未认证
     */

    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int statusId;
        private String name;

        public int getStatusId() {
            return statusId;
        }

        public void setStatusId(int statusId) {
            this.statusId = statusId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
