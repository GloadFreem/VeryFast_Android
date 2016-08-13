package com.jinzht.pro.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.activity.BrainDetailActivity;
import com.jinzht.pro.activity.InvestorDetailActivity;
import com.jinzht.pro.activity.InvestorgDetailActivity;
import com.jinzht.pro.base.BaseFragment;
import com.jinzht.pro.bean.MyCollectInvestorBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.view.CircleImageView;
import com.jinzht.pro.view.PullToRefreshLayout;
import com.jinzht.pro.view.PullableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的关注中投资人界面
 */
public class MyCollectInvestorFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout pageError;// 错误页面
    private ImageView btnTryagain;// 重试按钮
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 列表

    private MyAdapter myAdapter;
    private int pages = 0;
    List<MyCollectInvestorBean.DataBean> datas = new ArrayList<>();// 数据集合
    private int POSITION = 0;
    private final static int REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_collect_investor, container, false);
        pageError = (LinearLayout) view.findViewById(R.id.page_error);// 错误页面
        btnTryagain = (ImageView) view.findViewById(R.id.btn_tryagain);// 重试按钮
        btnTryagain.setOnClickListener(this);
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);// 刷新布局
        listview = (PullableListView) view.findViewById(R.id.listview);// 列表
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshView.setOnRefreshListener(new PullListener());
        myAdapter = new MyAdapter();
        listview.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.layout_empty_view_9dp, null), null, false);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                POSITION = position - 1;
                Intent intent = new Intent();
                intent.putExtra("id", String.valueOf(datas.get(position - 1).getUsersByUserCollectedId().getUserId()));
                switch (datas.get(position - 1).getUsersByUserCollectedId().getAuthentics().get(0).getIdentiytype().getIdentiyTypeId()) {
                    case 2:// 个人投资者
                        intent.setClass(mContext, InvestorDetailActivity.class);
                        startActivityForResult(intent, REQUEST_CODE);
                        break;
                    case 3:// 机构投资者
                        intent.setClass(mContext, InvestorgDetailActivity.class);
                        startActivityForResult(intent, REQUEST_CODE);
                        break;
                    case 4:// 智囊团
                        intent.setClass(mContext, BrainDetailActivity.class);
                        startActivityForResult(intent, REQUEST_CODE);
                        break;
                }
            }
        });

        GetInvestorList getInvestorList = new GetInvestorList(0);
        getInvestorList.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tryagain:// 重试加载网络
                if (clickable) {
                    clickable = false;
                    GetInvestorList getInvestorList = new GetInvestorList(0);
                    getInvestorList.execute();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mycollect_investor, null);
                holder.ivFavicon = (CircleImageView) convertView.findViewById(R.id.iv_favicon);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvPosition = (TextView) convertView.findViewById(R.id.tv_position);
                holder.tvCompName = (TextView) convertView.findViewById(R.id.tv_comp_name);
                holder.ivTag = (ImageView) convertView.findViewById(R.id.iv_tag);
                holder.tvAddr = (TextView) convertView.findViewById(R.id.tv_addr);
                holder.tvField1 = (TextView) convertView.findViewById(R.id.tv_field1);
                holder.tvField2 = (TextView) convertView.findViewById(R.id.tv_field2);
                holder.tvField3 = (TextView) convertView.findViewById(R.id.tv_field3);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(mContext).load(datas.get(position).getUsersByUserCollectedId().getHeadSculpture()).into(holder.ivFavicon);
            holder.tvName.setText(datas.get(position).getUsersByUserCollectedId().getName());
            holder.tvPosition.setText(datas.get(position).getUsersByUserCollectedId().getAuthentics().get(0).getPosition());
            holder.tvCompName.setText(datas.get(position).getUsersByUserCollectedId().getAuthentics().get(0).getCompanyName());
            switch (datas.get(position).getUsersByUserCollectedId().getAuthentics().get(0).getIdentiytype().getIdentiyTypeId()) {
                case 2:// 个人投资者
                    holder.ivTag.setImageResource(R.mipmap.icon_tag_investor);
                    break;
                case 3:// 机构投资者
                    holder.ivTag.setImageResource(R.mipmap.icon_tag_investorg);
                    break;
                case 4:// 智囊团
                    holder.ivTag.setImageResource(R.mipmap.icon_tag_brain);
                    break;
            }
            String province = datas.get(position).getUsersByUserCollectedId().getAuthentics().get(0).getCity().getProvince().getName();
            String city = datas.get(position).getUsersByUserCollectedId().getAuthentics().get(0).getCity().getName();
            if ("北京天津上海重庆香港澳门钓鱼岛".contains(province)) {
                holder.tvAddr.setText(province);
            } else {
                holder.tvAddr.setText(province + " | " + city);
            }
            String[] fields = datas.get(position).getUsersByUserCollectedId().getAuthentics().get(0).getIndustoryArea().split("，");
            if (fields.length == 0) {
                holder.tvField1.setVisibility(View.INVISIBLE);
                holder.tvField2.setVisibility(View.INVISIBLE);
                holder.tvField3.setVisibility(View.INVISIBLE);
            } else if (fields.length == 1) {
                holder.tvField1.setText(fields[0]);
                holder.tvField2.setVisibility(View.INVISIBLE);
                holder.tvField3.setVisibility(View.INVISIBLE);
            } else if (fields.length == 2) {
                holder.tvField1.setText(fields[0]);
                holder.tvField2.setText(fields[1]);
                holder.tvField3.setVisibility(View.INVISIBLE);
            } else if (fields.length == 3) {
                holder.tvField1.setText(fields[0]);
                holder.tvField2.setText(fields[1]);
                holder.tvField3.setText(fields[2]);
            }
            return convertView;
        }

        class ViewHolder {
            private CircleImageView ivFavicon;
            private TextView tvName;
            private TextView tvPosition;
            private TextView tvCompName;
            private ImageView ivTag;
            private TextView tvAddr;
            private TextView tvField1;
            private TextView tvField2;
            private TextView tvField3;
        }
    }

    // 获取我的关注投资人列表
    private class GetInvestorList extends AsyncTask<Void, Void, MyCollectInvestorBean> {
        private int page;

        public GetInvestorList(int page) {
            this.page = page;
        }

        @Override
        protected MyCollectInvestorBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETMYCOLLECT)),
                            "type", "1",
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETMYCOLLECT,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("关注的投资人列表", body);
                return FastJsonTools.getBean(body, MyCollectInvestorBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(MyCollectInvestorBean myCollectInvestorBean) {
            super.onPostExecute(myCollectInvestorBean);
            clickable = true;
            if (myCollectInvestorBean == null) {
                pageError.setVisibility(View.VISIBLE);
                refreshView.setVisibility(View.GONE);
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (myCollectInvestorBean.getStatus() == 200) {
                    pageError.setVisibility(View.GONE);
                    refreshView.setVisibility(View.VISIBLE);
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        datas = myCollectInvestorBean.getData();
                        if (datas != null && datas.size() != 0) {
                            listview.setBackgroundResource(R.color.bg_main);
                        } else {
                            listview.setBackgroundResource(R.mipmap.bg_empty);
                        }
                        if (datas != null) {
                            listview.setAdapter(myAdapter);
                        }
                    } else {
                        for (MyCollectInvestorBean.DataBean dataBean : myCollectInvestorBean.getData()) {
                            datas.add(dataBean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (myCollectInvestorBean.getStatus() == 201) {
                    pageError.setVisibility(View.GONE);
                    refreshView.setVisibility(View.VISIBLE);
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    pageError.setVisibility(View.VISIBLE);
                    refreshView.setVisibility(View.GONE);
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
            GetInvestorList getInvestorList = new GetInvestorList(0);
            getInvestorList.execute();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            pages++;
            Log.i("页码", String.valueOf(pages));
            GetInvestorList getInvestorList = new GetInvestorList(pages);
            getInvestorList.execute();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            if (resultCode == InvestorDetailActivity.RESULT_CODE || resultCode == InvestorgDetailActivity.RESULT_CODE || resultCode == BrainDetailActivity.RESULT_CODE) {
                if (data.getIntExtra("FLAG", 0) == 2) {// 在详情中取消了关注
                    datas.remove(POSITION);
                    myAdapter.notifyDataSetChanged();
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
