package com.jinzht.pro.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 投资人、智囊团列表
 */
public class InvestorListBean {

    /**
     * message :
     * status : 200
     * data : [{"collectCount":1000,"collected":true,"areas":["电影","旅游","体育"],"user":{"name":"段辉","userId":4,"headSculpture":"http://e.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=75aaa91fa444ad342eea8f83e59220c2/0bd162d9f2d3572cf556972e8f13632763d0c388.jpg","wechatId":"11833245","authentics":[{"city":{"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true},"name":"段辉","companyName":"北京金指投","companyAddress":"北京市","position":"职员","introduce":"最大的敌人是自己","companyIntroduce":"公司是一艘大船","industoryArea":"1,2,3"}]},"commited":true}]
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

    public static class DataBean implements Serializable {
        @Override
        public String toString() {
            return "DataBean{" +
                    "collectCount=" + collectCount +
                    ", collected=" + collected +
                    ", user=" + user +
                    ", commited=" + commited +
                    ", areas=" + areas +
                    '}';
        }

        /**
         * collectCount : 1000
         * collected : true
         * areas : ["电影","旅游","体育"]
         * user : {"name":"段辉","userId":4,"headSculpture":"http://e.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=75aaa91fa444ad342eea8f83e59220c2/0bd162d9f2d3572cf556972e8f13632763d0c388.jpg","wechatId":"11833245","authentics":[{"city":{"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true},"name":"段辉","companyName":"北京金指投","companyAddress":"北京市","position":"职员","introduce":"最大的敌人是自己","companyIntroduce":"公司是一艘大船","industoryArea":"1,2,3"}]}
         * commited : true
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

        public static class UserBean implements Serializable {
            /**
             * name : 段辉
             * userId : 4
             * headSculpture : http://e.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=75aaa91fa444ad342eea8f83e59220c2/0bd162d9f2d3572cf556972e8f13632763d0c388.jpg
             * wechatId : 11833245
             * authentics : [{"city":{"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true},"name":"段辉","companyName":"北京金指投","companyAddress":"北京市","position":"职员","introduce":"最大的敌人是自己","companyIntroduce":"公司是一艘大船","industoryArea":"1,2,3"}]
             */
            private String name;
            private int userId;
            private String headSculpture;
            private String wechatId;
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

            public String getWechatId() {
                return wechatId;
            }

            public void setWechatId(String wechatId) {
                this.wechatId = wechatId;
            }

            public List<AuthenticsBean> getAuthentics() {
                return authentics;
            }

            public void setAuthentics(List<AuthenticsBean> authentics) {
                this.authentics = authentics;
            }

            public static class AuthenticsBean implements Serializable{
                /**
                 * city : {"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true}
                 * name : 段辉
                 * companyName : 北京金指投
                 * companyAddress : 北京市
                 * position : 职员
                 * introduce : 最大的敌人是自己
                 * companyIntroduce : 公司是一艘大船
                 * industoryArea : 1,2,3
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

                public static class CityBean implements Serializable{
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

                    public static class ProvinceBean implements Serializable{
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