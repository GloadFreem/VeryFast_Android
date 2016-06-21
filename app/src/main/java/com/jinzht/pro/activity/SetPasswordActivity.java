package com.jinzht.pro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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

/**
 * 设置密码页面
 */
public class SetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton setPasswordBtBack;// 返回
    private EditText setEdPassword1;// 设置密码输入框
    private EditText setEdPassword2;// 确认密码输入框
    private Button setPasswordConfirm;// 确认按钮

    @Override
    protected int getResourcesId() {
        return R.layout.activity_set_password;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        setPasswordBtBack = (ImageButton) findViewById(R.id.set_password_bt_back);// 返回
        setPasswordBtBack.setOnClickListener(this);
        setEdPassword1 = (EditText) findViewById(R.id.set_ed_password1);// 设置密码输入框
        setEdPassword2 = (EditText) findViewById(R.id.set_ed_password2);// 确认密码输入框
        setPasswordConfirm = (Button) findViewById(R.id.set_password_confirm);// 确认按钮
        setPasswordConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_password_bt_back:// 返回上一页
                finish();
                break;
            case R.id.set_password_confirm:// 确认密码，进入首页
                if (StringUtils.isBlank(setEdPassword1.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入符合规范的密码");
                } else if (StringUtils.length(setEdPassword1.getText().toString()) < 6 || StringUtils.length(setEdPassword1.getText().toString()) > 20) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入符合规范的密码");
                } else if (StringUtils.isBlank(setEdPassword2.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "确认密码不能为空");
                } else if (!setEdPassword1.getText().toString().equals(setEdPassword2.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "密码不一致");
                } else {
                    ResetPwdTask resetPwdTask = new ResetPwdTask();
                    resetPwdTask.execute();
                }
                break;
        }
    }

    // 重置密码
    private class ResetPwdTask extends AsyncTask<Void, Void, CommonBean> {
        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    String pwd = MD5Utils.encode(setEdPassword1.getText().toString() + getIntent().getStringExtra("telephone").trim() + "lindyang");
                    body = OkHttpUtils.resetPwdPost(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.RESETPASSWORD)),
                            "telephone", getIntent().getStringExtra("telephone"),
                            "verifyCode", getIntent().getStringExtra("verifyCode"),
                            "password", pwd,
                            Constant.BASE_URL + Constant.RESETPASSWORD,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("注册信息", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            if (commonBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                return;
            } else {
                if (commonBean.getStatus() == 200) {
                    String pwd = null;
                    try {
                        pwd = MD5Utils.encode(setEdPassword1.getText().toString() + getIntent().getStringExtra("telephone").trim() + "lindyang");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SharedPreferencesUtils.saveInformation(mContext, getIntent().getStringExtra("telephone"), pwd);
//                    SharedPreferencesUtils.setIsLogin(mContext, true);
//                    SharedPreferencesUtils.setChoseUserType(mContext, false);
//                    SharedPreferencesUtils.setAuth(mContext, false);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
//                    SharedPreferencesUtils.setIsLogin(mContext, false);
                    SuperToastUtils.showSuperToast(mContext, 2, commonBean.getMessage());
                }
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
