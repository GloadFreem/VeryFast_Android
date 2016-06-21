package com.jinzht.pro.bean;

import java.util.List;

/**
 * 投资领域列表
 */
public class FieldListBean {

    /**
     * message :
     * status : 200
     * data : [{"areaId":1,"name":"电影","isvalid":true},{"areaId":2,"name":"旅游","isvalid":true},{"areaId":3,"name":"体育","isvalid":true},{"areaId":4,"name":"娱乐","isvalid":true}]
     */

    private String message;
    private int status;
    /**
     * areaId : 1
     * name : 电影
     * isvalid : true
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
        private int areaId;
        private String name;
        private boolean isvalid;

        public int getAreaId() {
            return areaId;
        }

        public void setAreaId(int areaId) {
            this.areaId = areaId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isIsvalid() {
            return isvalid;
        }

        public void setIsvalid(boolean isvalid) {
            this.isvalid = isvalid;
        }
    }
}
