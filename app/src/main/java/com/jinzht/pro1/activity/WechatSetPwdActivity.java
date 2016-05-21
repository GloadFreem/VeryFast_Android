package com.jinzht.pro1.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 微信登录用户设置密码界面
 */
public class WechatSetPwdActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private EditText wechatSetpwdEd1;// 设置密码输入框
    private EditText wechatSetpwdEd2;// 确认密码输入框
    private Button wechatSetpwdBtnConfirm;// 确认按钮

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
        wechatSetpwdEd1 = (EditText) findViewById(R.id.wechat_setpwd_ed1);// 设置密码输入框
        wechatSetpwdEd2 = (EditText) findViewById(R.id.wechat_setpwd_ed2);// 确认密码输入框
        wechatSetpwdBtnConfirm = (Button) findViewById(R.id.wechat_setpwd_btn_confirm);// 确认按钮
        wechatSetpwdBtnConfirm.setOnClickListener(this);

        tvTitle.setText(getResources().getString(R.string.set_password));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.wechat_setpwd_btn_confirm:// 点击确认,进入主页
                SuperToastUtils.showSuperToast(this, 2, "确认");
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
