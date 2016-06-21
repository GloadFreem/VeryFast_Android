package com.jinzht.pro.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 投资机构列表
 */
public class InvestorgListBean {
    /**
     * message :
     * status : 200
     * data : {"founddations":[{"foundationId":1,"name":"聚英天使企业家领头基金","image":"http://f.hiphotos.baidu.com/image/h%3D200/sign=8387106fbe014a909e3e41bd99763971/472309f790529822ef9e961ed0ca7bcb0b46d4ba.jpg","content":"两国总理认真听取了双方外交、经济、工业、财金、教育、科技、交通、环保、住建、农业、商务、司法、社保、医疗等26个部门负责人的汇报。双方对中德合作取得的进展感到满意，并就深化各领域合作达成一系列新的共识。","url":"http://f.hiphotos.baidu.com/image/h%3D200/sign=8387106fbe014a909e3e41bd99763971/472309f790529822ef9e961ed0ca7bcb0b46d4ba.jpg"}],"investors":[{"collectCount":0,"collected":false,"areas":["电影","旅游","体育"],"user":{"name":"QQ群","userId":1,"headSculpture":"http://img4q.duitang.com/uploads/item/201311/09/20131109003922_JLyhT.jpeg","authentics":[{"city":{"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true},"name":"QQ群","companyName":"梦工厂","companyAddress":"陕西西安","position":"职业经理人","introduce":"","companyIntroduce":"","industoryArea":"1,2,3"}]},"commited":false}]}
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

        private List<FounddationsBean> founddations;
        private List<InvestorsBean> investors;

        public List<FounddationsBean> getFounddations() {
            return founddations;
        }

        public void setFounddations(List<FounddationsBean> founddations) {
            this.founddations = founddations;
        }

        public List<InvestorsBean> getInvestors() {
            return investors;
        }

        public void setInvestors(List<InvestorsBean> investors) {
            this.investors = investors;
        }

        public static class FounddationsBean {
            /**
             * foundationId : 1
             * name : 聚英天使企业家领头基金
             * image : http://f.hiphotos.baidu.com/image/h%3D200/sign=8387106fbe014a909e3e41bd99763971/472309f790529822ef9e961ed0ca7bcb0b46d4ba.jpg
             * content : 两国总理认真听取了双方外交、经济、工业、财金、教育、科技、交通、环保、住建、农业、商务、司法、社保、医疗等26个部门负责人的汇报。双方对中德合作取得的进展感到满意，并就深化各领域合作达成一系列新的共识。
             * url : http://f.hiphotos.baidu.com/image/h%3D200/sign=8387106fbe014a909e3e41bd99763971/472309f790529822ef9e961ed0ca7bcb0b46d4ba.jpg
             */
            private int foundationId;
            private String name;
            private String image;
            private String content;
            private String url;

            public int getFoundationId() {
                return foundationId;
            }

            public void setFoundationId(int foundationId) {
                this.foundationId = foundationId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class InvestorsBean implements Serializable{
            /**
             * collectCount : 0
             * collected : false
             * areas : ["电影","旅游","体育"]
             * user : {"name":"QQ群","userId":1,"headSculpture":"http://img4q.duitang.com/uploads/item/201311/09/20131109003922_JLyhT.jpeg","authentics":[{"city":{"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true},"name":"QQ群","companyName":"梦工厂","companyAddress":"陕西西安","position":"职业经理人","introduce":"","companyIntroduce":"","industoryArea":"1,2,3"}]}
             * commited : false
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

            public static class UserBean implements Serializable{
                /**
                 * name : QQ群
                 * userId : 1
                 * headSculpture : http://img4q.duitang.com/uploads/item/201311/09/20131109003922_JLyhT.jpeg
                 * authentics : [{"city":{"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true},"name":"QQ群","companyName":"梦工厂","companyAddress":"陕西西安","position":"职业经理人","introduce":"","companyIntroduce":"","industoryArea":"1,2,3"}]
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

                public static class AuthenticsBean implements Serializable{
                    /**
                     * city : {"cityId":6,"province":{"provinceId":2,"name":"山西省","isInvlid":true},"name":"柳林","isInvlid":true}
                     * name : QQ群
                     * companyName : 梦工厂
                     * companyAddress : 陕西西安
                     * position : 职业经理人
                     * introduce :
                     * companyIntroduce :
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
}
