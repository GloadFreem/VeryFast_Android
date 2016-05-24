package com.jinzht.pro1.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.view.CircleImageView;

/**
 * 我的资料界面
 */
public class MyInfoActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private RelativeLayout btnFavicon;// 头像按钮
    private CircleImageView ivFavicon;// 头像图片
    private RelativeLayout btnInvite;// 指环码按钮
    private TextView tvInvite;// 指环码
    private TextView tvTag;// 认证状态
    private TextView tvName;// 姓名
    private RelativeLayout btnType;// 平台身份按钮
    private TextView tvType;// 平台身份
    private TextView tvIdcard;// 身份证号
    private RelativeLayout btnComp;// 公司按钮
    private TextView tvComp;// 公司名称
    private TextView tvPosition;// 职位名
    private RelativeLayout btnAddr;// 所在地按钮
    private TextView tvAddr;// 所在地
    private TextView tvRemind;// 提示
    private TextView btnBottom;// 底部按钮

    @Override
    protected int getResourcesId() {
        return R.layout.activity_my_info;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        findView();
    }

    private void findView() {
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("我的资料");
        btnFavicon = (RelativeLayout) findViewById(R.id.myinfo_btn_favicon);// 头像按钮
        btnFavicon.setOnClickListener(this);
        ivFavicon = (CircleImageView) findViewById(R.id.myinfo_iv_favicon);// 头像图片
        btnInvite = (RelativeLayout) findViewById(R.id.myinfo_btn_invite);// 指环码按钮
        btnInvite.setOnClickListener(this);
        tvInvite = (TextView) findViewById(R.id.myinfo_tv_invite);// 指环码
        tvTag = (TextView) findViewById(R.id.myinfo_tv_certification_tag);// 认证状态
        tvName = (TextView) findViewById(R.id.myinfo_tv_name);// 姓名
        btnType = (RelativeLayout) findViewById(R.id.myinfo_btn_type);// 平台身份按钮
        btnType.setOnClickListener(this);
        tvType = (TextView) findViewById(R.id.myinfo_tv_type);// 平台身份
        tvIdcard = (TextView) findViewById(R.id.myinfo_tv_idcard);// 身份证号
        btnComp = (RelativeLayout) findViewById(R.id.myinfo_btn_comp);// 公司按钮
        btnComp.setOnClickListener(this);
        tvComp = (TextView) findViewById(R.id.myinfo_tv_comp);// 公司名称
        tvPosition = (TextView) findViewById(R.id.myinfo_tv_position);// 职位名
        btnAddr = (RelativeLayout) findViewById(R.id.myinfo_btn_addr);// 所在地按钮
        btnAddr.setOnClickListener(this);
        tvAddr = (TextView) findViewById(R.id.myinfo_tv_addr);// 所在地
        tvRemind = (TextView) findViewById(R.id.myinfo_tv_remind);// 提示
        btnBottom = (TextView) findViewById(R.id.myinfo_btn_bottom);// 底部按钮
        btnBottom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.myinfo_btn_favicon:// 选择头像
                break;
            case R.id.myinfo_btn_invite:// 点击查看指环码
                break;
            case R.id.myinfo_btn_type:// 点击查看和新增身份
                break;
            case R.id.myinfo_btn_comp:// 点击修改公司名
                break;
            case R.id.myinfo_btn_addr:// 点击修改所在地
                break;
            case R.id.myinfo_btn_bottom:// 提交认证或催一催
                break;
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
