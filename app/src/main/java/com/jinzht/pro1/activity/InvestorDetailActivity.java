package com.jinzht.pro1.activity;

import android.graphics.Paint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.FullBaseActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.view.CircleImageView;

/**
 * 投资人详情页
 */
public class InvestorDetailActivity extends FullBaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private LinearLayout btnShare;// 分享
    private CircleImageView favicon;// 头像
    private TextView name;// 姓名
    private TextView position;// 职位
    private TextView compName;// 公司名称
    private TextView addr;// 所在地
    private TextView field1;// 投资领域1
    private TextView field2;// 投资领域2
    private TextView field3;// 投资领域3
    private TextView desc;// 个人简介
    private RelativeLayout submit;// 提交项目
    private RelativeLayout collect;// 关注

    @Override
    protected int getResourcesId() {
        return R.layout.activity_investor_detail;
    }

    @Override
    protected void init() {
//        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        btnBack = (LinearLayout) findViewById(R.id.title_btn_back);// 返回
        btnBack.setOnClickListener(this);
        btnShare = (LinearLayout) findViewById(R.id.title_btn_share);// 分享
        btnShare.setOnClickListener(this);
        favicon = (CircleImageView) findViewById(R.id.investor_detail_favicon);// 头像
        name = (TextView) findViewById(R.id.investor_detail_name);// 姓名
        position = (TextView) findViewById(R.id.investor_detail_position);// 职位
        compName = (TextView) findViewById(R.id.investor_detail_comp_name);// 公司名称
        compName.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        addr = (TextView) findViewById(R.id.investor_detail_addr);// 所在地
        field1 = (TextView) findViewById(R.id.investor_detail_field1);// 投资领域1
        field2 = (TextView) findViewById(R.id.investor_detail_field2);// 投资领域2
        field3 = (TextView) findViewById(R.id.investor_detail_field3);// 投资领域3
        desc = (TextView) findViewById(R.id.investor_detail_desc);// 个人简介
        submit = (RelativeLayout) findViewById(R.id.investor_detail_btn_submit);// 提交项目
        submit.setOnClickListener(this);
        collect = (RelativeLayout) findViewById(R.id.investor_detail_btn_collect);// 关注
        collect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_btn_back:// 返回上一页
                finish();
                break;
            case R.id.title_btn_share:// 分享
                SuperToastUtils.showSuperToast(this, 2, "分享");
                break;
            case R.id.investor_detail_btn_submit:// 提交项目
                SuperToastUtils.showSuperToast(this, 2, "提交");
                break;
            case R.id.investor_detail_btn_collect:// 关注
                SuperToastUtils.showSuperToast(this, 2, "关注");
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
