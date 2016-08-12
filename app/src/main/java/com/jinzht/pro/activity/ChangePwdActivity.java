package com.jinzht.pro.activity;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.CommonBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.DialogUtils;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;

/**
 * 修改密码界面
 */
public class ChangePwdActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private EditText edPwdOld;// 旧密码输入框
    private EditText edPwdNew;// 新密码输入框
    private EditText edPwdConfirm;// 确认密码输入框
    private Button btnConfirm;// 确认按钮

    @Override
    protected int getResourcesId() {
        return R.layout.activity_change_pwd;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("修改登录密码");
        edPwdOld = (EditText) findViewById(R.id.change_pwd_ed_pwd_old);// 旧密码输入框
        edPwdNew = (EditText) findViewById(R.id.change_pwd_ed_pwd_new);// 新密码输入框
        edPwdConfirm = (EditText) findViewById(R.id.change_pwd_ed_pwd_confirm);// 确认密码输入框
        btnConfirm = (Button) findViewById(R.id.change_pwd_btn_confirm);// 确认按钮
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.change_pwd_btn_confirm:// 确认
                if (StringUtils.isBlank(edPwdOld.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "旧密码不能为空");
                } else if (StringUtils.length(edPwdOld.getText().toString()) < 6 || StringUtils.length(edPwdOld.getText().toString()) > 20) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入符合规范的密码");
                } else if (StringUtils.isBlank(edPwdNew.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "新密码不能为空");
                } else if (StringUtils.length(edPwdNew.getText().toString()) < 6 || StringUtils.length(edPwdNew.getText().toString()) > 20) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入符合规范的密码");
                } else if (StringUtils.isBlank(edPwdConfirm.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "确认密码不能为空");
                } else if (!edPwdNew.getText().toString().equals(edPwdConfirm.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "密码不一致");
                } else {
                    if (clickable) {
                        clickable = false;
                        ChangePwdTask changePwdTask = new ChangePwdTask();
                        changePwdTask.execute();
                    }
                }
                break;
        }
    }

    // 修改密码
    private class ChangePwdTask extends AsyncTask<Void, Void, CommonBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    String pwdOld = MD5Utils.encode(edPwdOld.getText().toString() + SharedPreferencesUtils.getTelephone(mContext) + "lindyang");
                    String pwdNew = MD5Utils.encode(edPwdNew.getText().toString() + SharedPreferencesUtils.getTelephone(mContext) + "lindyang");
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.CHANGEPASSWORD)),
                            "passwordOld", pwdOld,
                            "passwordNew", pwdNew,
                            Constant.BASE_URL + Constant.CHANGEPASSWORD,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("修改密码返回值", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            clickable = true;
            dismissProgressDialog();
            if (commonBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (commonBean.getStatus() == 200) {
                    try {
                        String pwd = MD5Utils.encode(edPwdNew.getText().toString() + SharedPreferencesUtils.getTelephone(mContext) + "lindyang");
                        SharedPreferencesUtils.saveInformation(mContext, SharedPreferencesUtils.getTelephone(mContext), pwd);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    confirmDialog(true, commonBean.getMessage());
                } else {
                    confirmDialog(false, commonBean.getMessage());
                }
            }
        }
    }

    // 弹窗确认
    private void confirmDialog(final boolean succeed, String content) {
        final AlertDialog dialog = new AlertDialog.Builder(this, R.style.Custom_Dialog).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_confirm);
        ImageView ivTag = (ImageView) window.findViewById(R.id.iv_tag);
        TextView tvContent = (TextView) window.findViewById(R.id.tv_content);
        TextView btnConfirm = (TextView) window.findViewById(R.id.btn_confirm);
        if (succeed) {
            ivTag.setImageResource(R.mipmap.icon_dialog_confirm);
        } else {
            ivTag.setImageResource(R.mipmap.icon_exclamation);
        }
        tvContent.setText(content);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (succeed) {
                    finish();
                }
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
