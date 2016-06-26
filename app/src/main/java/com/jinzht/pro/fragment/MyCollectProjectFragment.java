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

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseFragment;
import com.jinzht.pro.bean.RoadshowProjectListBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.view.PullToRefreshLayout;
import com.jinzht.pro.view.PullableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的关注中项目界面
 */
public class MyCollectProjectFragment extends BaseFragment {

    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 列表

    private MyAdapter myAdapter;
    private int pages = 0;
    List<RoadshowProjectListBean.DataBean> datas = new ArrayList<>();// 数据集合
    private int POSITION = 0;
    private final static int REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_collect_project, container, false);
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
                SuperToastUtils.showSuperToast(mContext, 2, "点击了" + position);
//                Intent intent = new Intent(mContext, InvestorDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("detail", datas.get(position - 1));
//                intent.putExtras(bundle);
//                startActivityForResult(intent, REQUEST_CODE);
            }
        });
//        listview.setAdapter(new BaseAdapter() {
//            @Override
//            public int getCount() {
//                return 6;
//            }
//
//            @Override
//            public Object getItem(int position) {
//                return position;
//            }
//
//            @Override
//            public long getItemId(int position) {
//                return position;
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = null;
//                LayoutInflater inflater = LayoutInflater.from(mContext);
//                if (position < 3) {
//                    view = inflater.inflate(R.layout.item_project_roadshow, null);
//                } else {
//                    view = inflater.inflate(R.layout.item_project_preselect, null);
//                }
//                return view;
//            }
//        });

        GetProjectList getProjectList = new GetProjectList(0);
        getProjectList.execute();
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
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if ("预选项目".equals(datas.get(position).getFinancestatus().getName())) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                if (getItemViewType(position) == 0) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_project_preselect, null);
                } else {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_project_roadshow, null);
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }

        class ViewHolder {

        }
    }

    // 获取项目列表
    private class GetProjectList extends AsyncTask<Void, Void, RoadshowProjectListBean> {
        private int page;

        public GetProjectList(int page) {
            this.page = page;
        }

        @Override
        protected RoadshowProjectListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETMYCOLLECT)),
                            "type", "0",
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETMYCOLLECT,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("关注的项目列表", body);
                return FastJsonTools.getBean(body, RoadshowProjectListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(RoadshowProjectListBean roadshowProjectListBean) {
            super.onPostExecute(roadshowProjectListBean);
            if (roadshowProjectListBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                return;
            } else {
                if (roadshowProjectListBean.getStatus() == 200) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        datas = roadshowProjectListBean.getData();
                        if (datas != null && datas.size() != 0) {
                            listview.setAdapter(myAdapter);
                        }
                    } else {
                        for (RoadshowProjectListBean.DataBean dataBean : roadshowProjectListBean.getData()) {
                            datas.add(dataBean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (roadshowProjectListBean.getStatus() == 201) {
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                    SuperToastUtils.showSuperToast(mContext, 2, roadshowProjectListBean.getMessage());
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
            GetProjectList getProjectList = new GetProjectList(0);
            getProjectList.execute();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            pages++;
            Log.i("页码", String.valueOf(pages));
            GetProjectList getProjectList = new GetProjectList(pages);
            getProjectList.execute();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
//            if (resultCode == InvestorDetailActivity.RESULT_CODE) {
//                if (data.getBooleanExtra("needRefresh", false)) {// 在详情中进行了交互
//                    pages = 0;
//                    GetInvestorListTask getInvestorListTask = new GetInvestorListTask(0);
//                    getInvestorListTask.execute();
//                }
//            }
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
