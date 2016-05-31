package com.jinzht.pro1.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.view.CircleImageView;

/**
 * 完善信息页面
 */
public class ImproveInfoActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private ImageButton improveInfoBtBack;// 返回
    private ImageButton improveInfoBtContactService;// 联系客服
    private CircleImageView improveInfoIvUserimage;// 选择头像
    private TextView improveInfoTvSelectUsertype;// 选择用户类型提示，选择后消失
    private TextView tvDescXiangmufang;// 项目方描述
    private TextView tvDescTouziren;// 投资人描述
    private TextView tvDescTouzijigou;// 投资机构描述
    private TextView tvDescZhinangtuan;// 智囊团描述
    private RadioGroup improveInfoRgSelectUsertype;// 选择用户类型的RadioGroup
    private RadioButton improveInfoRbXiangmufang;// 项目方选项
    private RadioButton improveInfoRbTouziren;// 投资人选项
    private RadioButton improveInfoRbTouzijigou;// 投资机构选项
    private RadioButton improveInfoRbZhinangtuan;// 智囊团选项
    private Button improveInfoCompleteRegister;// 完成注册按钮

    @Override
    protected int getResourcesId() {
        return R.layout.activity_improve_info;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        improveInfoBtBack = (ImageButton) findViewById(R.id.improve_info_bt_back);// 返回
        improveInfoBtBack.setOnClickListener(this);
        improveInfoBtContactService = (ImageButton) findViewById(R.id.improve_info_bt_contact_service);// 联系客服
        improveInfoBtContactService.setOnClickListener(this);
        improveInfoIvUserimage = (CircleImageView) findViewById(R.id.improve_info_iv_userimage);// 选择头像
        improveInfoIvUserimage.setOnClickListener(this);
        improveInfoTvSelectUsertype = (TextView) findViewById(R.id.improve_info_tv_select_usertype);// 选择用户类型提示，选择后消失
        tvDescXiangmufang = (TextView) findViewById(R.id.tv_desc_xiangmufang);// 项目方描述
        tvDescTouziren = (TextView) findViewById(R.id.tv_desc_touziren);// 投资人描述
        tvDescTouzijigou = (TextView) findViewById(R.id.tv_desc_touzijigou);// 投资机构描述
        tvDescZhinangtuan = (TextView) findViewById(R.id.tv_desc_zhinangtuan);// 智囊团描述
        improveInfoRgSelectUsertype = (RadioGroup) findViewById(R.id.improve_info_rg_select_usertype);// 选择用户类型的RadioGroup
        improveInfoRgSelectUsertype.setOnCheckedChangeListener(this);
        improveInfoRbXiangmufang = (RadioButton) findViewById(R.id.improve_info_rb_xiangmufang);// 项目方选项
        improveInfoRbTouziren = (RadioButton) findViewById(R.id.improve_info_rb_touziren);// 投资人选项
        improveInfoRbTouzijigou = (RadioButton) findViewById(R.id.improve_info_rb_touzijigou);// 投资机构选项
        improveInfoRbZhinangtuan = (RadioButton) findViewById(R.id.improve_info_rb_zhinangtuan);// 智囊团选项
        improveInfoCompleteRegister = (Button) findViewById(R.id.improve_info_complete_register);// 完成注册按钮
        improveInfoCompleteRegister.setOnClickListener(this);

        improveInfoIvUserimage.setImageResource(R.drawable.ic_launcher);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.improve_info_bt_back:// 点击了返回
                finish();
                break;
            case R.id.improve_info_bt_contact_service:// 点击了联系客服
                SuperToastUtils.showSuperToast(this, 2, "点击了联系客服");
                break;
            case R.id.improve_info_iv_userimage:// 点击了选择头像
                SuperToastUtils.showSuperToast(this, 2, "点击了选择头像");
                break;
            case R.id.improve_info_complete_register:// 点击完成注册，跳转至获得邀请码界面
                Intent intent = new Intent(this, CompleteRegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        improveInfoTvSelectUsertype.setVisibility(View.GONE);
        switch (checkedId) {
            case R.id.improve_info_rb_xiangmufang:// 选择了项目方
                tvDescXiangmufang.setVisibility(View.VISIBLE);
                tvDescTouziren.setVisibility(View.GONE);
                tvDescTouzijigou.setVisibility(View.GONE);
                tvDescZhinangtuan.setVisibility(View.GONE);
                break;
            case R.id.improve_info_rb_touziren:// 选择了投资人
                tvDescXiangmufang.setVisibility(View.GONE);
                tvDescTouziren.setVisibility(View.VISIBLE);
                tvDescTouzijigou.setVisibility(View.GONE);
                tvDescZhinangtuan.setVisibility(View.GONE);
                break;
            case R.id.improve_info_rb_touzijigou:// 选择了投资机构
                tvDescXiangmufang.setVisibility(View.GONE);
                tvDescTouziren.setVisibility(View.GONE);
                tvDescTouzijigou.setVisibility(View.VISIBLE);
                tvDescZhinangtuan.setVisibility(View.GONE);
                break;
            case R.id.improve_info_rb_zhinangtuan:// 选择了智囊团
                tvDescXiangmufang.setVisibility(View.GONE);
                tvDescTouziren.setVisibility(View.GONE);
                tvDescTouzijigou.setVisibility(View.GONE);
                tvDescZhinangtuan.setVisibility(View.VISIBLE);
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
