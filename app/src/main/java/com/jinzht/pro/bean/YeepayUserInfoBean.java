package com.jinzht.pro.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 易宝账户查询返回信息
 */
@XStreamAlias("response")
public class YeepayUserInfoBean {
    @XStreamAsAttribute
    private String platformNo;// 商户编号
    @XStreamAlias("code")
    private String code;// 状态码，1成功，0失败，101用户不存在
    @XStreamAlias("description")
    private String description;// 描述信息
    @XStreamAlias("memberType")
    private String memberType;// 会员类型，PERSONAL个人会员，ENTERPRISE企业会员，GUARANTEE_CORP，担保公司
    @XStreamAlias("activeStatus")
    private String activeStatus;// 会员激活状态，ACTIVATED，已激活，DEACTIVATED，未激活
    @XStreamAlias("balance")
    private String balance;// 账户余额
    @XStreamAlias("availableAmount")
    private String availableAmount;// 可用余额
    @XStreamAlias("freezeAmount")
    private String freezeAmount;// 冻结金额
    @XStreamAlias("cardNo")
    private String cardNo;// 绑定的卡号，没有则表示没有绑卡
    @XStreamAlias("cardStatus")
    private String cardStatus;// 绑卡状态，VERIFIED
    @XStreamAlias("bank")
    private String bank;// 银行代码
    @XStreamAlias("autoTender")
    private String autoTender;// 是否已授权自动投标,true 或则false
    @XStreamAlias("autoRepayment")
    private String autoRepayment;// 是否自动还款
    @XStreamAlias("paySwift")
    private String paySwift;// 表示用户是否已开通快捷支付。NORMAL 表示未升级，UPGRADE 表示已升级
    @XStreamAlias("bindMobileNo")
    private String bindMobileNo;// 平台会员手机号

    public YeepayUserInfoBean() {
    }

    public YeepayUserInfoBean(String platformNo, String code, String description, String memberType, String activeStatus, String balance, String availableAmount, String freezeAmount, String cardNo, String cardStatus, String bank, String autoTender, String autoRepayment, String paySwift, String bindMobileNo) {
        this.platformNo = platformNo;
        this.code = code;
        this.description = description;
        this.memberType = memberType;
        this.activeStatus = activeStatus;
        this.balance = balance;
        this.availableAmount = availableAmount;
        this.freezeAmount = freezeAmount;
        this.cardNo = cardNo;
        this.cardStatus = cardStatus;
        this.bank = bank;
        this.autoTender = autoTender;
        this.autoRepayment = autoRepayment;
        this.paySwift = paySwift;
        this.bindMobileNo = bindMobileNo;
    }

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

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(String availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(String freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAutoTender() {
        return autoTender;
    }

    public void setAutoTender(String autoTender) {
        this.autoTender = autoTender;
    }

    public String getAutoRepayment() {
        return autoRepayment;
    }

    public void setAutoRepayment(String autoRepayment) {
        this.autoRepayment = autoRepayment;
    }

    public String getPaySwift() {
        return paySwift;
    }

    public void setPaySwift(String paySwift) {
        this.paySwift = paySwift;
    }

    public String getBindMobileNo() {
        return bindMobileNo;
    }

    public void setBindMobileNo(String bindMobileNo) {
        this.bindMobileNo = bindMobileNo;
    }

    @Override
    public String toString() {
        return "YeepayUserInfoBean{" +
                "platformNo='" + platformNo + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", memberType='" + memberType + '\'' +
                ", activeStatus='" + activeStatus + '\'' +
                ", balance='" + balance + '\'' +
                ", availableAmount='" + availableAmount + '\'' +
                ", freezeAmount='" + freezeAmount + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", cardStatus='" + cardStatus + '\'' +
                ", bank='" + bank + '\'' +
                ", autoTender='" + autoTender + '\'' +
                ", autoRepayment='" + autoRepayment + '\'' +
                ", paySwift='" + paySwift + '\'' +
                ", bindMobileNo='" + bindMobileNo + '\'' +
                '}';
    }
}
