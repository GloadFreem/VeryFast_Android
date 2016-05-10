package com.jinzht.pro1.fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseFragment;

/**
 * 预选项目
 */
public class PreselectionFragment extends BaseFragment {

    private ListView lvProject;// 项目列表

    @Override
    protected int setLayout(LayoutInflater inflater) {
        return R.layout.fragment_preselection;
    }

    @Override
    protected void onFirstUserVisible() {
        lvProject = (ListView) mActivity.findViewById(R.id.lv_project_preselection);// 项目列表
        lvProject.addHeaderView(View.inflate(mContext, R.layout.layout_empty_view, null));
        lvProject.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = View.inflate(mContext, R.layout.item_project_preselect, null);
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
