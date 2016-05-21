package com.jinzht.pro1.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.BillAdapter;
import com.jinzht.pro1.adapter.RecyclerViewData;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.callback.ItemClickListener;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 交易账单界面
 */
public class BillActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private RecyclerView billRv;// 账单列表

    private BillAdapter adapter;// RecyclerView数据填充器
    private List<String> years;// 年
    private List<String> days;// 月日
    private List<String> mins;// 时分
    private List<String> types;// 交易类型
    private List<String> amounts;// 交易金额
    private List<String> nums;// 流水号
    private List<String> succeed;// 成功与否
    private List<Integer> imgs;// 项目图片
    private List<String> titles;// 项目标题

    @Override
    protected int getResourcesId() {
        return R.layout.activity_bill;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("交易账单");
        billRv = (RecyclerView) findViewById(R.id.bill_rv);// 账单列表

        // 填充列表
        initList();
    }

    private void initList() {
        // 准备数据
        years = new ArrayList<>(Arrays.asList("","2016年", "", "", "2015年", "", "", "2014年", ""));
        days = new ArrayList<>(Arrays.asList("","5月20日", "3月8日", "1月1日", "12月12日", "10月10日", "8月3日", "7月1日", "4月1日"));
        mins = new ArrayList<>(Arrays.asList("","14:20", "11:16", "8:16", "23:50", "7:00", "9:36", "12:20", "16:16"));
        types = new ArrayList<>(Arrays.asList("","账户充值", "项目认投", "账户提现", "账户充值", "项目认投", "账户提现", "项目认投", "账户提现"));
        amounts = new ArrayList<>(Arrays.asList("","¥ 500万", "¥ 400万", "¥ 10万", "¥ 60万", "¥ 500万", "¥ 4000元", "¥ 30万", "¥ 600元"));
        nums = new ArrayList<>(Arrays.asList("","cz160520142006", "rt160308111605", "tx160101081620", "cz151212235011", "rt151010070050", "tx150803093622", "rt140701122022", "tx140401161616"));
        succeed = new ArrayList<>(Arrays.asList("","充值成功", "认投成功", "提现中", "充值失败", "退款中", "提现成功", "项目成功", "提现成功"));
        imgs = new ArrayList<>(Arrays.asList(null,null, R.mipmap.activity_photo1, null, null, R.mipmap.activity_photo2, null, R.mipmap.activity_photo3, null));
        titles = new ArrayList<>(Arrays.asList("","", "逸景营地", "", "", "国联质检", "", "杰邦科技", ""));
        adapter = new BillAdapter(mContext, years, days, mins, types, amounts, nums, succeed, imgs, titles);
        // 填充数据
        RecyclerViewData.setVertical(billRv, mContext, adapter);
        // 条目点击事件
        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SuperToastUtils.showSuperToast(mContext, 2, "点击了条目" + position);
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
            case R.id.btn_back:// 返回上一页
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
