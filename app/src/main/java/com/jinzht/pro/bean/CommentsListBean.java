package com.jinzht.pro.bean;

import java.util.List;

/**
 * 路演项目列表
 */
public class CommentsListBean {

    /**
     * message : 评论列表获取成功!
     * status : 200
     * data : [{"commentId":5,"users":{"name":"QQ群","userId":1,"headSculpture":"http://img4q.duitang.com/uploads/item/201311/09/20131109003922_JLyhT.jpeg"},"content":"说话很文邹","commentDate":"2016-06-18 00:00:00","flag":true},{"commentId":6,"users":{"name":"QQ群","userId":1,"headSculpture":"http://img4q.duitang.com/uploads/item/201311/09/20131109003922_JLyhT.jpeg"},"content":"说话很文邹","commentDate":"2016-06-18 00:00:00","flag":true},{"commentId":7,"users":{"name":"QQ群","userId":1,"headSculpture":"http://img4q.duitang.com/uploads/item/201311/09/20131109003922_JLyhT.jpeg"},"content":"说话很文邹","commentDate":"2016-06-18 00:00:00","flag":true},{"commentId":8,"users":{"name":"QQ群","userId":1,"headSculpture":"http://img4q.duitang.com/uploads/item/201311/09/20131109003922_JLyhT.jpeg"},"content":"说话很文邹","commentDate":"2016-06-18 00:00:00","flag":true}]
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
         * commentId : 5
         * users : {"name":"QQ群","userId":1,"headSculpture":"http://img4q.duitang.com/uploads/item/201311/09/20131109003922_JLyhT.jpeg"}
         * content : 说话很文邹
         * commentDate : 2016-06-18 00:00:00
         * flag : true
         */
        private int commentId;
        private UsersBean users;
        private String content;
        private String commentDate;
        private boolean flag;

        public int getCommentId() {
            return commentId;
        }

        public void setCommentId(int commentId) {
            this.commentId = commentId;
        }

        public UsersBean getUsers() {
            return users;
        }

        public void setUsers(UsersBean users) {
            this.users = users;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCommentDate() {
            return commentDate;
        }

        public void setCommentDate(String commentDate) {
            this.commentDate = commentDate;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public static class UsersBean {
            /**
             * name : QQ群
             * userId : 1
             * headSculpture : http://img4q.duitang.com/uploads/item/201311/09/20131109003922_JLyhT.jpeg
             */
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
    }
}
