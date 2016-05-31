package com.jinzht.pro1.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseFragment;
import com.jinzht.pro1.utils.DialogUtils;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.view.PullToRefreshLayout;
import com.jinzht.pro1.view.PullableListView;

/**
 * 活动界面
 */
public class ActivityFragment extends BaseFragment implements View.OnClickListener {

    private EditText activityEdtSearch;// 搜索输入框
    private RelativeLayout activityBtnSearch;// 搜索按钮
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView activityLv;// 活动列表

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        activityEdtSearch = (EditText) view.findViewById(R.id.activity_edt_search);// 搜索输入框
        activityBtnSearch = (RelativeLayout) view.findViewById(R.id.activity_btn_search);// 搜索按钮
        activityBtnSearch.setOnClickListener(this);
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);// 刷新布局
        activityLv = (PullableListView) view.findViewById(R.id.activity_lv);// 活动列表
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshView.setOnRefreshListener(new PullListener());
        activityLv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 5;
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
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_activity, null);
                TextView apply = (TextView) view.findViewById(R.id.item_activity_btn_apply);
                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtils.activityApplyDialog(getActivity());
                    }
                });
                return view;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_btn_search:// 点击搜索
                SuperToastUtils.showSuperToast(mContext, 2, "搜索");
                break;
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
