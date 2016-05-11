package com.jinzht.pro1.activity;

import android.content.Intent;
import android.os.SystemClock;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.FullBaseActivity;

/**
 * 启动页
 */
public class WelcomeActivity extends FullBaseActivity {

    @Override
    protected int getResourcesId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void init() {
        SystemClock.sleep(1500);
        Intent intent = new Intent(this, RoadshowDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
