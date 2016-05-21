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
 * 实名认证上传营业执照界面
 */
public class CertificationCompActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回键
    private TextView tvTitle;// 标题
    private TextView certificationTvCompName;// 上传营业执照提示
    private RelativeLayout certificationRlCompPhoto;// 营业执照照片
    private EditText certificationEdCompnum;// 营业执照注册号
    private TextView certificationTvField1;// 选择投资领域
    private Button certificationBtnNext;// 下一步按钮


    @Override
    protected int getResourcesId() {
        return R.layout.activity_certification_comp;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回键
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        certificationTvCompName = (TextView) findViewById(R.id.certification_tv_comp_name);// 上传营业执照提示
        certificationRlCompPhoto = (RelativeLayout) findViewById(R.id.certification_rl_comp_photo);// 营业执照照片
        certificationRlCompPhoto.setOnClickListener(this);
        certificationEdCompnum = (EditText) findViewById(R.id.certification_ed_compnum);// 营业执照注册号
        certificationTvField1 = (TextView) findViewById(R.id.certification_tv_field1);// 选择投资领域
        certificationTvField1.setOnClickListener(this);
        certificationBtnNext = (Button) findViewById(R.id.certification_btn_next);// 下一步按钮
        certificationBtnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.certification_rl_comp_photo:// 点击上传营业执照照片
                SuperToastUtils.showSuperToast(this, 2, "上传营业执照");
                break;
            case R.id.certification_tv_field1:// 选择投资领域
                SuperToastUtils.showSuperToast(this, 2, "选择投资领域");
                break;
            case R.id.certification_btn_next:// 跳转到下一步界面
                Intent intent = new Intent(this, CertificationDescActivity.class);
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
