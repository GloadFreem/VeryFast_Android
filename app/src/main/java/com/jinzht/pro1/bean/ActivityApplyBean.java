package com.jinzht.pro1.bean;

/**
 * 活动报名返回信息
 */
public class ActivityApplyBean {

    /**
     * message : 报名信息提交成功!
     * status : 200
     * data : {"attendUid":4,"content":"我想参加本次活动","enrollDate":"2016-06-04 15:27:01"}
     */

    private String message;
    private int status;
    /**
     * attendUid : 4
     * content : 我想参加本次活动
     * enrollDate : 2016-06-04 15:27:01
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
        private int attendUid;
        private String content;
        private String enrollDate;

        public int getAttendUid() {
            return attendUid;
        }

        public void setAttendUid(int attendUid) {
            this.attendUid = attendUid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getEnrollDate() {
            return enrollDate;
        }

        public void setEnrollDate(String enrollDate) {
            this.enrollDate = enrollDate;
        }
    }
}
