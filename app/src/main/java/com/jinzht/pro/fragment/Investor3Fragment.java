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
import com.jinzht.pro.activity.BrainDetailActivity;
import com.jinzht.pro.activity.CommonWebViewActivity;
import com.jinzht.pro.base.BaseFragment;
import com.jinzht.pro.bean.CommonBean;
import com.jinzht.pro.bean.EventMsg;
import com.jinzht.pro.bean.InvestorListBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.CacheUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
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
 * 智囊团列表
 */
public class Investor3Fragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout pageError;// 错误页面
    private ImageView btnTryagain;// 重试按钮
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 智囊团列表

    private MyAdapter myAdapter;
    private int pages = 0;
    List<InvestorListBean.DataBean> datas = new ArrayList<>();// 数据集合
    private int POSITION = 0;
    private final static int REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_investor3, container, false);
        pageError = (LinearLayout) view.findViewById(R.id.page_error);// 错误页面
        btnTryagain = (ImageView) view.findViewById(R.id.btn_tryagain);// 重试按钮
        btnTryagain.setOnClickListener(this);
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);// 刷新布局
        listview = (PullableListView) view.findViewById(R.id.lv_investor3);// 智囊团列表
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
                if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                    POSITION = position - 1;
                    Intent intent = new Intent(mContext, BrainDetailActivity.class);
                    intent.putExtra("id", String.valueOf(datas.get(position - 1).getUser().getUserId()));
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    Intent intent = new Intent(mContext, CommonWebViewActivity.class);
                    intent.putExtra("title", "智囊团详情");
                    intent.putExtra("url", "file:///android_asset/error.html");
                    startActivity(intent);
                }
            }
        });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                GetInvestorListTask getInvestorListTask = new GetInvestorListTask(0);
//                getInvestorListTask.execute();
//            }
//        }, 1500);

        // 读取缓存
        String cacheInvestor3List = (String) CacheUtils.readObject(Constant.CACHE_INVESTOR3_LIST);
        if (!StringUtils.isBlank(cacheInvestor3List)) {
            InvestorListBean bean = FastJsonTools.getBean(cacheInvestor3List, InvestorListBean.class);
            if (bean != null && bean.getStatus() == 200) {
                datas = bean.getData();
                if (datas != null && datas.size() != 0) {
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
            }, 1500);
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
            if (!StringUtils.isBlank(datas.get(position).getUser().getHeadSculpture())) {
                Glide.with(mContext)
                        .load(datas.get(position).getUser().getHeadSculpture())
//                        .placeholder(R.mipmap.ic_default_favicon)
                        .error(R.mipmap.ic_default_favicon)
                        .into(holder.itemBrainFavicon);
            }
            holder.itemBrainName.setText(datas.get(position).getUser().getName());
            holder.itemBrainPosition.setText(datas.get(position).getUser().getAuthentics().get(0).getPosition());
            holder.itemBrainCompName.setText(datas.get(position).getUser().getAuthentics().get(0).getCompanyName());
            String province = datas.get(position).getUser().getAuthentics().get(0).getCity().getProvince().getName();
            String city = datas.get(position).getUser().getAuthentics().get(0).getCity().getName();
            if ("北京天津上海重庆香港澳门钓鱼岛".contains(province)) {
                holder.itemBrainAddr.setText(province);
            } else {
                holder.itemBrainAddr.setText(province + " | " + city);
            }
            holder.itemBrainDesc.setText(datas.get(position).getUser().getAuthentics().get(0).getCompanyIntroduce());
            if (datas.get(position).isCollected()) {
                holder.itemBrainBtnCollect.setBackgroundResource(R.drawable.bg_code_gray);
                holder.itemBrainTvCollect.setText("已关注");
            } else {
                holder.itemBrainBtnCollect.setBackgroundResource(R.drawable.bg_btn_green);
                if (datas.get(position).getCollectCount() >= 1000) {
                    holder.itemBrainTvCollect.setText("关注(999...)");
                } else {
                    holder.itemBrainTvCollect.setText("关注(" + datas.get(position).getCollectCount() + ")");
                }
            }
            // 关注
            holder.itemBrainBtnCollect.setOnClickListener(new View.OnClickListener() {// 关注
                @Override
                public void onClick(View v) {
                    if (clickable) {
                        clickable = false;
                        POSITION = position;
                        if (datas.get(position).isCollected()) {
                            CollectInvestorTask collectInvestorTask = new CollectInvestorTask(datas.get(position).getUser().getUserId(), 2);
                            collectInvestorTask.execute();
                        } else {
                            CollectInvestorTask collectInvestorTask = new CollectInvestorTask(datas.get(position).getUser().getUserId(), 1);
                            collectInvestorTask.execute();
                        }
                    }
                }
            });
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

    // 获取智囊团列表
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
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETINVESTORLIST)),
                            "type", "4",
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETINVESTORLIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("智囊团列表信息", body);
                return FastJsonTools.getBean(body, InvestorListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(InvestorListBean investorListBean) {
            super.onPostExecute(investorListBean);
            clickable = true;
            if (investorListBean == null) {
//                pageError.setVisibility(View.VISIBLE);
//                refreshView.setVisibility(View.GONE);
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (investorListBean.getStatus() == 200) {
                    pageError.setVisibility(View.GONE);
                    refreshView.setVisibility(View.VISIBLE);
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        datas = investorListBean.getData();
                        if (datas != null && datas.size() != 0) {
                            listview.setBackgroundResource(R.color.bg_main);
                        } else {
//                            listview.setBackgroundResource(R.mipmap.bg_empty);
                        }
                        if (datas != null) {
                            listview.setAdapter(myAdapter);
                            // 缓存数据
                            CacheUtils.saveObject(JSON.toJSONString(investorListBean), Constant.CACHE_INVESTOR3_LIST);
                        }
                    } else {
                        for (InvestorListBean.DataBean dataBean : investorListBean.getData()) {
                            datas.add(dataBean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (investorListBean.getStatus() == 201) {
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

    // 关注智囊团
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
                Log.i("关注智囊团返回信息", body);
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
            if (resultCode == BrainDetailActivity.RESULT_CODE) {
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
