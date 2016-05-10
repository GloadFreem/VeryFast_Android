package com.jinzht.pro1.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 注册的第一个页面
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
                SuperToastUtils.showSuperToast(this, 2, "点击了客服按钮");
                break;
            case R.id.register1_tv_getcode:// 点击获取验证码
                SuperToastUtils.showSuperToast(this, 2, "获取验证码");
                register1TvGetcode.setBackgroundResource(R.drawable.bg_code_gray);
                register1TvGetcode.setTextColor(getResources().getColor(R.color.custom_orange));
                break;
            case R.id.register1_bt_register:// 点击注册，跳转到注册的第二个页面
                if (register1_cb_agreement.isChecked()) {
                    Intent intent = new Intent(this, Register2Activity.class);
                    startActivity(intent);
                } else {
                    SuperToastUtils.showSuperToast(this, 2, "同意《金指投用户协议》才能注册");
                }
                break;
            case R.id.register1_tv_user_agreement:// 点击查看用户协议，跳转到用户协议界面
                SuperToastUtils.showSuperToast(this, 2, "点击了用户协议");
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
