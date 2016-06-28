package com.jinzht.pro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.jinzht.pro.base.YeepayWebViewActivity;
import com.jinzht.pro.bean.CommonBean;
import com.jinzht.pro.bean.ToRechargeBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 易宝充值
 */
public class YeepayRechargeActivity extends YeepayWebViewActivity {

    private double amount_totle;

    @Override
    protected void setWebTitle() {
        tvTitle.setText("账户充值");
    }

    @Override
    protected void getRequest() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        time = time.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
        String requestNo = time + getIntent().getStringExtra("userId");


        if (getIntent().getDoubleExtra("recharge", 0) == 0) {
            if (getIntent().getBooleanExtra("isRecharge", false)) {
                amount_totle = Double.parseDouble(getIntent().getStringExtra("amount"));
            } else {
                amount_totle = Double.parseDouble(getIntent().getStringExtra("amount")) * 10000;
            }
        } else {
            amount_totle = getIntent().getDoubleExtra("recharge", 0);
        }

        ToRechargeBean toRechargeBean = new ToRechargeBean();
        toRechargeBean.setPlatformNo(Constant.PLATFORMNO);
        toRechargeBean.setPlatformUserNo("jinzht_0000_" + getIntent().getStringExtra("userId"));
        toRechargeBean.setRequestNo(requestNo);
        toRechargeBean.setAmount(String.format("%.2f", amount_totle));
        toRechargeBean.setFeeMode("PLATFORM");
        toRechargeBean.setCallbackUrl(Constant.BASE_URL + Constant.YEEPAY_CALLBACK);
        toRechargeBean.setNotifyUrl(Constant.BASE_URL + Constant.YEEPAY_NOTIFY);

        xStream.alias("request", ToRechargeBean.class);// 重命名标签
        xStream.useAttributeFor(ToRechargeBean.class, "platformNo");// 标记为属性
        String xml = xStream.toXML(toRechargeBean);
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
        webview.postUrl(Constant.YEEPAY_GATEWAY + Constant.YEEPAY_RECHARGE, postData.getBytes());
    }

    @Override
    protected void saveResult(String respResult) {
        super.saveResult(respResult);
        // 将充值信息提交到服务器
        RechargeTask rechargeTask = new RechargeTask();
        rechargeTask.execute();
    }

    @Override
    protected void saveSign(String signResult) {
        backSign = signResult;
        if (callBackBean != null && "1".equals(callBackBean.getCode())) {
            if (getIntent().getBooleanExtra("isRecharge", false)) {
                Intent intent = new Intent(mContext, AccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("TAG", "充值");
                startActivity(intent);
            } else {
                // 跳转到投标界面
                Intent intent = new Intent(mContext, YeepayTenderActivity.class);
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
            }
        }
    }

    // 将充值信息提交到服务器
    private class RechargeTask extends AsyncTask<Void, Void, CommonBean> {
        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.RECHARGE)),
                            "amount", String.valueOf(amount_totle),
                            "tradeCode", callBackBean.getRequestNo(),
                            Constant.BASE_URL + Constant.RECHARGE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("投资完成返回信息", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }
    }
}
