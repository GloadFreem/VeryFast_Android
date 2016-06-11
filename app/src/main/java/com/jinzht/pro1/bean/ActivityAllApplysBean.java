package com.jinzht.pro1.bean;

import java.util.List;

/**
 * 所有活动报名人
 */
public class ActivityAllApplysBean {

    /**
     * message :
     * status : 200
     * data : [{"attendUid":12,"users":{"userId":7,"headSculpture":"http://192.168.5.107:8080/jinzht/upload/headerImages/jinzht_user_7.jpg","authentics":[{"name":"段辉","companyName":"金指投","position":""}]},"content":"","enrollDate":"2016-06-11 13:22:49"}]
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
         * attendUid : 12
         * users : {"userId":7,"headSculpture":"http://192.168.5.107:8080/jinzht/upload/headerImages/jinzht_user_7.jpg","authentics":[{"name":"段辉","companyName":"金指投","position":""}]}
         * content :
         * enrollDate : 2016-06-11 13:22:49
         */
        private int attendUid;
        private UsersBean users;
        private String content;
        private String enrollDate;

        public int getAttendUid() {
            return attendUid;
        }

        public void setAttendUid(int attendUid) {
            this.attendUid = attendUid;
        }

        public UsersBean getUsers() {
            return users;
        }

        public void setUsers(UsersBean users) {
            this.users = users;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getEnrollDate() {
            return enrollDate;
        }

        public void setEnrollDate(String enrollDate) {
            this.enrollDate = enrollDate;
        }

        public static class UsersBean {
            /**
             * userId : 7
             * headSculpture : http://192.168.5.107:8080/jinzht/upload/headerImages/jinzht_user_7.jpg
             * authentics : [{"name":"段辉","companyName":"金指投","position":""}]
             */
            private int userId;
            private String headSculpture;
            private List<AuthenticsBean> authentics;

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getHeadSculpture() {
                return headSculpture;
            }

            public void setHeadSculpture(String headSculpture) {
                this.headSculpture = headSculpture;
            }

            public List<AuthenticsBean> getAuthentics() {
                return authentics;
            }

            public void setAuthentics(List<AuthenticsBean> authentics) {
                this.authentics = authentics;
            }

            public static class AuthenticsBean {
                /**
                 * name : 段辉
                 * companyName : 金指投
                 * position :
                 */
                private String name;
                private String companyName;
                private String position;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getCompanyName() {
                    return companyName;
                }

                public void setCompanyName(String companyName) {
                    this.companyName = companyName;
                }

                public String getPosition() {
                    return position;
                }

                public void setPosition(String position) {
                    this.position = position;
                }
            }
        }
    }
}
