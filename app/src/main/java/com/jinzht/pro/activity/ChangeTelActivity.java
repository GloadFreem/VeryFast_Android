package com.jinzht.pro.activity;

import android.app.AlertDialog;
import android.graphics.Color;
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
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.utils.UiUtils;

/**
 * 更换绑定手机界面
 */
public class ChangeTelActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private EditText edTelNew;// 新手机号码输入框
    private EditText edCode;// 验证码输入框
    private TextView btnCode;// 获取验证码按钮
    private EditText edPwd;// 密码输入框
    private EditText edIdcard;// 身份证号码输入框
    private Button btnConfirm;// 确认按钮

    private int timer = 60;// 60s倒计时，用于验证码

    @Override
    protected int getResourcesId() {
        return R.layout.activity_change_tel;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("更换绑定手机");
        edTelNew = (EditText) findViewById(R.id.change_tel_ed_tel_new);// 新手机号码输入框
        edCode = (EditText) findViewById(R.id.change_tel_ed_code);// 验证码输入框
        btnCode = (TextView) findViewById(R.id.change_tel_tv_code);// 获取验证码按钮
        btnCode.setOnClickListener(this);
        edIdcard = (EditText) findViewById(R.id.change_tel_ed_idcard);// 身份证号码输入框
        edPwd = (EditText) findViewById(R.id.change_tel_ed_pwd);// 密码输入框
        btnConfirm = (Button) findViewById(R.id.change_tel_btn_confirm);// 确认按钮
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.change_tel_tv_code:// 点击获取验证码
                if (StringUtils.isBlank(edTelNew.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "手机号码不能为空");
                } else if (!StringUtils.isTel(edTelNew.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入正确的手机号码");
                } else {
                    UiUtils.getHandler().postDelayed(runnable, 1000);
                    btnCode.setText(timer + "秒后重新发送");
                    btnCode.setBackgroundResource(R.drawable.bg_code_gray);
                    btnCode.setTextColor(getResources().getColor(R.color.custom_orange));
                    GetCodeTask getCodeTask = new GetCodeTask();
                    getCodeTask.execute();
                }
                break;
            case R.id.change_tel_btn_confirm:// 确认
                if (StringUtils.isBlank(edTelNew.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "手机号码不能为空");
                } else if (!StringUtils.isTel(edTelNew.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入正确的手机号码");
                } else if (StringUtils.isBlank(edCode.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "验证码不能为空");
                } else if (StringUtils.isBlank(edIdcard.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请填写身份证号码");
                } else if (StringUtils.length(edIdcard.getText().toString()) != 18) {
                    SuperToastUtils.showSuperToast(this, 2, "请填写正确的身份证号码");
                } else if (StringUtils.isBlank(edPwd.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "密码不能为空");
                } else if (StringUtils.length(edPwd.getText().toString()) < 6 || StringUtils.length(edPwd.getText().toString()) > 20) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入符合规范的密码");
                } else {
                    if (clickable) {
                        clickable = false;
                        ChangeTelTask changeTelTask = new ChangeTelTask();
                        changeTelTask.execute();
                    }
                }
                break;
        }
    }

    // 获取验证码
    private class GetCodeTask extends AsyncTask<Void, Void, CommonBean> {
        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETVERIFYCODE)),
                            "telephone", edTelNew.getText().toString(),
                            "type", "3",
                            Constant.BASE_URL + Constant.GETVERIFYCODE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("验证码信息", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            if (commonBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (commonBean.getStatus() == 200) {
                    SuperToastUtils.showSuperToast(mContext, 2, commonBean.getMessage());
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, commonBean.getMessage());
                }
            }
        }
    }

    // 倒计时
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            btnCode.setClickable(false);
            timer--;
            btnCode.setText(timer + "秒后重新发送");
            UiUtils.getHandler().postDelayed(this, 1000);
            if (timer == 0) {
                timer = 60;
                btnCode.setText("获取验证码");
                btnCode.setBackgroundResource(R.drawable.bg_code_orange);
                btnCode.setTextColor(Color.WHITE);
                btnCode.setClickable(true);
                UiUtils.getHandler().removeCallbacks(this);
            }
        }
    };

    // 更改电话号码
    private class ChangeTelTask extends AsyncTask<Void, Void, CommonBean> {
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
                    String pwd = MD5Utils.encode(edPwd.getText().toString() + edTelNew.getText().toString() + "lindyang");
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.CHANGETEL)),
                            "telephone", edTelNew.getText().toString(),
                            "code", edCode.getText().toString(),
                            "identityCardNo", edIdcard.getText().toString(),
                            "password", pwd,
                            Constant.BASE_URL + Constant.CHANGETEL,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("更改手机号返回值", body);
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
                        String pwd = MD5Utils.encode(edPwd.getText().toString() + edTelNew.getText().toString() + "lindyang");
                        SharedPreferencesUtils.saveInformation(mContext, edTelNew.getText().toString(), pwd);
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
