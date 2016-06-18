package com.jinzht.pro1.bean;

import java.util.List;

/**
 * 路演音频
 */
public class RoadshowVoiceBean {

    /**
     * message : 项目现场获取成功!
     * status : 200
     * data : [{"sceneId":2,"audioPath":"http://www.jinzht.com/jinzht/audio.mav","totlalTime":15456,"beginTime":1465401600000,"endTime":1465747200000,"scenecomments":[{"commentId":0,"content":"现场很火爆","isvalid":1}],"audiorecords":[{"playId":5,"playTime":0,"imageUrl":"http://e.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=75aaa91fa444ad342eea8f83e59220c2/0bd162d9f2d3572cf556972e8f13632763d0c388.jpg"},{"playId":4,"playTime":0,"imageUrl":"http://e.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=75aaa91fa444ad342eea8f83e59220c2/0bd162d9f2d3572cf556972e8f13632763d0c388.jpg"}]}]
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
         * audioPath : http://www.jinzht.com/jinzht/audio.mav
         * totlalTime : 15456
         * beginTime : 1465401600000
         * endTime : 1465747200000
         * scenecomments : [{"commentId":0,"content":"现场很火爆","isvalid":1}]
         * audiorecords : [{"playId":5,"playTime":0,"imageUrl":"http://e.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=75aaa91fa444ad342eea8f83e59220c2/0bd162d9f2d3572cf556972e8f13632763d0c388.jpg"},{"playId":4,"playTime":0,"imageUrl":"http://e.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=75aaa91fa444ad342eea8f83e59220c2/0bd162d9f2d3572cf556972e8f13632763d0c388.jpg"}]
         */
        private int sceneId;
        private String audioPath;
        private int totlalTime;
        private long beginTime;
        private long endTime;
        private List<ScenecommentsBean> scenecomments;// 评论
        private List<AudiorecordsBean> audiorecords;// 图片

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

        public int getTotlalTime() {
            return totlalTime;
        }

        public void setTotlalTime(int totlalTime) {
            this.totlalTime = totlalTime;
        }

        public long getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(long beginTime) {
            this.beginTime = beginTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public List<ScenecommentsBean> getScenecomments() {
            return scenecomments;
        }

        public void setScenecomments(List<ScenecommentsBean> scenecomments) {
            this.scenecomments = scenecomments;
        }

        public List<AudiorecordsBean> getAudiorecords() {
            return audiorecords;
        }

        public void setAudiorecords(List<AudiorecordsBean> audiorecords) {
            this.audiorecords = audiorecords;
        }

        public static class ScenecommentsBean {
            /**
             * commentId : 0
             * content : 现场很火爆
             * isvalid : 1
             */
            private int commentId;
            private String content;
            private int isvalid;

            public int getCommentId() {
                return commentId;
            }

            public void setCommentId(int commentId) {
                this.commentId = commentId;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getIsvalid() {
                return isvalid;
            }

            public void setIsvalid(int isvalid) {
                this.isvalid = isvalid;
            }
        }

        public static class AudiorecordsBean {
            /**
             * playId : 5
             * playTime : 0
             * imageUrl : http://e.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=75aaa91fa444ad342eea8f83e59220c2/0bd162d9f2d3572cf556972e8f13632763d0c388.jpg
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
}
