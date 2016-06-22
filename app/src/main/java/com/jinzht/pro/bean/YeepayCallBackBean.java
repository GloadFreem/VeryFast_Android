package com.jinzht.pro.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 易宝跳转回调
 */
@XStreamAlias("response")
public class YeepayCallBackBean {
    @XStreamAsAttribute
    private String platformNo;// 商户编号
    @XStreamAlias("requestNo")
    private String requestNo;// 请求流水号
    @XStreamAlias("service")
    private String service;// 服务名
    @XStreamAlias("code")
    private String code;// 状态码，1成功，0失败
    @XStreamAlias("description")
    private String description;// 描述

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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "YeepayCallBackBean{" +
                "platformNo='" + platformNo + '\'' +
                ", requestNo='" + requestNo + '\'' +
                ", service='" + service + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
