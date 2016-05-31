package com.jinzht.pro1.activity;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.view.PullToRefreshLayout;
import com.jinzht.pro1.view.PullableListView;

/**
 * 圈子详情页
 */
public class CircleDetailActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private LinearLayout llComment;// 评论输入框布局
    private EditText edComment;// 评论输入框
    private TextView btnComment;// 评论按钮
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 列表

    private ListAdapter listAdapter;// 列表数据填充器

    @Override
    protected int getResourcesId() {
        return R.layout.activity_circle_detail;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        findView();
        refreshView.setOnRefreshListener(new PullListener());
        initListView();
    }

    private void findView() {
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("详情");
        llComment = (LinearLayout) findViewById(R.id.ll_comment);// 评论输入框布局
        edComment = (EditText) findViewById(R.id.ed_comment);// 评论输入框
        btnComment = (TextView) findViewById(R.id.btn_comment);// 评论按钮
        btnComment.setOnClickListener(this);
        refreshView = (PullToRefreshLayout) findViewById(R.id.refresh_view);// 刷新布局
        listview = (PullableListView) findViewById(R.id.listview);// 列表
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.btn_comment:// 发表评论
                break;
        }
    }

    private void initListView() {
        listAdapter = new ListAdapter();
        listview.setAdapter(listAdapter);
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }

    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 4;
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
            View view = null;
            if (position == 0) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_circle_detail, null);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_circle_comment, null);
            }
            return view;
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
}
