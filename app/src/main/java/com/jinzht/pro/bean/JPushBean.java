package com.jinzht.pro.bean;

/**
 * 极光推送收到的消息
 */
public class JPushBean {

    /**
     * type : web
     * content : http://www.jinzht.com
     * ext : 成功
     */

    private String type;
    private String content;
    private String ext;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    //    推送消息类型（type）分为：根据type字段进行相应业务逻辑。
//    1."web"：Web 链接推送，收到消息后，获取url字段，判断不为空时直接使用webview加载url
//    2."project"：项目推送，收到消息后，获取projectId字段，判断不为空时直接走项目详情逻辑（注意：该处项目为路演项目，请使用路演项目逻辑）
//    3."authentic"：认证进度，当收到该消息时，直接走认证身份查看逻辑，同时，站内信消息为有新消息，站内信中会有认证状况信息
//    4."invest"：投资状况，当收到该消息时，直接走投资查看业务，同时，站内信消息为新消息，站内信中有投资状况消息
//    5."withdraw":提现到账情况，当收到该消息时，直接走交易账单业务，同时，站内信为新消息，站内信有提现到账状况信息
//    6."goldcon":金条信息，当收到该消息时，直接走金条交易明细业务，同时，站内信为新消息，站内信有金条状况信息
//    7."system":系统通知，当收到该消息时，直接走站内信业务，查看站内信消息业务
//    8."action":活动推送，当收到该消息时，获取actionId字段，判断不为空时直接走活动详情业务逻辑
//    注意：除1,2,8之外，收到所有消息时，都将首页站内信信息设为“标红”状态，提示用户有新消息
}
