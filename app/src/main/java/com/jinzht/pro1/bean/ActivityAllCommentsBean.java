package com.jinzht.pro1.bean;

import java.util.List;

/**
 * 所有活动评论人
 */
public class ActivityAllCommentsBean {

    /**
     * message :
     * status : 200
     * data : {"prises":["段辉","QQ群"],"comments":[{"commentId":13,"usersByUserId":{"userId":7},"content":"啊","userName":"段辉"},{"commentId":2,"usersByUserId":{"userId":1},"content":"活动真实有效","userName":"QQ群"},{"commentId":4,"usersByUserId":{"userId":1},"content":"活动真实有效","userName":"QQ群","atUserName":"段辉"},{"commentId":3,"usersByUserId":{"userId":1},"content":"还可以","userName":"QQ群","atUserName":"段辉"},{"commentId":1,"usersByUserId":{"userId":1},"content":"说的不错","userName":"QQ群"}]}
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
             * commentId : 13
             * usersByUserId : {"userId":7}
             * content : 啊
             * userName : 段辉
             */
            private int commentId;
            private UsersByUserIdBean usersByUserId;
            private String content;
            private String userName;
            private String atUserName;

            public int getCommentId() {
                return commentId;
            }

            public void setCommentId(int commentId) {
                this.commentId = commentId;
            }

            public UsersByUserIdBean getUsersByUserId() {
                return usersByUserId;
            }

            public void setUsersByUserId(UsersByUserIdBean usersByUserId) {
                this.usersByUserId = usersByUserId;
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

            public String getAtUserName() {
                return atUserName;
            }

            public void setAtUserName(String atUserName) {
                this.atUserName = atUserName;
            }

            public static class UsersByUserIdBean {
                /**
                 * userId : 7
                 */
                private int userId;

                public int getUserId() {
                    return userId;
                }

                public void setUserId(int userId) {
                    this.userId = userId;
                }
            }
        }
    }
}