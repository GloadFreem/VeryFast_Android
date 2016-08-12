package com.jinzht.pro.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.CommonBean;
import com.jinzht.pro.bean.WebBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.utils.UiUtils;

/**
 * 微信登录的手机验证
 */
public class WechatVerifyActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private EditText edTel;// 手机号码输入框
    private EditText edCode;// 验证码输入框
    private TextView btnCode;// 获取验证码按钮
    private EditText edInviteCode;// 指环码输入框
    private Button btnNext;// 下一步按钮
    private CheckBox cbAgree;// 同意用户协议选择框
    private TextView tvUserAgreement;// 用户协议

    private int timer = 60;// 60s倒计时，用于验证码

    @Override
    protected int getResourcesId() {
        return R.layout.activity_wechat_verify;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("手机验证");
        edTel = (EditText) findViewById(R.id.wechat_verify_ed_tel);// 手机号码输入框
        edCode = (EditText) findViewById(R.id.wechat_verify_ed_code);// 验证码输入框
        btnCode = (TextView) findViewById(R.id.wechat_verify_tv_code);// 获取验证码按钮
        btnCode.setOnClickListener(this);
        edInviteCode = (EditText) findViewById(R.id.wechat_verify_ed_invite_code);// 指环码输入框
        btnNext = (Button) findViewById(R.id.wechat_verify_btn_next);// 下一步按钮
        btnNext.setOnClickListener(this);
        cbAgree = (CheckBox) findViewById(R.id.wechat_verify_cb_agree);// 同意用户协议选择框
        tvUserAgreement = (TextView) findViewById(R.id.wechat_verify_tv_user_agreement);// 用户协议
        tvUserAgreement.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.wechat_verify_tv_code:// 点击获取验证码
                if (StringUtils.isBlank(edTel.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "手机号码不能为空");
                } else if (!StringUtils.isTel(edTel.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入正确的手机号码");
                } else {
                    UiUtils.getHandler().postDelayed(runnable, 1000);
                    btnCode.setText(timer + "秒后重新发送");
                    btnCode.setBackgroundResource(R.drawable.bg_code_gray);
                    btnCode.setTextColor(getResources().getColor(R.color.custom_orange));
                    GetCodeTask getCodeTask = new GetCodeTask();
                    getCodeTask.execute();
                }
                break;
            case R.id.wechat_verify_btn_next:// 跳转到下一步界面
                if (cbAgree.isChecked()) {
                    if (StringUtils.isBlank(edTel.getText().toString())) {
                        SuperToastUtils.showSuperToast(mContext, 2, "手机号码不能为空");
                    } else if (!StringUtils.isTel(edTel.getText().toString())) {
                        SuperToastUtils.showSuperToast(mContext, 2, "请输入正确的手机号码");
                    } else if (StringUtils.isBlank(edCode.getText().toString())) {
                        SuperToastUtils.showSuperToast(mContext, 2, "验证码不能为空");
                    } else {
                        Intent intent = new Intent(this, WechatSetPwdActivity.class);
                        intent.putExtra("telephone", edTel.getText().toString());
                        intent.putExtra("verifyCode", edCode.getText().toString());
                        intent.putExtra("inviteCode", edInviteCode.getText().toString());
                        intent.putExtra("usertype", getIntent().getIntExtra("usertype", -1));
                        startActivity(intent);
                    }
                } else {
                    SuperToastUtils.showSuperToast(this, 2, "您必须同意《金指投用户协议》才可以注册");
                }
                break;
            case R.id.wechat_verify_tv_user_agreement:// 点击查看用户协议，跳转到用户协议界面
                if (clickable) {
                    clickable = false;
                    UserProtocolTask userProtocolTask = new UserProtocolTask();
                    userProtocolTask.execute();
                }
                break;
        }
    }

    // 获取验证码
    private class GetCodeTask extends AsyncTask<Void, Void, CommonBean> {
        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETVERIFYCODE)),
                            "telephone", edTel.getText().toString(),
                            "type", "2",
                            Constant.BASE_URL + Constant.GETVERIFYCODE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("验证码信息", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            if (commonBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                SuperToastUtils.showSuperToast(mContext, 2, commonBean.getMessage());
            }
        }
    }

    // 倒计时
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            btnCode.setClickable(false);
            timer--;
            btnCode.setText(timer + "秒后重新发送");
            UiUtils.getHandler().postDelayed(this, 1000);
            if (timer == 0) {
                timer = 60;
                btnCode.setText("获取验证码");
                btnCode.setBackgroundResource(R.drawable.bg_code_orange);
                btnCode.setTextColor(Color.WHITE);
                btnCode.setClickable(true);
                UiUtils.getHandler().removeCallbacks(this);
            }
        }
    };

    // 用户协议
    private class UserProtocolTask extends AsyncTask<Void, Void, WebBean> {
        @Override
        protected WebBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.USERPROTOCOL)),
                            Constant.BASE_URL + Constant.USERPROTOCOL,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("用户协议", body);
                return FastJsonTools.getBean(body, WebBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(WebBean webBean) {
            super.onPostExecute(webBean);
            clickable = true;
            if (webBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (webBean.getStatus() == 200) {
                    if (!StringUtils.isBlank(webBean.getData().getUrl())) {
                        Intent intent = new Intent(mContext, CommonWebViewActivity.class);
                        intent.putExtra("title", "用户协议");
                        intent.putExtra("url", webBean.getData().getUrl());
                        startActivity(intent);
                    }
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, webBean.getMessage());
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
