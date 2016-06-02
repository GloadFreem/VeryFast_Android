package com.jinzht.pro1.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.bean.CustomerServiceBean;
import com.jinzht.pro1.bean.RegisterBean;
import com.jinzht.pro1.utils.AESUtils;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.FastJsonTools;
import com.jinzht.pro1.utils.MD5Utils;
import com.jinzht.pro1.utils.NetWorkUtils;
import com.jinzht.pro1.utils.OkHttpUtils;
import com.jinzht.pro1.utils.SharePreferencesUtils;
import com.jinzht.pro1.utils.StringUtils;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 注册的第二个页面
 */
public class Register2Activity extends BaseActivity implements View.OnClickListener {

    private ImageButton register2BtBack;// 返回
    private ImageButton register2BtContactService;// 联系客服
    private EditText register2EdPassword1;// 设置密码输入框
    private EditText register2EdPassword2;// 确认密码输入框
    private Button register2BtConfirm;// 下一步按钮


    @Override
    protected int getResourcesId() {
        return R.layout.activity_register2;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        register2BtBack = (ImageButton) findViewById(R.id.register2_bt_back);// 返回
        register2BtBack.setOnClickListener(this);
        register2BtContactService = (ImageButton) findViewById(R.id.register2_bt_contact_service);// 联系客服
        register2BtContactService.setOnClickListener(this);
        register2EdPassword1 = (EditText) findViewById(R.id.register2_ed_password1);// 设置密码输入框
        register2EdPassword2 = (EditText) findViewById(R.id.register2_ed_password2);// 确认密码输入框
        register2BtConfirm = (Button) findViewById(R.id.register2_bt_confirm);// 下一步按钮
        register2BtConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register2_bt_back:// 点击了返回
                finish();
                break;
            case R.id.register2_bt_contact_service:// 点击了联系客服
                CustomerServiceTask customerServiceTask = new CustomerServiceTask();
                customerServiceTask.execute();
                break;
            case R.id.register2_bt_confirm:// 点击下一步按钮，进入完善信息页面
                if (StringUtils.isBlank(register2EdPassword1.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入符合规范的密码");
                } else if (StringUtils.length(register2EdPassword1.getText().toString()) < 6 || StringUtils.length(register2EdPassword1.getText().toString()) > 20) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入符合规范的密码");
                } else if (StringUtils.isBlank(register2EdPassword2.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "确认密码不能为空");
                } else if (!register2EdPassword1.getText().toString().equals(register2EdPassword2.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "密码不一致");
                } else {
                    RegisterTask registerTask = new RegisterTask();
                    registerTask.execute();
                }
//                Intent intent = new Intent(this, SetUserTypeActivity.class);
//                startActivity(intent);
                break;
        }
    }

    // 注册接口
    private class RegisterTask extends AsyncTask<Void, Void, RegisterBean> {
        @Override
        protected RegisterBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    String pwd = MD5Utils.encode(register2EdPassword1.getText().toString() + getIntent().getStringExtra("telephone").trim() + "lindyang");
                    body = OkHttpUtils.registerPost(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.REGISTER)),
                            "telephone", getIntent().getStringExtra("telephone"),
                            "verifyCode", getIntent().getStringExtra("verifyCode"),
                            "inviteCode", getIntent().getStringExtra("inviteCode"),
                            "password", pwd,
                            Constant.BASE_URL + Constant.REGISTER,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("注册信息", body);
                return FastJsonTools.getBean(body, RegisterBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(RegisterBean registerBean) {
            super.onPostExecute(registerBean);
            if (registerBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                return;
            } else {
                if (registerBean.getStatus() == 200) {
                    String pwd = null;
                    try {
                        pwd = MD5Utils.encode(register2EdPassword1.getText().toString() + getIntent().getStringExtra("telephone").trim() + "lindyang");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SharePreferencesUtils.saveInformation(mContext, getIntent().getStringExtra("telephone"), pwd);
                    SharePreferencesUtils.setIsLogin(mContext, true);
                    SharePreferencesUtils.setPerfectInformation(mContext, false);
                    SharePreferencesUtils.setAuth(mContext, false);
                    Intent intent = new Intent(mContext, SetUserTypeActivity.class);
                    startActivity(intent);
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, registerBean.getMessage());
                }
            }
        }
    }

    // 客服接口
    private class CustomerServiceTask extends AsyncTask<Void, Void, CustomerServiceBean> {
        @Override
        protected CustomerServiceBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.CUSTOMERSERVICE)),
                            Constant.BASE_URL + Constant.CUSTOMERSERVICE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("客服信息", body);
                return FastJsonTools.getBean(body, CustomerServiceBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CustomerServiceBean customerServiceBean) {
            super.onPostExecute(customerServiceBean);
            if (customerServiceBean.getStatus() == 200) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + customerServiceBean.getData().getTel());
                intent.setData(data);
                startActivity(intent);
            } else {
                SuperToastUtils.showSuperToast(mContext, 2, customerServiceBean.getMessage());
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
