package com.jinzht.pro.bean;

/**
 * 设置身份类型
 */
public class SetUserTypeBean {

    /**
     * inviteCode : JZT0003914F
     */

    private DataBean data;
    /**
     * data : {"inviteCode":"JZT0003914F"}
     * message : 投资人身份添加成功!
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
        private String inviteCode;

        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }
    }
}
