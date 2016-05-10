package com.jinzht.pro1.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 设置密码页面
 */
public class SetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton setPasswordBtBack;// 返回
    private EditText setEdPassword1;// 设置密码输入框
    private EditText setEdPassword2;// 确认密码输入框
    private Button setPasswordConfirm;// 确认按钮

    @Override
    protected int getResourcesId() {
        return R.layout.activity_set_password;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        setPasswordBtBack = (ImageButton) findViewById(R.id.set_password_bt_back);// 返回
        setPasswordBtBack.setOnClickListener(this);
        setEdPassword1 = (EditText) findViewById(R.id.set_ed_password1);// 设置密码输入框
        setEdPassword2 = (EditText) findViewById(R.id.set_ed_password2);// 确认密码输入框
        setPasswordConfirm = (Button) findViewById(R.id.set_password_confirm);// 确认按钮
        setPasswordConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_password_bt_back:// 返回上一页
                finish();
                break;
            case R.id.set_password_confirm:// 确认密码，进入首页
                SuperToastUtils.showSuperToast(this, 2, "点击了确认按钮");
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
