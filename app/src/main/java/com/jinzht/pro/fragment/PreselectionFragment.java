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
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.activity.PreselectionDetailsActivity;
import com.jinzht.pro.base.BaseFragment;
import com.jinzht.pro.bean.ProjectListBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.view.CircleImageView;
import com.jinzht.pro.view.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 预选项目列表
 */
public class PreselectionFragment extends BaseFragment {

    private PullToRefreshLayout refreshView;// 刷新布局
    private ListView listview;// 项目列表

    private MyAdapter myAdapter;
    private int pages = 0;
    List<ProjectListBean.DataBean> datas = new ArrayList<>();// 数据集合
    private int POSITION = 0;
    private final static int REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preselection, container, false);
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);// 刷新布局
        listview = (ListView) view.findViewById(R.id.lv_project_preselection);// 项目列表
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshView.setOnRefreshListener(new PullListener());
        myAdapter = new MyAdapter();
        listview.addHeaderView(View.inflate(mContext, R.layout.layout_empty_view_9dp, null));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                POSITION = position - 1;
                Intent intent = new Intent(mContext, PreselectionDetailsActivity.class);
                intent.putExtra("id", String.valueOf(datas.get(position - 1).getProjectId()));
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        GetProjectListTask getProjectListTask = new GetProjectListTask(0);
        getProjectListTask.execute();
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_project_preselect, null);
                holder.itemProjectImg = (CircleImageView) convertView.findViewById(R.id.item_project_img);
                holder.itemProjectTitle = (TextView) convertView.findViewById(R.id.item_project_title);
                holder.itemProjectAddr = (TextView) convertView.findViewById(R.id.item_project_addr);
                holder.itemProjectCompname = (TextView) convertView.findViewById(R.id.item_project_compname);
                holder.itemProjectField1 = (TextView) convertView.findViewById(R.id.item_project_field1);
                holder.itemProjectField2 = (TextView) convertView.findViewById(R.id.item_project_field2);
                holder.itemProjectField3 = (TextView) convertView.findViewById(R.id.item_project_field3);
                holder.itemProjectPopularity = (TextView) convertView.findViewById(R.id.item_project_popularity);
                holder.itemProjectAmount = (TextView) convertView.findViewById(R.id.item_project_amount);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(mContext).load(datas.get(position).getStartPageImage()).into(holder.itemProjectImg);
            holder.itemProjectTitle.setText(datas.get(position).getAbbrevName());
            holder.itemProjectAddr.setText(datas.get(position).getAddress());
            holder.itemProjectCompname.setText(datas.get(position).getFullName());
            String[] fields = datas.get(position).getIndustoryType().split("，");
            if (fields.length == 1) {
                holder.itemProjectField1.setText(fields[0]);
                holder.itemProjectField2.setVisibility(View.GONE);
                holder.itemProjectField3.setVisibility(View.GONE);
            } else if (fields.length == 2) {
                holder.itemProjectField1.setText(fields[0]);
                holder.itemProjectField2.setText(fields[1]);
                holder.itemProjectField3.setVisibility(View.GONE);
            } else if (fields.length == 3) {
                holder.itemProjectField1.setText(fields[0]);
                holder.itemProjectField2.setText(fields[1]);
                holder.itemProjectField3.setText(fields[2]);
            }
            holder.itemProjectPopularity.setText(String.valueOf(datas.get(position).getCollectionCount()));
            holder.itemProjectAmount.setText(datas.get(position).getRoadshows().get(0).getRoadshowplan().getFinanceTotal() + "万");
            return convertView;
        }

        class ViewHolder {
            private CircleImageView itemProjectImg;
            private TextView itemProjectTitle;
            private TextView itemProjectAddr;
            private TextView itemProjectCompname;
            private TextView itemProjectField1;
            private TextView itemProjectField2;
            private TextView itemProjectField3;
            private TextView itemProjectPopularity;
            private TextView itemProjectAmount;
        }
    }

    // 获取项目列表
    private class GetProjectListTask extends AsyncTask<Void, Void, ProjectListBean> {
        private int page;

        public GetProjectListTask(int page) {
            this.page = page;
        }

        @Override
        protected ProjectListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETPROJECTLIST)),
                            "page", String.valueOf(page),
                            "type", "1",
                            Constant.BASE_URL + Constant.GETPROJECTLIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("预选项目列表", body);
                return FastJsonTools.getBean(body, ProjectListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ProjectListBean projectListBean) {
            super.onPostExecute(projectListBean);
            if (projectListBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                return;
            } else {
                if (projectListBean.getStatus() == 200) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        datas = projectListBean.getData();
                        listview.setAdapter(myAdapter);
                    } else {
                        for (ProjectListBean.DataBean dataBean : projectListBean.getData()) {
                            datas.add(dataBean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                } else if (projectListBean.getStatus() == 201) {
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                    SuperToastUtils.showSuperToast(mContext, 2, projectListBean.getMessage());
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
            GetProjectListTask getProjectListTask = new GetProjectListTask(0);
            getProjectListTask.execute();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            pages++;
            Log.i("页码", String.valueOf(pages));
            GetProjectListTask getProjectListTask = new GetProjectListTask(pages);
            getProjectListTask.execute();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            if (resultCode == PreselectionDetailsActivity.RESULT_CODE) {
                if (data.getIntExtra("FLAG", 0) == 1) {// 在详情中关注了项目
                    datas.get(POSITION).setCollectionCount(datas.get(POSITION).getCollectionCount() + 1);
                } else if (data.getIntExtra("FLAG", 0) == 2) {// 在详情中取消了关注
                    datas.get(POSITION).setCollectionCount(datas.get(POSITION).getCollectionCount() - 1);
                }
                myAdapter.notifyDataSetChanged();
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
