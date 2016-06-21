package com.jinzht.pro.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.utils.UiHelp;

/**
 * 更改公司界面
 */
public class ChangeCompActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView btnSave;// 保存
    private ImageView btnClean;// 清空
    private EditText edCompName;// 公司名输入框

    @Override
    protected int getResourcesId() {
        return R.layout.activity_change_comp;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        findView();
    }

    private void findView() {
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        btnSave = (TextView) findViewById(R.id.btn_save);// 保存
        btnSave.setOnClickListener(this);
        btnClean = (ImageView) findViewById(R.id.btn_clean);// 清空
        btnClean.setOnClickListener(this);
        edCompName = (EditText) findViewById(R.id.ed_comp_name);// 公司名输入框
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.btn_save:// 保存
                break;
            case R.id.btn_clean:// 清空
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
