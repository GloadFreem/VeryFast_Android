package com.jinzht.pro1.fragment;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.CirclePhotosAdapter;
import com.jinzht.pro1.adapter.RecyclerViewData;
import com.jinzht.pro1.base.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 圈子界面
 */
public class CircleFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout titleBtnLeft;// title左侧按钮
    private TextView tvTitle;// 标题
    private LinearLayout titleBtnRight;// title右侧按钮
    private ListView listview;// 列表

    private List<Integer> photos;// 每个item的图片
    private CirclePhotosAdapter photosAdapter;// 每个item的图片适配器

    @Override
    protected int setLayout(LayoutInflater inflater) {
        return R.layout.fragment_circle;
    }

    @Override
    protected void onFirstUserVisible() {
        findView();
        initList();
    }

    private void findView() {
        titleBtnLeft = (LinearLayout) mActivity.findViewById(R.id.title_btn_left);// title左侧按钮
        titleBtnLeft.setVisibility(View.GONE);
        tvTitle = (TextView) mActivity.findViewById(R.id.tv_title);// 标题
        tvTitle.setText("广场");
        titleBtnRight = (LinearLayout) mActivity.findViewById(R.id.title_btn_right);// title右侧按钮
        titleBtnRight.setOnClickListener(this);
        listview = (ListView) mActivity.findViewById(R.id.listview);// 列表
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_btn_right:// 点击发表内容
                break;
        }
    }

    private void initList() {
        photos = new ArrayList<>(Arrays.asList(R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d, R.mipmap.a, R.mipmap.a, R.mipmap.a));
        photosAdapter = new CirclePhotosAdapter(mContext, photos);
        listview.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.layout_empty_view_9dp, null), null, false);
        listview.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 3;
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
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_circle, null);
                RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerview);
                RecyclerViewData.setGrid(rv, mContext, photosAdapter, 3);
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
