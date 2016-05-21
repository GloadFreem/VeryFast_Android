package com.jinzht.pro1.activity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 微信登录的手机验证
 */
public class WechatVerifyActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private EditText wechatVerifyEdTel;// 手机号码输入框
    private EditText wechatVerifyEdCode;// 验证码输入框
    private TextView wechatVerifyTvCode;// 获取验证码按钮
    private EditText wechatVerifyEdInviteCode;// 指环码输入框
    private Button wechatVerifyBtnNext;// 下一步按钮
    private CheckBox wechatVerifyCbAgree;// 同意用户协议选择框
    private TextView wechatVerifyTvUserAgreement;// 用户协议

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
        wechatVerifyEdTel = (EditText) findViewById(R.id.wechat_verify_ed_tel);// 手机号码输入框
        wechatVerifyEdCode = (EditText) findViewById(R.id.wechat_verify_ed_code);// 验证码输入框
        wechatVerifyTvCode = (TextView) findViewById(R.id.wechat_verify_tv_code);// 获取验证码按钮
        wechatVerifyTvCode.setOnClickListener(this);
        wechatVerifyEdInviteCode = (EditText) findViewById(R.id.wechat_verify_ed_invite_code);// 指环码输入框
        wechatVerifyBtnNext = (Button) findViewById(R.id.wechat_verify_btn_next);// 下一步按钮
        wechatVerifyBtnNext.setOnClickListener(this);
        wechatVerifyCbAgree = (CheckBox) findViewById(R.id.wechat_verify_cb_agree);// 同意用户协议选择框
        wechatVerifyTvUserAgreement = (TextView) findViewById(R.id.wechat_verify_tv_user_agreement);// 用户协议
        wechatVerifyTvUserAgreement.setOnClickListener(this);

        tvTitle.setText("手机验证");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.wechat_verify_tv_code:// 点击获取验证码
                wechatVerifyTvCode.setBackgroundResource(R.drawable.bg_ed_wechat_code_gray);
                wechatVerifyTvCode.setTextColor(getResources().getColor(R.color.custom_orange));
                break;
            case R.id.wechat_verify_btn_next:// 跳转到下一步界面
                if (wechatVerifyCbAgree.isChecked()) {
                    SuperToastUtils.showSuperToast(this, 2, "下一步");
                } else {
                    SuperToastUtils.showSuperToast(this, 2, "同意《金指投用户协议》才能注册");
                }
                break;
            case R.id.wechat_verify_tv_user_agreement:// 点击查看用户协议，跳转到用户协议界面
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
