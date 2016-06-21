package com.jinzht.pro.bean;

import java.util.List;

/**
 * 认证接口返回信息
 */
public class AuthenticateBean {

    /**
     * authentic : {"authId":7,"autrhrecords":[],"buinessLicence":"","buinessLicenceNo":"","city":"","companyAddress":"","companyIntroduce":"","companyName":"","identiyCarA":"upload/identify/identify_card_a_1.jpg","identiyCarB":"upload/identify/identify_card_b_1.jpg","identiyCarNo":"","identiytype":"","industoryarea":"","industorytype":"","introduce":"","name":"陈生珠","optional":"","position":"","users":""}
     */

    private DataBean data;
    /**
     * data : {"authentic":{"authId":7,"autrhrecords":[],"buinessLicence":"","buinessLicenceNo":"","city":"","companyAddress":"","companyIntroduce":"","companyName":"","identiyCarA":"upload/identify/identify_card_a_1.jpg","identiyCarB":"upload/identify/identify_card_b_1.jpg","identiyCarNo":"","identiytype":"","industoryarea":"","industorytype":"","introduce":"","name":"陈生珠","optional":"","position":"","users":""}}
     * message : 认证信息提交成功
     * status : 401
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
         * authId : 7
         * autrhrecords : []
         * buinessLicence :
         * buinessLicenceNo :
         * city :
         * companyAddress :
         * companyIntroduce :
         * companyName :
         * identiyCarA : upload/identify/identify_card_a_1.jpg
         * identiyCarB : upload/identify/identify_card_b_1.jpg
         * identiyCarNo :
         * identiytype :
         * industoryarea :
         * industorytype :
         * introduce :
         * name : 陈生珠
         * optional :
         * position :
         * users :
         */

        private AuthenticBean authentic;

        public AuthenticBean getAuthentic() {
            return authentic;
        }

        public void setAuthentic(AuthenticBean authentic) {
            this.authentic = authentic;
        }

        public static class AuthenticBean {
            private int authId;
            private String buinessLicence;
            private String buinessLicenceNo;
            private String city;
            private String companyAddress;
            private String companyIntroduce;
            private String companyName;
            private String identiyCarA;
            private String identiyCarB;
            private String identiyCarNo;
            private String identiytype;
            private String industoryarea;
            private String industorytype;
            private String introduce;
            private String name;
            private String optional;
            private String position;
            private String users;
            private List<?> autrhrecords;

            public int getAuthId() {
                return authId;
            }

            public void setAuthId(int authId) {
                this.authId = authId;
            }

            public String getBuinessLicence() {
                return buinessLicence;
            }

            public void setBuinessLicence(String buinessLicence) {
                this.buinessLicence = buinessLicence;
            }

            public String getBuinessLicenceNo() {
                return buinessLicenceNo;
            }

            public void setBuinessLicenceNo(String buinessLicenceNo) {
                this.buinessLicenceNo = buinessLicenceNo;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getCompanyAddress() {
                return companyAddress;
            }

            public void setCompanyAddress(String companyAddress) {
                this.companyAddress = companyAddress;
            }

            public String getCompanyIntroduce() {
                return companyIntroduce;
            }

            public void setCompanyIntroduce(String companyIntroduce) {
                this.companyIntroduce = companyIntroduce;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public String getIdentiyCarA() {
                return identiyCarA;
            }

            public void setIdentiyCarA(String identiyCarA) {
                this.identiyCarA = identiyCarA;
            }

            public String getIdentiyCarB() {
                return identiyCarB;
            }

            public void setIdentiyCarB(String identiyCarB) {
                this.identiyCarB = identiyCarB;
            }

            public String getIdentiyCarNo() {
                return identiyCarNo;
            }

            public void setIdentiyCarNo(String identiyCarNo) {
                this.identiyCarNo = identiyCarNo;
            }

            public String getIdentiytype() {
                return identiytype;
            }

            public void setIdentiytype(String identiytype) {
                this.identiytype = identiytype;
            }

            public String getIndustoryarea() {
                return industoryarea;
            }

            public void setIndustoryarea(String industoryarea) {
                this.industoryarea = industoryarea;
            }

            public String getIndustorytype() {
                return industorytype;
            }

            public void setIndustorytype(String industorytype) {
                this.industorytype = industorytype;
            }

            public String getIntroduce() {
                return introduce;
            }

            public void setIntroduce(String introduce) {
                this.introduce = introduce;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOptional() {
                return optional;
            }

            public void setOptional(String optional) {
                this.optional = optional;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getUsers() {
                return users;
            }

            public void setUsers(String users) {
                this.users = users;
            }

            public List<?> getAutrhrecords() {
                return autrhrecords;
            }

            public void setAutrhrecords(List<?> autrhrecords) {
                this.autrhrecords = autrhrecords;
            }
        }
    }
}
