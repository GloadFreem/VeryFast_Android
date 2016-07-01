package com.jinzht.pro.bean;

/**
 * 是否有未读站内信
 */
public class HaveNotReadMessageBean {

    /**
     * flag : true
     */

    private DataBean data;
    /**
     * data : {"flag":true}
     * message :
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
        private boolean flag;

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
    }
}
