package com.jinzht.pro1.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jinzht.pro1.fragment.MyCollectInvestorFragment;
import com.jinzht.pro1.fragment.MyCollectProjectFragment;

/**
 * 我的关注Fragment适配器，包括项目和投资人
 */
public class MyCollectFragmentAdapter extends FragmentPagerAdapter {


    public MyCollectFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MyCollectProjectFragment myCollectProjectFragment = new MyCollectProjectFragment();
                return myCollectProjectFragment;
            case 1:
                MyCollectInvestorFragment myCollectInvestorFragment = new MyCollectInvestorFragment();
                return myCollectInvestorFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
