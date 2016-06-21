package com.jinzht.pro.bean;

import java.util.List;

/**
 * 省份列表
 */
public class ProvinceListBean {

    /**
     * message :
     * status : 200
     * data : [{"provinceId":1,"name":"陕西省","isInvlid":true},{"provinceId":2,"name":"山西省","isInvlid":true},{"provinceId":3,"name":"河南省","isInvlid":true}]
     */

    private String message;
    private int status;
    /**
     * provinceId : 1
     * name : 陕西省
     * isInvlid : true
     */

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
