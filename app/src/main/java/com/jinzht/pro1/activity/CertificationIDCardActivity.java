package com.jinzht.pro1.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 实名认证上传身份证界面
 */
public class CertificationIDCardActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private RelativeLayout certificationRlIdcardZheng;// 身份证正面
    private RelativeLayout certificationRlIdcardFan;// 身份证反面
    private EditText certificationEdIdnum;// 身份证号码
    private EditText certificationEdName;// 姓名
    private EditText certificationEdCompanyName;// 公司名称
    private TextView certificationTvCompanyAddr1;// 公司所在地
    private EditText certificationEdPosition;// 职位
    private Button certificationBtnNext;// 下一步按钮

    @Override
    protected int getResourcesId() {
        return R.layout.activity_certification_idcard;
    }

    @Override
    protected void init() {
        UiHelp.setTranslucentStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        certificationRlIdcardZheng = (RelativeLayout) findViewById(R.id.certification_rl_idcard_zheng);// 身份证正面
        certificationRlIdcardZheng.setOnClickListener(this);
        certificationRlIdcardFan = (RelativeLayout) findViewById(R.id.certification_rl_idcard_fan);// 身份证反面
        certificationRlIdcardFan.setOnClickListener(this);
        certificationEdIdnum = (EditText) findViewById(R.id.certification_ed_idnum);// 身份证号码
        certificationEdName = (EditText) findViewById(R.id.certification_ed_name);// 姓名
        certificationEdCompanyName = (EditText) findViewById(R.id.certification_ed_company_name);// 公司名称
        certificationTvCompanyAddr1 = (TextView) findViewById(R.id.certification_tv_company_addr1);// 公司所在地
        certificationTvCompanyAddr1.setOnClickListener(this);
        certificationEdPosition = (EditText) findViewById(R.id.certification_ed_position);// 职位
        certificationBtnNext = (Button) findViewById(R.id.certification_btn_next);// 下一步按钮
        certificationBtnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.certification_rl_idcard_zheng:// 点击上传身份证正面照片
                SuperToastUtils.showSuperToast(this, 2, "正面");
                break;
            case R.id.certification_rl_idcard_fan:// 点击上传身份证反面照片
                SuperToastUtils.showSuperToast(this, 2, "反面");
                break;
            case R.id.certification_tv_company_addr1:// 点击选择公司所在地
                SuperToastUtils.showSuperToast(this, 2, "所在地");
                break;
            case R.id.certification_btn_next:// 跳转到下一步界面
                Intent intent = new Intent(this, CertificationCompActivity.class);
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
