package com.jinzht.pro1.bean;

import java.util.List;

/**
 * 活动列表
 */
public class ActivityListBean {

    /**
     * message :
     * status : 401
     * data : [{"actionId":1,"name":"周一见【投资人】","address":"西安","description":"资本大时代","memberLimit":10,"startTime":"2016-06-06 00:00:00","endTime":"2016-06-06 00:00:00","type":1}]
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
         * actionId : 1
         * name : 周一见【投资人】
         * address : 西安
         * description : 资本大时代
         * memberLimit : 10
         * startTime : 2016-06-06 00:00:00
         * endTime : 2016-06-06 00:00:00
         * type : 1
         */
        private int actionId;
        private String name;
        private String address;
        private String description;
        private int memberLimit;
        private String startTime;
        private String endTime;
        private int type;

        public int getActionId() {
            return actionId;
        }

        public void setActionId(int actionId) {
            this.actionId = actionId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getMemberLimit() {
            return memberLimit;
        }

        public void setMemberLimit(int memberLimit) {
            this.memberLimit = memberLimit;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}