package com.jinzht.pro.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.jinzht.pro.R;
import com.jinzht.pro.base.FullBaseActivity;
import com.jinzht.pro.bean.IsLoginBean;
import com.jinzht.pro.bean.LoginBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.StringUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.jpush.android.api.JPushInterface;

/**
 * 启动页
 */
public class WelcomeActivity extends FullBaseActivity {

    private IWXAPI api;// IWXAPI 是第三方app和微信通信的openApi接口
    private Intent intent;

    private MessageReceiver mMessageReceiver;// 自定义信息广播接收者，用于极光推送
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    private long startTime = 0;// 访问网络开始时间
    private long endTime = 0;// 结束时间
    private long dTime = 0;// 时间差，小于3s则等待至3s后继续执行

    @Override
    protected int getResourcesId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void init() {
        registerMessageReceiver();// 注册信息广播接收者
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);
        api.registerApp(Constant.APP_ID);
//        MobclickAgent.updateOnlineConfig(mContext);// 友盟

        if (!SharedPreferencesUtils.getIsNotFirst(mContext)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    intent = new Intent(mContext, GuideActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 800);
        } else {
//            startTime = System.currentTimeMillis();
//            IsLoginTask isLoginTask = new IsLoginTask();
//            isLoginTask.execute();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 判断是否保存有账号密码，有就进主页，没有就去登录
                    if (!StringUtils.isBlank(SharedPreferencesUtils.getTelephone(mContext))
                            && !StringUtils.isBlank(SharedPreferencesUtils.getPassword(mContext))) {
                        intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 800);
        }
    }

    // 检查用户是否已登录
    private class IsLoginTask extends AsyncTask<Void, Void, IsLoginBean> {
        @Override
        protected IsLoginBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.ISLOGIN)),
                            Constant.BASE_URL + Constant.ISLOGIN,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("登录状态", body);
                return FastJsonTools.getBean(body, IsLoginBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(IsLoginBean isLoginBean) {
            super.onPostExecute(isLoginBean);
            if (isLoginBean != null && isLoginBean.getStatus() == 200) {
                endTime = System.currentTimeMillis();
                dTime = endTime - startTime;
                Log.i("时间差", String.valueOf(dTime));
                if (isLoginBean.getData().getIdentityType().getIdentiyTypeId() == -1) {
                    // 已登录，但未选择身份类型，进入选择身份类型
                    intent = new Intent(mContext, SetUserTypeActivity.class);
                    if (SharedPreferencesUtils.getIsWechatLogin(mContext)) {
                        intent.putExtra("isWechatLogin", 1);
                    }
                    if (dTime < 3000) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(intent);
                                finish();
                            }
                        }, 3000 - dTime);
                    } else {
                        startActivity(intent);
                        finish();
                    }
                } else {
                    // 已登录，且已选择身份类型，进入主页
                    intent = new Intent(mContext, MainActivity.class);
                    if (dTime < 3000) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(intent);
                                finish();
                            }
                        }, 3000 - dTime);
                    } else {
                        startActivity(intent);
                        finish();
                    }
                }
            } else {
                // 未登录，自动登录
                WelcomeActivity.AutoLoginTask autoLoginTask = new WelcomeActivity.AutoLoginTask();
                autoLoginTask.execute();
            }
        }
    }

    // 自动登录
    private class AutoLoginTask extends AsyncTask<Void, Void, LoginBean> {
        @Override
        protected LoginBean doInBackground(Void... params) {
            String body = "";
            Log.i("极光推送", JPushInterface.getRegistrationID(mContext) + " ");
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.loginPost(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.LOGIN)),
                            "telephone", SharedPreferencesUtils.getTelephone(mContext),
                            "password", SharedPreferencesUtils.getPassword(mContext),
                            "regId", JPushInterface.getRegistrationID(mContext),
                            Constant.BASE_URL + Constant.LOGIN,
                            mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("自动登录返回信息", body);
                return FastJsonTools.getBean(body, LoginBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(LoginBean loginBean) {
            super.onPostExecute(loginBean);
            endTime = System.currentTimeMillis();
            dTime = endTime - startTime;
            Log.i("时间差", String.valueOf(dTime));
            if (loginBean != null && loginBean.getStatus() == 200) {
                // 保存userId
                SharedPreferencesUtils.saveExtUserId(mContext, String.valueOf(loginBean.getData().getExtUserId()));
                SharedPreferencesUtils.saveUserId(mContext, loginBean.getData().getUserId());
                if (loginBean.getData().getIdentityType().getIdentiyTypeId() == -1) {
                    // 未选择身份类型，进入选择身份类型
                    intent = new Intent(mContext, SetUserTypeActivity.class);
                    if (SharedPreferencesUtils.getIsWechatLogin(mContext)) {
                        intent.putExtra("isWechatLogin", 1);
                    }
                    if (dTime < 3000) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(intent);
                                finish();
                            }
                        }, 3000 - dTime);
                    } else {
                        startActivity(intent);
                        finish();
                    }
                } else {
                    // 保存userType，进入主页
                    SharedPreferencesUtils.saveUserType(mContext, loginBean.getData().getIdentityType().getIdentiyTypeId());
                    intent = new Intent(mContext, MainActivity.class);
                    if (dTime < 3000) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(intent);
                                finish();
                            }
                        }, 3000 - dTime);
                    } else {
                        startActivity(intent);
                        finish();
                    }
                }
            } else {
                // 自动登录未成功，进入登录页
                intent = new Intent(mContext, LoginActivity.class);
                if (dTime < 3000) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                            finish();
                        }
                    }, 3000 - dTime);
                } else {
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    // 自定义信息广播接收者，用于极光推送
    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String message = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + message + "\n");
                if (!StringUtils.isBlank(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
            }
        }
    }

    // 注册信息广播接收者
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
    }
}
