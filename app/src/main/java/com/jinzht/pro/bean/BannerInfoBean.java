package com.jinzht.pro.bean;

import java.util.List;

/**
 * banner信息
 */
public class BannerInfoBean {

    /**
     * data : [{"body":{"bannerId":2,"name":"投资建议","description":"投资建议","image":"http://www.jinzht.com/media/banner/img/2016/06/d003d371ac8942f9a0c3406a53b3166e.jpg","url":"http://www.jinzht.com"},"type":"Project","extr":{"projectId":1,"borrowerUserNumber":"jinzht_0000_3677","financestatus":{"name":"融资中"},"industoryType":"金融，互联网","roadshows":[{"roadshowplan":{"profit":"0.2","limitAmount":1,"financeTotal":200,"financedMount":100}}]}}]
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
         * body : {"bannerId":2,"name":"投资建议","description":"投资建议","image":"http://www.jinzht.com/media/banner/img/2016/06/d003d371ac8942f9a0c3406a53b3166e.jpg","url":"http://www.jinzht.com"}
         * type : Project
         * extr : {"projectId":1,"borrowerUserNumber":"jinzht_0000_3677","financestatus":{"name":"融资中"},"industoryType":"金融，互联网","roadshows":[{"roadshowplan":{"profit":"0.2","limitAmount":1,"financeTotal":200,"financedMount":100}}]}
         */
        private BodyBean body;
        private String type;
        private ExtrBean extr;

        public BodyBean getBody() {
            return body;
        }

        public void setBody(BodyBean body) {
            this.body = body;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public ExtrBean getExtr() {
            return extr;
        }

        public void setExtr(ExtrBean extr) {
            this.extr = extr;
        }

        public static class BodyBean {
            /**
             * bannerId : 2
             * name : 投资建议
             * description : 投资建议
             * image : http://www.jinzht.com/media/banner/img/2016/06/d003d371ac8942f9a0c3406a53b3166e.jpg
             * url : http://www.jinzht.com
             */
            private int bannerId;
            private String name;
            private String description;
            private String image;
            private String url;

            public int getBannerId() {
                return bannerId;
            }

            public void setBannerId(int bannerId) {
                this.bannerId = bannerId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class ExtrBean {
            /**
             * projectId : 1
             * borrowerUserNumber : jinzht_0000_3677
             * financestatus : {"name":"融资中"}
             * industoryType : 金融，互联网
             * roadshows : [{"roadshowplan":{"profit":"0.2","limitAmount":1,"financeTotal":200,"financedMount":100}}]
             */
            private int projectId;
            private String borrowerUserNumber;
            private FinancestatusBean financestatus;
            private String industoryType;
            private List<RoadshowsBean> roadshows;

            public int getProjectId() {
                return projectId;
            }

            public void setProjectId(int projectId) {
                this.projectId = projectId;
            }

            public String getBorrowerUserNumber() {
                return borrowerUserNumber;
            }

            public void setBorrowerUserNumber(String borrowerUserNumber) {
                this.borrowerUserNumber = borrowerUserNumber;
            }

            public FinancestatusBean getFinancestatus() {
                return financestatus;
            }

            public void setFinancestatus(FinancestatusBean financestatus) {
                this.financestatus = financestatus;
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
                 * roadshowplan : {"profit":"0.2","limitAmount":1,"financeTotal":200,"financedMount":100}
                 */
                private RoadshowplanBean roadshowplan;

                public RoadshowplanBean getRoadshowplan() {
                    return roadshowplan;
                }

                public void setRoadshowplan(RoadshowplanBean roadshowplan) {
                    this.roadshowplan = roadshowplan;
                }

                public static class RoadshowplanBean {
                    /**
                     * profit : 0.2
                     * limitAmount : 1
                     * financeTotal : 200
                     * financedMount : 100
                     */
                    private String profit;
                    private int limitAmount;
                    private int financeTotal;
                    private int financedMount;

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