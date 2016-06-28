package com.jinzht.pro.bean;

/**
 * 分享返回值
 */
public class ShareBean {

    /**
     * image : http://www.bz55.com/uploads/allimg/130531/1-1305310Z505.jpg
     * title : 您身边的投资专家
     * url : http://192.168.5.139:8080/jinzht/JZT0004670F
     * content : 金指投--市值百亿的企业
     */

    private DataBean data;
    /**
     * data : {"image":"http://www.bz55.com/uploads/allimg/130531/1-1305310Z505.jpg","title":"您身边的投资专家","url":"http://192.168.5.139:8080/jinzht/JZT0004670F","content":"金指投--市值百亿的企业"}
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
        private String image;
        private String title;
        private String url;
        private String content;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}