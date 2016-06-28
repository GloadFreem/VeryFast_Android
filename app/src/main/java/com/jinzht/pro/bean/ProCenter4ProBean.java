package com.jinzht.pro.bean;

import java.util.List;

/**
 * 项目方的项目中心
 */
public class ProCenter4ProBean {

    /**
     * data : [{"projectId":1,"userId":4670,"financestatus":{"name":"融资中"},"abbrevName":"北京金指投","fullName":"北京金指投信息科技有限公司","description":"致力于成长型企业第一平台！","projectType":0,"address":"陕西 | 西安","startPageImage":"http://www.jinzht.com/static/app/img/icon.jpg","collectionCount":100,"timeLeft":100,"industoryType":"金融，互联网","roadshows":[{"roadShowId":1,"roadshowplan":{"financingId":1,"beginDate":"2016-06-24 15:06:25","endDate":"2016-06-30 00:00:00","profit":"0.2","limitAmount":1,"financeTotal":200,"financedMount":100}}]},{"projectId":2,"userId":4670,"financestatus":{"name":"预选项目"},"abbrevName":"上海嘉盛信","fullName":"上海嘉盛信息科技有限公司","description":"上海嘉盛信息科技有限公司","projectType":0,"address":"上海","startPageImage":"http://www.jinzht.com/static/app/img/icon.jpg","collectionCount":112,"timeLeft":111,"industoryType":"农业，餐饮，传统","roadshows":[{"roadShowId":2,"roadshowplan":{"financingId":2,"beginDate":"2016-06-24 15:06:25","endDate":"2016-06-30 00:00:00","profit":"0.2","limitAmount":1,"financeTotal":200,"financedMount":100}}]}]
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
        private int projectId;
        private int userId;
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

        public static class RoadshowsBean {
            /**
             * roadShowId : 1
             * roadshowplan : {"financingId":1,"beginDate":"2016-06-24 15:06:25","endDate":"2016-06-30 00:00:00","profit":"0.2","limitAmount":1,"financeTotal":200,"financedMount":100}
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
                 * beginDate : 2016-06-24 15:06:25
                 * endDate : 2016-06-30 00:00:00
                 * profit : 0.2
                 * limitAmount : 1
                 * financeTotal : 200
                 * financedMount : 100
                 */
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
