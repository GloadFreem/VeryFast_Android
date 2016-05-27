package com.jinzht.pro1.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.RecyclerViewData;
import com.jinzht.pro1.adapter.ReleasePhotosAdapter;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.callback.ItemClickListener;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 在圈子发布话题界面
 */
public class ReleaseCircleActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private LinearLayout btnRelease;// 发布
    private EditText edContent;// 发布的内容
    private RecyclerView recyclerview;// 图片

    List<Integer> photos;// 要发布的照片
    ReleasePhotosAdapter adapter;// RecyclerView数据填充器

    @Override
    protected int getResourcesId() {
        return R.layout.activity_release_circle;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        btnRelease = (LinearLayout) findViewById(R.id.title_btn_right);// 发布
        btnRelease.setOnClickListener(this);
        edContent = (EditText) findViewById(R.id.ed_content);// 发布的内容
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);// 图片

        initRecyclerView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.title_btn_right:// 发布
                break;
        }
    }

    private void initRecyclerView() {
        // 准备数据
        photos = new ArrayList<>(Arrays.asList(R.mipmap.a, R.mipmap.a, R.mipmap.a, R.mipmap.a, R.mipmap.a, R.mipmap.a, R.mipmap.a, R.mipmap.a));
        adapter = new ReleasePhotosAdapter(mContext, photos);
        // 填充数据
        RecyclerViewData.setGrid(recyclerview, mContext, adapter, 3);
        // 条目点击事件
        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (photos.size() == 9) {
                    SuperToastUtils.showSuperToast(mContext, 2, "点击了照片" + position);
                } else if (photos.size() == 0) {
                    SuperToastUtils.showSuperToast(mContext, 2, "添加照片");
                } else if (photos.size() > 0 && photos.size() < 9) {
                    if (position == photos.size()) {
                        SuperToastUtils.showSuperToast(mContext, 2, "添加照片");
                    } else {
                        SuperToastUtils.showSuperToast(mContext, 2, "点击了照片" + position);
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemSubViewClick(View view, int position) {

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
