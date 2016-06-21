package com.jinzht.pro.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.adapter.AccountAdapter;
import com.jinzht.pro.adapter.RecyclerViewData;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.callback.ItemClickListener;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 我的金条界面
 */
public class MyGoldActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView goldAmount;// 金条数量
    private RecyclerView recyclerView;// 条目网格

    private List<Integer> icons;// 填充的item图标
    private List<String> names;// 填充的item名
    private AccountAdapter mAdapter;// Recycler填充器

    @Override
    protected int getResourcesId() {
        return R.layout.activity_my_gold;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        btnBack = (LinearLayout) findViewById(R.id.gold_btn_back);// 返回
        btnBack.setOnClickListener(this);
        goldAmount = (TextView) findViewById(R.id.gold_tv_amount);// 金条数量
        recyclerView = (RecyclerView) findViewById(R.id.gold_rv_items);// 条目网格

        // 填充RecyclerView
        initItems();
    }

    private void initItems() {
        // 准备数据
        icons = new ArrayList<>(Arrays.asList(R.mipmap.icon_inout, R.mipmap.icon_give, R.mipmap.icon_accumulate, R.mipmap.icon_useage));
        names = new ArrayList<>(Arrays.asList("收支明细", "邀请送金条", "金条积累规则", "金条使用规则"));
        mAdapter = new AccountAdapter(mContext, icons, names);
        // 填充数据
        RecyclerViewData.setGrid(recyclerView, mContext, mAdapter, 2);
        // 条目点击事件
        mAdapter.setItemClickListener(new ItemClickListener() {
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
            case R.id.gold_btn_back:// 返回上一页
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
