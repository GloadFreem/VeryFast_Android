package com.jinzht.pro.activity;

import android.content.Intent;
import android.util.Log;

import com.jinzht.pro.base.YeepayWebViewActivity;
import com.jinzht.pro.bean.ToBindCardBean;
import com.jinzht.pro.utils.Constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 易宝绑定银行卡
 */
public class YeepayBindCardActivity extends YeepayWebViewActivity {

    public final static int RESULT_CODE = 0;

    @Override
    protected void setWebTitle() {
        tvTitle.setText("绑定银行卡");
    }

    @Override
    protected void getRequest() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        time = time.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
        String requestNo = time + getIntent().getStringExtra("userId");

        ToBindCardBean toBindCardBean = new ToBindCardBean();
        toBindCardBean.setPlatformNo(Constant.PLATFORMNO);
        toBindCardBean.setRequestNo(requestNo);
        toBindCardBean.setCallbackUrl(Constant.BASE_URL + Constant.YEEPAY_CALLBACK);
        toBindCardBean.setNotifyUrl(Constant.BASE_URL + Constant.YEEPAY_NOTIFY);
        toBindCardBean.setPlatformUserNo("jinzht_0000_" + getIntent().getStringExtra("userId"));

        xStream.alias("request", ToBindCardBean.class);// 重命名标签
        xStream.useAttributeFor(ToBindCardBean.class, "platformNo");// 标记为属性
        String xml = xStream.toXML(toBindCardBean);
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
        webview.postUrl(Constant.YEEPAY_GATEWAY + Constant.YEEPAY_BIND, postData.getBytes());
    }

    @Override
    protected void saveSign(String signResult) {
        backSign = signResult;
        if (callBackBean != null && "1".equals(callBackBean.getCode())) {
            Intent intent = new Intent();
            intent.putExtra("needRefresh", true);
            setResult(RESULT_CODE, intent);
            finish();
        }
    }
}
