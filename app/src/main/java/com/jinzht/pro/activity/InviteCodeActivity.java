package com.jinzht.pro.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.view.CircleImageView;

/**
 * 查看指环码界面
 */
public class InviteCodeActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private CircleImageView ivFavicon;// 头像
    private TextView tvInviteCode;// 指环码
    private ImageView btnCopy;// 复制按钮

    @Override
    protected int getResourcesId() {
        return R.layout.activity_invite_code;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        findView();

        if (!StringUtils.isBlank(SharedPreferencesUtils.getLocalFavicon(mContext))) {
            Glide.with(mContext).load(SharedPreferencesUtils.getLocalFavicon(mContext)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivFavicon);
        } else if (!StringUtils.isBlank(SharedPreferencesUtils.getOnlineFavicon(mContext))) {
            Glide.with(mContext).load(SharedPreferencesUtils.getOnlineFavicon(mContext)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivFavicon);
        } else {
            ivFavicon.setImageResource(R.drawable.ic_launcher);
        }
    }

    private void findView() {
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("指环码");
        ivFavicon = (CircleImageView) findViewById(R.id.iv_favicon);// 头像
        tvInviteCode = (TextView) findViewById(R.id.tv_invite_code);// 指环码
        tvInviteCode.setText(getIntent().getStringExtra("inviteCode"));
        btnCopy = (ImageView) findViewById(R.id.btn_copy);// 复制按钮
        btnCopy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.btn_copy:// 复制指环码
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(tvInviteCode.getText().toString());
                SuperToastUtils.showSuperToast(mContext, 2, "已复制到剪贴板");
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
