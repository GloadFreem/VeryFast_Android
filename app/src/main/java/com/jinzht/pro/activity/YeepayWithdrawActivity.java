package com.jinzht.pro.activity;

import android.content.Intent;
import android.util.Log;

import com.jinzht.pro.base.YeepayWebViewActivity;
import com.jinzht.pro.bean.ToWithdrawBean;
import com.jinzht.pro.utils.Constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 易宝提现
 */
public class YeepayWithdrawActivity extends YeepayWebViewActivity {

    @Override
    protected void setWebTitle() {
        tvTitle.setText("提现");
    }

    @Override
    protected void getRequest() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        time = time.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
        String requestNo = time + getIntent().getStringExtra("userId");

        ToWithdrawBean toWithdrawBean = new ToWithdrawBean();
        toWithdrawBean.setPlatformNo(Constant.PLATFORMNO);
        toWithdrawBean.setRequestNo(requestNo);
        toWithdrawBean.setCallbackUrl(Constant.BASE_URL + Constant.YEEPAY_CALLBACK);
        toWithdrawBean.setNotifyUrl(Constant.BASE_URL + Constant.YEEPAY_NOTIFY);
        toWithdrawBean.setPlatformUserNo("jinzht_0000_" + getIntent().getStringExtra("userId"));
        toWithdrawBean.setFeeMode("PLATFORM");// PLATFORM或者USER

        xStream.alias("request", ToWithdrawBean.class);// 重命名标签
        xStream.useAttributeFor(ToWithdrawBean.class, "platformNo");// 标记为属性
        String xml = xStream.toXML(toWithdrawBean);
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
        String postData = "req=" + request + "&sign=" + sign;
        Log.i("请求参数", request);
        webview.postUrl(Constant.YEEPAY_GATEWAY + Constant.YEEPAY_WITHDRAW, postData.getBytes());
    }

    @Override
    protected void saveSign(String signResult) {
        backSign = signResult;
        if (callBackBean != null && "1".equals(callBackBean.getCode())) {
            Intent intent = new Intent(mContext, AccountActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("TAG", "提现");
            startActivity(intent);
        }
    }
}
