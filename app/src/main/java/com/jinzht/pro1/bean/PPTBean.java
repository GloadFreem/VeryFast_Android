package com.jinzht.pro1.bean;

import java.util.List;

/**
 * 路演PPT
 */
public class PPTBean {

    /**
     * message : 项目现场获取成功!
     * status : 200
     * data : [{"playId":4,"playTime":23,"imageUrl":"http://192.168.5.129:8080/jinzht/upload/dog.jpg"},{"playId":6,"playTime":34,"imageUrl":"http://192.168.5.129:8080/jinzht/upload/dog.jpg"},{"playId":7,"playTime":45,"imageUrl":"http://192.168.5.129:8080/jinzht/upload/dog.jpg"},{"playId":8,"playTime":67,"imageUrl":"http://192.168.5.129:8080/jinzht/upload/dog.jpg"},{"playId":9,"playTime":23,"imageUrl":"http://192.168.5.129:8080/jinzht/upload/dog.jpg"}]
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
         * playTime : 23
         * imageUrl : http://192.168.5.129:8080/jinzht/upload/dog.jpg
         */
        private int playId;
        private int playTime;
        private String imageUrl;

        public int getPlayId() {
            return playId;
        }

        public void setPlayId(int playId) {
            this.playId = playId;
        }

        public int getPlayTime() {
            return playTime;
        }

        public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
