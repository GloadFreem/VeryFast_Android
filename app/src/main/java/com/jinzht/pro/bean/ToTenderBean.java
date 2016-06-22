package com.jinzht.pro.bean;

import java.util.List;

/**
 * 易宝投标请求参数
 */
public class ToTenderBean {
    private String platformNo;// 商户编号
    private String requestNo;// 请求流水号
    private String platformUserNo;// 出款人平台用户编号
    private String userType;// 出款人用户类型，目前只支持传入MEMBER
    private String bizType;// 业务类型，投标TENDER
    private List<ToTenderDetailBean> details;// 资金明细集合
    private List<ToTenderPropertyBean> extend;// 业务扩展属性集合
    private String callbackUrl;
    private String notifyUrl;

    public ToTenderBean() {
    }

    public ToTenderBean(String platformNo, String requestNo, String platformUserNo, String userType, String bizType, List<ToTenderDetailBean> details, List<ToTenderPropertyBean> extend, String callbackUrl, String notifyUrl) {
        this.platformNo = platformNo;
        this.requestNo = requestNo;
        this.platformUserNo = platformUserNo;
        this.userType = userType;
        this.bizType = bizType;
        this.details = details;
        this.extend = extend;
        this.callbackUrl = callbackUrl;
        this.notifyUrl = notifyUrl;
    }

    public String getPlatformNo() {
        return platformNo;
    }

    public void setPlatformNo(String platformNo) {
        this.platformNo = platformNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getPlatformUserNo() {
        return platformUserNo;
    }

    public void setPlatformUserNo(String platformUserNo) {
        this.platformUserNo = platformUserNo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public List<ToTenderDetailBean> getDetails() {
        return details;
    }

    public void setDetails(List<ToTenderDetailBean> details) {
        this.details = details;
    }

    public List<ToTenderPropertyBean> getExtend() {
        return extend;
    }

    public void setExtend(List<ToTenderPropertyBean> extend) {
        this.extend = extend;
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
}
