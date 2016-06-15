package com.jinzht.pro1.fragment;

import android.content.Intent;
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
import com.jinzht.pro1.activity.InvestorDetailActivity;
import com.jinzht.pro1.activity.SubmitProjectActivity;
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
 * 投资人列表
 */
public class Investor1Fragment extends BaseFragment {

    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 投资人列表

    private MyAdapter myAdapter;
    private int pages = 0;
    List<InvestorListBean.DataBean> datas = new ArrayList<>();// 数据集合
    private int POSITION = 0;
    private final static int REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_investor1, container, false);
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);// 刷新布局
        listview = (PullableListView) view.findViewById(R.id.lv_investor1);// 投资人列表
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
                Intent intent = new Intent(mContext, InvestorDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("detail", datas.get(position - 1));
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE);
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_investor1, null);
                holder.itemInvestorFavicon = (CircleImageView) convertView.findViewById(R.id.item_investor_favicon);
                holder.itemInvestorName = (TextView) convertView.findViewById(R.id.item_investor_name);
                holder.itemInvestorPosition = (TextView) convertView.findViewById(R.id.item_investor_position);
                holder.itemInvestorCompName = (TextView) convertView.findViewById(R.id.item_investor_comp_name);
                holder.itemInvestorAddr = (TextView) convertView.findViewById(R.id.item_investor_addr);
                holder.itemInvestorField1 = (TextView) convertView.findViewById(R.id.item_investor_field1);
                holder.itemInvestorField2 = (TextView) convertView.findViewById(R.id.item_investor_field2);
                holder.itemInvestorField3 = (TextView) convertView.findViewById(R.id.item_investor_field3);
                holder.itemInvestorBtnCollect = (RelativeLayout) convertView.findViewById(R.id.item_investor_btn_collect);
                holder.itemInvestorTvCollect = (TextView) convertView.findViewById(R.id.item_investor_tv_collect);
                holder.itemInvestorBtnSubmit = (RelativeLayout) convertView.findViewById(R.id.item_investor_btn_submit);
                holder.itemInvestorTvSubmit = (TextView) convertView.findViewById(R.id.item_investor_tv_submit);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(mContext).load(datas.get(position).getUser().getHeadSculpture()).into(holder.itemInvestorFavicon);
            holder.itemInvestorName.setText(datas.get(position).getUser().getName());
            holder.itemInvestorPosition.setText(datas.get(position).getUser().getAuthentics().get(0).getPosition());
            holder.itemInvestorCompName.setText(datas.get(position).getUser().getAuthentics().get(0).getCompanyName());
            holder.itemInvestorAddr.setText(datas.get(position).getUser().getAuthentics().get(0).getCity().getProvince().getName() + " | " + datas.get(position).getUser().getAuthentics().get(0).getCity().getName());
            if (datas.get(position).getAreas().size() == 1) {
                holder.itemInvestorField1.setText(datas.get(position).getAreas().get(0));
                holder.itemInvestorField2.setVisibility(View.INVISIBLE);
                holder.itemInvestorField3.setVisibility(View.INVISIBLE);
            } else if (datas.get(position).getAreas().size() == 2) {
                holder.itemInvestorField1.setText(datas.get(position).getAreas().get(0));
                holder.itemInvestorField2.setText(datas.get(position).getAreas().get(1));
                holder.itemInvestorField3.setVisibility(View.INVISIBLE);
            } else if (datas.get(position).getAreas().size() == 3) {
                holder.itemInvestorField1.setText(datas.get(position).getAreas().get(0));
                holder.itemInvestorField2.setText(datas.get(position).getAreas().get(1));
                holder.itemInvestorField3.setText(datas.get(position).getAreas().get(2));
            }
            if (datas.get(position).isCollected()) {
                holder.itemInvestorBtnCollect.setBackgroundResource(R.drawable.bg_code_gray);
                holder.itemInvestorTvCollect.setText("已关注");
            } else {
                holder.itemInvestorBtnCollect.setBackgroundResource(R.drawable.bg_btn_green);
                if (datas.get(position).getCollectCount() >= 1000) {
                    holder.itemInvestorTvCollect.setText("关注(999...)");
                } else {
                    holder.itemInvestorTvCollect.setText("关注(" + datas.get(position).getCollectCount() + ")");
                }
            }
            if (datas.get(position).isCommited()) {
                holder.itemInvestorBtnSubmit.setBackgroundResource(R.drawable.bg_code_gray);
                holder.itemInvestorBtnSubmit.setClickable(false);
                holder.itemInvestorTvSubmit.setText("已提交");
            } else {
                holder.itemInvestorBtnSubmit.setBackgroundResource(R.drawable.bg_code_orange);
                holder.itemInvestorBtnSubmit.setClickable(true);
                holder.itemInvestorTvSubmit.setText("提交项目");
            }
            holder.itemInvestorBtnCollect.setOnClickListener(new View.OnClickListener() {// 关注
                @Override
                public void onClick(View v) {
                    POSITION = position;
                    if (datas.get(position).isCollected()) {
                        CollectInvestorTask collectInvestorTask = new CollectInvestorTask(datas.get(position).getUser().getUserId(), 2);
                        collectInvestorTask.execute();
                    } else {
                        CollectInvestorTask collectInvestorTask = new CollectInvestorTask(datas.get(position).getUser().getUserId(), 1);
                        collectInvestorTask.execute();
                    }
                }
            });
            holder.itemInvestorBtnSubmit.setOnClickListener(new View.OnClickListener() {// 提交项目
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SubmitProjectActivity.class);
                    intent.putExtra("id", String.valueOf(datas.get(position).getUser().getUserId()));
                    intent.putExtra("favicon", datas.get(position).getUser().getHeadSculpture());
                    intent.putExtra("name", datas.get(position).getUser().getName());
                    intent.putExtra("position", datas.get(position).getUser().getAuthentics().get(0).getPosition());
                    intent.putExtra("compName", datas.get(position).getUser().getAuthentics().get(0).getCompanyName());
                    intent.putExtra("addr", datas.get(position).getUser().getAuthentics().get(0).getCity().getProvince().getName() + " | " + datas.get(position).getUser().getAuthentics().get(0).getCity().getName());
                    startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHolder {
            private CircleImageView itemInvestorFavicon;
            private TextView itemInvestorName;
            private TextView itemInvestorPosition;
            private TextView itemInvestorCompName;
            private TextView itemInvestorAddr;
            private TextView itemInvestorField1;
            private TextView itemInvestorField2;
            private TextView itemInvestorField3;
            private RelativeLayout itemInvestorBtnCollect;
            private TextView itemInvestorTvCollect;
            private RelativeLayout itemInvestorBtnSubmit;
            private TextView itemInvestorTvSubmit;
        }
    }

    // 获取投资人列表
    private class GetInvestorListTask extends AsyncTask<Void, Void, InvestorListBean> {
        private int page;

        public GetInvestorListTask(int page) {
            this.page = page;
        }

        @Override
        protected InvestorListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETINVESOTORLIST)),
                            "type", "2",
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

    // 关注投资人
    private class CollectInvestorTask extends AsyncTask<Void, Void, CommonBean> {
        int userId;
        int flag;

        public CollectInvestorTask(int userId, int flag) {
            this.userId = userId;
            this.flag = flag;
        }

        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.COLLECTINVESTOR)),
                            "userId", String.valueOf(userId),
                            "flag", String.valueOf(flag),
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
                    if (flag == 1) {
                        datas.get(POSITION).setCollected(true);
                        datas.get(POSITION).setCollectCount(datas.get(POSITION).getCollectCount() + 1);
                    } else if (flag == 2) {
                        datas.get(POSITION).setCollected(false);
                        datas.get(POSITION).setCollectCount(datas.get(POSITION).getCollectCount() - 1);
                    }
                    myAdapter.notifyDataSetChanged();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, commonBean.getMessage());
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            if (resultCode == InvestorDetailActivity.RESULT_CODE) {
                if (data.getBooleanExtra("needRefresh", false)) {// 在详情中进行了交互
                    GetInvestorListTask getInvestorListTask = new GetInvestorListTask(0);
                    getInvestorListTask.execute();
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
