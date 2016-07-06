package com.jinzht.pro.activity;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.GoldInOutListBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.utils.UiUtils;
import com.jinzht.pro.view.PullToRefreshLayout;
import com.jinzht.pro.view.PullableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 金条收支明细界面
 */
public class GoldInOutActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 列表

    private MyAdapter myAdapter;
    private int pages = 0;
    private List<GoldInOutListBean.DataBean> datas = new ArrayList<>();

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
        refreshView = (PullToRefreshLayout) findViewById(R.id.refresh_view);
        listview = (PullableListView) findViewById(R.id.listview);

        refreshView.setOnRefreshListener(new PullListener());
        myAdapter = new MyAdapter();

        GetInOutList getInOutList = new GetInOutList(0);
        getInOutList.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
        }
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

            String year = datas.get(position).getTradeDate().substring(0, 4) + "年";
            String month = datas.get(position).getTradeDate().substring(5, 7) + "月";
            if ('0' == month.charAt(0)) {
                month = month.substring(1);
            }
            String day = datas.get(position).getTradeDate().substring(8, 10) + "日";
            if ('0' == day.charAt(0)) {
                day = day.substring(1);
            }

            // 年月
            if (position == 0) {
                holder.itemLlYear.setVisibility(View.VISIBLE);
                holder.itemYearmonth.setText(year + month);
            } else {
                if (datas.get(position).getTradeDate().substring(5, 7).equals(datas.get(position - 1).getTradeDate().substring(5, 7))) {
                    holder.itemLlYear.setVisibility(View.GONE);
                } else {
                    holder.itemYearmonth.setText(year + month);
                }
            }
            holder.itemDay.setText(day);// 日
            holder.itemType.setText(datas.get(position).getRewardtradetype().getName());// 标题
            if (String.valueOf(datas.get(position).getCount()).contains("-")) {
                holder.itemAmount.setText(String.valueOf(datas.get(position).getCount()));
                holder.itemType.setTextColor(0xff48ae58);
                holder.itemAmount.setTextColor(0xff48ae58);
            } else {
                holder.itemAmount.setText("+" + String.valueOf(datas.get(position).getCount()));
                holder.itemType.setTextColor(UiUtils.getColor(R.color.custom_orange));
                holder.itemAmount.setTextColor(UiUtils.getColor(R.color.custom_orange));
            }
            holder.itemDecs.setText(datas.get(position).getDesc());
            if (position == getCount() - 1) {
                holder.itemEndtag.setVisibility(View.VISIBLE);
            } else {
                holder.itemEndtag.setVisibility(View.GONE);
            }
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

    // 获取交易账单列表
    private class GetInOutList extends AsyncTask<Void, Void, GoldInOutListBean> {
        private int page;

        public GetInOutList(int page) {
            this.page = page;
        }

        @Override
        protected GoldInOutListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETGOLDINOUTLIST)),
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETGOLDINOUTLIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("金条收支明细", body);
                return FastJsonTools.getBean(body, GoldInOutListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(GoldInOutListBean goldInOutListBean) {
            super.onPostExecute(goldInOutListBean);
            if (goldInOutListBean == null) {
                listview.setBackgroundResource(R.mipmap.bg_empty);
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (goldInOutListBean.getStatus() == 200) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        datas = goldInOutListBean.getData();
                        if (datas != null && datas.size() != 0) {
                            listview.setBackgroundResource(R.color.bg_main);
                        } else {
                            listview.setBackgroundResource(R.mipmap.bg_empty);
                        }
                        if (datas != null) {
                            listview.setAdapter(myAdapter);
                        }
                    } else {
                        for (GoldInOutListBean.DataBean dataBean : goldInOutListBean.getData()) {
                            datas.add(dataBean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (goldInOutListBean.getStatus() == 201) {
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    listview.setBackgroundResource(R.mipmap.bg_empty);
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                    SuperToastUtils.showSuperToast(mContext, 2, goldInOutListBean.getMessage());
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
            GetInOutList getInOutList = new GetInOutList(0);
            getInOutList.execute();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            pages++;
            Log.i("页码", String.valueOf(pages));
            GetInOutList getInOutList = new GetInOutList(pages);
            getInOutList.execute();
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }

}
