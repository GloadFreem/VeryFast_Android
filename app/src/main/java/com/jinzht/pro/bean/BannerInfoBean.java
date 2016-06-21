package com.jinzht.pro.bean;

import java.util.List;

/**
 * banner信息
 */
public class BannerInfoBean {

    /**
     * message :
     * status : 200
     * data : [{"extr":{"projectId":1,"industoryType":"金融，互联网，","roadshows":[{"roadshowplan":{"financeTotal":200,"financedMount":100}}]},"body":{"bannerId":1,"name":"信息","description":"新三板上市","image":"http://www.jinzht.com/image/app.png","url":"http://www.jinzht.com"},"type":"Project"},{"body":{"bannerId":2,"name":"项目","description":"国联质检上市","image":"http://www.jinzht.com/image/app.png","url":"http://www.jinzht.com"},"type":"Web"},{"body":{"bannerId":3,"name":"项目","description":"杰邦科技上市","image":"http://www.jinzht.com/image/app.png","url":"http://www.jinzht.com"},"type":"Web"},{"body":{"bannerId":4,"name":"重磅新闻","description":"金指投app3.0上线","image":"http://www.jinzht.com/image/app.png","url":"http://www.jinzht.com"},"type":"Web"}]
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
         * extr : {"projectId":1,"industoryType":"金融，互联网，","roadshows":[{"roadshowplan":{"financeTotal":200,"financedMount":100}}]}
         * body : {"bannerId":1,"name":"信息","description":"新三板上市","image":"http://www.jinzht.com/image/app.png","url":"http://www.jinzht.com"}
         * type : Project
         */
        private ExtrBean extr;
        private BodyBean body;
        private String type;

        public ExtrBean getExtr() {
            return extr;
        }

        public void setExtr(ExtrBean extr) {
            this.extr = extr;
        }

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

        public static class ExtrBean {
            /**
             * projectId : 1
             * industoryType : 金融，互联网，
             * roadshows : [{"roadshowplan":{"financeTotal":200,"financedMount":100}}]
             */
            private int projectId;
            private String industoryType;
            private List<RoadshowsBean> roadshows;

            public int getProjectId() {
                return projectId;
            }

            public void setProjectId(int projectId) {
                this.projectId = projectId;
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

            public static class RoadshowsBean {
                /**
                 * roadshowplan : {"financeTotal":200,"financedMount":100}
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
                     * financeTotal : 200
                     * financedMount : 100
                     */
                    private int financeTotal;
                    private int financedMount;

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

        public static class BodyBean {
            /**
             * bannerId : 1
             * name : 信息
             * description : 新三板上市
             * image : http://www.jinzht.com/image/app.png
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
    }
}
