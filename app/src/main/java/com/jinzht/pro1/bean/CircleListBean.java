package com.jinzht.pro1.bean;

import java.util.List;

/**
 * 圈子列表
 */
public class CircleListBean {

    /**
     * message :
     * status : 200
     * data : [{"publicContentId":27,"flag":false,"users":{"name":"陈生珠","userId":1,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg","authentics":[{"city":{"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true},"name":"陈生珠","companyName":"北京金指投","companyAddress":"北京市","position":"职员"}]},"content":"金融[1]  指货币的发行、流通和回笼，贷款的发放和收回，存款的存入和提取，汇兑的往来等经济活动。","publicDate":"2016-06-07 00:00:00","contentimageses":[{"url":"http://imgsrc.baidu.com/forum/pic/item/a8ec8a13632762d01ae1ae27a0ec08fa503dc64f.jpg"},{"url":"http://image.tianjimedia.com/uploadImages/2012/258/J10FD4QC0T5A.jpg"},{"url":"http://www.meiwai.net/uploads/allimg/c150822/1440244203H130-15524.png"},{"url":"http://media.chunyuyisheng.com/media/images/2013/05/15/9173ce6d26d9.jpg"},{"url":"http://www.shifenkafei.com/data/upload/553deb1621af2.jpg"}],"shareCount":6,"commentCount":2,"priseCount":1}]
     */

    private String message;
    private int status;
    /**
     * publicContentId : 27
     * flag : false
     * users : {"name":"陈生珠","userId":1,"headSculpture":"http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg","authentics":[{"city":{"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true},"name":"陈生珠","companyName":"北京金指投","companyAddress":"北京市","position":"职员"}]}
     * content : 金融[1]  指货币的发行、流通和回笼，贷款的发放和收回，存款的存入和提取，汇兑的往来等经济活动。
     * publicDate : 2016-06-07 00:00:00
     * contentimageses : [{"url":"http://imgsrc.baidu.com/forum/pic/item/a8ec8a13632762d01ae1ae27a0ec08fa503dc64f.jpg"},{"url":"http://image.tianjimedia.com/uploadImages/2012/258/J10FD4QC0T5A.jpg"},{"url":"http://www.meiwai.net/uploads/allimg/c150822/1440244203H130-15524.png"},{"url":"http://media.chunyuyisheng.com/media/images/2013/05/15/9173ce6d26d9.jpg"},{"url":"http://www.shifenkafei.com/data/upload/553deb1621af2.jpg"}]
     * shareCount : 6
     * commentCount : 2
     * priseCount : 1
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
        private boolean flag;
        /**
         * name : 陈生珠
         * userId : 1
         * headSculpture : http://www.jinzht.com:8080/jinzht/upload/jinzht_user_1.jpg
         * authentics : [{"city":{"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true},"name":"陈生珠","companyName":"北京金指投","companyAddress":"北京市","position":"职员"}]
         */

        private UsersBean users;
        private String content;
        private String publicDate;
        private int shareCount;
        private int commentCount;
        private int priseCount;
        /**
         * url : http://imgsrc.baidu.com/forum/pic/item/a8ec8a13632762d01ae1ae27a0ec08fa503dc64f.jpg
         */

        private List<ContentimagesesBean> contentimageses;

        @Override
        public String toString() {
            return "DataBean{" +
                    "publicContentId=" + publicContentId +
                    ", flag=" + flag +
                    ", users=" + users +
                    ", content='" + content + '\'' +
                    ", publicDate='" + publicDate + '\'' +
                    ", shareCount=" + shareCount +
                    ", commentCount=" + commentCount +
                    ", priseCount=" + priseCount +
                    ", contentimageses=" + contentimageses +
                    '}';
        }

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

        public static class UsersBean {
            private String name;
            private int userId;
            private String headSculpture;
            /**
             * city : {"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true}
             * name : 陈生珠
             * companyName : 北京金指投
             * companyAddress : 北京市
             * position : 职员
             */

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
                 * cityId : 6
                 * province : {"provinceId":2,"name":"山西省","isInvlid":true}
                 * name : 柳林
                 * isInvlid : true
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
                    private int cityId;
                    /**
                     * provinceId : 2
                     * name : 山西省
                     * isInvlid : true
                     */

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
            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
