package com.jinzht.pro1.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseFragment;

/**
 * 我的关注中投资人界面
 */
public class MyCollectInvestorFragment extends BaseFragment {

    private ListView listView;// 投资人列表

    @Override
    protected int setLayout(LayoutInflater inflater) {
        return R.layout.fragment_my_collect_investor;
    }

    @Override
    protected void onFirstUserVisible() {
        listView = (ListView) mActivity.findViewById(R.id.mycollect_investor_list);// 投资人列表
        listView.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.layout_empty_view_9dp, null), null, false);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 8;
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
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_mycollect_investor, null);
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
