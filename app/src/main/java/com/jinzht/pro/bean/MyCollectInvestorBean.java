package com.jinzht.pro.bean;

import java.util.List;

/**
 * 关注的投资人列表
 */
public class MyCollectInvestorBean {

    /**
     * data : [{"collectId":21,"usersByUserCollectedId":{"name":"陈昌容","userId":3553,"headSculpture":"http://www.jinzht.com/static/app/img/icon.jpg","authentics":[{"authId":3668,"identiytype":{"identiyTypeId":2,"name":"个人投资者"},"city":{"cityId":5,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"太原","isInvlid":true},"name":"陈昌容","companyName":"四川渝惠捷保网络技术有限公司","companyAddress":"四川 成都","position":"董事长","introduce":"","industoryArea":"互联网，金融，投资"}]},"collectedDate":"2016-06-27 10:24:55"},{"collectId":22,"usersByUserCollectedId":{"name":"张安定","userId":3573,"headSculpture":"http://www.jinzht.com/static/app/img/icon.jpg","authentics":[{"authId":3688,"identiytype":{"identiyTypeId":2,"name":"个人投资者"},"city":{"cityId":5,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"太原","isInvlid":true},"name":"张安定","companyName":"郑州剑鱼食品有限公司","companyAddress":"北京 通州","position":"总经理","introduce":"我是郑州剑鱼食品的张安定","industoryArea":"互联网，金融，投资"}]},"collectedDate":"2016-06-27 10:24:57"},{"collectId":23,"usersByUserCollectedId":{"name":"陈勇","userId":3595,"headSculpture":"http://www.jinzht.com/static/app/img/icon.jpg","authentics":[{"authId":3710,"identiytype":{"identiyTypeId":2,"name":"个人投资者"},"city":{"cityId":5,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"太原","isInvlid":true},"name":"陈勇","companyName":"南通大通宝富风机有限公","companyAddress":"江苏 南通","position":"副主任工程师","introduce":"","industoryArea":"互联网，金融，投资"}]},"collectedDate":"2016-06-27 10:28:11"},{"collectId":24,"usersByUserCollectedId":{"name":"黄毅","userId":3608,"headSculpture":"http://www.jinzht.com/static/app/img/icon.jpg","authentics":[{"authId":3723,"identiytype":{"identiyTypeId":2,"name":"个人投资者"},"city":{"cityId":5,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"太原","isInvlid":true},"name":"黄毅","companyName":"金指投","companyAddress":"陕西 西安","position":"运营","introduce":"","industoryArea":"互联网，金融，投资"}]},"collectedDate":"2016-06-27 10:28:14"},{"collectId":25,"usersByUserCollectedId":{"name":"戚亚平","userId":3619,"headSculpture":"http://www.jinzht.com/static/app/img/icon.jpg","authentics":[{"authId":3734,"identiytype":{"identiyTypeId":2,"name":"个人投资者"},"city":{"cityId":5,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"太原","isInvlid":true},"name":"戚亚平","companyName":"西安双誉物资有限公司","companyAddress":"陕西 西安","position":"总经理","introduce":"","industoryArea":"互联网，金融，投资"}]},"collectedDate":"2016-06-27 10:28:16"},{"collectId":26,"usersByUserCollectedId":{"name":"张自治","userId":3623,"headSculpture":"http://www.jinzht.com/static/app/img/icon.jpg","authentics":[{"authId":3738,"identiytype":{"identiyTypeId":2,"name":"个人投资者"},"city":{"cityId":5,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"太原","isInvlid":true},"name":"张自治","companyName":"西安智生安数码科技有限","companyAddress":"陕西 西安","position":"总经理","introduce":"","industoryArea":"互联网，金融，投资"}]},"collectedDate":"2016-06-27 10:28:17"},{"collectId":27,"usersByUserCollectedId":{"name":"赵引","userId":3627,"headSculpture":"http://www.jinzht.com:80/media/user/photo/2016/05/1f3442ce55e141b0932c69c653cf69ed.jpg","authentics":[{"authId":3742,"identiytype":{"identiyTypeId":2,"name":"个人投资者"},"city":{"cityId":5,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"太原","isInvlid":true},"name":"赵引","companyName":"善合集团","companyAddress":"陕西 西安","position":"营销总监","introduce":"","industoryArea":"互联网，金融，投资"}]},"collectedDate":"2016-06-27 10:28:18"},{"collectId":28,"usersByUserCollectedId":{"name":"周锋","userId":3638,"headSculpture":"http://www.jinzht.com/static/app/img/icon.jpg","authentics":[{"authId":3753,"identiytype":{"identiyTypeId":2,"name":"个人投资者"},"city":{"cityId":5,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"太原","isInvlid":true},"name":"周锋","companyName":"新疆昌吉隆源亿康药业","companyAddress":"新疆 昌吉","position":"总经理","introduce":"","industoryArea":"互联网，金融，投资"}]},"collectedDate":"2016-06-27 10:28:19"},{"collectId":29,"usersByUserCollectedId":{"name":"陶鹏","userId":3641,"headSculpture":"http://www.jinzht.com/static/app/img/icon.jpg","authentics":[{"authId":3756,"identiytype":{"identiyTypeId":2,"name":"个人投资者"},"city":{"cityId":5,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"太原","isInvlid":true},"name":"陶鹏","companyName":"创投中国","companyAddress":"广东 深圳","position":"投资经理","introduce":"","industoryArea":"互联网，金融，投资"}]},"collectedDate":"2016-06-27 10:28:19"}]
     * message :
     * status : 200
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
         * collectId : 21
         * usersByUserCollectedId : {"name":"陈昌容","userId":3553,"headSculpture":"http://www.jinzht.com/static/app/img/icon.jpg","authentics":[{"authId":3668,"identiytype":{"identiyTypeId":2,"name":"个人投资者"},"city":{"cityId":5,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"太原","isInvlid":true},"name":"陈昌容","companyName":"四川渝惠捷保网络技术有限公司","companyAddress":"四川 成都","position":"董事长","introduce":"","industoryArea":"互联网，金融，投资"}]}
         * collectedDate : 2016-06-27 10:24:55
         */
        private int collectId;
        private UsersByUserCollectedIdBean usersByUserCollectedId;
        private String collectedDate;

        public int getCollectId() {
            return collectId;
        }

        public void setCollectId(int collectId) {
            this.collectId = collectId;
        }

        public UsersByUserCollectedIdBean getUsersByUserCollectedId() {
            return usersByUserCollectedId;
        }

        public void setUsersByUserCollectedId(UsersByUserCollectedIdBean usersByUserCollectedId) {
            this.usersByUserCollectedId = usersByUserCollectedId;
        }

        public String getCollectedDate() {
            return collectedDate;
        }

        public void setCollectedDate(String collectedDate) {
            this.collectedDate = collectedDate;
        }

        public static class UsersByUserCollectedIdBean {
            /**
             * name : 陈昌容
             * userId : 3553
             * headSculpture : http://www.jinzht.com/static/app/img/icon.jpg
             * authentics : [{"authId":3668,"identiytype":{"identiyTypeId":2,"name":"个人投资者"},"city":{"cityId":5,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"太原","isInvlid":true},"name":"陈昌容","companyName":"四川渝惠捷保网络技术有限公司","companyAddress":"四川 成都","position":"董事长","introduce":"","industoryArea":"互联网，金融，投资"}]
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
                 * authId : 3668
                 * identiytype : {"identiyTypeId":2,"name":"个人投资者"}
                 * city : {"cityId":5,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"太原","isInvlid":true}
                 * name : 陈昌容
                 * companyName : 四川渝惠捷保网络技术有限公司
                 * companyAddress : 四川 成都
                 * position : 董事长
                 * introduce :
                 * industoryArea : 互联网，金融，投资
                 */
                private int authId;
                private IdentiytypeBean identiytype;
                private CityBean city;
                private String name;
                private String companyName;
                private String companyAddress;
                private String position;
                private String introduce;
                private String industoryArea;

                public int getAuthId() {
                    return authId;
                }

                public void setAuthId(int authId) {
                    this.authId = authId;
                }

                public IdentiytypeBean getIdentiytype() {
                    return identiytype;
                }

                public void setIdentiytype(IdentiytypeBean identiytype) {
                    this.identiytype = identiytype;
                }

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

                public String getIndustoryArea() {
                    return industoryArea;
                }

                public void setIndustoryArea(String industoryArea) {
                    this.industoryArea = industoryArea;
                }

                public static class IdentiytypeBean {
                    /**
                     * identiyTypeId : 2
                     * name : 个人投资者
                     */
                    private int identiyTypeId;
                    private String name;

                    public int getIdentiyTypeId() {
                        return identiyTypeId;
                    }

                    public void setIdentiyTypeId(int identiyTypeId) {
                        this.identiyTypeId = identiyTypeId;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }
                }

                public static class CityBean {
                    /**
                     * cityId : 5
                     * province : {"provinceId":2,"name":"山西省","isInvlid":true}
                     * name : 太原
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
