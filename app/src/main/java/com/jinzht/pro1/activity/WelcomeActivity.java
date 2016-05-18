package com.jinzht.pro1.activity;

import android.content.Intent;

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

        Intent intent = new Intent(this, MessageActivity .class);
        startActivity(intent);
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
