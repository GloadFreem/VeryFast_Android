package com.jinzht.pro.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.utils.UiHelp;

/**
 * 完成注册，获得指环码界面
 */
public class CompleteRegisterActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvInviteCode;// 指环码
    private ImageButton btnGotoIdentification;// 去认证按钮
    private Button btnGotoTry;// 试用按钮

    private Intent intent;

    @Override
    protected int getResourcesId() {
        return R.layout.activity_complete_register;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        tvInviteCode = (TextView) findViewById(R.id.complete_register_code);// 指环码
        btnGotoIdentification = (ImageButton) findViewById(R.id.complete_register_btn_goto_identification);// 去认证按钮
        btnGotoIdentification.setOnClickListener(this);
        btnGotoTry = (Button) findViewById(R.id.complete_register_btn_goto_try);// 试用按钮
        btnGotoTry.setOnClickListener(this);

        tvInviteCode.setText(getIntent().getStringExtra("inviteCode"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complete_register_btn_goto_identification:// 去认证
                if (getIntent().getIntExtra("isWechatLogin", 0) == 1) {
                    intent = new Intent(this, WechatVerifyActivity.class);
                    intent.putExtra("usertype", getIntent().getIntExtra("usertype", -1));
                    startActivity(intent);
                } else {
                    intent = new Intent(this, CertificationIDCardActivity.class);
                    intent.putExtra("usertype", getIntent().getIntExtra("usertype", -1));
                    startActivity(intent);
                }
                break;
            case R.id.complete_register_btn_goto_try:// 试用模式，进入主页
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
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
