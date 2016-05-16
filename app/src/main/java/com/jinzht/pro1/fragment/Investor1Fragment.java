package com.jinzht.pro1.fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseFragment;

/**
 * 投资人列表
 */
public class Investor1Fragment extends BaseFragment {

    private ListView lvInvestor1;// 投资人列表

    @Override
    protected int setLayout(LayoutInflater inflater) {
        return R.layout.fragment_investor1;
    }

    @Override
    protected void onFirstUserVisible() {
        lvInvestor1 = (ListView) mActivity.findViewById(R.id.lv_investor1);// 投资人列表
        lvInvestor1.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.layout_empty_view, null));
        lvInvestor1.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_investor1, null);
                return view;
            }
        });
    }

    @Override
    protected void onUserVisble() {

    }

    @Override
    protected void onFirstUserInvisble() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
