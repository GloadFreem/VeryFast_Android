package com.jinzht.pro1.bean;

import java.util.List;

/**
 * 路演音频
 */
public class RoadshowVoiceBean {

    /**
     * message : 项目现场获取成功!
     * status : 200
     * data : [{"sceneId":2,"audioPath":"http://192.168.5.129:8080/jinzht/upload/audio.mp3","totlalTime":15456}]
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
         * sceneId : 2
         * audioPath : http://192.168.5.129:8080/jinzht/upload/audio.mp3
         * totlalTime : 15456
         */
        private int sceneId;
        private String audioPath;
        private long totlalTime;

        public int getSceneId() {
            return sceneId;
        }

        public void setSceneId(int sceneId) {
            this.sceneId = sceneId;
        }

        public String getAudioPath() {
            return audioPath;
        }

        public void setAudioPath(String audioPath) {
            this.audioPath = audioPath;
        }

        public long getTotlalTime() {
            return totlalTime;
        }

        public void setTotlalTime(long totlalTime) {
            this.totlalTime = totlalTime;
        }
    }
}