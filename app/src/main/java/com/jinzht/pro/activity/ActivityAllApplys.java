package com.jinzht.pro.activity;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.ActivityAllApplysBean;
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
 * 活动全部报名人
 */
public class ActivityAllApplys extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 列表

    private List<ActivityAllApplysBean.DataBean> applys = new ArrayList<>();// 全部报名人
    private int pages = 0;
    private MyAdapter myAdapter;

    @Override
    protected int getResourcesId() {
        return R.layout.activity_activity_all_applys;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("已报名人");
        refreshView = (PullToRefreshLayout) findViewById(R.id.refresh_view);// 刷新布局
        refreshView.setOnRefreshListener(new PullListener());// 设置刷新接口
        listview = (PullableListView) findViewById(R.id.listview);// 列表
        myAdapter = new MyAdapter();
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
        GetAllApplysTask getAllApplysTask = new GetAllApplysTask(0);
        getAllApplysTask.execute();
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
            return applys.size();
        }

        @Override
        public Object getItem(int position) {
            return applys.get(position);
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_activity_all_applys, null);
                holder.rlApply = (RelativeLayout) convertView.findViewById(R.id.rl_apply);
                holder.ivFavicon = (CircleImageView) convertView.findViewById(R.id.iv_favicon);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvPosition = (TextView) convertView.findViewById(R.id.tv_position);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(mContext).load(applys.get(position).getUsers().getHeadSculpture()).into(holder.ivFavicon);
            holder.tvName.setText(applys.get(position).getUsers().getAuthentics().get(0).getName());
            holder.tvPosition.setText(applys.get(position).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(position).getUsers().getAuthentics().get(0).getPosition());
            holder.tvTime.setText(DateUtils.timeLogic(applys.get(position).getEnrollDate()));
            return convertView;
        }

        class ViewHolder {
            public RelativeLayout rlApply;
            public CircleImageView ivFavicon;
            public TextView tvName;
            public TextView tvPosition;
            public TextView tvTime;
        }
    }

    // 获取全部报名人
    private class GetAllApplysTask extends AsyncTask<Void, Void, ActivityAllApplysBean> {
        int page;

        public GetAllApplysTask(int page) {
            this.page = page;
        }

        @Override
        protected ActivityAllApplysBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETALLACTIVITYAPPLYS)),
                            "contentId", String.valueOf(getIntent().getIntExtra("id", 0)),
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETALLACTIVITYAPPLYS,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("全部报名人", body);
                return FastJsonTools.getBean(body, ActivityAllApplysBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ActivityAllApplysBean activityAllApplysBean) {
            super.onPostExecute(activityAllApplysBean);
            if (activityAllApplysBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (activityAllApplysBean.getStatus() == 200) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        applys = activityAllApplysBean.getData();
                        listview.setAdapter(myAdapter);
                    } else {
                        for (ActivityAllApplysBean.DataBean dataBean : activityAllApplysBean.getData()) {
                            Log.i("内容", dataBean.toString());
                            applys.add(dataBean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (activityAllApplysBean.getStatus() == 201) {
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                    SuperToastUtils.showSuperToast(mContext, 2, activityAllApplysBean.getMessage());
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
            GetAllApplysTask getAllApplysTask = new GetAllApplysTask(0);
            getAllApplysTask.execute();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            pages++;
            Log.i("页码", String.valueOf(pages));
            GetAllApplysTask getAllApplysTask = new GetAllApplysTask(pages);
            getAllApplysTask.execute();
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }

}
