package com.jinzht.pro1.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseFragment;
import com.jinzht.pro1.bean.ActivityListBean;
import com.jinzht.pro1.utils.AESUtils;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.DateUtils;
import com.jinzht.pro1.utils.DialogUtils;
import com.jinzht.pro1.utils.FastJsonTools;
import com.jinzht.pro1.utils.MD5Utils;
import com.jinzht.pro1.utils.NetWorkUtils;
import com.jinzht.pro1.utils.OkHttpUtils;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.view.PullToRefreshLayout;
import com.jinzht.pro1.view.PullableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动界面
 */
public class ActivityFragment extends BaseFragment implements View.OnClickListener {

    private EditText activityEdtSearch;// 搜索输入框
    private RelativeLayout activityBtnSearch;// 搜索按钮
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 活动列表

    private MyAdapter myAdapter;
    private int pages = 0;
    private List<ActivityListBean.DataBean> datas = new ArrayList<>();// 数据集合

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        activityEdtSearch = (EditText) view.findViewById(R.id.activity_edt_search);// 搜索输入框
        activityBtnSearch = (RelativeLayout) view.findViewById(R.id.activity_btn_search);// 搜索按钮
        activityBtnSearch.setOnClickListener(this);
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);// 刷新布局
        listview = (PullableListView) view.findViewById(R.id.activity_lv);// 活动列表
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshView.setOnRefreshListener(new PullListener());// 设置刷新接口
        myAdapter = new MyAdapter();

        GetActivityListTask getActivityListTask = new GetActivityListTask(0);
        getActivityListTask.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_btn_search:// 点击搜索
                SuperToastUtils.showSuperToast(mContext, 2, "搜索");
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_activity, null);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.itemTime = (TextView) convertView.findViewById(R.id.item_time);
                holder.itemNum = (TextView) convertView.findViewById(R.id.item_num);
                holder.itemActivityFree = (TextView) convertView.findViewById(R.id.item_activity_free);
                holder.itemAddr = (TextView) convertView.findViewById(R.id.item_addr);
                holder.btnApply = (TextView) convertView.findViewById(R.id.btn_apply);
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
                holder.itemActivityFree.setText("免费");
            } else {
                holder.itemActivityFree.setText("付费");
            }
            holder.itemAddr.setText(datas.get(position).getAddress());
            holder.itemDistance.setText("");// 距离
            // 报名
            holder.btnApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.activityApplyDialog(getActivity());
                }
            });
            return convertView;
        }

        public class ViewHolder {
            public TextView tvTitle;
            public TextView itemTime;
            public TextView itemNum;
            public TextView itemActivityFree;
            public TextView itemAddr;
            public TextView btnApply;
            public TextView itemDistance;
        }
    }

    // 获取活动列表
    private class GetActivityListTask extends AsyncTask<Void, Void, ActivityListBean> {
        private int page;

        public GetActivityListTask(int page) {
            this.page = page;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("");
        }

        @Override
        protected ActivityListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETACTIVITYLIST)),
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETACTIVITYLIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("活动列表", body);
                return FastJsonTools.getBean(body, ActivityListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ActivityListBean activityListBean) {
            super.onPostExecute(activityListBean);
            dismissProgressDialog();
            if (activityListBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                return;
            } else {
                if (activityListBean.getStatus() == 200) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        datas = activityListBean.getData();
                        for (ActivityListBean.DataBean dataBean : datas) {
                            Log.i("内容", dataBean.toString());
                        }
                        listview.setAdapter(myAdapter);
                    } else {
                        for (ActivityListBean.DataBean dataBean : activityListBean.getData()) {
                            Log.i("内容", dataBean.toString());
                            datas.add(dataBean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (activityListBean.getStatus() == 201) {
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                    SuperToastUtils.showSuperToast(mContext, 2, activityListBean.getMessage());
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
            GetActivityListTask getActivityListTask = new GetActivityListTask(0);
            getActivityListTask.execute();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            pages++;
            Log.i("页码", String.valueOf(pages));
            GetActivityListTask getActivityListTask = new GetActivityListTask(pages);
            getActivityListTask.execute();
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
