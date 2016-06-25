package com.jinzht.pro.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.UserInfoBean;
import com.jinzht.pro.utils.UiHelp;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 平台身份界面
 */
public class UserTypeActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private ImageView ivType1;// 用户类型1图标
    private TextView tvType1;// 用户类型1
    private TextView tvDesc1;// 用户类型1描述
    private LinearLayout llType2;// 用户类型2布局
    private ImageView ivType2;// 用户类型2图标
    private TextView tvType2;// 用户类型2名称
    private TextView tvDesc2;// 用户类型2描述
    private TextView hint;// 新增用户提示
    private RelativeLayout rlAdd;// 新增身份布局
    private TextView btnAdd;// 新增身份按钮

    private List<UserInfoBean.DataBean.AuthenticsBean> datas = new ArrayList<>();

    @Override
    protected int getResourcesId() {
        return R.layout.activity_user_type;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        findView();
        EventBus.getDefault().register(this);
    }

    private void findView() {
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("平台身份");
        ivType1 = (ImageView) findViewById(R.id.iv_type1);// 用户类型1图标
        tvType1 = (TextView) findViewById(R.id.tv_type1);// 用户类型1
        tvDesc1 = (TextView) findViewById(R.id.tv_desc1);// 用户类型1描述
        llType2 = (LinearLayout) findViewById(R.id.ll_type2);// 用户类型2布局
        ivType2 = (ImageView) findViewById(R.id.iv_type2);// 用户类型2图标
        tvType2 = (TextView) findViewById(R.id.tv_type2);// 用户类型2名称
        tvDesc2 = (TextView) findViewById(R.id.tv_desc2);// 用户类型2描述
        hint = (TextView) findViewById(R.id.hint);// 新增用户提示
        rlAdd = (RelativeLayout) findViewById(R.id.rl_add);// 新增身份布局
        btnAdd = (TextView) findViewById(R.id.btn_add);// 新增身份按钮
        btnAdd.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void getAuthentics(List<UserInfoBean.DataBean.AuthenticsBean> list) {
        datas = list;
        if (datas != null && datas.size() != 0) {
            initData();
        }
    }

    private void initData() {
        tvType1.setText(datas.get(0).getIdentiytype().getName());
        switch (datas.get(0).getIdentiytype().getName()) {
            case "项目方":
                ivType1.setBackgroundResource(R.mipmap.icon_project_selected);
                tvDesc1.setText("项目方描述");
                break;
            case "个人投资者":
                ivType1.setBackgroundResource(R.mipmap.icon_investor);
                tvDesc1.setText("个人投资者描述");
                break;
            case "机构投资者":
                ivType1.setBackgroundResource(R.mipmap.icon_investorg);
                tvDesc1.setText("个人投资者描述");
                break;
            case "智囊团":
                ivType1.setBackgroundResource(R.mipmap.icon_brain);
                tvDesc1.setText("智囊团描述");
                break;
        }
        if (datas.size() > 1) {
            llType2.setVisibility(View.VISIBLE);
            hint.setVisibility(View.GONE);
            rlAdd.setVisibility(View.GONE);
            tvType2.setText(datas.get(1).getIdentiytype().getName());
            switch (datas.get(1).getIdentiytype().getName()) {
                case "项目方":
                    ivType2.setBackgroundResource(R.mipmap.icon_project_selected);
                    tvDesc2.setText("项目方描述");
                    break;
                case "个人投资者":
                    ivType2.setBackgroundResource(R.mipmap.icon_investor);
                    tvDesc2.setText("个人投资者描述");
                    break;
                case "机构投资者":
                    ivType2.setBackgroundResource(R.mipmap.icon_investorg);
                    tvDesc2.setText("个人投资者描述");
                    break;
                case "智囊团":
                    ivType2.setBackgroundResource(R.mipmap.icon_brain);
                    tvDesc2.setText("智囊团描述");
                    break;
            }
        } else {
            llType2.setVisibility(View.GONE);
            hint.setVisibility(View.VISIBLE);
            rlAdd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.btn_add:// 新增用户身份类型
                Intent intent = new Intent(this, SetUserTypeActivity.class);
                intent.putExtra("TAG", "addType");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
