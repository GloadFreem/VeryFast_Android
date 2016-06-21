package com.jinzht.pro.bean;

/**
 * 注册接口返回信息
 */
public class RegisterBean {

    /**
     * telePhone : 18729342354
     * platform : 1
     * userId : 2
     */

    private DataBean data;
    /**
     * data : {"telePhone":"18729342354","platform":1,"userId":2}
     * message : 恭喜您,注册成功！
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
        private String telePhone;
        private int platform;
        private long userId;

        public String getTelePhone() {
            return telePhone;
        }

        public void setTelePhone(String telePhone) {
            this.telePhone = telePhone;
        }

        public int getPlatform() {
            return platform;
        }

        public void setPlatform(int platform) {
            this.platform = platform;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "telePhone='" + telePhone + '\'' +
                    ", platform=" + platform +
                    ", userId=" + userId +
                    '}';
        }
    }
}
