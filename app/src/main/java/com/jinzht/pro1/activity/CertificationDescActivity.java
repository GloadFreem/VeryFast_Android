package com.jinzht.pro1.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 实名认证自我介绍界面
 */
public class CertificationDescActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private EditText certificationEdDescComp;// 公司介绍输入框
    private EditText certificationEdDescPerson;// 个人介绍输入框
    private Button certificationBtnNext;// 下一步

    @Override
    protected int getResourcesId() {
        return R.layout.activity_certification_desc;
    }

    @Override
    protected void init() {
        UiHelp.setTranslucentStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        certificationEdDescComp = (EditText) findViewById(R.id.certification_ed_desc_comp);// 公司介绍输入框
        certificationEdDescPerson = (EditText) findViewById(R.id.certification_ed_desc_person);// 个人介绍输入框
        certificationBtnNext = (Button) findViewById(R.id.certification_btn_next);// 下一步
        certificationBtnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.certification_btn_next:// 跳转到一下页
                Intent intent = new Intent(this, CertificationCapacityActivity.class);
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
