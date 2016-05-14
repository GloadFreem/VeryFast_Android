package com.jinzht.pro1.activity;

import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 认投项目输入金额界面
 */
public class InvestActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private EditText investEdInputMoney;// 金额输入框
    private TextView investBtnPullDown;// 下拉选择领投跟投
    private Button investBtnPay;// 支付按钮

    private TextView pullDownTextView;// 下拉窗口的TextView
    private PopupWindow pullDownWindow;// 下拉窗口
    private boolean isShowing = false;// PopupWindow是否展开的标识

    @Override
    protected int getResourcesId() {
        return R.layout.activity_invest;
    }

    @Override
    protected void init() {
        UiHelp.setTranslucentStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("认投项目");
        investEdInputMoney = (EditText) findViewById(R.id.invest_ed_input_money);// 金额输入框
        investBtnPullDown = (TextView) findViewById(R.id.invest_btn_pull_down);// 下拉选择领投跟投
        investBtnPullDown.setOnClickListener(this);
        investBtnPay = (Button) findViewById(R.id.invest_btn_pay);// 支付按钮
        investBtnPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.invest_btn_pull_down:// 选择领投或者跟投
                if (!isShowing) {// 如果是展开状态就啥都不干
                    if (pullDownWindow == null) {
                        initPopupWindow();
                    }
                    pullDownWindow.showAsDropDown(investBtnPullDown, 0, 0);
                    investBtnPullDown.setBackgroundResource(R.drawable.bg_invest_pull_down_ed);// 改变选中的TextView样式
                    isShowing = true;
                }
                break;
            case R.id.invest_btn_pay:// 支付
                SuperToastUtils.showSuperToast(this, 2, "钱没有鸟~");
                break;
        }
    }

    // 初始化下拉界面
    private void initPopupWindow() {
        pullDownTextView = (TextView) getLayoutInflater().inflate(R.layout.layout_invest_pulldown, null);
        // 点击下弹拉出的TextView，PopupWindow消失，两个TextView的内容交换
        pullDownTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = pullDownTextView.getText().toString();
                pullDownTextView.setText(investBtnPullDown.getText().toString());
                investBtnPullDown.setText(temp);
                pullDownWindow.dismiss();
            }
        });
        // 初始化下拉窗口
        pullDownWindow = new PopupWindow(pullDownTextView, investBtnPullDown.getWidth(), investBtnPullDown.getHeight());
        pullDownWindow.setOutsideTouchable(true);
        pullDownWindow.setBackgroundDrawable(new BitmapDrawable());
        pullDownWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                investBtnPullDown.setBackgroundResource(R.drawable.bg_ed_wechat_code_orange);// 消失时选中的TextView改回原来的样式
                isShowing = false;
            }
        });
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
