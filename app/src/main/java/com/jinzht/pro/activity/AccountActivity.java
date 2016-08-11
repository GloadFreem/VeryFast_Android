package com.jinzht.pro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.adapter.AccountAdapter;
import com.jinzht.pro.adapter.RecyclerViewData;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.UserInfoBean;
import com.jinzht.pro.bean.YeepaySignBean;
import com.jinzht.pro.bean.YeepayUserInfoBean;
import com.jinzht.pro.callback.ItemClickListener;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.MoneyFormat;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.thoughtworks.xstream.XStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 资金账户界面
 */
public class AccountActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView totleBalanceLower;// 小写账户余额
    private TextView totleBalanceUpper;// 大写账户余额
    private TextView availableLower;// 小写可用余额
    private TextView availableUpper;// 大写可用余额
    private TextView freezeLower;// 小写冻结资金
    private TextView freezeUpper;// 大写冻结资金
    private RecyclerView gvItems;// item的网格

    private String str;// 转换字体的临时字符串
    private SpannableString span;// 设置TextView不同字体

    private List<Integer> icons;// 填充的item图标
    private List<String> names;// 填充的item名
    private AccountAdapter rvAdaper;// GridView填充器

    private UserInfoBean.DataBean userInfo;// 用户信息
    private String request;// 易宝请求体
    private String sign;// 签名
    private YeepayUserInfoBean yeepayUserInfo;// 用户易宝账户信息

    private final static int REQUEST_CODE = 1;

    @Override
    protected int getResourcesId() {
        return R.layout.activity_account;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        btnBack = (LinearLayout) findViewById(R.id.account_btn_back);// 返回
        btnBack.setOnClickListener(this);

        totleBalanceLower = (TextView) findViewById(R.id.account_tv_totle_balance_lower);// 小写账户余额
        str = totleBalanceLower.getText().toString();
        span = new SpannableString(str);
        span.setSpan(new AbsoluteSizeSpan(16, true), str.length() - 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        totleBalanceLower.setText(span);
        totleBalanceUpper = (TextView) findViewById(R.id.account_tv_totle_balance_upper);// 大写账户余额

        availableLower = (TextView) findViewById(R.id.account_tv_available_lower);// 小写可用余额
        str = availableLower.getText().toString();
        span = new SpannableString(str);
        span.setSpan(new AbsoluteSizeSpan(12, true), str.length() - 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        availableLower.setText(span);
        availableUpper = (TextView) findViewById(R.id.account_tv_available_upper);// 大写可用余额

        freezeLower = (TextView) findViewById(R.id.account_tv_freeze_lower);// 小写冻结资金
        str = freezeLower.getText().toString();
        span = new SpannableString(str);
        span.setSpan(new AbsoluteSizeSpan(12, true), str.length() - 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        freezeLower.setText(span);
        freezeUpper = (TextView) findViewById(R.id.account_tv_freeze_upper);// 大写冻结资金

        gvItems = (RecyclerView) findViewById(R.id.account_gv_items);// item的网格

        // 填充GridView
        initGridView();

        EventBus.getDefault().register(this);
    }

    private void initGridView() {
        // 准备数据
        icons = new ArrayList<>(Arrays.asList(R.mipmap.icon_card, R.mipmap.icon_recharge, R.mipmap.icon_bill, R.mipmap.icon_withdraw));
        names = new ArrayList<>(Arrays.asList("银行卡", "账户充值", "交易账单", "资金提现"));
        rvAdaper = new AccountAdapter(mContext, icons, names);
        // 填充数据
        RecyclerViewData.setGrid(gvItems, mContext, rvAdaper, 2);
        // 条目点击事件
        rvAdaper.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:// 银行卡
                        if (yeepayUserInfo == null) {
                            SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                        } else if ("101".equals(yeepayUserInfo.getCode())) {// 没注册，去注册
                            intent.setClass(mContext, YeepayRegisterActivity.class);
                            intent.putExtra("TAG", "AccountActivity");
                            intent.putExtra("userId", String.valueOf(userInfo.getExtUserId()));
                            intent.putExtra("name", userInfo.getAuthentics().get(0).getName());
                            intent.putExtra("idNo", userInfo.getAuthentics().get(0).getIdentiyCarNo());
                            intent.putExtra("telephone", userInfo.getTelephone());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(intent, REQUEST_CODE);
                        } else if ("1".equals(yeepayUserInfo.getCode())) {// 已注册，去绑卡
                            intent.setClass(mContext, BankCardActivity.class);
                            intent.putExtra("bankNo", yeepayUserInfo.getCardNo());
                            intent.putExtra("bankName", yeepayUserInfo.getBank());
                            intent.putExtra("userId", String.valueOf(userInfo.getExtUserId()));
                            startActivity(intent);
                        } else {// 提示错误
                            SuperToastUtils.showSuperToast(mContext, 2, yeepayUserInfo.getDescription());
                        }
                        break;
                    case 1:// 账户充值
                        if (yeepayUserInfo == null) {
                            SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                        } else if ("101".equals(yeepayUserInfo.getCode())) {// 没注册，去注册
                            intent.setClass(mContext, YeepayRegisterActivity.class);
                            intent.putExtra("TAG", "AccountActivity");
                            intent.putExtra("userId", String.valueOf(userInfo.getExtUserId()));
                            intent.putExtra("name", userInfo.getAuthentics().get(0).getName());
                            intent.putExtra("idNo", userInfo.getAuthentics().get(0).getIdentiyCarNo());
                            intent.putExtra("telephone", userInfo.getTelephone());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(intent, REQUEST_CODE);
                        } else if ("1".equals(yeepayUserInfo.getCode())) {// 已注册，去充值
                            intent.setClass(mContext, RechargeActivity.class);
                            intent.putExtra("userId", String.valueOf(userInfo.getExtUserId()));
                            startActivity(intent);
                        }
                        break;
                    case 2:// 交易账单
                        intent.setClass(mContext, BillActivity.class);
                        startActivity(intent);
                        break;
                    case 3:// 资金提现
                        if (yeepayUserInfo == null) {
                            SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                        } else if ("101".equals(yeepayUserInfo.getCode())) {// 没注册，去注册
                            intent.setClass(mContext, YeepayRegisterActivity.class);
                            intent.putExtra("TAG", "AccountActivity");
                            intent.putExtra("userId", String.valueOf(userInfo.getExtUserId()));
                            intent.putExtra("name", userInfo.getAuthentics().get(0).getName());
                            intent.putExtra("idNo", userInfo.getAuthentics().get(0).getIdentiyCarNo());
                            intent.putExtra("telephone", userInfo.getTelephone());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(intent, REQUEST_CODE);
                        } else if ("1".equals(yeepayUserInfo.getCode())) {// 已注册，去提现
                            intent.setClass(mContext, YeepayWithdrawActivity.class);
                            intent.putExtra("userId", String.valueOf(userInfo.getExtUserId()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                        break;
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemSubViewClick(View view, int position) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void getUserInfo(UserInfoBean.DataBean bean) {
        userInfo = bean;
        if (userInfo != null) {
            GetSignTask getSignTask = new GetSignTask();
            getSignTask.execute();
        }
    }

    private void initData() {
        try {
            str = yeepayUserInfo.getBalance() + "元";
            span = new SpannableString(str);
            span.setSpan(new AbsoluteSizeSpan(16, true), str.length() - 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            totleBalanceLower.setText(span);
            if ("0.00".equals(yeepayUserInfo.getBalance())) {
                totleBalanceUpper.setText("零元");
            } else {
                totleBalanceUpper.setText(MoneyFormat.digitUppercase(yeepayUserInfo.getBalance()));
            }

            str = yeepayUserInfo.getAvailableAmount() + "元";
            span = new SpannableString(str);
            span.setSpan(new AbsoluteSizeSpan(12, true), str.length() - 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            availableLower.setText(span);
            if ("0.00".equals(yeepayUserInfo.getAvailableAmount())) {
                availableUpper.setText("零元");
            } else {
                availableUpper.setText(MoneyFormat.digitUppercase(yeepayUserInfo.getAvailableAmount()));
            }

            str = yeepayUserInfo.getFreezeAmount() + "元";
            span = new SpannableString(str);
            span.setSpan(new AbsoluteSizeSpan(12, true), str.length() - 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            freezeLower.setText(span);
            if ("0.00".equals(yeepayUserInfo.getFreezeAmount())) {
                freezeUpper.setText("零元");
            } else {
                freezeUpper.setText(MoneyFormat.digitUppercase(yeepayUserInfo.getFreezeAmount()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_btn_back:// 返回上一页
                finish();
                break;
        }
    }

    // 获取易宝账户查询签名
    private class GetSignTask extends AsyncTask<Void, Void, YeepaySignBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
            request = "<request platformNo=\"" + Constant.PLATFORMNO + "\"><platformUserNo>" + "jinzht_0000_" + userInfo.getExtUserId() + "</platformUserNo></request>";
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
                            "type", "1",
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
                dismissProgressDialog();
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (yeepaySignBean.getStatus() == 200) {
                    sign = yeepaySignBean.getData().getSign();
                    GetYeepayAccount getYeepayAccount = new GetYeepayAccount();
                    getYeepayAccount.execute();
                } else {
                    dismissProgressDialog();
                    SuperToastUtils.showSuperToast(mContext, 2, yeepaySignBean.getMessage());
                }
            }
        }
    }

    // 获取易宝账户信息
    private class GetYeepayAccount extends AsyncTask<Void, Void, YeepayUserInfoBean> {
        @Override
        protected YeepayUserInfoBean doInBackground(Void... params) {
            String body = "";
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
                    yeepayUserInfo = (YeepayUserInfoBean) xStream.fromXML(body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return yeepayUserInfo;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(YeepayUserInfoBean bean) {
            super.onPostExecute(bean);
            dismissProgressDialog();
            if (bean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                Log.i("易宝账户信息", bean.toString());
                if ("1".equals(bean.getCode())) {
                    if (yeepayUserInfo != null) {
                        initData();
                    }
                } else if ("101".equals(bean.getCode())) {
                    Log.i("返回码101", "去注册");
                    Intent intent = new Intent(mContext, YeepayRegisterActivity.class);
                    intent.putExtra("TAG", "AccountActivity");
                    intent.putExtra("userId", String.valueOf(userInfo.getExtUserId()));
                    intent.putExtra("name", userInfo.getAuthentics().get(0).getName());
                    intent.putExtra("idNo", userInfo.getAuthentics().get(0).getIdentiyCarNo());
                    intent.putExtra("telephone", userInfo.getTelephone());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    // 提示用户出错
                    SuperToastUtils.showSuperToast(mContext, 2, bean.getDescription());
                }
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ("注册".equals(intent.getStringExtra("TAG")) || "充值".equals(intent.getStringExtra("TAG")) || "提现".equals(intent.getStringExtra("TAG"))) {
            GetSignTask getSignTask = new GetSignTask();
            getSignTask.execute();
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
