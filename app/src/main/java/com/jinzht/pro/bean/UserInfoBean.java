package com.jinzht.pro.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息
 */
public class UserInfoBean {
    /**
     * message :
     * status : 200
     * data : {"headSculpture":"http://img4q.duitang.com/uploads/item/201311/09/20131109003922_JLyhT.jpeg","authentics":[{"authId":110,"identiytype":{"identiyTypeId":3,"name":"机构投资人"},"authenticstatus":{"statusId":6,"name":"未认证"},"city":{"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true},"name":"QQ群","identiyCarA":"http://192.168.5.108:8080/jinzht/upload/identityImages/jinzht_user_identiy_a_1.jpg","identiyCarB":"http://192.168.5.107:8080/jinzht/upload/identityImages/jinzht_user_identiy_b_1.jpg","identiyCarNo":"123456789123456789","companyName":"梦工厂","companyAddress":"陕西西安","position":"职业经理人","buinessLicence":"http://192.168.5.102:8080/jinzht/upload/identityImages/jinzht_user_identiy_buiness_1.jpg","buinessLicenceNo":"123456","introduce":"","companyIntroduce":"","industoryArea":"1,2,3"}]}
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

    public static class DataBean implements Serializable {
        /**
         * userId : 1
         * extUserId: 645
         * telephone : 13636369898
         * headSculpture : http://img4q.duitang.com/uploads/item/201311/09/20131109003922_JLyhT.jpeg
         * authentics : [{"authId":110,"identiytype":{"identiyTypeId":3,"name":"机构投资人"},"authenticstatus":{"statusId":6,"name":"未认证"},"city":{"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true},"name":"QQ群","identiyCarA":"http://192.168.5.108:8080/jinzht/upload/identityImages/jinzht_user_identiy_a_1.jpg","identiyCarB":"http://192.168.5.107:8080/jinzht/upload/identityImages/jinzht_user_identiy_b_1.jpg","identiyCarNo":"123456789123456789","companyName":"梦工厂","companyAddress":"陕西西安","position":"职业经理人","buinessLicence":"http://192.168.5.102:8080/jinzht/upload/identityImages/jinzht_user_identiy_buiness_1.jpg","buinessLicenceNo":"123456","introduce":"","companyIntroduce":"","industoryArea":"1,2,3"}]
         */
        private int userId;
        private int extUserId;
        private String telephone;
        private String headSculpture;// 头像
        private List<AuthenticsBean> authentics;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getExtUserId() {
            return extUserId;
        }

        public void setExtUserId(int extUserId) {
            this.extUserId = extUserId;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
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

        public static class AuthenticsBean implements Serializable {
            /**
             * authId : 110
             * identiytype : {"identiyTypeId":3,"name":"机构投资人"}
             * authenticstatus : {"statusId":6,"name":"未认证"}
             * city : {"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true}
             * name : QQ群
             * identiyCarA : http://192.168.5.108:8080/jinzht/upload/identityImages/jinzht_user_identiy_a_1.jpg
             * identiyCarB : http://192.168.5.107:8080/jinzht/upload/identityImages/jinzht_user_identiy_b_1.jpg
             * identiyCarNo : 123456789123456789
             * companyName : 梦工厂
             * companyAddress : 陕西西安
             * position : 职业经理人
             * buinessLicence : http://192.168.5.102:8080/jinzht/upload/identityImages/jinzht_user_identiy_buiness_1.jpg
             * buinessLicenceNo : 123456
             * introduce :
             * companyIntroduce :
             * industoryArea : 1,2,3
             */
            private int authId;// 认证id
            private IdentiytypeBean identiytype;// 用户类型
            private AuthenticstatusBean authenticstatus;// 是否认证
            private CityBean city;
            private String name;
            private String identiyCarA;
            private String identiyCarB;
            private String identiyCarNo;
            private String companyName;
            private String companyAddress;
            private String position;
            private String buinessLicence;// 营业执照照片
            private String buinessLicenceNo;// 营业执照号
            private String introduce;// 个人介绍
            private String companyIntroduce;
            private String industoryArea;// 投资领域id

            @Override
            public String toString() {
                return "AuthenticsBean{" +
                        "introduce='" + introduce + '\'' +
                        ", authId=" + authId +
                        ", name='" + name + '\'' +
                        ", identiyCarA='" + identiyCarA + '\'' +
                        ", identiyCarB='" + identiyCarB + '\'' +
                        ", identiyCarNo='" + identiyCarNo + '\'' +
                        ", companyName='" + companyName + '\'' +
                        ", companyAddress='" + companyAddress + '\'' +
                        ", position='" + position + '\'' +
                        ", buinessLicence='" + buinessLicence + '\'' +
                        ", buinessLicenceNo='" + buinessLicenceNo + '\'' +
                        ", companyIntroduce='" + companyIntroduce + '\'' +
                        ", industoryArea='" + industoryArea + '\'' +
                        '}';
            }

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

            public AuthenticstatusBean getAuthenticstatus() {
                return authenticstatus;
            }

            public void setAuthenticstatus(AuthenticstatusBean authenticstatus) {
                this.authenticstatus = authenticstatus;
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

            public String getIdentiyCarA() {
                return identiyCarA;
            }

            public void setIdentiyCarA(String identiyCarA) {
                this.identiyCarA = identiyCarA;
            }

            public String getIdentiyCarB() {
                return identiyCarB;
            }

            public void setIdentiyCarB(String identiyCarB) {
                this.identiyCarB = identiyCarB;
            }

            public String getIdentiyCarNo() {
                return identiyCarNo;
            }

            public void setIdentiyCarNo(String identiyCarNo) {
                this.identiyCarNo = identiyCarNo;
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

            public String getBuinessLicence() {
                return buinessLicence;
            }

            public void setBuinessLicence(String buinessLicence) {
                this.buinessLicence = buinessLicence;
            }

            public String getBuinessLicenceNo() {
                return buinessLicenceNo;
            }

            public void setBuinessLicenceNo(String buinessLicenceNo) {
                this.buinessLicenceNo = buinessLicenceNo;
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

            public static class IdentiytypeBean implements Serializable {
                /**
                 * identiyTypeId : 3
                 * name : 机构投资人
                 */
                private int identiyTypeId;
                private String name;

                @Override
                public String toString() {
                    return "IdentiytypeBean{" +
                            "identiyTypeId=" + identiyTypeId +
                            ", name='" + name + '\'' +
                            '}';
                }

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

            public static class AuthenticstatusBean implements Serializable {
                /**
                 * statusId : 6
                 * name : 未认证
                 */
                private int statusId;
                private String name;

                public int getStatusId() {
                    return statusId;
                }

                public void setStatusId(int statusId) {
                    this.statusId = statusId;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class CityBean implements Serializable {
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

                public static class ProvinceBean implements Serializable {
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
