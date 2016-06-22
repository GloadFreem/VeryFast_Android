package com.jinzht.pro.bean;

/**
 * 易宝充值请求参数
 */
public class ToRechargeBean {
    private String platformNo;// 商户编号
    private String platformUserNo;// 平台用户编号
    private String requestNo;// 请求流水号
    private String amount;// 充值金额
    private String feeMode;// 费率模式，固定值PLATFORM
    private String callbackUrl;// 页面回跳URL
    private String notifyUrl;// 服务器通知URL

    public ToRechargeBean() {
    }

    public ToRechargeBean(String platformNo, String platformUserNo, String requestNo, String amount, String feeMode, String callbackUrl, String notifyUrl) {
        this.platformNo = platformNo;
        this.platformUserNo = platformUserNo;
        this.requestNo = requestNo;
        this.amount = amount;
        this.feeMode = feeMode;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFeeMode() {
        return feeMode;
    }

    public void setFeeMode(String feeMode) {
        this.feeMode = feeMode;
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
        return "ToRechargeBean{" +
                "platformNo='" + platformNo + '\'' +
                ", platformUserNo='" + platformUserNo + '\'' +
                ", requestNo='" + requestNo + '\'' +
                ", amount='" + amount + '\'' +
                ", feeMode='" + feeMode + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                '}';
    }
}
