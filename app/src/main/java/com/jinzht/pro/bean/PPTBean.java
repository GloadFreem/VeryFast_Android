package com.jinzht.pro.bean;

import java.util.List;

/**
 * 路演PPT
 */
public class PPTBean {

    /**
     * message : 项目现场获取成功!
     * status : 200
     * data : [{"playId":4,"sortIndex":0,"playTime":23,"startTime":0,"endTime":23,"imageUrl":"http://192.168.5.129:8080/jinzht/upload/105.jpg"},{"playId":6,"sortIndex":1,"playTime":34,"startTime":24,"endTime":34,"imageUrl":"http://192.168.5.129:8080/jinzht/upload/101.jpg"},{"playId":7,"sortIndex":2,"playTime":45,"startTime":35,"endTime":45,"imageUrl":"http://192.168.5.129:8080/jinzht/upload/102.jpg"},{"playId":8,"sortIndex":3,"playTime":67,"startTime":46,"endTime":67,"imageUrl":"http://192.168.5.129:8080/jinzht/upload/107.jpg"},{"playId":9,"sortIndex":4,"playTime":70,"startTime":68,"endTime":70,"imageUrl":"http://192.168.5.129:8080/jinzht/upload/100.jpg"}]
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
         * playId : 4
         * sortIndex : 0
         * playTime : 23
         * startTime : 0
         * endTime : 23
         * imageUrl : http://192.168.5.129:8080/jinzht/upload/105.jpg
         */
        private int playId;
        private int sortIndex;
        private int playTime;
        private int startTime;
        private int endTime;
        private String imageUrl;

        public int getPlayId() {
            return playId;
        }

        public void setPlayId(int playId) {
            this.playId = playId;
        }

        public int getSortIndex() {
            return sortIndex;
        }

        public void setSortIndex(int sortIndex) {
            this.sortIndex = sortIndex;
        }

        public int getPlayTime() {
            return playTime;
        }

        public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }

        public int getStartTime() {
            return startTime;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}