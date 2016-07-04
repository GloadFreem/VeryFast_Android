package com.jinzht.pro.bean;

/**
 * 项目方上传项目时的信息
 */
public class UploadProjectInfo {

    /**
     * tel : 400-1234-5896
     * email : xiangmu@jinzht.com
     */

    private DataBean data;
    /**
     * data : {"tel":"400-1234-5896","email":"xiangmu@jinzht.com"}
     * message :
     * status : 200
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
        private String tel;
        private String email;

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
