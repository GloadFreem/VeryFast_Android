package com.jinzht.pro1.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 平台身份界面
 */
public class UserTypeActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private ImageView ivType;// 用户类型图标
    private TextView tvType;// 用户类型
    private TextView tvDesc;// 用户类型描述
    private TextView btnAdd;// 新增身份按钮

    @Override
    protected int getResourcesId() {
        return R.layout.activity_user_type;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        findView();
    }

    private void findView() {
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("平台身份");
        ivType = (ImageView) findViewById(R.id.iv_type);// 用户类型图标
        tvType = (TextView) findViewById(R.id.tv_type);// 用户类型
        tvDesc = (TextView) findViewById(R.id.tv_desc);// 用户类型描述
        btnAdd = (TextView) findViewById(R.id.btn_add);// 新增身份按钮
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.btn_add:// 新增用户身份类型
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
