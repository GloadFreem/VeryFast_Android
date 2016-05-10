package com.jinzht.pro1.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.view.CircleImageView;

/**
 * 登录页
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private CircleImageView loginIvUserimage;// 用户头像
    private EditText loginEdTel;// 电话号码输入框
    private EditText loginEdPassword;// 密码输入框
    private TextView login_tv_forget_password;// 忘记密码按钮
    private RelativeLayout loginRlLogin;// 登录按钮
    private RelativeLayout loginRlRegister;// 没有账号按钮
    private TextView loginTvWechat;// 微信登录按钮

    private Intent intent;

    @Override
    protected int getResourcesId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        loginIvUserimage = (CircleImageView) findViewById(R.id.login_iv_userimage);// 用户头像
        loginEdTel = (EditText) findViewById(R.id.login_ed_tel);// 电话号码输入框
        loginEdPassword = (EditText) findViewById(R.id.login_ed_password);// 密码输入框
        login_tv_forget_password = (TextView) findViewById(R.id.login_tv_forget_password);// 忘记密码按钮
        login_tv_forget_password.setOnClickListener(this);
        loginRlLogin = (RelativeLayout) findViewById(R.id.login_rl_login);// 登录按钮
        loginRlLogin.setOnClickListener(this);
        loginRlRegister = (RelativeLayout) findViewById(R.id.login_rl_register);// 没有账号按钮
        loginRlRegister.setOnClickListener(this);
        loginTvWechat = (TextView) findViewById(R.id.login_tv_wechat);// 微信登录按钮
        loginTvWechat.setOnClickListener(this);

        loginIvUserimage.setImageResource(R.drawable.ic_launcher);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_tv_forget_password:// 点击忘记密码，进入找回密码页面
                intent = new Intent(this, FindPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.login_rl_login:// 点击登录，进入主页
//                intent = new Intent(this,MainActivity.class);
//                startActivity(intent);
                SuperToastUtils.showSuperToast(this, 2, "点击了登录");
                break;
            case R.id.login_rl_register:// 点击没有账号，进入注册页面
                intent = new Intent(this, Register1Activity.class);
                startActivity(intent);
                break;
            case R.id.login_tv_wechat:// 点击微信登录，进入微信授权界面
                SuperToastUtils.showSuperToast(this, 2, "点击了微信登录");
                break;
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
