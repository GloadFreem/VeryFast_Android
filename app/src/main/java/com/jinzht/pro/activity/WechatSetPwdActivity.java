package com.jinzht.pro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.RegisterBean;
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

import cn.jpush.android.api.JPushInterface;

/**
 * 微信登录用户设置密码界面
 */
public class WechatSetPwdActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private EditText edPwd1;// 设置密码输入框
    private EditText edPwd2;// 确认密码输入框
    private Button btnConfirm;// 确认按钮

    @Override
    protected int getResourcesId() {
        return R.layout.activity_wechat_set_pwd;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        edPwd1 = (EditText) findViewById(R.id.wechat_setpwd_ed1);// 设置密码输入框
        edPwd2 = (EditText) findViewById(R.id.wechat_setpwd_ed2);// 确认密码输入框
        btnConfirm = (Button) findViewById(R.id.wechat_setpwd_btn_confirm);// 确认按钮
        btnConfirm.setOnClickListener(this);

        tvTitle.setText(getResources().getString(R.string.set_password));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.wechat_setpwd_btn_confirm:// 点击确认,进入主页
                if (StringUtils.isBlank(edPwd1.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入符合规范的密码");
                } else if (StringUtils.length(edPwd1.getText().toString()) < 6 || StringUtils.length(edPwd1.getText().toString()) > 20) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入符合规范的密码");
                } else if (StringUtils.isBlank(edPwd2.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "确认密码不能为空");
                } else if (!edPwd1.getText().toString().equals(edPwd2.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "密码不一致");
                } else {
                    RegisterTask registerTask = new RegisterTask();
                    registerTask.execute();
                }
                break;
        }
    }

    // 注册接口
    private class RegisterTask extends AsyncTask<Void, Void, RegisterBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected RegisterBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    String pwd = MD5Utils.encode(edPwd1.getText().toString() + getIntent().getStringExtra("telephone").trim() + "lindyang");
                    body = OkHttpUtils.registerPost(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.WECHATREGISTER)),
                            "telephone", getIntent().getStringExtra("telephone"),
                            "password", pwd,
                            "verifyCode", getIntent().getStringExtra("verifyCode"),
                            "inviteCode", getIntent().getStringExtra("inviteCode"),
                            "regId", JPushInterface.getRegistrationID(mContext),
                            Constant.BASE_URL + Constant.WECHATREGISTER,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("注册信息", body);
                return FastJsonTools.getBean(body, RegisterBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(RegisterBean registerBean) {
            super.onPostExecute(registerBean);
            dismissProgressDialog();
            if (registerBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (registerBean.getStatus() == 200) {
                    String pwd = null;
                    try {
                        pwd = MD5Utils.encode(edPwd1.getText().toString() + getIntent().getStringExtra("telephone").trim() + "lindyang");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SharedPreferencesUtils.saveInformation(mContext, getIntent().getStringExtra("telephone"), pwd);
                    SharedPreferencesUtils.saveExtUserId(mContext, String.valueOf(registerBean.getData().getUserId()));
                    SharedPreferencesUtils.saveUserId(mContext, registerBean.getData().getUserId());
                    SharedPreferencesUtils.setIsWechatLogin(mContext, false);
                    Intent intent = new Intent(mContext, CertificationIDCardActivity.class);
                    intent.putExtra("usertype", getIntent().getIntExtra("usertype", -1));
                    startActivity(intent);
                    finish();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, registerBean.getMessage());
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
