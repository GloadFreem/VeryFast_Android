package com.jinzht.pro.bean;

/**
 * 关注项目
 */
public class ProjectCollectBean {

    /**
     * message : 取消关注
     * status : 200
     * data : {"flag":1}
     */

    private String message;
    private int status;
    /**
     * flag : 1
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

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }
    }
}
