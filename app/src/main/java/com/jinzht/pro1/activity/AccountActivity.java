package com.jinzht.pro1.activity;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.AccountAdapter;
import com.jinzht.pro1.adapter.RecyclerViewData;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.callback.ItemClickListener;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 资金账户界面
 */
public class AccountActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView totleBalanceLower;// 小写账户余额
    private TextView totleBalanceUpper;// 大写账户余额
    private TextView availableLower;// 小写可用余额
    private TextView freezeLower;// 小写冻结资金
    private TextView availableUpper;// 大写可用余额
    private TextView freezeUpper;// 大写冻结资金
    private RecyclerView gvItems;// item的网格

    private String str;// 转换字体的临时字符串
    private SpannableString span;// 设置TextView不同字体

    private List<Integer> icons;// 填充的item图标
    private List<String> names;// 填充的item名
    private AccountAdapter rvAdaper;// GridView填充器

    @Override
    protected int getResourcesId() {
        return R.layout.activity_account;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        btnBack = (LinearLayout) findViewById(R.id.account_btn_back);// 返回
        btnBack.setOnClickListener(this);
        totleBalanceLower = (TextView) findViewById(R.id.account_tv_totle_balance_lower);// 小写账户余额
        str = totleBalanceLower.getText().toString();
        span = new SpannableString(str);
        span.setSpan(new AbsoluteSizeSpan(16, true), str.length() - 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        totleBalanceLower.setText(span);
        totleBalanceUpper = (TextView) findViewById(R.id.account_tv_totle_balance_upper);// 大写账户余额
        availableLower = (TextView) findViewById(R.id.account_tv_available_lower);// 小写可用余额
        str = availableLower.getText().toString();
        span = new SpannableString(str);
        span.setSpan(new AbsoluteSizeSpan(12, true), str.length() - 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        availableLower.setText(span);
        freezeLower = (TextView) findViewById(R.id.account_tv_freeze_lower);// 小写冻结资金
        str = freezeLower.getText().toString();
        span = new SpannableString(str);
        span.setSpan(new AbsoluteSizeSpan(12, true), str.length() - 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        freezeLower.setText(span);
        availableUpper = (TextView) findViewById(R.id.account_tv_available_upper);// 大写可用余额
        freezeUpper = (TextView) findViewById(R.id.account_tv_freeze_upper);// 大写冻结资金
        gvItems = (RecyclerView) findViewById(R.id.account_gv_items);// item的网格

        // 填充GridView
        initGridView();
    }

    private void initGridView() {
        // 准备数据
        icons = new ArrayList<>(Arrays.asList(R.mipmap.icon_card, R.mipmap.icon_recharge, R.mipmap.icon_bill, R.mipmap.icon_withdraw));
        names = new ArrayList<>(Arrays.asList("银行卡", "账户充值", "交易账单", "资金提现"));
        rvAdaper = new AccountAdapter(mContext, icons, names);
        // 填充数据
        RecyclerViewData.setGrid(gvItems, mContext, rvAdaper, 2);
        // 条目点击事件
        rvAdaper.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SuperToastUtils.showSuperToast(mContext, 2, names.get(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemSubViewClick(View view, int position) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_btn_back:// 返回上一页
                finish();
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
