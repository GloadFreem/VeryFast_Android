package com.jinzht.pro.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.view.CircleImageView;

/**
 * 圈子转发界面，没有用到
 */
public class TranspondCircleActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private LinearLayout btnTranspond;// 转发按钮
    private EditText edContent;// 自己想说的内容输入框
    private CircleImageView ivFavicon;// 被转发人头像
    private TextView tvName;// 被转发人姓名
    private TextView tvTranspond;// 被转发内容

    @Override
    protected int getResourcesId() {
        return R.layout.activity_transpond_circle;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        findView();
    }

    private void findView() {
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        btnTranspond = (LinearLayout) findViewById(R.id.btn_transpond);// 转发按钮
        btnTranspond.setOnClickListener(this);
        edContent = (EditText) findViewById(R.id.ed_content);// 自己想说的内容输入框
        ivFavicon = (CircleImageView) findViewById(R.id.iv_favicon);// 被转发人头像
        tvName = (TextView) findViewById(R.id.tv_name);// 被转发人姓名
        tvTranspond = (TextView) findViewById(R.id.tv_transpond);// 被转发内容
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.btn_transpond:// 转发
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
