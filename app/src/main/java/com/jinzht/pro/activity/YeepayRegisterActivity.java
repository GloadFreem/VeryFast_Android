package com.jinzht.pro.activity;

import android.content.Intent;
import android.util.Log;

import com.jinzht.pro.base.YeepayWebViewActivity;
import com.jinzht.pro.bean.ToRegisterBean;
import com.jinzht.pro.utils.Constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 易宝账户注册
 */
public class YeepayRegisterActivity extends YeepayWebViewActivity {

    @Override
    protected void setWebTitle() {
        tvTitle.setText("易宝账户注册");
    }

    @Override
    protected void getRequest() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        time = time.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
        String requestNo = time + getIntent().getStringExtra("userId");

        ToRegisterBean toRegisterBean = new ToRegisterBean();
        toRegisterBean.setPlatformNo(Constant.PLATFORMNO);
        toRegisterBean.setPlatformUserNo("jinzht_0000_" + getIntent().getStringExtra("userId"));
        toRegisterBean.setRequestNo(requestNo);
        toRegisterBean.setRealName(getIntent().getStringExtra("name"));
        toRegisterBean.setIdCardType("G2_IDCARD");
        toRegisterBean.setIdCardNo(getIntent().getStringExtra("idNo"));
        toRegisterBean.setMobile(getIntent().getStringExtra("telephone"));
        toRegisterBean.setCallbackUrl(Constant.BASE_URL + Constant.YEEPAY_CALLBACK);
        toRegisterBean.setNotifyUrl(Constant.BASE_URL + Constant.YEEPAY_NOTIFY);

        xStream.alias("request", ToRegisterBean.class);// 重命名标签
        xStream.useAttributeFor(ToRegisterBean.class, "platformNo");// 标记为属性
        String xml = xStream.toXML(toRegisterBean);
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
        webview.postUrl(Constant.YEEPAY_GATEWAY + Constant.YEEPAY_REGISTER, postData.getBytes());
    }

    @Override
    protected void saveSign(String signResult) {
        backSign = signResult;
        if (callBackBean != null && "1".equals(callBackBean.getCode())) {
            Intent intent = new Intent();
            switch (getIntent().getStringExtra("TAG")) {
                // 跳转到充值界面
                case "InvestActivity":
                    intent.setClass(mContext, YeepayRechargeActivity.class);
                    intent.putExtra("userId", getIntent().getStringExtra("userId"));
                    intent.putExtra("amount", getIntent().getStringExtra("amount"));
                    intent.putExtra("profit", getIntent().getStringExtra("profit"));
                    intent.putExtra("borrower_user_no", getIntent().getStringExtra("borrower_user_no"));
                    intent.putExtra("projectId", getIntent().getStringExtra("projectId"));
                    intent.putExtra("abbrevName", getIntent().getStringExtra("abbrevName"));
                    intent.putExtra("fullName", getIntent().getStringExtra("fullName"));
                    intent.putExtra("type", getIntent().getStringExtra("type"));
                    intent.putExtra("img", getIntent().getStringExtra("img"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    break;
                // 从资金账户来，注册完后返回
                case "AccountActivity":
                    Intent intent1 = new Intent(mContext, AccountActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent1.putExtra("TAG", "注册");
                    startActivity(intent1);
                    break;
            }
        }
    }
}
