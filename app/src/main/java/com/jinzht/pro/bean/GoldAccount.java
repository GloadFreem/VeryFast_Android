package com.jinzht.pro.bean;

/**
 * 金条账户
 */
public class GoldAccount {

    /**
     * rewardId : 1
     * count : 100
     */

    private DataBean data;
    /**
     * data : {"rewardId":1,"count":100}
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
        private int rewardId;
        private int count;

        public int getRewardId() {
            return rewardId;
        }

        public void setRewardId(int rewardId) {
            this.rewardId = rewardId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
