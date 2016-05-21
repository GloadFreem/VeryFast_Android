package com.jinzht.pro1.activity;

import android.graphics.Paint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 上传项目界面
 */
public class UploadActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回键
    private TextView tvTitle;// 标题
    private TextView uploadEmail;// 复制邮箱地址
    private TextView uploadTel;// 点击拨打电话

    @Override
    protected int getResourcesId() {
        return R.layout.activity_upload;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回键
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("上传项目");
        uploadEmail = (TextView) findViewById(R.id.upload_email);// 复制邮箱地址
        uploadEmail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        uploadTel = (TextView) findViewById(R.id.upload_tel);// 点击拨打电话
        uploadTel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.upload_tel:// 点击拨打电话
                SuperToastUtils.showSuperToast(this, 2, "打电话");
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
