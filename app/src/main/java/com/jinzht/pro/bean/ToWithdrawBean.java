package com.jinzht.pro.bean;

/**
 * 易宝账户提现
 */
public class ToWithdrawBean {
    private String platformNo;// 商户编号，固定值
    private String platformUserNo;// 用户编号
    private String requestNo;// 请求流水号，时间+uid，例如：201603191203241064
    private String feeMode;// 固定值,PLATFORM或者USER
    private String callbackUrl;// 页面回跳URL
    private String notifyUrl;// 服务器通知URL

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
        return "ToWithdrawBean{" +
                "platformNo='" + platformNo + '\'' +
                ", platformUserNo='" + platformUserNo + '\'' +
                ", requestNo='" + requestNo + '\'' +
                ", feeMode='" + feeMode + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                '}';
    }
}
