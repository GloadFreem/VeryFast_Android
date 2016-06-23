package com.jinzht.pro.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.UserInfoBean;
import com.jinzht.pro.bean.YeepaySignBean;
import com.jinzht.pro.bean.YeepayUserInfoBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.thoughtworks.xstream.XStream;

import java.math.BigDecimal;

/**
 * 认投项目输入金额界面
 */
public class InvestActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private EditText edInputMoney;// 金额输入框
    private TextView btnPullDown;// 下拉选择领投跟投
    private Button btnPay;// 支付按钮

    private TextView pullDownTextView;// 下拉窗口的TextView
    private PopupWindow pullDownWindow;// 下拉窗口
    private boolean isShowing = false;// PopupWindow是否展开的标识
    private int type = 0;// 跟投0，领投1

    private UserInfoBean.DataBean userInfoBean;// 用户信息
    private String request;// 易宝请求体
    private String sign;// 签名

    @Override
    protected int getResourcesId() {
        return R.layout.activity_invest;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("认投项目");
        edInputMoney = (EditText) findViewById(R.id.invest_ed_input_money);// 金额输入框
        btnPullDown = (TextView) findViewById(R.id.invest_btn_pull_down);// 下拉选择领投跟投
        btnPullDown.setOnClickListener(this);
        btnPay = (Button) findViewById(R.id.invest_btn_pay);// 支付按钮
        btnPay.setOnClickListener(this);

        setPricePoint(edInputMoney);
    }

    // 保证金额输入框只能输入两位小数
    public static void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.invest_btn_pull_down:// 选择领投或者跟投
                if (isShowing) {// 如果是展开状态就合上
                    pullDownWindow.dismiss();
                    isShowing = false;
                } else {
                    isShowing = true;
                    if (pullDownWindow == null) {
                        initPopupWindow();
                    }
                    pullDownWindow.showAsDropDown(btnPullDown, 0, 0);
                    btnPullDown.setBackgroundResource(R.drawable.bg_invest_pull_down_ed);// 改变选中的TextView样式
                }
                break;
            case R.id.invest_btn_pay:// 支付
                switch (btnPullDown.getText().toString()) {
                    case "跟投":
                        type = 0;
                        break;
                    case "领投":
                        type = 1;
                        break;
                }
                if (StringUtils.isBlank(edInputMoney.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入投资金额");
                } else if (Double.parseDouble(edInputMoney.getText().toString()) < getIntent().getDoubleExtra("limitAmount", 0)) {
                    SuperToastUtils.showSuperToast(mContext, 2, "本项目最小投资额度不低于" + String.valueOf(getIntent().getDoubleExtra("limitAmount", 0)) + "万");
                } else {
                    if (StringUtils.isBlank(SharedPreferencesUtils.getUserId(mContext))) {
                        GetUserInfo getUserInfo = new GetUserInfo();
                        getUserInfo.execute();
                    } else {
                        GetSignTask getSignTask = new GetSignTask();
                        getSignTask.execute();
                    }
                }
                break;
        }
    }

    // 初始化下拉界面
    private void initPopupWindow() {
        pullDownTextView = (TextView) getLayoutInflater().inflate(R.layout.layout_invest_pulldown, null);
        // 点击下弹拉出的TextView，PopupWindow消失，两个TextView的内容交换
        pullDownTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = pullDownTextView.getText().toString();
                pullDownTextView.setText(btnPullDown.getText().toString());
                btnPullDown.setText(temp);
                pullDownWindow.dismiss();
                isShowing = false;
            }
        });
        // 初始化下拉窗口
        pullDownWindow = new PopupWindow(pullDownTextView, btnPullDown.getWidth(), btnPullDown.getHeight());
        pullDownWindow.setOutsideTouchable(false);
        pullDownWindow.setBackgroundDrawable(new BitmapDrawable());
        pullDownWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                btnPullDown.setBackgroundResource(R.drawable.bg_ed_wechat_code_orange);// 消失时选中的TextView改回原来的样式
                isShowing = false;
            }
        });
    }

    // 获取易宝签名
    private class GetSignTask extends AsyncTask<Void, Void, YeepaySignBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            request = "<request platformNo=\"" + Constant.PLATFORMNO + "\"><platformUserNo>" + "jinzht_0000_" + SharedPreferencesUtils.getUserId(mContext) + "</platformUserNo></request>";
        }

        @Override
        protected YeepaySignBean doInBackground(Void... params) {
            String body = "";
            Log.i("request", request);
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SIGN)),
                            "method", "sign",
                            "req", request,
                            "sign", "",
                            "type", "0",
                            Constant.BASE_URL + Constant.SIGN,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("签名", body);
                return FastJsonTools.getBean(body, YeepaySignBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(YeepaySignBean yeepaySignBean) {
            super.onPostExecute(yeepaySignBean);
            if (yeepaySignBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (yeepaySignBean.getStatus() == 200) {
                    sign = yeepaySignBean.getData().getSign();
                    IsRegisteredTask isRegisteredTask = new IsRegisteredTask();
                    isRegisteredTask.execute();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, yeepaySignBean.getMessage());
                }
            }
        }
    }

    // 获取用户信息
    private class GetUserInfo extends AsyncTask<Void, Void, UserInfoBean> {
        @Override
        protected UserInfoBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETUSERINFO)),
                            Constant.BASE_URL + Constant.GETUSERINFO,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("用户信息", body);
                return FastJsonTools.getBean(body, UserInfoBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(UserInfoBean bean) {
            super.onPostExecute(bean);
            if (bean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (bean.getStatus() == 200) {
                    SharedPreferencesUtils.saveUserId(mContext, String.valueOf(bean.getData().getUserId()));
                    userInfoBean = bean.getData();
                    if (StringUtils.isBlank(sign)) {
                        GetSignTask getSignTask = new GetSignTask();
                        getSignTask.execute();
                    } else {
                        // 进入易宝注册页面
                        Intent intent = new Intent(mContext, YeepayRegisterActivity.class);
//                        intent.putExtra("activity", "WantInvestActivity");
                        intent.putExtra("userId", String.valueOf(userInfoBean.getUserId()));
                        intent.putExtra("amount", edInputMoney.getText().toString());
                        intent.putExtra("profit", getIntent().getDoubleExtra("profit", 0));
                        intent.putExtra("borrower_user_no", getIntent().getStringExtra("borrower_user_no"));
                        intent.putExtra("projectId", getIntent().getStringExtra("projectId"));
                        intent.putExtra("abbrevName", getIntent().getStringExtra("abbrevName"));
                        intent.putExtra("fullName", getIntent().getStringExtra("fullName"));
                        intent.putExtra("type", String.valueOf(type));
                        intent.putExtra("img", getIntent().getStringExtra("img"));
                        intent.putExtra("name", userInfoBean.getAuthentics().get(0).getName());
                        intent.putExtra("idNo", userInfoBean.getAuthentics().get(0).getIdentiyCarNo());
                        intent.putExtra("telephone", userInfoBean.getTelephone());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        finish();
                    }
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, bean.getMessage());
                }
            }
        }
    }

    // 判断用户是否在易宝注册过
    private class IsRegisteredTask extends AsyncTask<Void, Void, YeepayUserInfoBean> {
        @Override
        protected YeepayUserInfoBean doInBackground(Void... params) {
            String body = "";
            YeepayUserInfoBean yeepayUserInfoBean = null;
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.yeepayPost(
                            "service", Constant.YEEPAY_ACCOUNTINFO,
                            "req", request,
                            "sign", sign,
                            Constant.YEEPAY_DIRECT
                    );
                    XStream xStream = new XStream();
                    xStream.processAnnotations(YeepayUserInfoBean.class);// 指定对应的Bean
                    yeepayUserInfoBean = (YeepayUserInfoBean) xStream.fromXML(body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return yeepayUserInfoBean;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(YeepayUserInfoBean bean) {
            super.onPostExecute(bean);
            if (bean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                Log.i("易宝账户信息", bean.toString());
                if ("1".equals(bean.getCode())) {
                    BigDecimal bigDecimal = new BigDecimal(Double.parseDouble(edInputMoney.getText().toString()) * 10000);
                    double v = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    if (v > Double.parseDouble(bean.getAvailableAmount())) {
                        Log.i("余额不足" + bean.getAvailableAmount(), "去充值");
                        Intent intent = new Intent(mContext, YeepayRechargeActivity.class);
                        intent.putExtra("userId", SharedPreferencesUtils.getUserId(mContext));
                        intent.putExtra("amount", edInputMoney.getText().toString());
                        intent.putExtra("profit", getIntent().getDoubleExtra("profit", 0));
                        intent.putExtra("borrower_user_no", getIntent().getStringExtra("borrower_user_no"));
                        intent.putExtra("projectId", getIntent().getStringExtra("projectId"));
                        intent.putExtra("abbrevName", getIntent().getStringExtra("abbrevName"));
                        intent.putExtra("fullName", getIntent().getStringExtra("fullName"));
                        intent.putExtra("type", String.valueOf(type));
                        intent.putExtra("img", getIntent().getStringExtra("img"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.i("余额充足" + bean.getAvailableAmount(), "去投标");
                        Intent intent = new Intent(mContext, YeepayTenderActivity.class);
                        intent.putExtra("userId", SharedPreferencesUtils.getUserId(mContext));
                        intent.putExtra("amount", edInputMoney.getText().toString());
                        intent.putExtra("profit", getIntent().getDoubleExtra("profit", 0));
                        intent.putExtra("borrower_user_no", getIntent().getStringExtra("borrower_user_no"));
                        intent.putExtra("projectId", getIntent().getStringExtra("projectId"));
                        intent.putExtra("abbrevName", getIntent().getStringExtra("abbrevName"));
                        intent.putExtra("fullName", getIntent().getStringExtra("fullName"));
                        intent.putExtra("type", String.valueOf(type));
                        intent.putExtra("img", getIntent().getStringExtra("img"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                } else if ("101".equals(bean.getCode())) {
                    Log.i("返回码101", "去注册");
                    GetUserInfo getUserInfo = new GetUserInfo();
                    getUserInfo.execute();
                } else {
                    // 提示用户出错
                    SuperToastUtils.showSuperToast(mContext, 2, bean.getDescription());
                }
            }
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
