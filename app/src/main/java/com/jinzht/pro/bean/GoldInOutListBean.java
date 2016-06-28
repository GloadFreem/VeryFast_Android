package com.jinzht.pro.bean;

import java.util.List;

/**
 * 金条收支明细
 */
public class GoldInOutListBean {

    /**
     * data : [{"rewardtradetype":{"name":"登录奖励"},"desc":"交易奖励","count":-10,"tradeDate":"2016-05-03 00:00:00"},{"rewardtradetype":{"name":"注册奖励"},"desc":"交易奖励","count":10,"tradeDate":"2016-05-23 00:00:00"},{"rewardtradetype":{"name":"注册奖励"},"desc":"交易奖励","count":-20,"tradeDate":"2016-06-23 00:00:00"},{"rewardtradetype":{"name":"注册奖励"},"desc":"交易奖励","count":10,"tradeDate":"2016-06-25 00:00:00"}]
     * message :
     * status : 200
     */

    private String message;
    private int status;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * rewardtradetype : {"name":"登录奖励"}
         * desc : 交易奖励
         * count : -10
         * tradeDate : 2016-05-03 00:00:00
         */
        private RewardtradetypeBean rewardtradetype;
        private String desc;
        private int count;
        private String tradeDate;

        public RewardtradetypeBean getRewardtradetype() {
            return rewardtradetype;
        }

        public void setRewardtradetype(RewardtradetypeBean rewardtradetype) {
            this.rewardtradetype = rewardtradetype;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getTradeDate() {
            return tradeDate;
        }

        public void setTradeDate(String tradeDate) {
            this.tradeDate = tradeDate;
        }

        public static class RewardtradetypeBean {
            /**
             * name : 登录奖励
             */
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
