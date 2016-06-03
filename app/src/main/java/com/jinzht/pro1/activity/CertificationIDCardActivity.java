package com.jinzht.pro1.activity;

import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 实名认证上传身份证界面
 */
public class CertificationIDCardActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private RelativeLayout idcardZheng;// 身份证正面
    private RelativeLayout idcardFan;// 身份证反面
    private EditText edIdnum;// 身份证号码
    private EditText edName;// 姓名
    private RelativeLayout rlCompanyName;// 公司名称整体布局
    private EditText edCompanyName;// 公司名称
    private TextView tvCompanyAddr;// 公司所在地提示
    private TextView btnCompanyAddr1;// 公司所在地选择按钮
    private RelativeLayout rlPosition;// 担任职务整体布局
    private EditText edPosition;// 职位
    private View line;// 职务下面的分割线
    private RelativeLayout rlField;// 投资领域整体布局
    private TextView tvField;// 投资领域提示
    private TextView tvSelectField;// 选择投资领域按钮
    private Button btnNext;// 下一步按钮


    private String str;// 转换字体的临时字符串
    private SpannableString span;// 设置TextView不同字体

    @Override
    protected int getResourcesId() {
        return R.layout.activity_certification_idcard;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        idcardZheng = (RelativeLayout) findViewById(R.id.certification_rl_idcard_zheng);// 身份证正面
        idcardZheng.setOnClickListener(this);
        idcardFan = (RelativeLayout) findViewById(R.id.certification_rl_idcard_fan);// 身份证反面
        idcardFan.setOnClickListener(this);
        edIdnum = (EditText) findViewById(R.id.certification_ed_idnum);// 身份证号码
        edName = (EditText) findViewById(R.id.certification_ed_name);// 姓名
        rlCompanyName = (RelativeLayout) findViewById(R.id.rl_company_name);// 公司名称整体布局
        edCompanyName = (EditText) findViewById(R.id.certification_ed_company_name);// 公司名称
        tvCompanyAddr = (TextView) findViewById(R.id.certification_tv_company_addr);// 公司所在地提示
        btnCompanyAddr1 = (TextView) findViewById(R.id.certification_tv_company_addr1);// 公司所在地选择按钮
        btnCompanyAddr1.setOnClickListener(this);
        rlPosition = (RelativeLayout) findViewById(R.id.rl_position);// 担任职务整体布局
        edPosition = (EditText) findViewById(R.id.certification_ed_position);// 职位
        line = findViewById(R.id.line);// 职务下面的分割线
        rlField = (RelativeLayout) findViewById(R.id.rl_field);// 投资领域整体布局
        tvField = (TextView) findViewById(R.id.tv_field);// 投资领域提示
        tvSelectField = (TextView) findViewById(R.id.tv_select_field);// 选择投资领域按钮
        tvSelectField.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.certification_btn_next);// 下一步按钮
        btnNext.setOnClickListener(this);

        setMytitle();
        initDifferent();

    }

    // 根据身份类型不同而加载不同标题
    private void setMytitle() {
        // 项目方、投资人、智囊团都是3个步骤
        if (Constant.USERTYPE_XMF == getIntent().getIntExtra("usertype", 0)
                || Constant.USERTYPE_TZR == getIntent().getIntExtra("usertype", 0)
                || Constant.USERTYPE_ZNT == getIntent().getIntExtra("usertype", 0)) {
            str = "实名认证(1/3)";
            span = new SpannableString(str);
            span.setSpan(new AbsoluteSizeSpan(13, true), 4, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvTitle.setText(span);
            // 投资机构是4个步骤
        } else if (Constant.USERTYPE_TZJG == getIntent().getIntExtra("usertype", 0)) {
            str = "实名认证(1/4)";
            span = new SpannableString(str);
            span.setSpan(new AbsoluteSizeSpan(13, true), 4, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvTitle.setText(span);
        }
    }

    // 根据身份类型不同而加载不同控件
    private void initDifferent() {
        switch (getIntent().getIntExtra("usertype", 0)) {
            case Constant.USERTYPE_XMF:// 项目方，无领域选择
                rlField.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                break;
            case Constant.USERTYPE_TZR:// 投资人，无公司名、职务
                rlCompanyName.setVisibility(View.GONE);
                rlPosition.setVisibility(View.GONE);
                tvCompanyAddr.setText("所在地");
                btnCompanyAddr1.setText("请选择所在地");
                break;
            case Constant.USERTYPE_TZJG:// 投资机构，无领域选择
                rlField.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                break;
            case Constant.USERTYPE_ZNT:// 智囊团，公司名和职务选填，投资领域改为服务领域
                edCompanyName.setHint("请输入公司名称(选填)");
                edPosition.setHint("请输入担任职务(选填)");
                tvField.setText("服务领域");
                tvSelectField.setText("请选择服务领域");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.certification_rl_idcard_zheng:// 点击上传身份证正面照片
                SuperToastUtils.showSuperToast(this, 2, "正面");
                break;
            case R.id.certification_rl_idcard_fan:// 点击上传身份证反面照片
                SuperToastUtils.showSuperToast(this, 2, "反面");
                break;
            case R.id.certification_tv_company_addr1:// 点击选择公司所在地
                SuperToastUtils.showSuperToast(this, 2, "所在地");
                break;
            case R.id.tv_select_field:// 点击选择投资领域
                SuperToastUtils.showSuperToast(this, 2, "投资领域");
                break;
            case R.id.certification_btn_next:// 跳转到下一步界面
                Intent intent = new Intent(this, CertificationCompActivity.class);
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
}
