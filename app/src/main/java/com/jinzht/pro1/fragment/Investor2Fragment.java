package com.jinzht.pro1.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseFragment;
import com.jinzht.pro1.view.PullToRefreshLayout;
import com.jinzht.pro1.view.PullableListView;

/**
 * 投资机构列表
 */
public class Investor2Fragment extends BaseFragment {

    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView lvInvestor2;// 投资机构列表

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_investor2, container, false);
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);// 刷新布局
        lvInvestor2 = (PullableListView) view.findViewById(R.id.lv_investor2);// 投资机构列表
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshView.setOnRefreshListener(new PullListener());
        lvInvestor2.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.layout_empty_view_9dp, null));
        lvInvestor2.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 6;
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
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                if (position < 3) {
                    view = LayoutInflater.from(mContext).inflate(R.layout.item_investor2_fund, null);
                } else {
                    view = LayoutInflater.from(mContext).inflate(R.layout.item_investor2, null);
                }
                return view;
            }
        });
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
