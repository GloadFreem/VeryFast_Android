package com.jinzht.pro1.bean;

/**
 * 微信登录返回结果
 */
public class WechatLoginBean {
    /**
     * telePhone :
     * platform : 1
     * userId : 3
     */

    private DataBean data;
    /**
     * data : {"telePhone":"","platform":1,"userId":3}
     * msg : 微信登录成功！
     * status : 200
     */

    private String msg;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        private int telePhone;
        private int platform;
        private int userId;

        public int getTelePhone() {
            return telePhone;
        }

        public void setTelePhone(int telePhone) {
            this.telePhone = telePhone;
        }

        public int getPlatform() {
            return platform;
        }

        public void setPlatform(int platform) {
            this.platform = platform;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "telePhone=" + telePhone +
                    ", platform=" + platform +
                    ", userId=" + userId +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "WechatLoginBean{" +
                "data=" + data +
                ", msg='" + msg + '\'' +
                ", status=" + status +
                '}';
    }
}
