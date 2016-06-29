package com.jinzht.pro.bean;

import java.util.List;

/**
 * 项目中心的项目方提交的记录
 */
public class CommitRecordsBean {

    /**
     * data : [{"record":{"recordId":1,"status":{"recordId":1,"name":"未阅读"},"recordDate":"2016-06-27"},"user":{"userId":4670,"headSculpture":"http://192.168.5.139:8080/jinzht/upload/headerImages/jinzht_user_4670.jpg","authentics":[{"authId":4785,"identiytype":{"identiyTypeId":3,"name":"机构投资者"},"name":"陈生珠","companyName":"百度一下","companyAddress":"陕西 西安","position":"旅游 驴友"}]}},{"record":{"recordId":1,"status":{"recordId":1,"name":"未阅读"},"recordDate":"2016-06-27"},"user":{"userId":4670,"headSculpture":"http://192.168.5.139:8080/jinzht/upload/headerImages/jinzht_user_4670.jpg","authentics":[{"authId":4785,"identiytype":{"identiyTypeId":3,"name":"机构投资者"},"name":"陈生珠","companyName":"百度一下","companyAddress":"陕西 西安","position":"旅游 驴友"}]}}]
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
         * record : {"recordId":1,"status":{"recordId":1,"name":"未阅读"},"recordDate":"2016-06-27"}
         * user : {"userId":4670,"headSculpture":"http://192.168.5.139:8080/jinzht/upload/headerImages/jinzht_user_4670.jpg","authentics":[{"authId":4785,"identiytype":{"identiyTypeId":3,"name":"机构投资者"},"name":"陈生珠","companyName":"百度一下","companyAddress":"陕西 西安","position":"旅游 驴友"}]}
         */
        private RecordBean record;
        private UserBean user;

        public RecordBean getRecord() {
            return record;
        }

        public void setRecord(RecordBean record) {
            this.record = record;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class RecordBean {
            /**
             * recordId : 1
             * status : {"recordId":1,"name":"未阅读"}
             * recordDate : 2016-06-27
             */
            private int recordId;
            private StatusBean status;
            private String recordDate;

            public int getRecordId() {
                return recordId;
            }

            public void setRecordId(int recordId) {
                this.recordId = recordId;
            }

            public StatusBean getStatus() {
                return status;
            }

            public void setStatus(StatusBean status) {
                this.status = status;
            }

            public String getRecordDate() {
                return recordDate;
            }

            public void setRecordDate(String recordDate) {
                this.recordDate = recordDate;
            }

            public static class StatusBean {
                /**
                 * recordId : 1
                 * name : 未阅读
                 */
                private int recordId;
                private String name;

                public int getRecordId() {
                    return recordId;
                }

                public void setRecordId(int recordId) {
                    this.recordId = recordId;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }

        public static class UserBean {
            /**
             * userId : 4670
             * headSculpture : http://192.168.5.139:8080/jinzht/upload/headerImages/jinzht_user_4670.jpg
             * authentics : [{"authId":4785,"identiytype":{"identiyTypeId":3,"name":"机构投资者"},"name":"陈生珠","companyName":"百度一下","companyAddress":"陕西 西安","position":"旅游 驴友"}]
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
                 * authId : 4785
                 * identiytype : {"identiyTypeId":3,"name":"机构投资者"}
                 * name : 陈生珠
                 * companyName : 百度一下
                 * companyAddress : 陕西 西安
                 * position : 旅游 驴友
                 */
                private int authId;
                private IdentiytypeBean identiytype;
                private String name;
                private String companyName;
                private String companyAddress;
                private String position;

                public int getAuthId() {
                    return authId;
                }

                public void setAuthId(int authId) {
                    this.authId = authId;
                }

                public IdentiytypeBean getIdentiytype() {
                    return identiytype;
                }

                public void setIdentiytype(IdentiytypeBean identiytype) {
                    this.identiytype = identiytype;
                }

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

                public String getCompanyAddress() {
                    return companyAddress;
                }

                public void setCompanyAddress(String companyAddress) {
                    this.companyAddress = companyAddress;
                }

                public String getPosition() {
                    return position;
                }

                public void setPosition(String position) {
                    this.position = position;
                }

                public static class IdentiytypeBean {
                    /**
                     * identiyTypeId : 3
                     * name : 机构投资者
                     */
                    private int identiyTypeId;
                    private String name;

                    public int getIdentiyTypeId() {
                        return identiyTypeId;
                    }

                    public void setIdentiyTypeId(int identiyTypeId) {
                        this.identiyTypeId = identiyTypeId;
                    }

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
}
