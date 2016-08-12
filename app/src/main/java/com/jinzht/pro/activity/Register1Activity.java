package com.jinzht.pro.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.CommonBean;
import com.jinzht.pro.bean.CustomerServiceBean;
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
 * 注册的第一个页面，输入手机号和验证码
 */
public class Register1Activity extends BaseActivity implements View.OnClickListener {

    private ImageButton register1BtBack;// 返回
    private ImageButton register1BtContactService;// 联系客服
    private EditText register1EdTel;// 手机号码输入框
    private EditText register1EdCode;// 验证码输入框
    private TextView register1TvGetcode;// 获取验证码
    private EditText register1EdInviteCode;// 指环码输入框
    private TextView register1TvOptional;// 指环码选填提示
    private Button register1BtRegister;// 注册按钮
    private CheckBox register1_cb_agreement;// 是否同意用户协议按钮
    private TextView register1TvUserAgreement;// 查看用户协议按钮

    private int timer = 60;// 60s倒计时，用于验证码

    @Override
    protected int getResourcesId() {
        return R.layout.activity_register1;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        register1BtBack = (ImageButton) findViewById(R.id.register1_bt_back);// 返回
        register1BtBack.setOnClickListener(this);
        register1BtContactService = (ImageButton) findViewById(R.id.register1_bt_contact_service);// 联系客服
        register1BtContactService.setOnClickListener(this);
        register1EdTel = (EditText) findViewById(R.id.register1_ed_tel);// 手机号码输入框
        register1EdCode = (EditText) findViewById(R.id.register1_ed_code);// 验证码输入框
        register1TvGetcode = (TextView) findViewById(R.id.register1_tv_getcode);// 获取验证码
        register1TvGetcode.setOnClickListener(this);
        register1EdInviteCode = (EditText) findViewById(R.id.register1_ed_invite_code);// 指环码输入框
        register1EdInviteCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    register1TvOptional.setVisibility(View.GONE);
                } else {
                    register1TvOptional.setVisibility(View.VISIBLE);
                }
            }
        });
        register1TvOptional = (TextView) findViewById(R.id.register1_tv_optional);// 指环码选填提示
        register1BtRegister = (Button) findViewById(R.id.register1_bt_register);// 注册按钮
        register1BtRegister.setOnClickListener(this);
        register1_cb_agreement = (CheckBox) findViewById(R.id.register1_cb_agreement);// 是否同意用户协议按钮
        register1TvUserAgreement = (TextView) findViewById(R.id.register1_tv_user_agreement);// 查看用户协议按钮
        register1TvUserAgreement.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register1_bt_back:// 返回上一页
                finish();
                break;
            case R.id.register1_bt_contact_service:// 打电话给客服
                if (clickable) {
                    clickable = false;
                    CustomerServiceTask customerServiceTask = new CustomerServiceTask();
                    customerServiceTask.execute();
                }
                break;
            case R.id.register1_tv_getcode:// 点击获取验证码
                if (StringUtils.isBlank(register1EdTel.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "手机号码不能为空");
                } else if (!StringUtils.isTel(register1EdTel.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入正确的手机号码");
                } else {
                    UiUtils.getHandler().postDelayed(runnable, 1000);
                    register1TvGetcode.setText(timer + "秒后重新发送");
                    register1TvGetcode.setBackgroundResource(R.drawable.bg_code_gray);
                    register1TvGetcode.setTextColor(getResources().getColor(R.color.custom_orange));
                    GetCodeTask getCodeTask = new GetCodeTask();
                    getCodeTask.execute();
                }
                break;
            case R.id.register1_bt_register:// 点击注册，跳转到注册的第二个页面
                if (register1_cb_agreement.isChecked()) {
                    if (StringUtils.isBlank(register1EdTel.getText().toString())) {
                        SuperToastUtils.showSuperToast(mContext, 2, "手机号码不能为空");
                    } else if (!StringUtils.isTel(register1EdTel.getText().toString())) {
                        SuperToastUtils.showSuperToast(mContext, 2, "请输入正确的手机号码");
                    } else if (StringUtils.isBlank(register1EdCode.getText().toString())) {
                        SuperToastUtils.showSuperToast(mContext, 2, "验证码不能为空");
                    } else {
                        Intent intent = new Intent(this, Register2Activity.class);
                        intent.putExtra("telephone", register1EdTel.getText().toString());
                        intent.putExtra("verifyCode", register1EdCode.getText().toString());
                        intent.putExtra("inviteCode", register1EdInviteCode.getText().toString());
                        startActivity(intent);
                    }
                } else {
                    SuperToastUtils.showSuperToast(this, 2, "您必须同意《金指投用户协议》才可以注册");
                }
                break;
            case R.id.register1_tv_user_agreement:// 点击查看用户协议，跳转到用户协议界面
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
                            "telephone", register1EdTel.getText().toString(),
                            "type", "0",
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
            register1TvGetcode.setClickable(false);
            timer--;
            register1TvGetcode.setText(timer + "秒后重新发送");
            UiUtils.getHandler().postDelayed(this, 1000);
            if (timer == 0) {
                timer = 60;
                register1TvGetcode.setText("获取验证码");
                register1TvGetcode.setBackgroundResource(R.drawable.bg_code_orange);
                register1TvGetcode.setTextColor(Color.WHITE);
                register1TvGetcode.setClickable(true);
                UiUtils.getHandler().removeCallbacks(this);
            }
        }
    };

    // 客服接口
    private class CustomerServiceTask extends AsyncTask<Void, Void, CustomerServiceBean> {
        @Override
        protected CustomerServiceBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.CUSTOMERSERVICE)),
                            Constant.BASE_URL + Constant.CUSTOMERSERVICE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("客服信息", body);
                return FastJsonTools.getBean(body, CustomerServiceBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CustomerServiceBean customerServiceBean) {
            super.onPostExecute(customerServiceBean);
            clickable = true;
            if (customerServiceBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (customerServiceBean.getStatus() == 200) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + customerServiceBean.getData().getTel());
                    intent.setData(data);
                    startActivity(intent);
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, customerServiceBean.getMessage());
                }
            }
        }
    }

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
