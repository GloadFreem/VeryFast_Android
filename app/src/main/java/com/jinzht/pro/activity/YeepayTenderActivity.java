package com.jinzht.pro.activity;

import android.util.Log;

import com.jinzht.pro.base.YeepayWebViewActivity;
import com.jinzht.pro.bean.ToTenderBean;
import com.jinzht.pro.bean.ToTenderDetailBean;
import com.jinzht.pro.bean.ToTenderPropertyBean;
import com.jinzht.pro.utils.Constant;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 易宝投标
 */
public class YeepayTenderActivity extends YeepayWebViewActivity {

    @Override
    protected void setWebTitle() {
        tvTitle.setText("投标");
    }

    @Override
    protected void getRequest() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        time = time.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
        String requestNo = time + getIntent().getStringExtra("userId");

        ToTenderBean toTenderBean = new ToTenderBean();
        toTenderBean.setPlatformNo(Constant.PLATFORMNO);
        toTenderBean.setPlatformUserNo("jinzht_0000_" + getIntent().getStringExtra("userId"));
        toTenderBean.setRequestNo(requestNo);
        toTenderBean.setUserType("MEMBER");
        toTenderBean.setBizType("TENDER");
        toTenderBean.setCallbackUrl(Constant.BASE_URL + Constant.YEEPAY_CALLBACK);
        toTenderBean.setNotifyUrl(Constant.BASE_URL + Constant.YEEPAY_NOTIFY);

        double amount_totle = Double.parseDouble(getIntent().getStringExtra("amount")) * 10000.0000;
        double profit = getIntent().getDoubleExtra("profit", 0);// 分润比率
        double amount_platform = amount_totle * profit;// 平台分润金额
        double amount_user = amount_totle - amount_platform;// 借款人得到金额

        ToTenderDetailBean detailBean1 = new ToTenderDetailBean("MEMBER", getIntent().getStringExtra("borrower_user_no"), String.valueOf(amount_user), "TENDER");
        ToTenderDetailBean detailBean2 = new ToTenderDetailBean("MERCHANT", Constant.PLATFORMNO, String.valueOf(amount_platform), "COMMISSION");
        List<ToTenderDetailBean> details = new ArrayList<>();
        details.add(detailBean1);
        details.add(detailBean2);
        toTenderBean.setDetails(details);

        ToTenderPropertyBean propertyBean1 = new ToTenderPropertyBean("tenderOrderNo", "jinzht_project_" + getIntent().getStringExtra("projectId"));// 项目编号
        ToTenderPropertyBean propertyBean2 = new ToTenderPropertyBean("tenderName", getIntent().getStringExtra("abbrevName"));// 项目名称，公司简称
        ToTenderPropertyBean propertyBean3 = new ToTenderPropertyBean("tenderAmount", String.valueOf(amount_totle));// 项目金额
        ToTenderPropertyBean propertyBean4 = new ToTenderPropertyBean("tenderDescription", getIntent().getStringExtra("fullName"));// 项目描述信息，公司全称
        ToTenderPropertyBean propertyBean5 = new ToTenderPropertyBean("borrowerPlatformUserNo", getIntent().getStringExtra("borrower_user_no"));// 项目的借款人平台用户编号
        List<ToTenderPropertyBean> extend = new ArrayList<>();
        extend.add(propertyBean1);
        extend.add(propertyBean2);
        extend.add(propertyBean3);
        extend.add(propertyBean4);
        extend.add(propertyBean5);
        toTenderBean.setExtend(extend);

        xStream.alias("request", ToTenderBean.class);// 重命名标签
        xStream.useAttributeFor(ToTenderBean.class, "platformNo");// 将platformNo设置为属性
        xStream.alias("detail", ToTenderDetailBean.class);// 重命名标签
        xStream.alias("property", ToTenderPropertyBean.class);// 重命名标签
        xStream.useAttributeFor(ToTenderPropertyBean.class, "name");// 将name设置为属性
        xStream.useAttributeFor(ToTenderPropertyBean.class, "value");// 将value设置为属性

        String xml = xStream.toXML(toTenderBean);
        xml = xml.replaceAll("\\r", "");
        xml = xml.replaceAll("\\n", "");
        xml = xml.replaceAll(" <", "<");
        xml = xml.replaceAll("> ", ">");

        request = xml;

        GetSignTask getSignTask = new GetSignTask();
        getSignTask.execute();
    }

    @Override
    protected void loadUrl() {
        request = URLEncoder.encode(request);
        String postData = "req=" + request + "&sign=" + sign;
        Log.i("请求参数", request);
        webview.postUrl(Constant.YEEPAY_GATEWAY + Constant.YEEPAY_TENDE, postData.getBytes());
    }

    @Override
    protected void saveSign(String signResult) {
        backSign = signResult;
        if (callBackBean != null && "1".equals(callBackBean.getCode())) {

        }
    }

//    private class InvestTask extends AsyncTask<Void, Void, CommonBean> {
//
//    }
}