package com.jinzht.pro.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jinzht.pro.fragment.Investor1Fragment;
import com.jinzht.pro.fragment.Investor2Fragment;
import com.jinzht.pro.fragment.Investor3Fragment;

/**
 * 投资人Fragment适配器，包括投资人、投资机构、智囊团
 */
public class InvestorFragmentAdapter extends FragmentPagerAdapter {


    public InvestorFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Investor1Fragment investor1Fragment = new Investor1Fragment();
                return investor1Fragment;
            case 1:
                Investor2Fragment investor2Fragment = new Investor2Fragment();
                return investor2Fragment;
            case 2:
                Investor3Fragment investor3Fragment = new Investor3Fragment();
                return investor3Fragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
