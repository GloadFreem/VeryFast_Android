package com.jinzht.pro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.ActivityListBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.DateUtils;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.view.PullToRefreshLayout;
import com.jinzht.pro.view.PullableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的活动界面
 */
public class MyActivitysActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private LinearLayout pageError;// 错误页面
    private ImageView btnTryagain;// 重试按钮
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 活动列表

    private MyAdapter myAdapter;
    private int pages = 0;
    private List<ActivityListBean.DataBean> datas = new ArrayList<>();// 数据集合

    @Override
    protected int getResourcesId() {
        return R.layout.activity_my_activitys;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        findView();
        refreshView.setOnRefreshListener(new PullListener());// 设置刷新接口
        myAdapter = new MyAdapter();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ActivityDetailActivity.class);
                intent.putExtra("id", datas.get(position).getActionId());
                startActivity(intent);
            }
        });

        GetMyActivityListTask getMyActivityListTask = new GetMyActivityListTask(0);
        getMyActivityListTask.execute();
    }

    private void findView() {
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("我的活动");
        pageError = (LinearLayout) findViewById(R.id.page_error);// 错误页面
        btnTryagain = (ImageView) findViewById(R.id.btn_tryagain);// 重试按钮
        btnTryagain.setOnClickListener(this);
        refreshView = (PullToRefreshLayout) findViewById(R.id.refresh_view);// 刷新布局
        listview = (PullableListView) findViewById(R.id.listview);// 活动列表
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_tryagain:// 重试加载网络
                if (clickable) {
                    clickable = false;
                    GetMyActivityListTask getMyActivityListTask = new GetMyActivityListTask(0);
                    getMyActivityListTask.execute();
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_myactivity, null);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.itemTime = (TextView) convertView.findViewById(R.id.item_time);
                holder.itemNum = (TextView) convertView.findViewById(R.id.item_num);
                holder.tvFree = (TextView) convertView.findViewById(R.id.tv_free);
                holder.itemAddr = (TextView) convertView.findViewById(R.id.item_addr);
                holder.ivTag = (ImageView) convertView.findViewById(R.id.iv_tag);
                holder.itemDistance = (TextView) convertView.findViewById(R.id.item_distance);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvTitle.setText(datas.get(position).getName());
            String time = datas.get(position).getStartTime();
            holder.itemTime.setText(time.substring(0, 10).replaceAll("-", ".") + " " + DateUtils.getWeek(time) + " " + time.substring(11, 16));
            holder.itemNum.setText(datas.get(position).getMemberLimit() + "人");
            if (datas.get(position).getType() == 1) {
                holder.tvFree.setText("免费");
            } else {
                holder.tvFree.setText("付费");
            }
            holder.itemAddr.setText(datas.get(position).getAddress());
            holder.itemDistance.setText("");// 距离
            // 是否过期
            int i = DateUtils.timeDiff4Mins(datas.get(position).getEndTime());
            Log.i("时间差", String.valueOf(i));
            if (DateUtils.timeDiff4Mins(datas.get(position).getEndTime()) > 0) {
                holder.ivTag.setVisibility(View.VISIBLE);
            } else {
                holder.ivTag.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }

        class ViewHolder {
            private TextView tvTitle;
            private TextView itemTime;
            private TextView itemNum;
            private TextView tvFree;
            private TextView itemAddr;
            private ImageView ivTag;
            private TextView itemDistance;
        }
    }

    // 获取我的活动列表
    private class GetMyActivityListTask extends AsyncTask<Void, Void, ActivityListBean> {
        private int page;

        public GetMyActivityListTask(int page) {
            this.page = page;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (page == 0) {
                showProgressDialog();
            }
        }

        @Override
        protected ActivityListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETMYACTIVITYLIST)),
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETMYACTIVITYLIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("我的活动列表", body);
                return FastJsonTools.getBean(body, ActivityListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ActivityListBean activityListBean) {
            super.onPostExecute(activityListBean);
            clickable = true;
            dismissProgressDialog();
            if (activityListBean == null) {
                pageError.setVisibility(View.VISIBLE);
                refreshView.setVisibility(View.GONE);
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (activityListBean.getStatus() == 200) {
                    pageError.setVisibility(View.GONE);
                    refreshView.setVisibility(View.VISIBLE);
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        datas = activityListBean.getData();
                        if (datas != null && datas.size() != 0) {
                            listview.setBackgroundResource(R.color.bg_main);
                        } else {
                            listview.setBackgroundResource(R.mipmap.bg_empty);
                        }
                        if (datas != null) {
                            listview.setAdapter(myAdapter);
                        }
                    } else {
                        for (ActivityListBean.DataBean dataBean : activityListBean.getData()) {
                            datas.add(dataBean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (activityListBean.getStatus() == 201) {
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
            GetMyActivityListTask getMyActivityListTask = new GetMyActivityListTask(0);
            getMyActivityListTask.execute();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            pages++;
            Log.i("页码", String.valueOf(pages));
            GetMyActivityListTask getMyActivityListTask = new GetMyActivityListTask(pages);
            getMyActivityListTask.execute();
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
