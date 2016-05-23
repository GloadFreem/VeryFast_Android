package com.jinzht.pro1.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 关于平台界面
 */
public class AboutUsActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private TextView tvVerson;// 版本号
    private RelativeLayout btnIntroduce;// 平台介绍
    private RelativeLayout btnGudie;// 新手指南
    private RelativeLayout btnAgreement;// 用户协议
    private RelativeLayout btnStatement;// 免责声明
    private RelativeLayout btnFeedback;// 意见反馈

    @Override
    protected int getResourcesId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("关于平台");
        tvVerson = (TextView) findViewById(R.id.about_us_tv_verson);// 版本号
        btnIntroduce = (RelativeLayout) findViewById(R.id.about_us_btn_introduce);// 平台介绍
        btnIntroduce.setOnClickListener(this);
        btnGudie = (RelativeLayout) findViewById(R.id.about_us_btn_gudie);// 新手指南
        btnGudie.setOnClickListener(this);
        btnAgreement = (RelativeLayout) findViewById(R.id.about_us_btn_agreement);// 用户协议
        btnAgreement.setOnClickListener(this);
        btnStatement = (RelativeLayout) findViewById(R.id.about_us_btn_statement);// 免责声明
        btnStatement.setOnClickListener(this);
        btnFeedback = (RelativeLayout) findViewById(R.id.about_us_btn_feedback);// 意见反馈
        btnFeedback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.about_us_btn_introduce:// 进入平台介绍
                SuperToastUtils.showSuperToast(this, 2, "平台介绍");
                break;
            case R.id.about_us_btn_gudie:// 进入新手指南
                SuperToastUtils.showSuperToast(this, 2, "新手指南");
                break;
            case R.id.about_us_btn_agreement:// 进入用户协议
                SuperToastUtils.showSuperToast(this, 2, "用户协议");
                break;
            case R.id.about_us_btn_statement:// 进入免责声明
                SuperToastUtils.showSuperToast(this, 2, "免责声明");
                break;
            case R.id.about_us_btn_feedback:// 进入意见反馈
                SuperToastUtils.showSuperToast(this, 2, "意见反馈");
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
