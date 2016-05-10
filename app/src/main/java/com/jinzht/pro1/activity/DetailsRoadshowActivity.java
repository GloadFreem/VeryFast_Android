package com.jinzht.pro1.activity;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 路演项目详情
 */
public class DetailsRoadshowActivity extends BaseActivity {


    @Override
    protected int getResourcesId() {
        return R.layout.activity_details_roadshow;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
