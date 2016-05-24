package com.jinzht.pro1.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.view.CircleImageView;

/**
 * 查看指环码界面
 */
public class InviteCodeActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private CircleImageView ivFavicon;// 头像
    private TextView tvInviteCode;// 指环码
    private ImageView btnCopy;// 复制按钮

    @Override
    protected int getResourcesId() {
        return R.layout.activity_invite_code;
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
        tvTitle.setText("指环码");
        ivFavicon = (CircleImageView) findViewById(R.id.iv_favicon);// 头像
        tvInviteCode = (TextView) findViewById(R.id.tv_invite_code);// 指环码
        btnCopy = (ImageView) findViewById(R.id.btn_copy);// 复制按钮
        btnCopy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.btn_copy:// 复制指环码
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
