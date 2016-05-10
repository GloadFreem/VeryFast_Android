package com.jinzht.pro1.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 找回密码页面
 */
public class FindPasswordActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton findPasswordBtBack;// 返回
    private EditText findPasswordEdTel;// 电话号码输入框
    private EditText findPasswordEdCode;// 验证码输入框
    private TextView findPasswordTvGetCode;// 获取验证码按钮
    private Button findPasswordNext;// 下一步

    @Override
    protected int getResourcesId() {
        return R.layout.activity_find_password;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        findPasswordBtBack = (ImageButton) findViewById(R.id.find_password_bt_back);// 返回
        findPasswordBtBack.setOnClickListener(this);
        findPasswordEdTel = (EditText) findViewById(R.id.find_password_ed_tel);// 电话号码输入框
        findPasswordEdCode = (EditText) findViewById(R.id.find_password_ed_code);// 验证码输入框
        findPasswordTvGetCode = (TextView) findViewById(R.id.find_password_tv_getcode);// 获取验证码按钮
        findPasswordTvGetCode.setOnClickListener(this);
        findPasswordNext = (Button) findViewById(R.id.find_password_next);// 下一步
        findPasswordNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_password_bt_back:// 返回上一页
                finish();
                break;
            case R.id.find_password_tv_getcode:// 点击获取验证码
                SuperToastUtils.showSuperToast(this, 2, "获取验证码");
                findPasswordTvGetCode.setBackgroundResource(R.drawable.bg_code_gray);
                findPasswordTvGetCode.setTextColor(getResources().getColor(R.color.custom_orange));
                break;
            case R.id.find_password_next:// 点击下一步，跳转至设置密码界面
                Intent intent = new Intent(this, SetPasswordActivity.class);
                startActivity(intent);
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
