package com.jinzht.pro1.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.bean.LoginBean;
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
import com.jinzht.pro1.utils.UiUtils;
import com.jinzht.pro1.view.CircleImageView;
import com.mob.tools.utils.UIHandler;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 登录页
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, Handler.Callback, PlatformActionListener {

    private CircleImageView loginIvUserimage;// 用户头像
    private EditText loginEdTel;// 电话号码输入框
    private EditText loginEdPassword;// 密码输入框
    private TextView login_tv_forget_password;// 忘记密码按钮
    private RelativeLayout loginRlLogin;// 登录按钮
    private RelativeLayout loginRlRegister;// 没有账号按钮
    private TextView loginTvWechat;// 微信登录按钮

    private static final int MSG_USERID_FOUND = 1;// 用户信息已存在
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;

    private Intent intent;

    @Override
    protected int getResourcesId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        loginIvUserimage = (CircleImageView) findViewById(R.id.login_iv_userimage);// 用户头像
        loginEdTel = (EditText) findViewById(R.id.login_ed_tel);// 电话号码输入框
        loginEdPassword = (EditText) findViewById(R.id.login_ed_password);// 密码输入框
        login_tv_forget_password = (TextView) findViewById(R.id.login_tv_forget_password);// 忘记密码按钮
        login_tv_forget_password.setOnClickListener(this);
        loginRlLogin = (RelativeLayout) findViewById(R.id.login_rl_login);// 登录按钮
        loginRlLogin.setOnClickListener(this);
        loginRlRegister = (RelativeLayout) findViewById(R.id.login_rl_register);// 没有账号按钮
        loginRlRegister.setOnClickListener(this);
        loginTvWechat = (TextView) findViewById(R.id.login_tv_wechat);// 微信登录按钮
        loginTvWechat.setOnClickListener(this);

        loginIvUserimage.setImageResource(R.drawable.ic_launcher);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_tv_forget_password:// 点击忘记密码，进入找回密码页面
                intent = new Intent(this, FindPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.login_rl_login:// 点击登录，进入主页
                if (StringUtils.isBlank(loginEdTel.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入正确的手机号码");
                } else if (!StringUtils.isTel(loginEdTel.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入正确的手机号码");
                } else if (StringUtils.isBlank(loginEdPassword.getText().toString())) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入符合规范的密码");
                } else if (StringUtils.length(loginEdPassword.getText().toString()) < 6 || StringUtils.length(loginEdPassword.getText().toString()) > 20) {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入符合规范的密码");
                } else {
                    LoginTask loginTask = new LoginTask();
                    loginTask.execute();
                }
                break;
            case R.id.login_rl_register:// 点击没有账号，进入注册页面
                intent = new Intent(this, Register1Activity.class);
                startActivity(intent);
                break;
            case R.id.login_tv_wechat:// 点击微信登录，进入微信授权界面
                authorize(new Wechat(this));
                break;
        }
    }

    // 微信授权登录
    private void authorize(Platform plat) {
        if (plat.isValid()) {
            String userId = plat.getDb().getUserId();
            if (!TextUtils.isEmpty(userId)) {
                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
                login(plat, userId, null);
                return;
            }
        }
        //若本地没有授权过就请求用户数据
        plat.setPlatformActionListener(this);//
        plat.SSOSetting(true);//此处设置为false，则在优先采用客户端授权的方法，设置true会采用网页方式
        plat.showUser(null);//获得用户数据

    }

    // 发送登录信息
    private void login(Platform plat, String userId, HashMap<String, Object> userInfo) {
        Message msg = new Message();
        msg.what = MSG_LOGIN;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_USERID_FOUND: {
                Toast.makeText(this, R.string.userid_found, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_LOGIN: {
                String text = getString(R.string.logining, msg.obj);
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                Platform platform = (Platform) msg.obj;
                WechatLoginTask wechatLoginTask = new WechatLoginTask(platform);
            }
            break;
            case MSG_AUTH_CANCEL: {
                Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_ERROR: {
                Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_COMPLETE: {
                Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT).show();
            }
            break;
        }
        return false;
    }

    // 微信授权成功
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (i == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
            login(platform, platform.getDb().getUserId(), hashMap);
            Log.i("微信授权成功", "name:" + platform.getName() + "id:" + platform.getDb().getUserId() + "icon" + platform.getDb().getUserIcon() + "hashMap" + hashMap);
        }
    }

    // 微信授权错误
    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        if (i == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        throwable.printStackTrace();
    }

    // 微信授权取消
    @Override
    public void onCancel(Platform platform, int i) {
        if (i == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }

    // 应用微信授权登录
    private class WechatLoginTask extends AsyncTask<Void, Void, LoginBean> {
        private Platform platform;

        public WechatLoginTask(Platform platform) {
            this.platform = platform;
        }

        @Override
        protected LoginBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.wechatLoginPost(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.WECHATLOGIN)),
                            "wechatID", platform.getDb().getUserId(),
                            Constant.BASE_URL + Constant.WECHATLOGIN,
                            mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("微信登录返回信息", body);
                return FastJsonTools.getBean(body, LoginBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(LoginBean loginBean) {
            super.onPostExecute(loginBean);
            if (loginBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                return;
            } else {
                if (loginBean.getStatus() == 200) {
                    intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, loginBean.getMessage());
                }
            }
        }
    }

    // 普通登录接口
    private class LoginTask extends AsyncTask<Void, Void, LoginBean> {
        @Override
        protected LoginBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    String pwd = MD5Utils.encode(loginEdPassword.getText().toString() + loginEdTel.getText().toString() + "lindyang");
                    body = OkHttpUtils.loginPost(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.LOGIN)),
                            "telephone", loginEdTel.getText().toString(),
                            "password", pwd,
                            Constant.BASE_URL + Constant.LOGIN,
                            mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("登录返回信息", body);
                return FastJsonTools.getBean(body, LoginBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(LoginBean loginBean) {
            super.onPostExecute(loginBean);
            if (loginBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                return;
            } else {
                if (loginBean.getStatus() == 200) {
                    String pwd = null;
                    try {
                        pwd = MD5Utils.encode(loginEdPassword.getText().toString() + loginEdTel.getText().toString().trim() + "lindyang");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SharePreferencesUtils.saveInformation(mContext, loginEdTel.getText().toString(), pwd);
                    SharePreferencesUtils.setIsLogin(mContext, true);
//                    SharePreferencesUtils.setPerfectInformation(mContext, false);
//                    SharePreferencesUtils.setAuth(mContext, false);
                    intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, loginBean.getMessage());
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
