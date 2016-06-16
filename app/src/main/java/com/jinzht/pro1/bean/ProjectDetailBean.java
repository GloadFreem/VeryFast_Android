package com.jinzht.pro1.bean;

import java.util.List;

/**
 * 项目详情
 */
public class ProjectDetailBean {

    /**
     * message : 圈子发布成功!
     * status : 200
     * data : {"projectId":1,"financestatus":{"name":"路演中"},"abbrevName":"北京金指投","fullName":"北京金指投信息科技有限公司","description":"致力于成长型企业第一平台！","projectType":0,"address":"上海","startPageImage":"http://www.jinzht.com:8080/upload/1.jpg","projectcommitrecords":[],"teams":[{"personId":1,"name":"黎明","icon":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg","position":"总经理","company":"金指投","address":"北京","introduce":"合伙人","url":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"}],"members":[{"memberId":1,"name":"王强","company":"上善国际","position":"总经理","address":"北京","industory":"金融","emial":"6725441541@shangshan.com"}],"roadshows":[{"roadShowId":1,"roadshowplan":{"financingId":1,"beginDate":"2016-06-07 00:00:00","endDate":"2016-06-10 00:00:00","financeTotal":200,"financedMount":100}}],"projectimageses":[{"projectImageId":3,"imageUrl":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"},{"projectImageId":2,"imageUrl":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"},{"projectImageId":1,"imageUrl":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"}],"collectionCount":1,"timeLeft":1}
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
         * projectId : 1
         * financestatus : {"name":"路演中"}
         * abbrevName : 北京金指投
         * fullName : 北京金指投信息科技有限公司
         * description : 致力于成长型企业第一平台！
         * projectType : 0
         * address : 上海
         * startPageImage : http://www.jinzht.com:8080/upload/1.jpg
         * teams : [{"personId":1,"name":"黎明","icon":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg","position":"总经理","company":"金指投","address":"北京","introduce":"合伙人","url":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"}]
         * members : [{"memberId":1,"name":"王强","company":"上善国际","position":"总经理","address":"北京","industory":"金融","emial":"6725441541@shangshan.com"}]
         * roadshows : [{"roadShowId":1,"roadshowplan":{"financingId":1,"beginDate":"2016-06-07 00:00:00","endDate":"2016-06-10 00:00:00","financeTotal":200,"financedMount":100}}]
         * projectimageses : [{"projectImageId":3,"imageUrl":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"},{"projectImageId":2,"imageUrl":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"},{"projectImageId":1,"imageUrl":"http://s2.nuomi.bdimg.com/upload/deal/2014/1/V_L/623682-1391756281052.jpg"}]
         * collectionCount : 1
         * timeLeft : 1
         */
        private int projectId;
        private FinancestatusBean financestatus;
        private String abbrevName;
        private String fullName;
        private String description;
        private int projectType;
        private String address;
        private String startPageImage;
        private int collectionCount;
        private int timeLeft;
        private List<TeamsBean> teams;
        private List<MembersBean> members;
        private List<RoadshowsBean> roadshows;
        private List<ProjectimagesesBean> projectimageses;

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
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

        public int getTimeLeft() {
            return timeLeft;
        }

        public void setTimeLeft(int timeLeft) {
            this.timeLeft = timeLeft;
        }

        public List<TeamsBean> getTeams() {
            return teams;
        }

        public void setTeams(List<TeamsBean> teams) {
            this.teams = teams;
        }

        public List<MembersBean> getMembers() {
            return members;
        }

        public void setMembers(List<MembersBean> members) {
            this.members = members;
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
             * name : 路演中
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

        public static class MembersBean {
            /**
             * memberId : 1
             * name : 王强
             * company : 上善国际
             * position : 总经理
             * address : 北京
             * industory : 金融
             * emial : 6725441541@shangshan.com
             */
            private int memberId;
            private String name;
            private String company;
            private String position;
            private String address;
            private String industory;
            private String emial;

            public int getMemberId() {
                return memberId;
            }

            public void setMemberId(int memberId) {
                this.memberId = memberId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getIndustory() {
                return industory;
            }

            public void setIndustory(String industory) {
                this.industory = industory;
            }

            public String getEmial() {
                return emial;
            }

            public void setEmial(String emial) {
                this.emial = emial;
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
             * projectImageId : 3
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
}
