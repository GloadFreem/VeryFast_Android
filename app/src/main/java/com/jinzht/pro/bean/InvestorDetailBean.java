package com.jinzht.pro.bean;

import java.util.List;

/**
 * 投资人详情
 */
public class InvestorDetailBean {

    /**
     * collectCount : 100
     * areas : ["互联网，金融，投资"]
     * collected : true
     * user : {"name":"陈生珠","userId":4670,"headSculpture":"http://ww1.sinaimg.cn/crop.3.45.1919.1919.1024/6b805731jw1em0hze051hj21hk1isn5k.jpg","extUserId":645,"authentics":[{"city":{"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true},"name":"陈生珠","companyName":"天虹基金","companyAddress":"陕西 西安","position":"哈韩","introduce":"运营","industoryArea":"互联网，金融，投资"}]}
     * commited : false
     */

    private DataBean data;
    private String message;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public static class DataBean {
        /**
         * data : {"collectCount":100,"areas":["互联网，金融，投资"],"collected":true,"user":{"name":"陈生珠","userId":4670,"headSculpture":"http://ww1.sinaimg.cn/crop.3.45.1919.1919.1024/6b805731jw1em0hze051hj21hk1isn5k.jpg","extUserId":645,"authentics":[{"city":{"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true},"name":"陈生珠","companyName":"天虹基金","companyAddress":"陕西 西安","position":"哈韩","introduce":"运营","industoryArea":"互联网，金融，投资"}]},"commited":false}
         * message :
         * status : 200
         */
        private int collectCount;
        private boolean collected;
        private UserBean user;
        private boolean commited;
        private List<String> areas;

        public int getCollectCount() {
            return collectCount;
        }

        public void setCollectCount(int collectCount) {
            this.collectCount = collectCount;
        }

        public boolean isCollected() {
            return collected;
        }

        public void setCollected(boolean collected) {
            this.collected = collected;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public boolean isCommited() {
            return commited;
        }

        public void setCommited(boolean commited) {
            this.commited = commited;
        }

        public List<String> getAreas() {
            return areas;
        }

        public void setAreas(List<String> areas) {
            this.areas = areas;
        }

        public static class UserBean {
            /**
             * name : 陈生珠
             * userId : 4670
             * headSculpture : http://ww1.sinaimg.cn/crop.3.45.1919.1919.1024/6b805731jw1em0hze051hj21hk1isn5k.jpg
             * extUserId : 645
             * authentics : [{"city":{"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true},"name":"陈生珠","companyName":"天虹基金","companyAddress":"陕西 西安","position":"哈韩","introduce":"运营","industoryArea":"互联网，金融，投资"}]
             */
            private String name;
            private int userId;
            private String headSculpture;
            private int extUserId;
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

            public int getExtUserId() {
                return extUserId;
            }

            public void setExtUserId(int extUserId) {
                this.extUserId = extUserId;
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
                 * companyName : 天虹基金
                 * companyAddress : 陕西 西安
                 * position : 哈韩
                 * introduce : 运营
                 * companyIntroduce : 公司是一艘大船
                 * industoryArea : 互联网，金融，投资
                 */
                private CityBean city;
                private String name;
                private String companyName;
                private String companyAddress;
                private String position;
                private String introduce;
                private String companyIntroduce;
                private String industoryArea;

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

                public String getIntroduce() {
                    return introduce;
                }

                public void setIntroduce(String introduce) {
                    this.introduce = introduce;
                }

                public String getCompanyIntroduce() {
                    return companyIntroduce;
                }

                public void setCompanyIntroduce(String companyIntroduce) {
                    this.companyIntroduce = companyIntroduce;
                }

                public String getIndustoryArea() {
                    return industoryArea;
                }

                public void setIndustoryArea(String industoryArea) {
                    this.industoryArea = industoryArea;
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
    }
}