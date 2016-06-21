package com.jinzht.pro.bean;

import java.util.List;

/**
 * 项目详情
 */
public class ProjectDetailBean {

    /**
     * message :
     * status : 200
     * data : {"extr":[{"buinessPlanId":1,"url":"http://www.jinzht.com/jinzht","content":"商业/n计划","icon":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"},{"reportId":1,"url":"http://www.jinzht.com/jinzht","content":"风控/n报告","projectId":1,"icon":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"},{"financeId":1,"projectId":1,"url":"http://www.jinzht.com:8080/jinzht/jinzht_27.jsp","content":"财务/n状况","icon":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"},{"financingCaseId":1,"url":"http://www.jinzht:8080/jinzht/","content":"融资/方案","icon":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"},{"financingExitId":1,"url":"http://www.jiznht.com/jinzht","projectId":1,"content":"退出/n渠道","icon":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"}],"project":{"projectId":1,"userId":1,"financestatus":{"name":"融资中"},"abbrevName":"北京金指投","fullName":"北京金指投信息科技有限公司","description":"致力于成长型企业第一平台！","projectType":0,"address":"陕西 | 西安","collected":true,"startPageImage":"http://imgsrc.baidu.com/forum/pic/item/89f4051f95cad1c87ccacc6b7e3e6709c83d5147.jpg","collectionCount":1,"commentCount":1,"timeLeft":-7,"industoryType":"金融，互联网，","teams":[{"personId":1,"name":"黎明","icon":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg","position":"总经理","company":"金指投","address":"北京","introduce":"合伙人","url":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"}],"roadshows":[{"roadShowId":1,"roadshowplan":{"financingId":1,"beginDate":"2016-06-07 00:00:00","endDate":"2016-06-10 00:00:00","financeTotal":200,"financedMount":100}}],"projectimageses":[{"projectImageId":1,"imageUrl":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"},{"projectImageId":3,"imageUrl":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"},{"projectImageId":2,"imageUrl":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"}]}}
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
         * extr : [{"buinessPlanId":1,"url":"http://www.jinzht.com/jinzht","content":"商业/n计划","icon":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"},{"reportId":1,"url":"http://www.jinzht.com/jinzht","content":"风控/n报告","projectId":1,"icon":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"},{"financeId":1,"projectId":1,"url":"http://www.jinzht.com:8080/jinzht/jinzht_27.jsp","content":"财务/n状况","icon":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"},{"financingCaseId":1,"url":"http://www.jinzht:8080/jinzht/","content":"融资/方案","icon":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"},{"financingExitId":1,"url":"http://www.jiznht.com/jinzht","projectId":1,"content":"退出/n渠道","icon":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"}]
         * project : {"projectId":1,"userId":1,"financestatus":{"name":"融资中"},"abbrevName":"北京金指投","fullName":"北京金指投信息科技有限公司","description":"致力于成长型企业第一平台！","projectType":0,"address":"陕西 | 西安","collected":true,"startPageImage":"http://imgsrc.baidu.com/forum/pic/item/89f4051f95cad1c87ccacc6b7e3e6709c83d5147.jpg","collectionCount":1,"commentCount":1,"timeLeft":-7,"industoryType":"金融，互联网，","teams":[{"personId":1,"name":"黎明","icon":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg","position":"总经理","company":"金指投","address":"北京","introduce":"合伙人","url":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"}],"roadshows":[{"roadShowId":1,"roadshowplan":{"financingId":1,"beginDate":"2016-06-07 00:00:00","endDate":"2016-06-10 00:00:00","financeTotal":200,"financedMount":100}}],"projectimageses":[{"projectImageId":1,"imageUrl":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"},{"projectImageId":3,"imageUrl":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"},{"projectImageId":2,"imageUrl":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"}]}
         */
        private ProjectBean project;
        private List<ExtrBean> extr;

        public ProjectBean getProject() {
            return project;
        }

        public void setProject(ProjectBean project) {
            this.project = project;
        }

        public List<ExtrBean> getExtr() {
            return extr;
        }

        public void setExtr(List<ExtrBean> extr) {
            this.extr = extr;
        }

        public static class ProjectBean {
            /**
             * projectId : 1
             * userId : 1
             * financestatus : {"name":"融资中"}
             * abbrevName : 北京金指投
             * fullName : 北京金指投信息科技有限公司
             * description : 致力于成长型企业第一平台！
             * projectType : 0
             * address : 陕西 | 西安
             * collected : true
             * startPageImage : http://imgsrc.baidu.com/forum/pic/item/89f4051f95cad1c87ccacc6b7e3e6709c83d5147.jpg
             * collectionCount : 1
             * commentCount : 1
             * timeLeft : -7
             * industoryType : 金融，互联网，
             * teams : [{"personId":1,"name":"黎明","icon":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg","position":"总经理","company":"金指投","address":"北京","introduce":"合伙人","url":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"}]
             * roadshows : [{"roadShowId":1,"roadshowplan":{"financingId":1,"beginDate":"2016-06-07 00:00:00","endDate":"2016-06-10 00:00:00","financeTotal":200,"financedMount":100}}]
             * projectimageses : [{"projectImageId":1,"imageUrl":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"},{"projectImageId":3,"imageUrl":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"},{"projectImageId":2,"imageUrl":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"}]
             */
            private int projectId;
            private int userId;
            private FinancestatusBean financestatus;// 项目状态：预选项目、待路演、融资中、融资成功、融资失败
            private String abbrevName;
            private String fullName;
            private String description;
            private int projectType;
            private String address;
            private boolean collected;
            private String startPageImage;
            private int collectionCount;
            private int commentCount;
            private int timeLeft;
            private String industoryType;// 行业类型
            private List<TeamsBean> teams;
            private List<RoadshowsBean> roadshows;
            private List<ProjectimagesesBean> projectimageses;

            @Override
            public String toString() {
                return "ProjectBean{" +
                        "projectId=" + projectId +
                        ", userId=" + userId +
                        ", abbrevName='" + abbrevName + '\'' +
                        ", fullName='" + fullName + '\'' +
                        ", description='" + description + '\'' +
                        ", projectType=" + projectType +
                        ", address='" + address + '\'' +
                        ", startPageImage='" + startPageImage + '\'' +
                        ", collectionCount=" + collectionCount +
                        ", commentCount=" + commentCount +
                        ", timeLeft=" + timeLeft +
                        ", industoryType='" + industoryType + '\'' +
                        ", collected=" + collected +
                        '}';
            }

            public int getProjectId() {
                return projectId;
            }

            public void setProjectId(int projectId) {
                this.projectId = projectId;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public FinancestatusBean getFinancestatus() {
                return financestatus;
            }

            public void setFinancestatus(FinancestatusBean financestatus) {
                this.financestatus = financestatus;
            }

            public String getAbbrevName() {
                return abbrevName;
            }

            public void setAbbrevName(String abbrevName) {
                this.abbrevName = abbrevName;
            }

            public String getFullName() {
                return fullName;
            }

            public void setFullName(String fullName) {
                this.fullName = fullName;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getProjectType() {
                return projectType;
            }

            public void setProjectType(int projectType) {
                this.projectType = projectType;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public boolean isCollected() {
                return collected;
            }

            public void setCollected(boolean collected) {
                this.collected = collected;
            }

            public String getStartPageImage() {
                return startPageImage;
            }

            public void setStartPageImage(String startPageImage) {
                this.startPageImage = startPageImage;
            }

            public int getCollectionCount() {
                return collectionCount;
            }

            public void setCollectionCount(int collectionCount) {
                this.collectionCount = collectionCount;
            }

            public int getCommentCount() {
                return commentCount;
            }

            public void setCommentCount(int commentCount) {
                this.commentCount = commentCount;
            }

            public int getTimeLeft() {
                return timeLeft;
            }

            public void setTimeLeft(int timeLeft) {
                this.timeLeft = timeLeft;
            }

            public String getIndustoryType() {
                return industoryType;
            }

            public void setIndustoryType(String industoryType) {
                this.industoryType = industoryType;
            }

            public List<TeamsBean> getTeams() {
                return teams;
            }

            public void setTeams(List<TeamsBean> teams) {
                this.teams = teams;
            }

            public List<RoadshowsBean> getRoadshows() {
                return roadshows;
            }

            public void setRoadshows(List<RoadshowsBean> roadshows) {
                this.roadshows = roadshows;
            }

            public List<ProjectimagesesBean> getProjectimageses() {
                return projectimageses;
            }

            public void setProjectimageses(List<ProjectimagesesBean> projectimageses) {
                this.projectimageses = projectimageses;
            }

            public static class FinancestatusBean {
                /**
                 * name : 融资中
                 */
                private String name;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class TeamsBean {
                /**
                 * personId : 1
                 * name : 黎明
                 * icon : http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg
                 * position : 总经理
                 * company : 金指投
                 * address : 北京
                 * introduce : 合伙人
                 * url : http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg
                 */
                private int personId;
                private String name;
                private String icon;
                private String position;
                private String company;
                private String address;
                private String introduce;
                private String url;

                public int getPersonId() {
                    return personId;
                }

                public void setPersonId(int personId) {
                    this.personId = personId;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getPosition() {
                    return position;
                }

                public void setPosition(String position) {
                    this.position = position;
                }

                public String getCompany() {
                    return company;
                }

                public void setCompany(String company) {
                    this.company = company;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getIntroduce() {
                    return introduce;
                }

                public void setIntroduce(String introduce) {
                    this.introduce = introduce;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

            public static class RoadshowsBean {
                /**
                 * roadShowId : 1
                 * roadshowplan : {"financingId":1,"beginDate":"2016-06-07 00:00:00","endDate":"2016-06-10 00:00:00","financeTotal":200,"financedMount":100}
                 */
                private int roadShowId;
                private RoadshowplanBean roadshowplan;

                public int getRoadShowId() {
                    return roadShowId;
                }

                public void setRoadShowId(int roadShowId) {
                    this.roadShowId = roadShowId;
                }

                public RoadshowplanBean getRoadshowplan() {
                    return roadshowplan;
                }

                public void setRoadshowplan(RoadshowplanBean roadshowplan) {
                    this.roadshowplan = roadshowplan;
                }

                public static class RoadshowplanBean {
                    /**
                     * financingId : 1
                     * beginDate : 2016-06-07 00:00:00
                     * endDate : 2016-06-10 00:00:00
                     * financeTotal : 200
                     * financedMount : 100
                     */
                    private int financingId;
                    private String beginDate;
                    private String endDate;
                    private int financeTotal;
                    private int financedMount;

                    public int getFinancingId() {
                        return financingId;
                    }

                    public void setFinancingId(int financingId) {
                        this.financingId = financingId;
                    }

                    public String getBeginDate() {
                        return beginDate;
                    }

                    public void setBeginDate(String beginDate) {
                        this.beginDate = beginDate;
                    }

                    public String getEndDate() {
                        return endDate;
                    }

                    public void setEndDate(String endDate) {
                        this.endDate = endDate;
                    }

                    public int getFinanceTotal() {
                        return financeTotal;
                    }

                    public void setFinanceTotal(int financeTotal) {
                        this.financeTotal = financeTotal;
                    }

                    public int getFinancedMount() {
                        return financedMount;
                    }

                    public void setFinancedMount(int financedMount) {
                        this.financedMount = financedMount;
                    }
                }
            }

            public static class ProjectimagesesBean {
                /**
                 * projectImageId : 1
                 * imageUrl : http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg
                 */
                private int projectImageId;
                private String imageUrl;

                public int getProjectImageId() {
                    return projectImageId;
                }

                public void setProjectImageId(int projectImageId) {
                    this.projectImageId = projectImageId;
                }

                public String getImageUrl() {
                    return imageUrl;
                }

                public void setImageUrl(String imageUrl) {
                    this.imageUrl = imageUrl;
                }
            }
        }

        public static class ExtrBean {
            /**
             * buinessPlanId : 1
             * url : http://www.jinzht.com/jinzht
             * content : 商业/n计划
             * icon : http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg
             */
            private int buinessPlanId;
            private String url;
            private String content;
            private String icon;

            public int getBuinessPlanId() {
                return buinessPlanId;
            }

            public void setBuinessPlanId(int buinessPlanId) {
                this.buinessPlanId = buinessPlanId;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }
    }
}