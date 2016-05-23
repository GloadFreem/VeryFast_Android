package com.jinzht.pro1.activity;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.MyCollectFragmentAdapter;
import com.jinzht.pro1.base.BaseFragmentActivity;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.utils.UiUtils;

/**
 * 我的关注界面
 */
public class MyCollectActivity extends BaseFragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private LinearLayout btnBack;// 返回
    private RadioGroup radioGroup;// 单选按钮集
    private RadioButton rbProject;// 项目按钮
    private RadioButton rbInvestor;// 投资人按钮
    private ViewPager viewPager;// ViewPager

    @Override
    protected int getResourcesId() {
        return R.layout.activity_my_collect;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        radioGroup = (RadioGroup) findViewById(R.id.mycollect_rg);// 单选按钮集
        rbProject = (RadioButton) findViewById(R.id.mycollect_rb_project);// 项目按钮
        rbProject.setOnClickListener(this);
        rbInvestor = (RadioButton) findViewById(R.id.mycollect_rb_investor);// 投资人按钮
        rbInvestor.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.mycollect_vp);// ViewPager

        // 设置RadioGroup页签的单选事件
        radioGroup.setOnCheckedChangeListener(this);
        // 给投资人类型填充数据
        viewPager.setAdapter(new MyCollectFragmentAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
        // 设置RadioGroup页签和项目ViewPager联动
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.mycollect_rb_project);
                        break;
                    case 1:
                        radioGroup.check(R.id.mycollect_rb_investor);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.mycollect_rb_project:// 展示项目列表
                viewPager.setCurrentItem(0);
                rbProject.setTextColor(UiUtils.getColor(R.color.bg_text));
                rbInvestor.setTextColor(Color.WHITE);
                break;
            case R.id.mycollect_rb_investor:// 展示投资人列表
                viewPager.setCurrentItem(1);
                rbProject.setTextColor(Color.WHITE);
                rbInvestor.setTextColor(UiUtils.getColor(R.color.bg_text));
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
