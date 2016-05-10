package com.jinzht.pro1.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jinzht.pro1.fragment.PreselectionFragment;
import com.jinzht.pro1.fragment.RoadshowFragment;

/**
 * 路演项目和预选项目
 */
public class ProjectFragmentAdapter extends FragmentPagerAdapter {


    public ProjectFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RoadshowFragment roadshowFragment = new RoadshowFragment();
                return roadshowFragment;
            case 1:
                PreselectionFragment preselectionFragment = new PreselectionFragment();
                return preselectionFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
