package com.jinzht.pro.bean;

import java.util.List;

/**
 * 市列表
 */
public class CityListBean {

    /**
     * message :
     * status : 200
     * data : [{"cityId":7,"name":"西安"}]
     */

    private String message;
    private int status;
    /**
     * cityId : 7
     * name : 西安
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
        private int cityId;
        private String name;

        public int getCityId() {
            return cityId;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
