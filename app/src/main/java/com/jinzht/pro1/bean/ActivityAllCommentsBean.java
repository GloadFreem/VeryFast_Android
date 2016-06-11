package com.jinzht.pro1.bean;

import java.util.List;

/**
 * 所有活动评论人
 */
public class ActivityAllCommentsBean {

    /**
     * message :
     * status : 200
     * data : {"prises":["陈生珠"],"comments":[{"commentId":2,"content":"活动真实有效","userName":"陈生珠"},{"commentId":1,"content":"说的不错","userName":"陈生珠"},{"commentId":4,"content":"活动真实有效","userName":"陈生珠","atUserName":"陈生珠"},{"commentId":3,"content":"还可以","userName":"陈生珠","atUserName":"陈生珠"}]}
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
        private List<String> prises;
        private List<CommentsBean> comments;

        public List<String> getPrises() {
            return prises;
        }

        public void setPrises(List<String> prises) {
            this.prises = prises;
        }

        public List<CommentsBean> getComments() {
            return comments;
        }

        public void setComments(List<CommentsBean> comments) {
            this.comments = comments;
        }

        public static class CommentsBean {
            /**
             * commentId : 2
             * content : 活动真实有效
             * userName : 陈生珠
             */
            private int commentId;
            private String content;
            private String userName;

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

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }
    }
}
