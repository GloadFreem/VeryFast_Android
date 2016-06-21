package com.jinzht.pro.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jinzht.pro.base.BaseFragment;
import com.jinzht.pro.fragment.MainFragmentFactory;

/**
 * 主页框架的适配器
 */
public class MainFragmentAdapter extends FragmentPagerAdapter {


    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment = MainFragmentFactory.getFragment(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
