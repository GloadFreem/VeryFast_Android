package com.jinzht.pro.bean;

/**
 * 活动点赞
 */
public class ActivityPriseBean {

    /**
     * message : 点赞成功!
     * status : 200
     * data : {"flag":2,"name":"段辉"}
     */

    private String message;
    private int status;
    /**
     * flag : 2
     * name : 段辉
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
        private int flag;
        private String name;

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
