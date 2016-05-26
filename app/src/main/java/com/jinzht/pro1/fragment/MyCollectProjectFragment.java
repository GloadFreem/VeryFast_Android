package com.jinzht.pro1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseFragment;

/**
 * 我的关注中项目界面
 */
public class MyCollectProjectFragment extends BaseFragment {

    private ListView listView;// 我关注的项目列表

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_collect_project,container,false);
        listView = (ListView) view.findViewById(R.id.mycollect_project_list);// 我关注的项目列表
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.layout_empty_view_9dp, null), null, false);
        listView.setAdapter(new BaseAdapter() {
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
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = null;
                LayoutInflater inflater = LayoutInflater.from(mContext);
                if (position < 3) {
                    view = inflater.inflate(R.layout.item_project_roadshow, null);
                } else {
                    view = inflater.inflate(R.layout.item_project_preselect, null);
                }
                return view;
            }
        });
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
