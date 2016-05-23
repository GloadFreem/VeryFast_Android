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
 * 更换绑定手机界面
 */
public class ChangeTelActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private EditText edTelOld;// 原手机号码输入框
    private EditText edTelNew;// 新手机号码输入框
    private EditText edCode;// 验证码输入框
    private TextView tvCode;// 获取验证码按钮
    private EditText edIdcard;// 身份证号码输入框
    private Button btnConfirm;// 确认按钮

    @Override
    protected int getResourcesId() {
        return R.layout.activity_change_tel;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("更换绑定手机");
        edTelOld = (EditText) findViewById(R.id.change_tel_ed_tel_old);// 原手机号码输入框
        edTelNew = (EditText) findViewById(R.id.change_tel_ed_tel_new);// 新手机号码输入框
        edCode = (EditText) findViewById(R.id.change_tel_ed_code);// 验证码输入框
        tvCode = (TextView) findViewById(R.id.change_tel_tv_code);// 获取验证码按钮
        tvCode.setOnClickListener(this);
        edIdcard = (EditText) findViewById(R.id.change_tel_ed_idcard);// 身份证号码输入框
        btnConfirm = (Button) findViewById(R.id.change_tel_btn_confirm);// 确认按钮
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.change_tel_tv_code:// 点击获取验证码
                SuperToastUtils.showSuperToast(this, 2, "验证码");
                break;
            case R.id.change_tel_btn_confirm:// 确认
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
