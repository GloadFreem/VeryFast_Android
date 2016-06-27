package com.jinzht.pro.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 易宝解绑后返回信息
 */
@XStreamAlias("response")
public class YibaoUnbindCallbackInfoBean {
    @XStreamAsAttribute
    private String platformNo;// 商户编号
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
        return "YibaoUnbindCallbackInfoBean{" +
                "platformNo='" + platformNo + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

