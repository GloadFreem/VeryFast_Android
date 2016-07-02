package com.jinzht.pro.bean;

/**
 * 金条奖励
 */
public class GoldAward {

    /**
     * message :
     * status : 200
     * data : {"count":10,"countTomorrow":11}
     */

    private String message;
    private int status;
    /**
     * count : 10
     * countTomorrow : 11
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
        private int count;
        private int countTomorrow;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getCountTomorrow() {
            return countTomorrow;
        }

        public void setCountTomorrow(int countTomorrow) {
            this.countTomorrow = countTomorrow;
        }
    }
}