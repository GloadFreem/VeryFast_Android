package com.jinzht.pro1.bean;

import java.util.List;

/**
 * 发表圈子动态返回信息
 */
public class ReleaseCircleBean {

    /**
     * message : 圈子发布成功!
     * status : 200
     * data : {"publicContentId":27,"content":"这是一篇圈子文章","contentimageses":[],"comments":[],"contentprises":[]}
     */

    private String message;
    private int status;
    /**
     * publicContentId : 27
     * content : 这是一篇圈子文章
     * contentimageses : []
     * comments : []
     * contentprises : []
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
        private int publicContentId;
        private String content;
        private List<?> contentimageses;
        private List<?> comments;
        private List<?> contentprises;

        public int getPublicContentId() {
            return publicContentId;
        }

        public void setPublicContentId(int publicContentId) {
            this.publicContentId = publicContentId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<?> getContentimageses() {
            return contentimageses;
        }

        public void setContentimageses(List<?> contentimageses) {
            this.contentimageses = contentimageses;
        }

        public List<?> getComments() {
            return comments;
        }

        public void setComments(List<?> comments) {
            this.comments = comments;
        }

        public List<?> getContentprises() {
            return contentprises;
        }

        public void setContentprises(List<?> contentprises) {
            this.contentprises = contentprises;
        }
    }
}
