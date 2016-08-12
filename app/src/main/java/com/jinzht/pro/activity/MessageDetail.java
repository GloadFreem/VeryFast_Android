package com.jinzht.pro.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.utils.DateUtils;
import com.jinzht.pro.utils.UiHelp;

/**
 * 纯文字时的站内信详情
 */
public class MessageDetail extends BaseActivity {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 界面标题
    private TextView tvName;// 来源名称
    private TextView tvTime;// 时间
    private TextView tvTitle2;// 站内信标题
    private TextView tvContent;// 内容

    @Override
    protected int getResourcesId() {
        return R.layout.activity_message_detail;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        tvTitle = (TextView) findViewById(R.id.tv_title);// 界面标题
        tvName = (TextView) findViewById(R.id.tv_name);// 来源名称
        tvTime = (TextView) findViewById(R.id.tv_time);// 时间
        tvTitle2 = (TextView) findViewById(R.id.tv_title2);// 站内信标题
        tvContent = (TextView) findViewById(R.id.tv_content);// 内容

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText(getIntent().getStringExtra("title"));
        tvName.setText(getIntent().getStringExtra("name"));
        tvTime.setText(DateUtils.timeLogic(getIntent().getStringExtra("time")));
        tvTitle2.setText(getIntent().getStringExtra("title"));
        tvContent.setText(getIntent().getStringExtra("content"));
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
