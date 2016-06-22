package com.jinzht.pro.bean;

/**
 * 易宝投标资金明细记录
 */
public class ToTenderDetailBean {
    private String targetUserType;// 收款人用户类型，个人MEMBER,商户MERCHANT
    private String targetPlatformUserNo;// 收款人平台用户编号
    private String amount;// 转入金额;
    private String bizType;// 业务类型，投标TENDER,分润COMMISSION

    public ToTenderDetailBean() {
    }

    public ToTenderDetailBean(String targetUserType, String targetPlatformUserNo, String amount, String bizType) {
        this.targetUserType = targetUserType;
        this.targetPlatformUserNo = targetPlatformUserNo;
        this.amount = amount;
        this.bizType = bizType;
    }

    public String getTargetUserType() {
        return targetUserType;
    }

    public void setTargetUserType(String targetUserType) {
        this.targetUserType = targetUserType;
    }

    public String getTargetPlatformUserNo() {
        return targetPlatformUserNo;
    }

    public void setTargetPlatformUserNo(String targetPlatformUserNo) {
        this.targetPlatformUserNo = targetPlatformUserNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }
}
