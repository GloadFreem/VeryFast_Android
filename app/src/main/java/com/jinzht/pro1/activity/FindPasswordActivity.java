package com.jinzht.pro1.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.bean.CommonBean;
import com.jinzht.pro1.utils.AESUtils;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.FastJsonTools;
import com.jinzht.pro1.utils.MD5Utils;
import com.jinzht.pro1.utils.NetWorkUtils;
import com.jinzht.pro1.utils.OkHttpUtils;
import com.jinzht.pro1.utils.StringUtils;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.utils.UiUtils;

/**
 * 找回密码页面
 */
public class FindPasswordActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton findPasswordBtBack;// 返回
    private EditText findPasswordEdTel;// 电话号码输入框
    private EditText findPasswordEdCode;// 验证码输入框
    private TextView findPasswordTvGetCode;// 获取验证码按钮
    private Button findPasswordNext;// 下一步

    private int timer = 60;// 60s倒计时，用于验证码

    @Override
    protected int getResourcesId() {
        return R.layout.activity_find_password;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        findPasswordBtBack = (ImageButton) findViewById(R.id.find_password_bt_back);// 返回
        findPasswordBtBack.setOnClickListener(this);
        findPasswordEdTel = (EditText) findViewById(R.id.find_password_ed_tel);// 电话号码输入框
        findPasswordEdCode = (EditText) findViewById(R.id.find_password_ed_code);// 验证码输入框
        findPasswordTvGetCode = (TextView) findViewById(R.id.find_password_tv_getcode);// 获取验证码按钮
        findPasswordTvGetCode.setOnClickListener(this);
        findPasswordNext = (Button) findViewById(R.id.find_password_next);// 下一步
        findPasswordNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_password_bt_back:// 返回上一页
                finish();
                break;
            case R.id.find_password_tv_getcode:// 点击获取验证码
                if (StringUtils.isBlank(findPasswordEdTel.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入正确的手机号码");
                } else if (!StringUtils.isTel(findPasswordEdTel.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入正确的手机号码");
                } else {
                    UiUtils.getHandler().postDelayed(runnable, 1000);
                    findPasswordTvGetCode.setText(timer + "秒后重新发送");
                    findPasswordTvGetCode.setBackgroundResource(R.drawable.bg_code_gray);
                    findPasswordTvGetCode.setTextColor(getResources().getColor(R.color.custom_orange));
                    GetCodeTask getCodeTask = new GetCodeTask();
                    getCodeTask.execute();
                }
                break;
            case R.id.find_password_next:// 点击下一步，跳转至设置密码界面
                if (StringUtils.isBlank(findPasswordEdTel.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入正确的手机号码");
                } else if (!StringUtils.isTel(findPasswordEdTel.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入正确的手机号码");
                } else if (StringUtils.isBlank(findPasswordEdCode.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入正确的验证码");
                } else {
                    Intent intent = new Intent(this, SetPasswordActivity.class);
                    intent.putExtra("telephone", findPasswordEdTel.getText().toString());
                    intent.putExtra("verifyCode", findPasswordEdCode.getText().toString());
                    startActivity(intent);
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
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.VERIFYCODE)),
                            "telephone", findPasswordEdTel.getText().toString(),
                            "type", "0",
                            Constant.BASE_URL + Constant.VERIFYCODE,
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
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                return;
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
            findPasswordTvGetCode.setClickable(false);
            timer--;
            findPasswordTvGetCode.setText(timer + "秒后重新发送");
            UiUtils.getHandler().postDelayed(this, 1000);
            if (timer == 0) {
                timer = 60;
                findPasswordTvGetCode.setText("获取验证码");
                findPasswordTvGetCode.setBackgroundResource(R.drawable.bg_code_orange);
                findPasswordTvGetCode.setTextColor(Color.WHITE);
                findPasswordTvGetCode.setClickable(true);
                UiUtils.getHandler().removeCallbacks(this);
            }
        }
    };

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
