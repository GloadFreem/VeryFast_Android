package com.jinzht.pro.bean;

/**
 * 检查更新
 */
public class UpdataBean {

    /**
     * message :
     * status : 200
     * data : {"versionId":1,"versionStr":"2.2.2","content":"更新内容","url":"http://www.jinzht.com/app/2.2.0","isForce":0,"updateTime":1464624000000,"platform":0}
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
         * versionId : 1
         * versionStr : 2.2.2
         * content : 更新内容
         * url : http://www.jinzht.com/app/2.2.0
         * isForce : true
         * updateTime : 1464624000000
         * platform : 0
         */
        private int versionId;
        private String versionStr;
        private String content;
        private String url;
        private boolean isForce;// 是否强制更新
        private long updateTime;
        private int platform;

        public int getVersionId() {
            return versionId;
        }

        public void setVersionId(int versionId) {
            this.versionId = versionId;
        }

        public String getVersionStr() {
            return versionStr;
        }

        public void setVersionStr(String versionStr) {
            this.versionStr = versionStr;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isForce() {
            return isForce;
        }

        public void setIsForce(boolean isForce) {
            this.isForce = isForce;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getPlatform() {
            return platform;
        }

        public void setPlatform(int platform) {
            this.platform = platform;
        }
    }
}
