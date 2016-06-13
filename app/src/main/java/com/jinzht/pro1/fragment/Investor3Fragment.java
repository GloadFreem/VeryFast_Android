package com.jinzht.pro1.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseFragment;
import com.jinzht.pro1.bean.CommonBean;
import com.jinzht.pro1.bean.InvestorListBean;
import com.jinzht.pro1.utils.AESUtils;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.FastJsonTools;
import com.jinzht.pro1.utils.MD5Utils;
import com.jinzht.pro1.utils.NetWorkUtils;
import com.jinzht.pro1.utils.OkHttpUtils;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.view.CircleImageView;
import com.jinzht.pro1.view.PullToRefreshLayout;
import com.jinzht.pro1.view.PullableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 智囊团列表
 */
public class Investor3Fragment extends BaseFragment {

    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 智囊团列表

    private MyAdapter myAdapter;
    private int pages = 0;
    List<InvestorListBean.DataBean> datas = new ArrayList<>();// 数据集合

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_investor3, container, false);
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);// 刷新布局
        listview = (PullableListView) view.findViewById(R.id.lv_investor3);// 智囊团列表
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshView.setOnRefreshListener(new PullListener());
        myAdapter = new MyAdapter();
        listview.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.layout_empty_view_9dp, null));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SuperToastUtils.showSuperToast(mContext, 2, "点击了条目" + position);
            }
        });
        GetInvestorListTask getInvestorListTask = new GetInvestorListTask(0);
        getInvestorListTask.execute();
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_investor3, null);
                holder.itemBrainFavicon = (CircleImageView) convertView.findViewById(R.id.item_brain_favicon);
                holder.itemBrainName = (TextView) convertView.findViewById(R.id.item_brain_name);
                holder.itemBrainPosition = (TextView) convertView.findViewById(R.id.item_brain_position);
                holder.itemBrainCompName = (TextView) convertView.findViewById(R.id.item_brain_comp_name);
                holder.itemBrainAddr = (TextView) convertView.findViewById(R.id.item_brain_addr);
                holder.itemBrainDesc = (TextView) convertView.findViewById(R.id.item_brain_desc);
                holder.itemBrainBtnCollect = (RelativeLayout) convertView.findViewById(R.id.item_brain_btn_collect);
                holder.itemBrainTvCollect = (TextView) convertView.findViewById(R.id.item_brain_tv_collect);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(mContext).load(datas.get(position).getUser().getHeadSculpture()).into(holder.itemBrainFavicon);
            holder.itemBrainName.setText(datas.get(position).getUser().getName());
            holder.itemBrainPosition.setText(datas.get(position).getUser().getAuthentics().get(0).getPosition());
            holder.itemBrainCompName.setText(datas.get(position).getUser().getAuthentics().get(0).getCompanyName());
            holder.itemBrainAddr.setText(datas.get(position).getUser().getAuthentics().get(0).getCity().getProvince().getName() + " | " + datas.get(position).getUser().getAuthentics().get(0).getCity().getName());

            return convertView;
        }

        class ViewHolder {
            private CircleImageView itemBrainFavicon;
            private TextView itemBrainName;
            private TextView itemBrainPosition;
            private TextView itemBrainCompName;
            private TextView itemBrainAddr;
            private TextView itemBrainDesc;
            private RelativeLayout itemBrainBtnCollect;
            private TextView itemBrainTvCollect;
        }
    }

    // 获取投资机构列表
    private class GetInvestorListTask extends AsyncTask<Void, Void, InvestorListBean> {
        private int page;

        public GetInvestorListTask(int page) {
            this.page = page;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("");
        }

        @Override
        protected InvestorListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETINVESOTORLIST)),
                            "type", "4",
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETINVESOTORLIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("投资人列表信息", body);
                return FastJsonTools.getBean(body, InvestorListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(InvestorListBean investorListBean) {
            super.onPostExecute(investorListBean);
            dismissProgressDialog();
            if (investorListBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                return;
            } else {
                if (investorListBean.getStatus() == 200) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        datas = investorListBean.getData();
//                        for (InvestorListBean.DataBean dataBean : datas) {
//                            Log.i("内容", dataBean.toString());
//                        }
                        listview.setAdapter(myAdapter);
                    } else {
                        for (InvestorListBean.DataBean dataBean : investorListBean.getData()) {
                            Log.i("内容", dataBean.toString());
                            datas.add(dataBean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (investorListBean.getStatus() == 201) {
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                    SuperToastUtils.showSuperToast(mContext, 2, investorListBean.getMessage());
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
            GetInvestorListTask getInvestorListTask = new GetInvestorListTask(0);
            getInvestorListTask.execute();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            pages++;
            Log.i("页码", String.valueOf(pages));
            GetInvestorListTask getInvestorListTask = new GetInvestorListTask(pages);
            getInvestorListTask.execute();
        }
    }

    // 关注智囊团
    private class CollectInvestorTask extends AsyncTask<Void, Void, CommonBean> {
        int userId;

        public CollectInvestorTask(int userId) {
            this.userId = userId;
        }

        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.COLLECTINVESTOR)),
                            "type", "7",
                            "userId", String.valueOf(userId),
                            Constant.BASE_URL + Constant.COLLECTINVESTOR,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("关注投资人返回信息", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            if (commonBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (commonBean.getStatus() == 200) {

                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, commonBean.getMessage());
                }
            }
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
