package com.jinzht.pro1.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
 * 投资机构列表
 */
public class Investor2Fragment extends BaseFragment {

    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 投资机构列表

    private MyAdapter myAdapter;
    private int pages = 0;
    List<InvestorListBean.DataBean> datas = new ArrayList<>();// 数据集合

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_investor2, container, false);
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);// 刷新布局
        listview = (PullableListView) view.findViewById(R.id.lv_investor2);// 投资机构列表
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
//        GetInvestorListTask getInvestorListTask = new GetInvestorListTask(0);
//        getInvestorListTask.execute();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size() + 3;
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
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position < 3) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                if (getItemViewType(position) == 0) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_investor2_fund, null);
                    holder.itemInvestorgFundImg = (ImageView) convertView.findViewById(R.id.item_investorg_fund_img);
                    holder.itemInvestorgFundTitle = (TextView) convertView.findViewById(R.id.item_investorg_fund_title);
                    holder.itemInvestorgFundContent = (TextView) convertView.findViewById(R.id.item_investorg_fund_content);
                } else {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_investor2, null);
                    holder.itemInvestorgFavicon = (CircleImageView) convertView.findViewById(R.id.item_investorg_favicon);
                    holder.itemInvestorgName = (TextView) convertView.findViewById(R.id.item_investorg_name);
                    holder.itemInvestorgAddr = (TextView) convertView.findViewById(R.id.item_investorg_addr);
                    holder.itemInvestorgField1 = (TextView) convertView.findViewById(R.id.item_investorg_field1);
                    holder.itemInvestorgField2 = (TextView) convertView.findViewById(R.id.item_investorg_field2);
                    holder.itemInvestorgField3 = (TextView) convertView.findViewById(R.id.item_investorg_field3);
                    holder.itemInvestorgBtnCollect = (RelativeLayout) convertView.findViewById(R.id.item_investorg_btn_collect);
                    holder.itemInvestorgTvCollect = (TextView) convertView.findViewById(R.id.item_investorg_tv_collect);
                    holder.itemInvestorgBtnSubmit = (RelativeLayout) convertView.findViewById(R.id.item_investorg_btn_submit);
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(mContext).load(datas.get(position).getUser().getHeadSculpture()).into(holder.itemInvestorgFavicon);
            holder.itemInvestorgName.setText(datas.get(position).getUser().getName());
            holder.itemInvestorgAddr.setText(datas.get(position).getUser().getAuthentics().get(0).getCity().getProvince().getName() + " | " + datas.get(position).getUser().getAuthentics().get(0).getCity().getName());

            return convertView;
        }

        class ViewHolder {
            private ImageView itemInvestorgFundImg;
            private TextView itemInvestorgFundTitle;
            private TextView itemInvestorgFundContent;

            private CircleImageView itemInvestorgFavicon;
            private TextView itemInvestorgName;
            private TextView itemInvestorgAddr;
            private TextView itemInvestorgField1;
            private TextView itemInvestorgField2;
            private TextView itemInvestorgField3;
            private RelativeLayout itemInvestorgBtnCollect;
            private TextView itemInvestorgTvCollect;
            private RelativeLayout itemInvestorgBtnSubmit;
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
                            "type", "3",
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

    // 关注投资机构
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
                            "type", "6",
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
