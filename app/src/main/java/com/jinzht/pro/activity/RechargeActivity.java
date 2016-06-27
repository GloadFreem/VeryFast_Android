package com.jinzht.pro.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;

/**
 * 充值输入金额界面
 */
public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private EditText edAmount;// 金额输入框
    private TextView btnNext;// 下一步

    @Override
    protected int getResourcesId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("账户充值");
        edAmount = (EditText) findViewById(R.id.ed_amount);// 金额输入框
        btnNext = (TextView) findViewById(R.id.btn_next);// 下一步
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_next:
                if (StringUtils.isBlank(edAmount.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入充值金额");
                } else if (Integer.parseInt(edAmount.getText().toString()) <= 0) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入正确的金额");
                } else {
                    Intent intent = new Intent(this, YeepayRechargeActivity.class);
                    intent.putExtra("isRecharge", true);
                    intent.putExtra("userId", getIntent().getStringExtra("userId"));
                    intent.putExtra("amount", edAmount.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
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
