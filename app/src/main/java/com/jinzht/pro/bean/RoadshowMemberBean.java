package com.jinzht.pro.bean;

import java.util.List;

/**
 * 路演项目成员
 */
public class RoadshowMemberBean {

    /**
     * message : 项目现场获取成功!
     * status : 200
     * data : [{"memberId":1,"name":"王强","company":"上善国际","position":"总经理","address":"北京","industory":"金融","emial":"6725441541@shangshan.com","icon":"http://192.168.5.129:8080/jinzht/upload/dog.jpg","telephone":"18729342354"}]
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
         * memberId : 1
         * name : 王强
         * company : 上善国际
         * position : 总经理
         * address : 北京
         * industory : 金融
         * emial : 6725441541@shangshan.com
         * icon : http://192.168.5.129:8080/jinzht/upload/dog.jpg
         * telephone : 18729342354
         */
        private int memberId;
        private String name;
        private String company;
        private String position;
        private String address;
        private String industory;
        private String emial;
        private String icon;
        private String telephone;

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

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }
    }
}
