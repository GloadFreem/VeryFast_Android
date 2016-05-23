package com.jinzht.pro1.activity;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 软件设置
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private RelativeLayout btnChangePwd;// 修改密码
    private RelativeLayout btnChangeTel;// 修改手机
    private ToggleButton toggleVoice;// 声音开关
    private ToggleButton toggleShake;// 震动开关
    private TextView btnCleanCache;// 清理缓存
    private TextView btnUpdate;// 版本更新
    private TextView btnExit;// 退出登录

    @Override
    protected int getResourcesId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("软件设置");
        btnChangePwd = (RelativeLayout) findViewById(R.id.setting_btn_change_pwd);// 修改密码
        btnChangePwd.setOnClickListener(this);
        btnChangeTel = (RelativeLayout) findViewById(R.id.setting_btn_change_tel);// 修改手机
        btnChangeTel.setOnClickListener(this);
        toggleVoice = (ToggleButton) findViewById(R.id.setting_toggle_voice);// 声音开关
        toggleVoice.setOnCheckedChangeListener(this);
        toggleShake = (ToggleButton) findViewById(R.id.setting_toggle_shark);// 震动开关
        toggleShake.setOnCheckedChangeListener(this);
        btnCleanCache = (TextView) findViewById(R.id.setting_btn_clean_cache);// 清理缓存
        btnCleanCache.setOnClickListener(this);
        btnUpdate = (TextView) findViewById(R.id.setting_btn_update);// 版本更新
        btnUpdate.setOnClickListener(this);
        btnExit = (TextView) findViewById(R.id.setting_btn_exit);// 退出登录
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.setting_btn_change_pwd:// 修改密码
                SuperToastUtils.showSuperToast(this, 2, "修改密码");
                break;
            case R.id.setting_btn_change_tel:// 修改手机号码
                SuperToastUtils.showSuperToast(this, 2, "修改手机号码");
                break;
            case R.id.setting_btn_clean_cache:// 清理缓存
                SuperToastUtils.showSuperToast(this, 2, "清理缓存");
                break;
            case R.id.setting_btn_update:// 版本更新
                SuperToastUtils.showSuperToast(this, 2, "版本更新");
                break;
            case R.id.setting_btn_exit:// 退出登录
                SuperToastUtils.showSuperToast(this, 2, "拜拜~");

        }
    }

    // 开关控件
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == toggleVoice) {
            if (isChecked) {
                SuperToastUtils.showSuperToast(this, 2, "声音开");
            } else {
                SuperToastUtils.showSuperToast(this, 2, "声音关");
            }
        }
        if (buttonView == toggleShake) {
            if (isChecked) {
                SuperToastUtils.showSuperToast(this, 2, "震动开");
            } else {
                SuperToastUtils.showSuperToast(this, 2, "震动关");
            }
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
