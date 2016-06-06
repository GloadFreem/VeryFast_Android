package com.jinzht.pro1.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.CirclePhotosAdapter;
import com.jinzht.pro1.adapter.RecyclerViewData;
import com.jinzht.pro1.base.BaseFragment;
import com.jinzht.pro1.bean.CircleListBean;
import com.jinzht.pro1.view.PullToRefreshLayout;
import com.jinzht.pro1.view.PullableListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 圈子界面
 */
public class CircleFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout titleBtnRight;// title右侧按钮
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 列表

    private List<Integer> photos;// 每个item的图片
    private CirclePhotosAdapter photosAdapter;// 每个item的图片适配器

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circle, container, false);
        titleBtnRight = (LinearLayout) view.findViewById(R.id.title_btn_right);// title右侧按钮
        titleBtnRight.setOnClickListener(this);
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);// 刷新布局
        listview = (PullableListView) view.findViewById(R.id.listview);// 列表
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshView.setOnRefreshListener(new PullListener());
        initList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_btn_right:// 点击发表内容
                break;
        }
    }

    private void initList() {
        photos = new ArrayList<>(Arrays.asList(R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d, R.mipmap.a, R.mipmap.a, R.mipmap.a));
        photosAdapter = new CirclePhotosAdapter(mContext, photos);
        listview.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.layout_empty_view_9dp, null), null, false);
        listview.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_circle, null);
                RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerview);
                RecyclerViewData.setGrid(rv, mContext, photosAdapter, 3);
                return view;
            }
        });
    }

    // 获取圈子列表
    private class GetCircleList extends AsyncTask<Void, Void, CircleListBean> {
        @Override
        protected CircleListBean doInBackground(Void... params) {
            String body = "";
            return null;
        }
    }

    private class PullListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉刷新
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 告诉控件刷新完毕
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 5000);
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 告诉控件加载完毕
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 5000);
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
