package com.jinzht.pro.bean;

import java.util.List;

/**
 * 活动详情
 */
public class ActivityDetailBean {

    /**
     * message :
     * status : 200
     * data : {"actionId":1,"name":"周一见【投资人】","address":"西安","description":"资本大时代","memberLimit":10,"startTime":"2016-06-06 00:00:00","endTime":"2016-06-06 00:00:00","actionprises":["QQ群"],"actionimages":[{"imgId":3,"url":"http://www.jinzht.com/jinzht/image/1.jpg"},{"imgId":2,"url":"http://www.jinzht.com/jinzht/image/1.jpg"},{"imgId":1,"url":"http://www.jinzht.com/jinzht/image/1.jpg"},{"imgId":5,"url":"http://www.jinzht.com/jinzht/image/1.jpg"},{"imgId":4,"url":"http://www.jinzht.com/jinzht/image/1.jpg"}],"type":1,"flag":0}
     */

    private String message;
    private int status;
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
        /**
         * actionId : 1
         * name : 周一见【投资人】
         * address : 西安
         * description : 资本大时代
         * memberLimit : 10
         * startTime : 2016-06-06 00:00:00
         * endTime : 2016-06-06 00:00:00
         * actionprises : ["QQ群"]
         * actionimages : [{"imgId":3,"url":"http://www.jinzht.com/jinzht/image/1.jpg"},{"imgId":2,"url":"http://www.jinzht.com/jinzht/image/1.jpg"},{"imgId":1,"url":"http://www.jinzht.com/jinzht/image/1.jpg"},{"imgId":5,"url":"http://www.jinzht.com/jinzht/image/1.jpg"},{"imgId":4,"url":"http://www.jinzht.com/jinzht/image/1.jpg"}]
         * type : 1
         * flag : false
         * attended : false
         */
        private int actionId;
        private String name;
        private String address;
        private String description;
        private int memberLimit;
        private String startTime;
        private String endTime;
        private int type;// 1免费，0付费
        private boolean flag;// true已点赞，false未点赞
        private boolean attended;// true已报名，false未报名
        private List<String> actionprises;
        private List<ActionimagesBean> actionimages;

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

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public boolean isAttended() {
            return attended;
        }

        public void setAttended(boolean attended) {
            this.attended = attended;
        }

        public List<String> getActionprises() {
            return actionprises;
        }

        public void setActionprises(List<String> actionprises) {
            this.actionprises = actionprises;
        }

        public List<ActionimagesBean> getActionimages() {
            return actionimages;
        }

        public void setActionimages(List<ActionimagesBean> actionimages) {
            this.actionimages = actionimages;
        }

        public static class ActionimagesBean {
            /**
             * imgId : 3
             * url : http://www.jinzht.com/jinzht/image/1.jpg
             */
            private int imgId;
            private String url;

            public int getImgId() {
                return imgId;
            }

            public void setImgId(int imgId) {
                this.imgId = imgId;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}