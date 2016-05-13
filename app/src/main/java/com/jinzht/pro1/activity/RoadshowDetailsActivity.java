package com.jinzht.pro1.activity;

//import android.support.v4.view.ViewPager;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.RoadshowFragmentAdaper;
import com.jinzht.pro1.base.BaseFragmentActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.utils.UiUtils;
import com.jinzht.pro1.view.WrapContentHeightViewPager;

/**
 * 路演项目详情
 */
public class RoadshowDetailsActivity extends BaseFragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private LinearLayout titleBtnLeft;// title左边按钮，返回
    private ImageView titleIvLeft;// title左侧按钮图标
    private TextView tvTitle;// 标题
    private LinearLayout titleBtnRight2;// title右侧第2个按钮，收藏
    private ImageView titleIvRight2;// title右侧第二个按钮图标
    private LinearLayout titleBtnRight;// title最右侧按钮，分享
    private ImageView titleIvRight;// title最右侧按钮图标
    private ViewPager detailsRoadshowPpt;// 播放PPT区域
    private RadioGroup detailsRgTab;// 详情、成员、现场总合tab
    private RadioButton detailsRbtnDetail;// 详情按钮
    private RadioButton detailsRbtnMember;// 成员按钮
    private RadioButton detailsRbtnLive;// 现场按钮
    private LinearLayout detailsLlInvest;// 客服和投资按钮布局
    private ImageButton detailsBtnService;// 客服按钮
    private RelativeLayout detailsBtnInvest;// 认投按钮
    private WrapContentHeightViewPager detailsVpModule;// 详情页的ViewPager
    private ScrollView detailsSl;// ScrollView

    @Override
    protected int getResourcesId() {
        return R.layout.activity_details_roadshow;
    }

    @Override
    protected void init() {
        UiHelp.setTranslucentStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        detailsSl = (ScrollView) findViewById(R.id.details_sl);
        titleBtnLeft = (LinearLayout) findViewById(R.id.title_btn_left);// title左边按钮，返回
        titleBtnLeft.setOnClickListener(this);
        titleIvLeft = (ImageView) findViewById(R.id.title_iv_left);// title左侧按钮图标
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题，此页不显示
        tvTitle.setVisibility(View.GONE);
        titleBtnRight2 = (LinearLayout) findViewById(R.id.title_btn_right2);// title右侧第2个按钮，收藏
        titleBtnRight2.setOnClickListener(this);
        titleIvRight2 = (ImageView) findViewById(R.id.title_iv_right2);// title右侧第二个按钮图标
        titleBtnRight = (LinearLayout) findViewById(R.id.title_btn_right);// title最右侧按钮，分享
        titleBtnRight.setOnClickListener(this);
        titleIvRight = (ImageView) findViewById(R.id.title_iv_right);// title最右侧按钮图标
        detailsRoadshowPpt = (ViewPager) findViewById(R.id.details_roadshow_ppt);// 播放PPT区域
        detailsRgTab = (RadioGroup) findViewById(R.id.details_rg_tab);// 详情、成员、现场总合tab
        detailsRbtnDetail = (RadioButton) findViewById(R.id.details_rbtn_detail);// 详情按钮
        detailsRbtnMember = (RadioButton) findViewById(R.id.details_rbtn_member);// 成员按钮
        detailsRbtnLive = (RadioButton) findViewById(R.id.details_rbtn_live);// 现场按钮
        detailsVpModule = (WrapContentHeightViewPager) findViewById(R.id.details_vp_module);// 详情页的ViewPager
        detailsLlInvest = (LinearLayout) findViewById(R.id.details_ll_invest);// 客服和投资按钮布局
        detailsBtnService = (ImageButton) findViewById(R.id.details_btn_service);// 客服按钮
        detailsBtnService.setOnClickListener(this);
        detailsBtnInvest = (RelativeLayout) findViewById(R.id.details_btn_invest);// 认投按钮
        detailsBtnInvest.setOnClickListener(this);

        // 设置tab的单选事件
        detailsRgTab.setOnCheckedChangeListener(this);
        // 给详情ViewPager填充数据
        detailsVpModule.setAdapter(new RoadshowFragmentAdaper(getSupportFragmentManager()));
        detailsVpModule.setCurrentItem(0);
        // 设置tab和ViewPager联动
        detailsVpModule.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        detailsRgTab.check(R.id.details_rbtn_detail);
                        detailsLlInvest.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        detailsRgTab.check(R.id.details_rbtn_member);
                        detailsLlInvest.setVisibility(View.GONE);
                        break;
                    case 2:
                        detailsRgTab.check(R.id.details_rbtn_live);
                        detailsLlInvest.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_btn_left:// 返回上一页
                finish();
                break;
            case R.id.title_btn_right2:// 收藏
                SuperToastUtils.showSuperToast(this, 2, "收藏");
                break;
            case R.id.title_btn_right:// 分享
                SuperToastUtils.showSuperToast(this, 2, "分享");
                break;
            case R.id.details_btn_service:// 给客服打电话
                SuperToastUtils.showSuperToast(mContext, 2, "客服");
                break;
            case R.id.details_btn_invest:// 认投
                SuperToastUtils.showSuperToast(mContext, 2, "认投");
                break;
        }
    }

    // 设置tab的单选事件
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.details_rbtn_detail:// 选择了详情
                detailsVpModule.setCurrentItem(0);
                detailsRbtnDetail.setTextColor(UiUtils.getColor(R.color.custom_orange));
                detailsRbtnMember.setTextColor(UiUtils.getColor(R.color.bg_text));
                detailsRbtnLive.setTextColor(UiUtils.getColor(R.color.bg_text));
                detailsLlInvest.setVisibility(View.VISIBLE);
                break;
            case R.id.details_rbtn_member:// 选择了成员
                detailsVpModule.setCurrentItem(1);
                detailsRbtnDetail.setTextColor(UiUtils.getColor(R.color.bg_text));
                detailsRbtnMember.setTextColor(UiUtils.getColor(R.color.custom_orange));
                detailsRbtnLive.setTextColor(UiUtils.getColor(R.color.bg_text));
                detailsLlInvest.setVisibility(View.GONE);
                break;
            case R.id.details_rbtn_live:// 选择了现场
                detailsVpModule.setCurrentItem(2);
                detailsRbtnDetail.setTextColor(UiUtils.getColor(R.color.bg_text));
                detailsRbtnMember.setTextColor(UiUtils.getColor(R.color.bg_text));
                detailsRbtnLive.setTextColor(UiUtils.getColor(R.color.custom_orange));
                detailsLlInvest.setVisibility(View.GONE);
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
