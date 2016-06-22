package com.jinzht.pro.bean;

/**
 * 易宝账户注册时请求参数
 */
public class ToRegisterBean {
    private String platformNo;// 商户编号，固定值
    private String platformUserNo;// 用户编号，jinzht_0000_uid
    private String requestNo;// 请求流水号，时间+uid，例如：201603191203241064
    private String realName;// 会员真实姓名
    private String idCardType;// 身份证类型，G2_IDCARD
    private String idCardNo;// 身份证号
    private String mobile;// 手机号
    private String callbackUrl;// 页面回跳URL
    private String notifyUrl;// 服务器通知URL

    public ToRegisterBean() {
    }

    public ToRegisterBean(String platformNo, String platformUserNo, String requestNo, String realName, String idCardType, String idCardNo, String mobile, String email, String callbackUrl, String notifyUrl) {
        this.platformNo = platformNo;
        this.platformUserNo = platformUserNo;
        this.requestNo = requestNo;
        this.realName = realName;
        this.idCardType = idCardType;
        this.idCardNo = idCardNo;
        this.mobile = mobile;
        this.callbackUrl = callbackUrl;
        this.notifyUrl = notifyUrl;
    }

    public String getPlatformNo() {
        return platformNo;
    }

    public void setPlatformNo(String platformNo) {
        this.platformNo = platformNo;
    }

    public String getPlatformUserNo() {
        return platformUserNo;
    }

    public void setPlatformUserNo(String platformUserNo) {
        this.platformUserNo = platformUserNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(String idCardType) {
        this.idCardType = idCardType;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    @Override
    public String toString() {
        return "ToRegisterBean{" +
                "platformNo='" + platformNo + '\'' +
                ", platformUserNo='" + platformUserNo + '\'' +
                ", requestNo='" + requestNo + '\'' +
                ", realName='" + realName + '\'' +
                ", idCardType='" + idCardType + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", mobile='" + mobile + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                '}';
    }
}

