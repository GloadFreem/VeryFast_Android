package com.jinzht.pro1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseFragment;

/**
 * 路演项目详情页中的成员
 */
public class RoadshowMemberFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roadshow_member, container, false);
        return view;
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
