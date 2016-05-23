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
 * 修改密码界面
 */
public class ChangePwdActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private EditText edPwdOld;// 旧密码输入框
    private EditText edPwdNew;// 新密码输入框
    private EditText edPwdConfirm;// 确认密码输入框
    private Button btnConfirm;// 确认按钮

    @Override
    protected int getResourcesId() {
        return R.layout.activity_change_pwd;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("修改登录密码");
        edPwdOld = (EditText) findViewById(R.id.change_pwd_ed_pwd_old);// 旧密码输入框
        edPwdNew = (EditText) findViewById(R.id.change_pwd_ed_pwd_new);// 新密码输入框
        edPwdConfirm = (EditText) findViewById(R.id.change_pwd_ed_pwd_confirm);// 确认密码输入框
        btnConfirm = (Button) findViewById(R.id.change_pwd_btn_confirm);// 确认按钮
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.change_pwd_btn_confirm:// 确认
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
