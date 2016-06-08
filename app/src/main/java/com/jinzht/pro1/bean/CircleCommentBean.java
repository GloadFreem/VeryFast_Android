package com.jinzht.pro1.bean;

/**
 * 圈子评论
 */
public class CircleCommentBean {

    /**
     * message : 回复成功!
     * status : 200
     * data : {"commentId":6,"usersByUserId":{"name":"陈生珠","userId":1,"headSculpture":"upload/jinzht_user_1.jpg"},"usersByAtUserId":{"name":"","userId":4,"wechatId":"11833245"},"content":"说的不错"}
     */

    private String message;
    private int status;
    /**
     * commentId : 6
     * usersByUserId : {"name":"陈生珠","userId":1,"headSculpture":"upload/jinzht_user_1.jpg"}
     * usersByAtUserId : {"name":"","userId":4,"wechatId":"11833245"}
     * content : 说的不错
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
        private int commentId;
        /**
         * name : 陈生珠
         * userId : 1
         * headSculpture : upload/jinzht_user_1.jpg
         */

        private UsersByUserIdBean usersByUserId;
        /**
         * name :
         * userId : 4
         * wechatId : 11833245
         */

        private UsersByAtUserIdBean usersByAtUserId;
        private String content;

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

        public UsersByAtUserIdBean getUsersByAtUserId() {
            return usersByAtUserId;
        }

        public void setUsersByAtUserId(UsersByAtUserIdBean usersByAtUserId) {
            this.usersByAtUserId = usersByAtUserId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public static class UsersByUserIdBean {
            private String name;
            private int userId;
            private String headSculpture;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getHeadSculpture() {
                return headSculpture;
            }

            public void setHeadSculpture(String headSculpture) {
                this.headSculpture = headSculpture;
            }
        }

        public static class UsersByAtUserIdBean {
            private String name;
            private int userId;
            private String wechatId;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getWechatId() {
                return wechatId;
            }

            public void setWechatId(String wechatId) {
                this.wechatId = wechatId;
            }
        }
    }
}
