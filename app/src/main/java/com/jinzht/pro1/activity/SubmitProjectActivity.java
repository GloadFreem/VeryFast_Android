package com.jinzht.pro1.activity;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.view.CircleImageView;

/**
 * 向投资人提交项目页面
 */
public class SubmitProjectActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView title;// 本页标题
    private LinearLayout btnService;// 联系客服
    private ImageView imgService;// 联系客服图标
    private CircleImageView favicon;// 投资人头像
    private TextView name;// 投资人姓名
    private TextView position;// 投资人职位
    private TextView compName;// 投资人公司名
    private TextView addr;// 投资人所在地
    private TextView tvRecommendReason;// 推荐理由四个字
    private EditText edRecommend;// 推荐理由输入框
    private RelativeLayout submitRlRecommend;// 推荐理由输入框背景
    private CircleImageView proLogo;// 项目logo
    private TextView proTitle;// 项目标题
    private ImageView proTag;// 项目状态
    private TextView proName;// 项目名
    private TextView proDesc;// 项目描述
    private TextView popularity;// 人气指数
    private TextView remainingTime;// 剩余时间
    private TextView financingAmount;// 融资金额
    private TextView btnSubmit;// 确认按钮

    @Override
    protected int getResourcesId() {
        return R.layout.activity_submit_project;
    }

    @Override
    protected void init() {
        UiHelp.setTranslucentStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.title_btn_left);// 返回
        btnBack.setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_title);// 本页标题
        title.setText("提交项目");
        btnService = (LinearLayout) findViewById(R.id.title_btn_right);// 联系客服
        btnService.setOnClickListener(this);
        imgService = (ImageView) findViewById(R.id.title_iv_right);// 联系客服图标
        imgService.setBackgroundResource(R.mipmap.iconfont_kefu);
        favicon = (CircleImageView) findViewById(R.id.submit_iv_favicon);// 投资人头像
        name = (TextView) findViewById(R.id.submit_tv_name);// 投资人姓名
        position = (TextView) findViewById(R.id.submit_tv_position);// 投资人职位
        compName = (TextView) findViewById(R.id.submit_tv_comp_name);// 投资人公司名
        addr = (TextView) findViewById(R.id.submit_tv_addr);// 投资人所在地
        tvRecommendReason = (TextView) findViewById(R.id.submit_tv_recommend_reason);// 推荐理由四个字
        // 将推荐理由的字数限制字体变小
        SpannableString span = new SpannableString("推荐理由 (20～300字)");
        span.setSpan(new AbsoluteSizeSpan(12, true), 5, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRecommendReason.setText(span);
        edRecommend = (EditText) findViewById(R.id.submit_et_recommend);// 推荐理由输入框
        submitRlRecommend = (RelativeLayout) findViewById(R.id.submit_rl_recommend);// 推荐理由输入框背景
        submitRlRecommend.setOnClickListener(this);
        proLogo = (CircleImageView) findViewById(R.id.submit_iv_pro_img);// 项目logo
        proTitle = (TextView) findViewById(R.id.submit_tv_pro_title);// 项目标题
        proTag = (ImageView) findViewById(R.id.submit_iv_tag);// 项目状态
        proName = (TextView) findViewById(R.id.submit_tv_pro_name);// 项目名
        proDesc = (TextView) findViewById(R.id.submit_tv_pro_desc);// 项目描述
        popularity = (TextView) findViewById(R.id.submit_popularity);// 人气指数
        remainingTime = (TextView) findViewById(R.id.submit_remaining_time);// 剩余时间
        financingAmount = (TextView) findViewById(R.id.submit_financing_amount);// 融资金额
        btnSubmit = (TextView) findViewById(R.id.submit_btn_confirm);// 确认按钮
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_btn_left:// 返回
                finish();
                break;
            case R.id.title_btn_right:// 联系客服
                SuperToastUtils.showSuperToast(this, 2, "客服");
                break;
            case R.id.submit_rl_recommend:// 点击此区域弹出键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edRecommend, InputMethodManager.SHOW_FORCED);
                break;
            case R.id.submit_btn_confirm:// 确认提交
                SuperToastUtils.showSuperToast(this, 2, "提交");
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
