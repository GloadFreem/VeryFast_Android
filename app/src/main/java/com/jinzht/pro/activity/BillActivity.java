package com.jinzht.pro.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.BillListBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.view.CircleImageView;
import com.jinzht.pro.view.PullToRefreshLayout;
import com.jinzht.pro.view.PullableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易账单界面
 */
public class BillActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 列表

    private MyAdapter myAdapter;
    private int pages = 0;
    private List<BillListBean.DataBean> datas = new ArrayList<>();

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
        refreshView = (PullToRefreshLayout) findViewById(R.id.refresh_view);// 刷新布局
        listview = (PullableListView) findViewById(R.id.listview);// 列表

        refreshView.setOnRefreshListener(new PullListener());
        myAdapter = new MyAdapter();

        GetBillList getBillList = new GetBillList(0);
        getBillList.execute();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bill, null);
                holder.emptyView = convertView.findViewById(R.id.empty_view);
                holder.itemLlYear = (LinearLayout) convertView.findViewById(R.id.item_ll_year);
                holder.itemBillYear = (TextView) convertView.findViewById(R.id.item_bill_year);
                holder.itemBillLine = (ImageView) convertView.findViewById(R.id.item_bill_line);
                holder.itemBillDay = (TextView) convertView.findViewById(R.id.item_bill_day);
                holder.itemBillMinute = (TextView) convertView.findViewById(R.id.item_bill_minute);
                holder.itemBillType = (TextView) convertView.findViewById(R.id.item_bill_type);
                holder.itemBillAmount = (TextView) convertView.findViewById(R.id.item_bill_amount);
                holder.itemBillNum = (TextView) convertView.findViewById(R.id.item_bill_num);
                holder.itemBillSucceed = (TextView) convertView.findViewById(R.id.item_bill_succeed);
                holder.itemBillPro = (LinearLayout) convertView.findViewById(R.id.item_bill_pro);
                holder.itemBillImg = (CircleImageView) convertView.findViewById(R.id.item_bill_img);
                holder.itemBillCompName = (TextView) convertView.findViewById(R.id.item_bill_comp_name);
                holder.itemBillEndtag = (TextView) convertView.findViewById(R.id.item_bill_endtag);
                holder.itemBillCard = (RelativeLayout) convertView.findViewById(R.id.item_bill_card);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == 0) {
                holder.emptyView.setVisibility(View.VISIBLE);
            } else {
                holder.emptyView.setVisibility(View.GONE);
            }

            String str;// 转换字体的临时字符串
            SpannableString span;// 设置TextView不同字体

            String year = datas.get(position).getRecord().getTradeDate().substring(0, 4) + "年";
            String day = datas.get(position).getRecord().getTradeDate().substring(5, 10).replace("-", "月") + "日";
            if ('0' == day.charAt(0)) {
                day = day.substring(1);
            }
            String minute = datas.get(position).getRecord().getTradeDate().substring(11, 16);
            if ('0' == minute.charAt(0)) {
                minute = minute.substring(1);
            }
            // 年
            if (position == 0) {
                holder.itemLlYear.setVisibility(View.VISIBLE);
                holder.itemBillYear.setText(year);
            } else {
                if (year.equals(datas.get(position - 1).getRecord().getTradeDate().substring(0, 4) + "年")) {
                    holder.itemLlYear.setVisibility(View.GONE);
                } else {
                    holder.itemLlYear.setVisibility(View.VISIBLE);
                    holder.itemBillYear.setText(year);
                }
            }
            holder.itemBillDay.setText(day);// 月日
            holder.itemBillMinute.setText(minute);// 时分
            holder.itemBillType.setText(datas.get(position).getRecord().getTradetype().getName());// 交易类型
            // 金额
            double amount = datas.get(position).getRecord().getAmount();
            if (amount < 100) {
                str = "¥ " + String.format("%.2f", amount) + "元";
                span = new SpannableString(str);
                span.setSpan(new AbsoluteSizeSpan(14, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new AbsoluteSizeSpan(14, true), str.length() - 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.itemBillAmount.setText(span);
            } else {
                amount = amount / 10000;
                str = "¥ " + String.format("%.2f", amount) + "万";
                span = new SpannableString(str);
                span.setSpan(new AbsoluteSizeSpan(14, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new AbsoluteSizeSpan(14, true), str.length() - 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.itemBillAmount.setText(span);
            }
            holder.itemBillNum.setText(datas.get(position).getRecord().getTradeCode());// 流水号
            // 状态
            holder.itemBillSucceed.setText(datas.get(position).getRecord().getTradestatus().getName());
            if (!datas.get(position).getRecord().getTradestatus().getName().contains("成功")) {
                holder.itemBillSucceed.setTextColor(Color.RED);
            } else {
                holder.itemBillSucceed.setTextColor(0xff747474);
            }
            // 布局
            if (StringUtils.isBlank(datas.get(position).getName())) {
                holder.itemBillCard.setBackgroundResource(R.mipmap.icon_bill_frame);
                holder.itemBillPro.setVisibility(View.GONE);
                holder.itemBillLine.setBackgroundResource(R.mipmap.line_blue_short);
            } else {
                holder.itemBillCard.setBackgroundResource(R.mipmap.icon_bill_frame_big);
                holder.itemBillPro.setVisibility(View.VISIBLE);
                holder.itemBillLine.setBackgroundResource(R.mipmap.line_blue_long);
                Glide.with(mContext).load(datas.get(position).getName()).into(holder.itemBillImg);
                holder.itemBillCompName.setText(datas.get(position).getName());
            }
            // 结束标识
            if (position == getCount() - 1) {
                holder.itemBillEndtag.setVisibility(View.VISIBLE);
            } else {
                holder.itemBillEndtag.setVisibility(View.GONE);
            }

            return convertView;
        }

        class ViewHolder {
            private View emptyView;// 空填充块
            private LinearLayout itemLlYear;// 年份布局
            private TextView itemBillYear;// 年
            private ImageView itemBillLine;// 蓝线
            private TextView itemBillDay;// 月日
            private TextView itemBillMinute;// 时分
            private TextView itemBillType;// 交易类型
            private TextView itemBillAmount;// 金额
            private TextView itemBillNum;// 流水号
            private TextView itemBillSucceed;// 成功与否
            private LinearLayout itemBillPro;// 项目图片和标题布局
            private CircleImageView itemBillImg;// 项目图片
            private TextView itemBillCompName;// 项目标题
            private TextView itemBillEndtag;// 结束标识
            private RelativeLayout itemBillCard;// 卡片背景
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
        }
    }

    // 获取交易账单列表
    private class GetBillList extends AsyncTask<Void, Void, BillListBean> {
        private int page;

        public GetBillList(int page) {
            this.page = page;
        }

        @Override
        protected BillListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETBILLLIST)),
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETBILLLIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("交易账单", body);
                return FastJsonTools.getBean(body, BillListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(BillListBean billListBean) {
            super.onPostExecute(billListBean);
            if (billListBean == null) {
                listview.setBackgroundResource(R.mipmap.bg_empty);
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (billListBean.getStatus() == 200) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        datas = billListBean.getData();
                        if (datas != null && datas.size() != 0) {
                            listview.setBackgroundResource(R.color.bg_main);
                        } else {
                            listview.setBackgroundResource(R.mipmap.bg_empty);
                        }
                        if (datas != null) {
                            listview.setAdapter(myAdapter);
                        }
                    } else {
                        for (BillListBean.DataBean dataBean : billListBean.getData()) {
                            datas.add(dataBean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (billListBean.getStatus() == 201) {
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    listview.setBackgroundResource(R.mipmap.bg_empty);
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                    SuperToastUtils.showSuperToast(mContext, 2, billListBean.getMessage());
                }
            }
        }
    }

    // 下拉刷新和上拉加载
    private class PullListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉刷新
            pages = 0;
            GetBillList getBillList = new GetBillList(0);
            getBillList.execute();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            pages++;
            Log.i("页码", String.valueOf(pages));
            GetBillList getBillList = new GetBillList(pages);
            getBillList.execute();
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
