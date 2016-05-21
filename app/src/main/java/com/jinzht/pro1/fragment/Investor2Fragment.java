package com.jinzht.pro1.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseFragment;

/**
 * 投资机构列表
 */
public class Investor2Fragment extends BaseFragment {

    private ListView lvInvestor2;// 投资机构列表

    @Override
    protected int setLayout(LayoutInflater inflater) {
        return R.layout.fragment_investor2;
    }

    @Override
    protected void onFirstUserVisible() {
        lvInvestor2 = (ListView) mActivity.findViewById(R.id.lv_investor2);// 投资机构列表
        lvInvestor2.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.layout_empty_view_9dp, null));
        lvInvestor2.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 6;
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
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                if (position < 3) {
                    view = LayoutInflater.from(mContext).inflate(R.layout.item_investor2_fund, null);
                } else {
                    view = LayoutInflater.from(mContext).inflate(R.layout.item_investor2, null);
                }
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
