package com.jinzht.pro1.activity;

import android.view.View;
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

    @Override
    protected int getResourcesId() {
        return R.layout.activity_circle_detail;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        findView();
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

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
