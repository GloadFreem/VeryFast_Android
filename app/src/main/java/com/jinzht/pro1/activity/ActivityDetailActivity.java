package com.jinzht.pro1.activity;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 活动详情页
 */
public class ActivityDetailActivity extends BaseActivity {



    @Override
    protected int getResourcesId() {
        return R.layout.activity_activity_detail;
    }

    @Override
    protected void init() {
        UiHelp.setTranslucentStatus(true,this);// 设置系统状态栏与应用标题栏背景一致
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
