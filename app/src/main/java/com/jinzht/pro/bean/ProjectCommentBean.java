package com.jinzht.pro.bean;

import java.util.List;

/**
 * 项目评论
 */
public class ProjectCommentBean {

    /**
     * message :
     * status : 200
     * data : [{"commentId":1,"content":"这个项目不错","users":{"name":"QQ群","headSculpture":"http://img4q.duitang.com/uploads/item/201311/09/20131109003922_JLyhT.jpeg"},"commentDate":"2016-06-17 00:00:00"}]
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
         * commentId : 1
         * content : 这个项目不错
         * users : {"name":"QQ群","headSculpture":"http://img4q.duitang.com/uploads/item/201311/09/20131109003922_JLyhT.jpeg"}
         * commentDate : 2016-06-17 00:00:00
         */
        private int commentId;
        private String content;
        private UsersBean users;
        private String commentDate;

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

        public UsersBean getUsers() {
            return users;
        }

        public void setUsers(UsersBean users) {
            this.users = users;
        }

        public String getCommentDate() {
            return commentDate;
        }

        public void setCommentDate(String commentDate) {
            this.commentDate = commentDate;
        }

        public static class UsersBean {
            /**
             * name : QQ群
             * headSculpture : http://img4q.duitang.com/uploads/item/201311/09/20131109003922_JLyhT.jpeg
             */
            private String name;
            private String headSculpture;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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
