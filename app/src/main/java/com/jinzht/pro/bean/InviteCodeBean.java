package com.jinzht.pro.bean;

/**
 * 邀请码、指环码
 */
public class InviteCodeBean {

    /**
     * message :
     * status : 200
     * data : {"codeId":1,"code":"dfsfdsf"}
     */

    private String message;
    private int status;
    /**
     * codeId : 1
     * code : dfsfdsf
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
        private int codeId;
        private String code;

        public int getCodeId() {
            return codeId;
        }

        public void setCodeId(int codeId) {
            this.codeId = codeId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
