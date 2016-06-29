package com.jinzht.pro.bean;

import java.util.List;

/**
 * 智囊团的项目中心
 */
public class ProCenter4BrainBean {

    private DataBean data;
    /**
     * data : {"invest":[{"projectId":1,"userId":4670,"financestatus":{"name":"融资中"},"abbrevName":"北京金指投","fullName":"北京金指投信息科技有限公司","description":"致力于成长型企业第一平台！","projectType":0,"address":"陕西 | 西安","startPageImage":"http://www.jinzht.com/static/app/img/icon.jpg","collectionCount":100,"timeLeft":100,"industoryType":"金融，互联网","roadshows":[{"roadShowId":1,"roadshowplan":{"financingId":1,"beginDate":"2016-06-24 15:06:25","endDate":"2016-06-30 00:00:00","profit":"0.2","limitAmount":1,"financeTotal":200,"financedMount":100}}]}],"comment":[{"projectId":1,"userId":4670,"financestatus":{"name":"融资中"},"abbrevName":"北京金指投","fullName":"北京金指投信息科技有限公司","description":"致力于成长型企业第一平台！","projectType":0,"address":"陕西 | 西安","startPageImage":"http://www.jinzht.com/static/app/img/icon.jpg","collectionCount":100,"timeLeft":100,"industoryType":"金融，互联网","roadshows":[{"roadShowId":1,"roadshowplan":{"financingId":1,"beginDate":"2016-06-24 15:06:25","endDate":"2016-06-30 00:00:00","profit":"0.2","limitAmount":1,"financeTotal":200,"financedMount":100}}]}]}
     * message :
     * status : 200
     */

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
         * projectId : 1
         * userId : 4670
         * financestatus : {"name":"融资中"}
         * abbrevName : 北京金指投
         * fullName : 北京金指投信息科技有限公司
         * description : 致力于成长型企业第一平台！
         * projectType : 0
         * address : 陕西 | 西安
         * startPageImage : http://www.jinzht.com/static/app/img/icon.jpg
         * collectionCount : 100
         * timeLeft : 100
         * industoryType : 金融，互联网
         * roadshows : [{"roadShowId":1,"roadshowplan":{"financingId":1,"beginDate":"2016-06-24 15:06:25","endDate":"2016-06-30 00:00:00","profit":"0.2","limitAmount":1,"financeTotal":200,"financedMount":100}}]
         */

        private List<InvestBean> invest;
        /**
         * projectId : 1
         * userId : 4670
         * financestatus : {"name":"融资中"}
         * abbrevName : 北京金指投
         * fullName : 北京金指投信息科技有限公司
         * description : 致力于成长型企业第一平台！
         * projectType : 0
         * address : 陕西 | 西安
         * startPageImage : http://www.jinzht.com/static/app/img/icon.jpg
         * collectionCount : 100
         * timeLeft : 100
         * industoryType : 金融，互联网
         * roadshows : [{"roadShowId":1,"roadshowplan":{"financingId":1,"beginDate":"2016-06-24 15:06:25","endDate":"2016-06-30 00:00:00","profit":"0.2","limitAmount":1,"financeTotal":200,"financedMount":100}}]
         */

        private List<CommentBean> comment;

        public List<InvestBean> getInvest() {
            return invest;
        }

        public void setInvest(List<InvestBean> invest) {
            this.invest = invest;
        }

        public List<CommentBean> getComment() {
            return comment;
        }

        public void setComment(List<CommentBean> comment) {
            this.comment = comment;
        }

        public static class InvestBean {
            private int projectId;
            private int userId;
            /**
             * name : 融资中
             */

            private FinancestatusBean financestatus;
            private String abbrevName;
            private String fullName;
            private String description;
            private int projectType;
            private String address;
            private String startPageImage;
            private int collectionCount;
            private int timeLeft;
            private String industoryType;
            /**
             * roadShowId : 1
             * roadshowplan : {"financingId":1,"beginDate":"2016-06-24 15:06:25","endDate":"2016-06-30 00:00:00","profit":"0.2","limitAmount":1,"financeTotal":200,"financedMount":100}
             */

            private List<RoadshowsBean> roadshows;

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

            public String getIndustoryType() {
                return industoryType;
            }

            public void setIndustoryType(String industoryType) {
                this.industoryType = industoryType;
            }

            public List<RoadshowsBean> getRoadshows() {
                return roadshows;
            }

            public void setRoadshows(List<RoadshowsBean> roadshows) {
                this.roadshows = roadshows;
            }

            public static class FinancestatusBean {
                private String name;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class RoadshowsBean {
                private int roadShowId;
                /**
                 * financingId : 1
                 * beginDate : 2016-06-24 15:06:25
                 * endDate : 2016-06-30 00:00:00
                 * profit : 0.2
                 * limitAmount : 1
                 * financeTotal : 200
                 * financedMount : 100
                 */

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
                    private int financingId;
                    private String beginDate;
                    private String endDate;
                    private String profit;
                    private int limitAmount;
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

                    public String getProfit() {
                        return profit;
                    }

                    public void setProfit(String profit) {
                        this.profit = profit;
                    }

                    public int getLimitAmount() {
                        return limitAmount;
                    }

                    public void setLimitAmount(int limitAmount) {
                        this.limitAmount = limitAmount;
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
        }

        public static class CommentBean {
            private int projectId;
            private int userId;
            /**
             * name : 融资中
             */

            private FinancestatusBean financestatus;
            private String abbrevName;
            private String fullName;
            private String description;
            private int projectType;
            private String address;
            private String startPageImage;
            private int collectionCount;
            private int timeLeft;
            private String industoryType;
            /**
             * roadShowId : 1
             * roadshowplan : {"financingId":1,"beginDate":"2016-06-24 15:06:25","endDate":"2016-06-30 00:00:00","profit":"0.2","limitAmount":1,"financeTotal":200,"financedMount":100}
             */

            private List<RoadshowsBean> roadshows;

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

            public String getIndustoryType() {
                return industoryType;
            }

            public void setIndustoryType(String industoryType) {
                this.industoryType = industoryType;
            }

            public List<RoadshowsBean> getRoadshows() {
                return roadshows;
            }

            public void setRoadshows(List<RoadshowsBean> roadshows) {
                this.roadshows = roadshows;
            }

            public static class FinancestatusBean {
                private String name;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class RoadshowsBean {
                private int roadShowId;
                /**
                 * financingId : 1
                 * beginDate : 2016-06-24 15:06:25
                 * endDate : 2016-06-30 00:00:00
                 * profit : 0.2
                 * limitAmount : 1
                 * financeTotal : 200
                 * financedMount : 100
                 */

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
                    private int financingId;
                    private String beginDate;
                    private String endDate;
                    private String profit;
                    private int limitAmount;
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

                    public String getProfit() {
                        return profit;
                    }

                    public void setProfit(String profit) {
                        this.profit = profit;
                    }

                    public int getLimitAmount() {
                        return limitAmount;
                    }

                    public void setLimitAmount(int limitAmount) {
                        this.limitAmount = limitAmount;
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
        }
    }
}
