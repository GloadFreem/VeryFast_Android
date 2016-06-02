package com.jinzht.pro1.bean;

/**
 * 客服接口返回信息
 */
public class CustomerServiceBean {

    /**
     * message :
     * status : 200
     * data : {"customServiceId":1,"name":"陈生珠","tel":"18729342354"}
     */

    private String message;
    private int status;
    /**
     * customServiceId : 1
     * name : 陈生珠
     * tel : 18729342354
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
        private long customServiceId;
        private String name;
        private String tel;

        public long getCustomServiceId() {
            return customServiceId;
        }

        public void setCustomServiceId(long customServiceId) {
            this.customServiceId = customServiceId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "customServiceId=" + customServiceId +
                    ", name='" + name + '\'' +
                    ", tel='" + tel + '\'' +
                    '}';
        }
    }
}
