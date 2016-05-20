package com.jinzht.pro1.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.PersonalCenterRVAdapter;
import com.jinzht.pro1.adapter.RecyclerViewData;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.callback.ItemClickListener;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.view.CircleImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 个人中心界面
 */
public class PersonalCenterActivity extends BaseActivity implements View.OnClickListener {

    private CircleImageView favicon;// 头像
    private ImageView level;// 等级
    private TextView name;// 姓名
    private TextView position;// 公司名和职位
    private RecyclerView recyclerView;// 条目集合
    private RelativeLayout btnExit;// 退出按钮

    private List<Integer> itemIcons;// 条目图标
    private List<String> itemNames;// 条目名
    private PersonalCenterRVAdapter rvAdapter;// 条目适配器

    @Override
    protected int getResourcesId() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        favicon = (CircleImageView) findViewById(R.id.personal_center_iv_favicon);// 头像
        favicon.setOnClickListener(this);
        level = (ImageView) findViewById(R.id.personal_center_v);// 等级
        name = (TextView) findViewById(R.id.personal_center_tv_name);// 姓名
        position = (TextView) findViewById(R.id.personal_center_tv_position);// 公司名和职位
        recyclerView = (RecyclerView) findViewById(R.id.personal_center_rv);// 条目集合
        btnExit = (RelativeLayout) findViewById(R.id.personal_center_btn_exit);// 退出按钮
        btnExit.setOnClickListener(this);
        // 处理RecyclerView的item
        initRecyclerView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal_center_iv_favicon:// 点击头像，进入个人信息
                SuperToastUtils.showSuperToast(this, 2, "个人信息");
                break;
            case R.id.personal_center_btn_exit:// 返回上一页
                finish();
                break;
        }
    }

    private void initRecyclerView() {
        // 准备数据
        itemIcons = new ArrayList<>(Arrays.asList(R.mipmap.icon_account, R.mipmap.icon_mycollect, R.mipmap.icon_myactivity, R.mipmap.icon_mygold, R.mipmap.icon_project_center, R.mipmap.icon_setting, R.mipmap.icon_aboutus, R.mipmap.icon_share_friends));
        itemNames = new ArrayList<>(Arrays.asList("资产账户", "我的关注", "我的活动", "我的金条", "项目中心", "软件设置", "关于平台", "推荐好友"));
        rvAdapter = new PersonalCenterRVAdapter(mContext, itemIcons, itemNames);
        // 填充数据
        RecyclerViewData.setGrid(recyclerView, mContext, rvAdapter, 4);
        // 条目点击事件
        rvAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SuperToastUtils.showSuperToast(mContext, 2, itemNames.get(position));
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
