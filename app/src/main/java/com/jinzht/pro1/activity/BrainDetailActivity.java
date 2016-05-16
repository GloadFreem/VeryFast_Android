package com.jinzht.pro1.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.FullBaseActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.view.CircleImageView;

/**
 * 智囊团详情页
 */
public class BrainDetailActivity extends FullBaseActivity implements View.OnClickListener {

    private LinearLayout titleBtnBack;
    private LinearLayout titleBtnShare;
    private CircleImageView brainDetailFavicon;
    private TextView brainDetailName;
    private TextView brainDetailPosition;
    private TextView brainDetailCompName;
    private TextView brainDetailAddr;
    private TextView brainDetailService;
    private TextView brainDetailDesc;
    private LinearLayout brainBtnCollect;

    @Override
    protected int getResourcesId() {
        return R.layout.activity_brain_detail;
    }

    @Override
    protected void init() {
        titleBtnBack = (LinearLayout) findViewById(R.id.title_btn_back);// 返回
        titleBtnBack.setOnClickListener(this);
        titleBtnShare = (LinearLayout) findViewById(R.id.title_btn_share);// 分享
        titleBtnShare.setOnClickListener(this);
        brainDetailFavicon = (CircleImageView) findViewById(R.id.brain_detail_favicon);// 头像
        brainDetailName = (TextView) findViewById(R.id.brain_detail_name);// 姓名
        brainDetailPosition = (TextView) findViewById(R.id.brain_detail_position);// 职位
        brainDetailCompName = (TextView) findViewById(R.id.brain_detail_comp_name);// 公司名
        brainDetailAddr = (TextView) findViewById(R.id.brain_detail_addr);// 所在地
        brainDetailService = (TextView) findViewById(R.id.brain_detail_service);// 服务领域
        brainDetailDesc = (TextView) findViewById(R.id.brain_detail_desc);// 个人简介
        brainBtnCollect = (LinearLayout) findViewById(R.id.brain_btn_collect);// 关注
        brainBtnCollect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_btn_back:// 返回
                finish();
                break;
            case R.id.title_btn_share:// 分享
                SuperToastUtils.showSuperToast(this, 2, "分享");
                break;
            case R.id.brain_btn_collect:// 关注
                SuperToastUtils.showSuperToast(this, 2, "关注");
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
