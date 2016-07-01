package com.jinzht.pro.bean;

/**
 * 是否已登录返回值
 */
public class IsLoginBean {
    /**
     * data : {"identityType":{"identiyTypeId":1,"name":"项目方"}}
     * message : 用戶已登录！
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
         * identityType : {"identiyTypeId":1,"name":"项目方"}
         */
        private IdentityTypeBean identityType;

        public IdentityTypeBean getIdentityType() {
            return identityType;
        }

        public void setIdentityType(IdentityTypeBean identityType) {
            this.identityType = identityType;
        }

        public static class IdentityTypeBean {

            /**
             * identiyTypeId : 1
             * name : 项目方
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
