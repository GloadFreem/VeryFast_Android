package com.jinzht.pro.bean;

/**
 * web URL 返回值
 */
public class WebBean {

    /**
     * message :
     * status : 200
     * data : {"shareId":1,"url":"http://www.jinzht.com:8080/jinzht/3/27"}
     */

    private String message;
    private int status;
    /**
     * shareId : 1
     * url : http://www.jinzht.com:8080/jinzht/3/27
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
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}