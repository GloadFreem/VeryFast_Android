package com.jinzht.pro1.bean;

import java.util.List;

/**
 * 投资能力列表
 */
public class CapacityListBean {

    /**
     * message : 登录成功！
     * status : 200
     * data : ["1, '(一)《私募投资基金监督管理暂行办法》规定的合格投资者'","2, '(二)投资单个融资项目的最低金额不低于100万元人民币的单位或个人","3, '(三)社会保障基金、企业年金等养老基金，慈善基金等社会公益基金，以及依法设立并在中国证券投资基金业协会备案的投资计划","4, '(四)净资产不低于1000万元人民币的单位","5, '(五)金融资产不低于300万元人民币或最近三年个人年均收入不低于50万元人民币的个人。上述个人除能提供相关财产、收入证明外，还应当能辨识、判断和承担相应投资风险","6, '(六)证券业协会规定的其他投资者"]
     */

    private String message;
    private int status;
    private List<String> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
