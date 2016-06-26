package com.jinzht.pro.bean;

import java.util.List;

/**
 * 交易账单列表
 */
public class BillListBean {

    /**
     * data : [{"img":"","record":{"amount":100,"tradeDate":"2016-06-25 00:00:00","tradeId":1,"tradeCode":"454456545","tradetype":{"name":"账户充值"},"tradestatus":{"name":"充值成功"}},"name":""},{"img":"http://www.jinzht.com/static/app/img/icon.jpg","record":{"amount":100,"tradeDate":"2016-06-25 00:00:00","tradeId":2,"tradeCode":"454456545","tradetype":{"name":"项目认投"},"tradestatus":{"name":"充值成功"}},"name":"北京金指投"}]
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
         * img :
         * record : {"amount":100,"tradeDate":"2016-06-25 00:00:00","tradeId":1,"tradeCode":"454456545","tradetype":{"name":"账户充值"},"tradestatus":{"name":"充值成功"}}
         * name :
         */
        private String img;
        private RecordBean record;
        private String name;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public RecordBean getRecord() {
            return record;
        }

        public void setRecord(RecordBean record) {
            this.record = record;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static class RecordBean {
            /**
             * amount : 100
             * tradeDate : 2016-06-25 00:00:00
             * tradeId : 1
             * tradeCode : 454456545
             * tradetype : {"name":"账户充值"}
             * tradestatus : {"name":"充值成功"}
             */
            private double amount;
            private String tradeDate;
            private int tradeId;
            private String tradeCode;
            private TradetypeBean tradetype;
            private TradestatusBean tradestatus;

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public String getTradeDate() {
                return tradeDate;
            }

            public void setTradeDate(String tradeDate) {
                this.tradeDate = tradeDate;
            }

            public int getTradeId() {
                return tradeId;
            }

            public void setTradeId(int tradeId) {
                this.tradeId = tradeId;
            }

            public String getTradeCode() {
                return tradeCode;
            }

            public void setTradeCode(String tradeCode) {
                this.tradeCode = tradeCode;
            }

            public TradetypeBean getTradetype() {
                return tradetype;
            }

            public void setTradetype(TradetypeBean tradetype) {
                this.tradetype = tradetype;
            }

            public TradestatusBean getTradestatus() {
                return tradestatus;
            }

            public void setTradestatus(TradestatusBean tradestatus) {
                this.tradestatus = tradestatus;
            }

            public static class TradetypeBean {
                /**
                 * name : 账户充值
                 */
                private String name;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class TradestatusBean {
                /**
                 * name : 充值成功
                 */
                private String name;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }
    }
}