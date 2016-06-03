package com.jinzht.pro1.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 完成注册，获得指环码界面
 */
public class CompleteRegisterActivity extends BaseActivity implements View.OnClickListener {

    private TextView completeRegisterCode;// 指环码
    private ImageButton completeRegisterBtnGotoIdentification;// 去认证按钮
    private Button completeRegisterBtnGotoTry;// 试用按钮

    private Intent intent;

    @Override
    protected int getResourcesId() {
        return R.layout.activity_complete_register;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        completeRegisterCode = (TextView) findViewById(R.id.complete_register_code);// 指环码
        completeRegisterBtnGotoIdentification = (ImageButton) findViewById(R.id.complete_register_btn_goto_identification);// 去认证按钮
        completeRegisterBtnGotoIdentification.setOnClickListener(this);
        completeRegisterBtnGotoTry = (Button) findViewById(R.id.complete_register_btn_goto_try);// 试用按钮
        completeRegisterBtnGotoTry.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complete_register_btn_goto_identification:// 去认证
                intent = new Intent(this, CertificationIDCardActivity.class);
                intent.putExtra("usertype", getIntent().getIntExtra("usertype", 0));
                startActivity(intent);
                break;
            case R.id.complete_register_btn_goto_try:// 试用模式，进入主页
                intent = new Intent(this, MainActivity.class);
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
