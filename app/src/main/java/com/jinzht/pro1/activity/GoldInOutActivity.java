package com.jinzht.pro1.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.StringUtils;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.utils.UiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 金条收支明细界面
 */
public class GoldInOutActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private ListView myListView;// 列表

    private List<String> yearmonths;// 年月
    private List<String> types;// 类型
    private List<String> amount;// 数量
    private List<String> descs;// 描述
    private List<String> days;// 日

    @Override
    protected int getResourcesId() {
        return R.layout.activity_gold_in_out;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("收支明细");
        myListView = (ListView) findViewById(R.id.gold_inout_list);// 列表
        initList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
        }
    }

    private void initList() {
        yearmonths = new ArrayList<>(Arrays.asList("2016年6月", "", "", "", "", "", "2016年5月", "", "", "", "", "", "2016年4月", "", ""));
        types = new ArrayList<>(Arrays.asList("登录奖励", "注册奖励", "身份认证", "邀请好友", "商城消费", "邀请好友", "金条理财", "登录奖励", "登录奖励", "商城消费", "投资抵现", "登录奖励", "商城消费", "登录奖励", "邀请好友"));
        amount = new ArrayList<>(Arrays.asList("+6", "+10", "+10", "+10", "-50", "+10", "-30", "+12", "+13", "-46", "-200", "+12", "-66", "+14", "+10"));
        descs = new ArrayList<>(Arrays.asList("每日登录奖励金条", "新用户成功注册奖励金条", "实名认证赠送金条", "成功邀请136****1234用户", "商城消费", "成功邀请136****1234用户", "购买理财产品", "每日登录奖励金条", "每日登录奖励金条", "商城消费", "投资抵扣现金", "每日登录奖励金条", "商城消费", "每日登录奖励金条", "成功邀请136****1234用户"));
        days = new ArrayList<>(Arrays.asList("21日", "20日", "18日", "16日", "15日", "14日", "12日", "11日", "10日", "09日", "04日", "01日", "31日", "28日", "24日"));
        myListView.setAdapter(new MyAdapter());
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return types.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gold_inout, null);
                holder.itemLlYear = (LinearLayout) convertView.findViewById(R.id.item_ll_year);
                holder.itemYearmonth = (TextView) convertView.findViewById(R.id.item_gold_inout_yearmonth);
                holder.itemEndtag = (TextView) convertView.findViewById(R.id.item_gold_inout_endtag);
                holder.itemType = (TextView) convertView.findViewById(R.id.item_gold_inout_type);
                holder.itemAmount = (TextView) convertView.findViewById(R.id.item_gold_inout_amount);
                holder.itemDecs = (TextView) convertView.findViewById(R.id.item_gold_inout_desc);
                holder.itemDay = (TextView) convertView.findViewById(R.id.item_gold_inout_day);
                holder.emptyView = convertView.findViewById(R.id.item_gold_inout_empty);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == 0) {
                holder.emptyView.setVisibility(View.VISIBLE);
            } else {
                holder.emptyView.setVisibility(View.GONE);
            }
            if (StringUtils.isBlank(yearmonths.get(position))) {
                holder.itemLlYear.setVisibility(View.GONE);
            } else {
                holder.itemLlYear.setVisibility(View.VISIBLE);
                holder.itemYearmonth.setText(yearmonths.get(position));
            }
            if (position == getCount() - 1) {
                holder.itemEndtag.setVisibility(View.VISIBLE);
            } else {
                holder.itemEndtag.setVisibility(View.GONE);
            }
            holder.itemType.setText(types.get(position));
            holder.itemAmount.setText(amount.get(position));
            if (amount.get(position).contains("+")) {
                holder.itemType.setTextColor(UiUtils.getColor(R.color.custom_orange));
                holder.itemAmount.setTextColor(UiUtils.getColor(R.color.custom_orange));
            } else {
                holder.itemType.setTextColor(0xff48ae58);
                holder.itemAmount.setTextColor(0xff48ae58);
            }
            holder.itemDecs.setText(descs.get(position));
            holder.itemDay.setText(days.get(position));
            return convertView;
        }

        class ViewHolder {
            public LinearLayout itemLlYear;// 年月布局
            public TextView itemYearmonth;// 年月
            public TextView itemEndtag;// 结束标识
            public TextView itemType;// 收支类型
            public TextView itemAmount;// 收支数量
            public TextView itemDecs;// 描述
            public TextView itemDay;// 日
            public View emptyView;
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }

}
