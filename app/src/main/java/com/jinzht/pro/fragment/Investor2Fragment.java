package com.jinzht.pro.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.activity.CommonWebViewActivity;
import com.jinzht.pro.activity.InvestorgDetailActivity;
import com.jinzht.pro.activity.SubmitProjectActivity;
import com.jinzht.pro.base.BaseFragment;
import com.jinzht.pro.bean.CommonBean;
import com.jinzht.pro.bean.EventMsg;
import com.jinzht.pro.bean.InvestorgListBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.CacheUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.view.CircleImageView;
import com.jinzht.pro.view.PullToRefreshLayout;
import com.jinzht.pro.view.PullableListView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 投资机构列表
 */
public class Investor2Fragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout pageError;// 错误页面
    private ImageView btnTryagain;// 重试按钮
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 投资机构列表

    private MyAdapter myAdapter;
    private int pages = 0;
    List<InvestorgListBean.DataBean.FounddationsBean> funds = new ArrayList<>();// 基金集合
    List<InvestorgListBean.DataBean.InvestorsBean> datas = new ArrayList<>();// 数据集合
    private int POSITION = 0;
    private final static int REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_investor2, container, false);
        pageError = (LinearLayout) view.findViewById(R.id.page_error);// 错误页面
        btnTryagain = (ImageView) view.findViewById(R.id.btn_tryagain);// 重试按钮
        btnTryagain.setOnClickListener(this);
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);// 刷新布局
        listview = (PullableListView) view.findViewById(R.id.lv_investor2);// 投资机构列表
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        refreshView.setOnRefreshListener(new PullListener());
        myAdapter = new MyAdapter();
        listview.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.layout_empty_view_9dp, null));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < funds.size() + 1) {
                    Intent intent = new Intent(mContext, CommonWebViewActivity.class);
                    intent.putExtra("title", funds.get(position - 1).getName());
                    intent.putExtra("url", funds.get(position - 1).getUrl());
                    startActivity(intent);
                } else {
                    if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                        POSITION = position - funds.size() - 1;
                        Intent intent = new Intent(mContext, InvestorgDetailActivity.class);
                        intent.putExtra("id", String.valueOf(datas.get(position - funds.size() - 1).getUser().getUserId()));
                        startActivityForResult(intent, REQUEST_CODE);
                    } else {
                        Intent intent = new Intent(mContext, CommonWebViewActivity.class);
                        intent.putExtra("title", "投资机构详情");
                        intent.putExtra("url", "file:///android_asset/error.html");
                        startActivity(intent);
                    }
                }
            }
        });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                GetInvestorListTask getInvestorListTask = new GetInvestorListTask(0);
//                getInvestorListTask.execute();
//            }
//        }, 1000);

        // 读取缓存
        String cacheInvestor2List = (String) CacheUtils.readObject(Constant.CACHE_INVESTOR2_LIST);
        if (!StringUtils.isBlank(cacheInvestor2List)) {
            InvestorgListBean bean = FastJsonTools.getBean(cacheInvestor2List, InvestorgListBean.class);
            if (bean != null && bean.getStatus() == 200) {
                funds = bean.getData().getFounddations();
                datas = bean.getData().getInvestors();
                if (funds != null && datas != null && datas.size() != 0) {
                    listview.setAdapter(myAdapter);
                }
            }
        } else {
            pageError.setVisibility(View.VISIBLE);
            refreshView.setVisibility(View.INVISIBLE);
        }
    }

    // 接收是否登录成功的提示
    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void getLoginInfo(EventMsg msg) {
        if ("登录成功".equals(msg.getMsg())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    GetInvestorListTask getInvestorListTask = new GetInvestorListTask(0);
                    getInvestorListTask.execute();
                }
            }, 1000);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tryagain:// 重试加载网络
                if (clickable) {
                    clickable = false;
                    GetInvestorListTask getInvestorListTask = new GetInvestorListTask(0);
                    getInvestorListTask.execute();
                }
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return funds.size() + datas.size();
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
            if (position < funds.size()) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
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
                    holder.itemInvestorgTvSubmit = (TextView) convertView.findViewById(R.id.item_investorg_tv_submit);
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (getItemViewType(position) == 0) {
                Glide.with(mContext).load(funds.get(position).getImage()).into(holder.itemInvestorgFundImg);
                holder.itemInvestorgFundTitle.setText(funds.get(position).getName());
                holder.itemInvestorgFundContent.setText(funds.get(position).getContent());
            } else {
                if (!StringUtils.isBlank(datas.get(position - funds.size()).getUser().getHeadSculpture())) {
                    Glide.with(mContext)
                            .load(datas.get(position - funds.size()).getUser().getHeadSculpture())
//                            .placeholder(R.mipmap.ic_default_favicon)
                            .error(R.mipmap.ic_default_favicon)
                            .into(holder.itemInvestorgFavicon);
                }
                holder.itemInvestorgName.setText(datas.get(position - funds.size()).getUser().getName());
                String province = datas.get(position - funds.size()).getUser().getAuthentics().get(0).getCity().getProvince().getName();
                String city = datas.get(position - funds.size()).getUser().getAuthentics().get(0).getCity().getName();
                if ("北京天津上海重庆香港澳门钓鱼岛".contains(province)) {
                    holder.itemInvestorgAddr.setText(province);
                } else {
                    holder.itemInvestorgAddr.setText(province + " | " + city);
                }
                if (datas.get(position - funds.size()).getAreas().size() == 0) {
                    holder.itemInvestorgField1.setVisibility(View.INVISIBLE);
                    holder.itemInvestorgField2.setVisibility(View.INVISIBLE);
                    holder.itemInvestorgField3.setVisibility(View.INVISIBLE);
                } else if (datas.get(position - funds.size()).getAreas().size() == 1) {
                    holder.itemInvestorgField1.setVisibility(View.VISIBLE);
                    holder.itemInvestorgField1.setText(datas.get(position - funds.size()).getAreas().get(0));
                    holder.itemInvestorgField2.setVisibility(View.INVISIBLE);
                    holder.itemInvestorgField3.setVisibility(View.INVISIBLE);
                } else if (datas.get(position - funds.size()).getAreas().size() == 2) {
                    holder.itemInvestorgField1.setVisibility(View.VISIBLE);
                    holder.itemInvestorgField1.setText(datas.get(position - funds.size()).getAreas().get(0));
                    holder.itemInvestorgField2.setVisibility(View.VISIBLE);
                    holder.itemInvestorgField2.setText(datas.get(position - funds.size()).getAreas().get(1));
                    holder.itemInvestorgField3.setVisibility(View.INVISIBLE);
                } else {
                    holder.itemInvestorgField1.setVisibility(View.VISIBLE);
                    holder.itemInvestorgField1.setText(datas.get(position - funds.size()).getAreas().get(0));
                    holder.itemInvestorgField2.setVisibility(View.VISIBLE);
                    holder.itemInvestorgField2.setText(datas.get(position - funds.size()).getAreas().get(1));
                    holder.itemInvestorgField3.setVisibility(View.VISIBLE);
                    holder.itemInvestorgField3.setText(datas.get(position - funds.size()).getAreas().get(2));
                }
                if (datas.get(position - funds.size()).isCollected()) {
                    holder.itemInvestorgBtnCollect.setBackgroundResource(R.drawable.bg_code_gray);
                    holder.itemInvestorgTvCollect.setText("已关注");
                } else {
                    holder.itemInvestorgBtnCollect.setBackgroundResource(R.drawable.bg_btn_green);
                    if (datas.get(position - funds.size()).getCollectCount() >= 1000) {
                        holder.itemInvestorgTvCollect.setText("关注(999...)");
                    } else {
                        holder.itemInvestorgTvCollect.setText("关注(" + datas.get(position - funds.size()).getCollectCount() + ")");
                    }
                }
                // 只有项目方才显示提交项目按钮
                if (SharedPreferencesUtils.getUserType(mContext) == Constant.USERTYPE_XMF) {
                    holder.itemInvestorgBtnSubmit.setVisibility(View.VISIBLE);
                } else {
                    holder.itemInvestorgBtnSubmit.setVisibility(View.GONE);
                }
                if (datas.get(position - funds.size()).isCommited()) {
                    holder.itemInvestorgBtnSubmit.setBackgroundResource(R.drawable.bg_code_gray);
                    holder.itemInvestorgTvSubmit.setText("已提交");
                    holder.itemInvestorgBtnSubmit.setClickable(false);
                } else {
                    holder.itemInvestorgBtnSubmit.setBackgroundResource(R.drawable.bg_code_orange);
                    holder.itemInvestorgTvSubmit.setText("提交项目");
                    holder.itemInvestorgBtnSubmit.setClickable(true);
                    // 提交项目
                    holder.itemInvestorgBtnSubmit.setOnClickListener(new View.OnClickListener() {// 提交项目
                        @Override
                        public void onClick(View v) {
                            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                                Intent intent = new Intent(mContext, SubmitProjectActivity.class);
                                intent.putExtra("id", String.valueOf(datas.get(position - funds.size()).getUser().getUserId()));
                                intent.putExtra("favicon", datas.get(position - funds.size()).getUser().getHeadSculpture());
                                intent.putExtra("name", datas.get(position - funds.size()).getUser().getName());
                                intent.putExtra("position", datas.get(position - funds.size()).getUser().getAuthentics().get(0).getPosition());
                                intent.putExtra("compName", datas.get(position - funds.size()).getUser().getAuthentics().get(0).getCompanyName());
                                String province = datas.get(position - funds.size()).getUser().getAuthentics().get(0).getCity().getProvince().getName();
                                String city = datas.get(position - funds.size()).getUser().getAuthentics().get(0).getCity().getName();
                                if ("北京天津上海重庆香港澳门钓鱼岛".contains(province)) {
                                    intent.putExtra("addr", province);
                                } else {
                                    intent.putExtra("addr", province + " | " + city);
                                }
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(mContext, CommonWebViewActivity.class);
                                intent.putExtra("title", "提交项目");
                                intent.putExtra("url", "file:///android_asset/error.html");
                                startActivity(intent);
                            }
                        }
                    });
                }
                // 关注
                holder.itemInvestorgBtnCollect.setOnClickListener(new View.OnClickListener() {// 关注
                    @Override
                    public void onClick(View v) {
                        if (clickable) {
                            clickable = false;
                            POSITION = position;
                            if (datas.get(position - funds.size()).isCollected()) {
                                CollectInvestorTask collectInvestorTask = new CollectInvestorTask(datas.get(position - funds.size()).getUser().getUserId(), 2);
                                collectInvestorTask.execute();
                            } else {
                                CollectInvestorTask collectInvestorTask = new CollectInvestorTask(datas.get(position - funds.size()).getUser().getUserId(), 1);
                                collectInvestorTask.execute();
                            }
                        }
                    }
                });
            }
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
            private TextView itemInvestorgTvSubmit;
        }
    }

    // 获取投资机构列表
    private class GetInvestorListTask extends AsyncTask<Void, Void, InvestorgListBean> {
        private int page;

        public GetInvestorListTask(int page) {
            this.page = page;
        }

        @Override
        protected InvestorgListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETINVESTORLIST)),
                            "type", "3",
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETINVESTORLIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("投资机构列表信息", body);
                return FastJsonTools.getBean(body, InvestorgListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(InvestorgListBean investorgListBean) {
            super.onPostExecute(investorgListBean);
            clickable = true;
            if (investorgListBean == null) {
//                pageError.setVisibility(View.VISIBLE);
//                refreshView.setVisibility(View.GONE);
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (investorgListBean.getStatus() == 200) {
                    pageError.setVisibility(View.GONE);
                    refreshView.setVisibility(View.VISIBLE);
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        funds = investorgListBean.getData().getFounddations();
                        datas = investorgListBean.getData().getInvestors();
                        if ((funds != null && funds.size() != 0) || (datas != null && datas.size() != 0)) {
                            listview.setBackgroundResource(R.color.bg_main);
                        } else {
//                            listview.setBackgroundResource(R.mipmap.bg_empty);
                        }
                        if (funds != null && datas != null) {
                            listview.setAdapter(myAdapter);
                            // 缓存数据
                            CacheUtils.saveObject(JSON.toJSONString(investorgListBean), Constant.CACHE_INVESTOR2_LIST);
                        }
                    } else {
                        for (InvestorgListBean.DataBean.InvestorsBean dataBean : investorgListBean.getData().getInvestors()) {
                            datas.add(dataBean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (investorgListBean.getStatus() == 201) {
//                    pageError.setVisibility(View.GONE);
//                    refreshView.setVisibility(View.VISIBLE);
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
//                    pageError.setVisibility(View.VISIBLE);
//                    refreshView.setVisibility(View.GONE);
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
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
                Log.i("关注投资机构返回信息", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            clickable = true;
            if (commonBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (commonBean.getStatus() == 200) {
                    if (flag == 1) {
                        datas.get(POSITION - funds.size()).setCollected(true);
                        datas.get(POSITION - funds.size()).setCollectCount(datas.get(POSITION - funds.size()).getCollectCount() + 1);
                    } else if (flag == 2) {
                        datas.get(POSITION - funds.size()).setCollected(false);
                        datas.get(POSITION - funds.size()).setCollectCount(datas.get(POSITION - funds.size()).getCollectCount() - 1);
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
            if (resultCode == InvestorgDetailActivity.RESULT_CODE) {
                if (data.getIntExtra("FLAG", 0) == 1) {// 在详情中关注了
                    datas.get(POSITION).setCollected(true);
                    datas.get(POSITION).setCollectCount(datas.get(POSITION).getCollectCount() + 1);
                } else if (data.getIntExtra("FLAG", 0) == 2) {// 在详情中取消了关注
                    datas.get(POSITION).setCollected(false);
                    datas.get(POSITION).setCollectCount(datas.get(POSITION).getCollectCount() - 1);
                }
                myAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
