package com.jinzht.pro.bean;

/**
 * 易宝投标扩展参数
 */
public class ToTenderPropertyBean {
    private String name;
    private String value;

    public ToTenderPropertyBean() {
    }

    public ToTenderPropertyBean(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
