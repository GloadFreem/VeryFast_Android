package com.jinzht.pro1.bean;

import java.util.List;

/**
 * 圈子列表
 */
public class CircleListBean {

    /**
     * message : 评论成功!
     * status : 200
     * data : [{"publicContentId":27,"content":"这是一篇圈子文章","contentimageses":[{"url":"upload/images/2016060301.jpg"}],"comments":[{"commentId":1,"usersByUserId":{"name":"","userId":1,"headSculpture":"upload/jinzht_user_1.jpg"},"usersByAtUserId":null,"content":"很不错，很受益"}],"contentprises":[{"priseId":13,"users":{"name":"","userId":1,"headSculpture":"upload/jinzht_user_1.jpg"}},{"priseId":2,"users":{"name":"","userId":4,"wechatId":"11833245"}}]}]
     */

    private String message;
    private int status;
    /**
     * publicContentId : 27
     * content : 这是一篇圈子文章
     * contentimageses : [{"url":"upload/images/2016060301.jpg"}]
     * comments : [{"commentId":1,"usersByUserId":{"name":"","userId":1,"headSculpture":"upload/jinzht_user_1.jpg"},"usersByAtUserId":null,"content":"很不错，很受益"}]
     * contentprises : [{"priseId":13,"users":{"name":"","userId":1,"headSculpture":"upload/jinzht_user_1.jpg"}},{"priseId":2,"users":{"name":"","userId":4,"wechatId":"11833245"}}]
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
        private int publicContentId;
        private String content;
        /**
         * url : upload/images/2016060301.jpg
         */

        private List<ContentimagesesBean> contentimageses;
        /**
         * commentId : 1
         * usersByUserId : {"name":"","userId":1,"headSculpture":"upload/jinzht_user_1.jpg"}
         * usersByAtUserId : null
         * content : 很不错，很受益
         */

        private List<CommentsBean> comments;
        /**
         * priseId : 13
         * users : {"name":"","userId":1,"headSculpture":"upload/jinzht_user_1.jpg"}
         */

        private List<ContentprisesBean> contentprises;

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

        public List<ContentimagesesBean> getContentimageses() {
            return contentimageses;
        }

        public void setContentimageses(List<ContentimagesesBean> contentimageses) {
            this.contentimageses = contentimageses;
        }

        public List<CommentsBean> getComments() {
            return comments;
        }

        public void setComments(List<CommentsBean> comments) {
            this.comments = comments;
        }

        public List<ContentprisesBean> getContentprises() {
            return contentprises;
        }

        public void setContentprises(List<ContentprisesBean> contentprises) {
            this.contentprises = contentprises;
        }

        public static class ContentimagesesBean {
            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class CommentsBean {
            private int commentId;
            /**
             * name :
             * userId : 1
             * headSculpture : upload/jinzht_user_1.jpg
             */

            private UsersByUserIdBean usersByUserId;
            private Object usersByAtUserId;
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

            public Object getUsersByAtUserId() {
                return usersByAtUserId;
            }

            public void setUsersByAtUserId(Object usersByAtUserId) {
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
        }

        public static class ContentprisesBean {
            private int priseId;
            /**
             * name :
             * userId : 1
             * headSculpture : upload/jinzht_user_1.jpg
             */

            private UsersBean users;

            public int getPriseId() {
                return priseId;
            }

            public void setPriseId(int priseId) {
                this.priseId = priseId;
            }

            public UsersBean getUsers() {
                return users;
            }

            public void setUsers(UsersBean users) {
                this.users = users;
            }

            public static class UsersBean {
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
}
