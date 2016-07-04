package com.jinzht.pro.bean;

/**
 * 登录接口的返回信息
 */
public class LoginBean {
    /**
     * data : {"identityType":{"identiyTypeId":3,"name":"机构投资者"},"userId":645}
     * message : 登录成功！
     * status : 200
     */
    private DataBean data;
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
         * identityType : {"identiyTypeId":3,"name":"机构投资者"}
         * userId : 645
         * extUserId: 645
         */
        private IdentityTypeBean identityType;
        private int userId;
        private int extUserId;

        public IdentityTypeBean getIdentityType() {
            return identityType;
        }

        public void setIdentityType(IdentityTypeBean identityType) {
            this.identityType = identityType;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getExtUserId() {
            return extUserId;
        }

        public void setExtUserId(int extUserId) {
            this.extUserId = extUserId;
        }

        public static class IdentityTypeBean {
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