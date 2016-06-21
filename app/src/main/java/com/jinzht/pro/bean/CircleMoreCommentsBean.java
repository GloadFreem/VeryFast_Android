package com.jinzht.pro.bean;

import java.util.List;

/**
 * 加载更多评论
 */
public class CircleMoreCommentsBean {

    /**
     * message :
     * status : 200
     * data : [{"commentId":2,"usersByUserId":{"name":"陈生珠","userId":1,"headSculpture":"http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=4f582c74b4fb43161a4a727e15946a15/72f082025aafa40f39ce8262ad64034f78f01900.jpg"},"usersByAtUserId":{"name":"段辉","userId":4,"headSculpture":"http://e.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=75aaa91fa444ad342eea8f83e59220c2/0bd162d9f2d3572cf556972e8f13632763d0c388.jpg"},"content":"说的不错","publicDate":1465228800000},{"commentId":3,"usersByUserId":{"name":"陈生珠","userId":1,"headSculpture":"http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=4f582c74b4fb43161a4a727e15946a15/72f082025aafa40f39ce8262ad64034f78f01900.jpg"},"usersByAtUserId":null,"content":"话说资本大时代！","publicDate":1465228800000},{"commentId":4,"usersByUserId":{"name":"段辉","userId":4,"headSculpture":"http://e.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=75aaa91fa444ad342eea8f83e59220c2/0bd162d9f2d3572cf556972e8f13632763d0c388.jpg"},"usersByAtUserId":{"name":"陈生珠","userId":1,"headSculpture":"http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=4f582c74b4fb43161a4a727e15946a15/72f082025aafa40f39ce8262ad64034f78f01900.jpg"},"content":"金融领域奇特现象！","publicDate":1465228800000},{"commentId":5,"usersByUserId":{"name":"陈生珠","userId":1,"headSculpture":"http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=4f582c74b4fb43161a4a727e15946a15/72f082025aafa40f39ce8262ad64034f78f01900.jpg"},"usersByAtUserId":{"name":"段辉","userId":4,"headSculpture":"http://e.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=75aaa91fa444ad342eea8f83e59220c2/0bd162d9f2d3572cf556972e8f13632763d0c388.jpg"},"content":"说的不错","publicDate":1465228800000}]
     */

    private String message;
    private int status;
    /**
     * commentId : 2
     * usersByUserId : {"name":"陈生珠","userId":1,"headSculpture":"http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=4f582c74b4fb43161a4a727e15946a15/72f082025aafa40f39ce8262ad64034f78f01900.jpg"}
     * usersByAtUserId : {"name":"段辉","userId":4,"headSculpture":"http://e.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=75aaa91fa444ad342eea8f83e59220c2/0bd162d9f2d3572cf556972e8f13632763d0c388.jpg"}
     * content : 说的不错
     * publicDate : 1465228800000
     */

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
        private int commentId;
        /**
         * name : 陈生珠
         * userId : 1
         * headSculpture : http://c.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=4f582c74b4fb43161a4a727e15946a15/72f082025aafa40f39ce8262ad64034f78f01900.jpg
         */

        private UsersByUserIdBean usersByUserId;
        /**
         * name : 段辉
         * userId : 4
         * headSculpture : http://e.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=75aaa91fa444ad342eea8f83e59220c2/0bd162d9f2d3572cf556972e8f13632763d0c388.jpg
         */

        private UsersByAtUserIdBean usersByAtUserId;
        private String content;
        private String publicDate;

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

        public String getPublicDate() {
            return publicDate;
        }

        public void setPublicDate(String publicDate) {
            this.publicDate = publicDate;
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
