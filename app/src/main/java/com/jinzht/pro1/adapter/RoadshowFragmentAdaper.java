package com.jinzht.pro1.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jinzht.pro1.fragment.RoadshowDetailsFragment;
import com.jinzht.pro1.fragment.RoadshowLiveFragment;
import com.jinzht.pro1.fragment.RoadshowMemberFragment;

/**
 * 路演项目中的详情、成员、现场Fragment数据填充
 */
public class RoadshowFragmentAdaper extends FragmentPagerAdapter {

    public RoadshowFragmentAdaper(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RoadshowDetailsFragment roadshowDetailsFragment = new RoadshowDetailsFragment();
                return roadshowDetailsFragment;
            case 1:
                RoadshowMemberFragment roadshowMemberFragment = new RoadshowMemberFragment();
                return roadshowMemberFragment;
            case 2:
                RoadshowLiveFragment roadshowLiveFragment = new RoadshowLiveFragment();
                return roadshowLiveFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
