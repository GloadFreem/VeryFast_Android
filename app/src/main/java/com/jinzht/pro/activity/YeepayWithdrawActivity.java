package com.jinzht.pro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.jinzht.pro.base.YeepayWebViewActivity;
import com.jinzht.pro.bean.CommonBean;
import com.jinzht.pro.bean.ToWithdrawBean;
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
    protected void saveResult(String respResult) {
        super.saveResult(respResult);
        // 将提现信息提交到服务器
        WithdrawTask withdrawTask = new WithdrawTask();
        withdrawTask.execute();
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

    // 将充值信息提交到服务器
    private class WithdrawTask extends AsyncTask<Void, Void, CommonBean> {
        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.WITHDRAW)),
                            "tradeCode", callBackBean.getRequestNo(),
                            Constant.BASE_URL + Constant.WITHDRAW,
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
