package com.jinzht.pro.bean;

import java.util.List;

/**
 * 圈子详情
 */
public class CircleDetailBean {

    /**
     * message :
     * status : 200
     * data : {"publicContentId":27,"flag":false,"users":{"name":"陈生珠","userId":1,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg","authentics":[{"city":{"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true},"name":"陈生珠","companyName":"北京金指投","companyAddress":"北京市","position":"职员"}]},"content":"金融[1]  指货币的发行、流通和回笼，贷款的发放和收回，存款的存入和提取，汇兑的往来等经济活动。","publicDate":"2016-06-07 00:00:00","contentimageses":[{"url":"http://imgsrc.baidu.com/forum/pic/item/a8ec8a13632762d01ae1ae27a0ec08fa503dc64f.jpg"},{"url":"http://image.tianjimedia.com/uploadImages/2012/258/J10FD4QC0T5A.jpg"},{"url":"http://www.meiwai.net/uploads/allimg/c150822/1440244203H130-15524.png"},{"url":"http://media.chunyuyisheng.com/media/images/2013/05/15/9173ce6d26d9.jpg"},{"url":"http://www.shifenkafei.com/data/upload/553deb1621af2.jpg"}],"comments":[{"commentId":4,"usersByUserId":{"name":"段辉","userId":4,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"},"usersByAtUserId":{"name":"陈生珠","userId":1,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"},"content":"金融领域奇特现象！","publicDate":1465228800000},{"commentId":3,"usersByUserId":{"name":"陈生珠","userId":1,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"},"usersByAtUserId":null,"content":"话说资本大时代！","publicDate":1465228800000},{"commentId":2,"usersByUserId":{"name":"陈生珠","userId":1,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"},"usersByAtUserId":{"name":"段辉","userId":4,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"},"content":"说的不错","publicDate":1465228800000},{"commentId":1,"usersByUserId":{"name":"陈生珠","userId":1,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"},"usersByAtUserId":{"name":"段辉","userId":4,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"},"content":"很不错，很受益","publicDate":1465228800000},{"commentId":5,"usersByUserId":{"name":"陈生珠","userId":1,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"},"usersByAtUserId":{"name":"段辉","userId":4,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"},"content":"说的不错","publicDate":1465228800000}],"contentprises":[{"priseId":2,"users":{"name":"段辉","userId":4,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"}}],"shareCount":6,"commentCount":5,"priseCount":1}
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
        /**
         * publicContentId : 27
         * flag : false
         * users : {"name":"陈生珠","userId":1,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg","authentics":[{"city":{"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true},"name":"陈生珠","companyName":"北京金指投","companyAddress":"北京市","position":"职员"}]}
         * content : 金融[1]  指货币的发行、流通和回笼，贷款的发放和收回，存款的存入和提取，汇兑的往来等经济活动。
         * publicDate : 2016-06-07 00:00:00
         * contentimageses : [{"url":"http://imgsrc.baidu.com/forum/pic/item/a8ec8a13632762d01ae1ae27a0ec08fa503dc64f.jpg"},{"url":"http://image.tianjimedia.com/uploadImages/2012/258/J10FD4QC0T5A.jpg"},{"url":"http://www.meiwai.net/uploads/allimg/c150822/1440244203H130-15524.png"},{"url":"http://media.chunyuyisheng.com/media/images/2013/05/15/9173ce6d26d9.jpg"},{"url":"http://www.shifenkafei.com/data/upload/553deb1621af2.jpg"}]
         * comments : [{"commentId":4,"usersByUserId":{"name":"段辉","userId":4,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"},"usersByAtUserId":{"name":"陈生珠","userId":1,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"},"content":"金融领域奇特现象！","publicDate":1465228800000},{"commentId":3,"usersByUserId":{"name":"陈生珠","userId":1,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"},"usersByAtUserId":null,"content":"话说资本大时代！","publicDate":1465228800000},{"commentId":2,"usersByUserId":{"name":"陈生珠","userId":1,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"},"usersByAtUserId":{"name":"段辉","userId":4,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"},"content":"说的不错","publicDate":1465228800000},{"commentId":1,"usersByUserId":{"name":"陈生珠","userId":1,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"},"usersByAtUserId":{"name":"段辉","userId":4,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"},"content":"很不错，很受益","publicDate":1465228800000},{"commentId":5,"usersByUserId":{"name":"陈生珠","userId":1,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"},"usersByAtUserId":{"name":"段辉","userId":4,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"},"content":"说的不错","publicDate":1465228800000}]
         * contentprises : [{"priseId":2,"users":{"name":"段辉","userId":4,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"}}]
         * shareCount : 6
         * commentCount : 5
         * priseCount : 1
         */
        private int publicContentId;
        private boolean flag;
        private UsersBean users;
        private String content;
        private String publicDate;
        private int shareCount;
        private int commentCount;
        private int priseCount;
        private List<ContentimagesesBean> contentimageses;
        private List<CommentsBean> comments;
        private List<ContentprisesBean> contentprises;

        public int getPublicContentId() {
            return publicContentId;
        }

        public void setPublicContentId(int publicContentId) {
            this.publicContentId = publicContentId;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
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

        public String getPublicDate() {
            return publicDate;
        }

        public void setPublicDate(String publicDate) {
            this.publicDate = publicDate;
        }

        public int getShareCount() {
            return shareCount;
        }

        public void setShareCount(int shareCount) {
            this.shareCount = shareCount;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public int getPriseCount() {
            return priseCount;
        }

        public void setPriseCount(int priseCount) {
            this.priseCount = priseCount;
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

        public static class UsersBean {
            /**
             * name : 陈生珠
             * userId : 1
             * headSculpture : http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg
             * authentics : [{"city":{"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true},"name":"陈生珠","companyName":"北京金指投","companyAddress":"北京市","position":"职员"}]
             */
            private String name;
            private int userId;
            private String headSculpture;
            private List<AuthenticsBean> authentics;

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

            public List<AuthenticsBean> getAuthentics() {
                return authentics;
            }

            public void setAuthentics(List<AuthenticsBean> authentics) {
                this.authentics = authentics;
            }

            public static class AuthenticsBean {
                /**
                 * city : {"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true}
                 * name : 陈生珠
                 * companyName : 北京金指投
                 * companyAddress : 北京市
                 * position : 职员
                 */
                private CityBean city;
                private String name;
                private String companyName;
                private String companyAddress;
                private String position;

                public CityBean getCity() {
                    return city;
                }

                public void setCity(CityBean city) {
                    this.city = city;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getCompanyName() {
                    return companyName;
                }

                public void setCompanyName(String companyName) {
                    this.companyName = companyName;
                }

                public String getCompanyAddress() {
                    return companyAddress;
                }

                public void setCompanyAddress(String companyAddress) {
                    this.companyAddress = companyAddress;
                }

                public String getPosition() {
                    return position;
                }

                public void setPosition(String position) {
                    this.position = position;
                }

                public static class CityBean {
                    /**
                     * cityId : 6
                     * province : {"provinceId":2,"name":"山西省","isInvlid":true}
                     * name : 柳林
                     * isInvlid : true
                     */
                    private int cityId;
                    private ProvinceBean province;
                    private String name;
                    private boolean isInvlid;

                    public int getCityId() {
                        return cityId;
                    }

                    public void setCityId(int cityId) {
                        this.cityId = cityId;
                    }

                    public ProvinceBean getProvince() {
                        return province;
                    }

                    public void setProvince(ProvinceBean province) {
                        this.province = province;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public boolean isIsInvlid() {
                        return isInvlid;
                    }

                    public void setIsInvlid(boolean isInvlid) {
                        this.isInvlid = isInvlid;
                    }

                    public static class ProvinceBean {
                        /**
                         * provinceId : 2
                         * name : 山西省
                         * isInvlid : true
                         */
                        private int provinceId;
                        private String name;
                        private boolean isInvlid;

                        public int getProvinceId() {
                            return provinceId;
                        }

                        public void setProvinceId(int provinceId) {
                            this.provinceId = provinceId;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                        public boolean isIsInvlid() {
                            return isInvlid;
                        }

                        public void setIsInvlid(boolean isInvlid) {
                            this.isInvlid = isInvlid;
                        }
                    }
                }
            }
        }

        public static class ContentimagesesBean {
            /**
             * url : http://imgsrc.baidu.com/forum/pic/item/a8ec8a13632762d01ae1ae27a0ec08fa503dc64f.jpg
             */
            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class CommentsBean {
            /**
             * commentId : 4
             * usersByUserId : {"name":"段辉","userId":4,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"}
             * usersByAtUserId : {"name":"陈生珠","userId":1,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"}
             * content : 金融领域奇特现象！
             * publicDate : 1465228800000
             */
            private int commentId;
            private UsersByUserIdBean usersByUserId;
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
                /**
                 * name : 段辉
                 * userId : 4
                 * headSculpture : http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg
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

            public static class UsersByAtUserIdBean {
                /**
                 * name : 陈生珠
                 * userId : 1
                 * headSculpture : http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg
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

            @Override
            public String toString() {
                return "CommentsBean{" +
                        "commentId=" + commentId +
                        ", usersByUserId=" + usersByUserId +
                        ", usersByAtUserId=" + usersByAtUserId +
                        ", content='" + content + '\'' +
                        ", publicDate='" + publicDate + '\'' +
                        '}';
            }
        }

        public static class ContentprisesBean {
            /**
             * priseId : 2
             * users : {"name":"段辉","userId":4,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg"}
             */
            private int priseId;
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
                /**
                 * name : 段辉
                 * userId : 4
                 * headSculpture : http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg
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
}
