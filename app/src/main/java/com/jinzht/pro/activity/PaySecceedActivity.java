package com.jinzht.pro.activity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.view.CircleImageView;

/**
 * 投资成功界面
 */
public class PaySecceedActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回按钮
    private TextView tvTitle;// 标题
    private TextView tvMoney;// 投资金额
    private CircleImageView ivImg;// 项目图片
    private TextView tvProjectname;// 项目名
    private TextView tvCompanyname;// 公司名
    private Button btnConfirm;// 确定按钮

    @Override
    protected int getResourcesId() {
        return R.layout.activity_pay_secceed;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回按钮
        btnBack.setVisibility(View.GONE);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("支付成功");
        tvMoney = (TextView) findViewById(R.id.tv_money);// 投资金额
        ivImg = (CircleImageView) findViewById(R.id.iv_img);// 项目图片
        tvProjectname = (TextView) findViewById(R.id.tv_projectname);// 项目名
        tvCompanyname = (TextView) findViewById(R.id.tv_companyname);// 公司名
        btnConfirm = (Button) findViewById(R.id.btn_confirm);// 确定按钮
        btnConfirm.setOnClickListener(this);

        double amount = Double.parseDouble(getIntent().getStringExtra("amount")) * 10000.00;
        tvMoney.setText(String.valueOf(amount));
        Glide.with(mContext).load(getIntent().getStringExtra("img")).into(ivImg);
        tvProjectname.setText(getIntent().getStringExtra("abbrevName"));
        tvCompanyname.setText(getIntent().getStringExtra("fullName"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:// 确定后跳转到项目中心
                SuperToastUtils.showSuperToast(mContext, 2, "去项目中心");
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
