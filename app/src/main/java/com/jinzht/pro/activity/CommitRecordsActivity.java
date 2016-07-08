package com.jinzht.pro.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.CommitRecordsBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.DateUtils;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.view.CircleImageView;
import com.jinzht.pro.view.PullToRefreshLayout;
import com.jinzht.pro.view.PullableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目中心的项目方查看提交记录
 */
public class CommitRecordsActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 列表

    private List<CommitRecordsBean.DataBean> datas = new ArrayList<>();
    private int pages = 0;
    private MyAdapter myAdapter;

    @Override
    protected int getResourcesId() {
        return R.layout.activity_commit_records;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("提交记录");

        refreshView = (PullToRefreshLayout) findViewById(R.id.refresh_view);// 刷新布局
        refreshView.setOnRefreshListener(new PullListener());// 设置刷新接口
        listview = (PullableListView) findViewById(R.id.listview);// 列表
        myAdapter = new MyAdapter();

        GetCommitRecordsTask getCommitRecordsTask = new GetCommitRecordsTask(0);
        getCommitRecordsTask.execute();
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
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_commit_record, null);
                holder.ivFavicon = (CircleImageView) convertView.findViewById(R.id.iv_favicon);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvPosition = (TextView) convertView.findViewById(R.id.tv_position);
                holder.tvIsRead = (TextView) convertView.findViewById(R.id.tv_isRead);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(mContext).load(datas.get(position).getUser().getHeadSculpture()).into(holder.ivFavicon);
            holder.tvName.setText(datas.get(position).getUser().getAuthentics().get(0).getName());
            holder.tvPosition.setText(datas.get(position).getUser().getAuthentics().get(0).getCompanyName() + datas.get(position).getUser().getAuthentics().get(0).getPosition());
            holder.tvTime.setText(DateUtils.timeLogic(datas.get(position).getRecord().getRecordDate()));
            holder.tvIsRead.setText(datas.get(position).getRecord().getStatus().getName());
            if ("未查看".equals(datas.get(position).getRecord().getStatus().getName())) {
                holder.tvIsRead.setTextColor(Color.RED);
            } else {
                holder.tvIsRead.setTextColor(0xff747474);
            }
            return convertView;
        }

        class ViewHolder {
            public CircleImageView ivFavicon;
            public TextView tvName;
            public TextView tvPosition;
            private TextView tvIsRead;
            private TextView tvTime;
        }
    }

    // 获取提交记录
    private class GetCommitRecordsTask extends AsyncTask<Void, Void, CommitRecordsBean> {
        int page;

        public GetCommitRecordsTask(int page) {
            this.page = page;
        }

        @Override
        protected CommitRecordsBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETCOMMITRECORDS)),
                            "projectId", getIntent().getStringExtra("id"),
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETCOMMITRECORDS,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("提交记录", body);
                return FastJsonTools.getBean(body, CommitRecordsBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommitRecordsBean commitRecordsBean) {
            super.onPostExecute(commitRecordsBean);
            if (commitRecordsBean == null) {
                listview.setBackgroundResource(R.mipmap.bg_empty);
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (commitRecordsBean.getStatus() == 200) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        datas = commitRecordsBean.getData();
                        if (datas != null && datas.size() != 0) {
                            listview.setBackgroundResource(R.color.white);
                        } else {
                            listview.setBackgroundResource(R.mipmap.bg_empty);
                        }
                        if (datas != null) {
                            listview.setAdapter(myAdapter);
                        }
                    } else {
                        for (CommitRecordsBean.DataBean dataBean : commitRecordsBean.getData()) {
                            datas.add(dataBean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (commitRecordsBean.getStatus() == 201) {
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    listview.setBackgroundResource(R.mipmap.bg_empty);
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                    SuperToastUtils.showSuperToast(mContext, 2, commitRecordsBean.getMessage());
                }
            }
        }
    }

    // 下拉刷新与上拉加载
    private class PullListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉刷新
            pages = 0;
            GetCommitRecordsTask getCommitRecordsTask = new GetCommitRecordsTask(0);
            getCommitRecordsTask.execute();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            pages++;
            Log.i("页码", String.valueOf(pages));
            GetCommitRecordsTask getCommitRecordsTask = new GetCommitRecordsTask(pages);
            getCommitRecordsTask.execute();
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
