package com.jinzht.pro1.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.FullBaseActivity;
import com.jinzht.pro1.bean.CommonBean;
import com.jinzht.pro1.bean.LoginBean;
import com.jinzht.pro1.utils.AESUtils;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.FastJsonTools;
import com.jinzht.pro1.utils.MD5Utils;
import com.jinzht.pro1.utils.NetWorkUtils;
import com.jinzht.pro1.utils.OkHttpUtils;
import com.jinzht.pro1.utils.SharedPreferencesUtils;
import com.jinzht.pro1.utils.StringUtils;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

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
        MobclickAgent.updateOnlineConfig(mContext);// 友盟
        new Thread(new Runnable() {
            @Override
            public void run() {
//                SystemClock.sleep(2500);// 延迟2.5秒
//                if (!SharedPreferencesUtils.getIsNotFirst(mContext)) {
//                    第一次打开应用，进入引导页
//                            intent = new Intent(mContext, GuideActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else {
                IsLoginTask isLoginTask = new IsLoginTask();
                isLoginTask.execute();
//                }
            }
        }).start();
    }

    // 检查用户是否已登录
    private class IsLoginTask extends AsyncTask<Void, Void, CommonBean> {
        @Override
        protected CommonBean doInBackground(Void... params) {
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
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            if (commonBean != null && commonBean.getStatus() == 200) {
                // 已登录，进入主页
                intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // 自动登录
                AutoLoginTask autoLoginTask = new AutoLoginTask();
                autoLoginTask.execute();
            }
        }
    }

    // 自动登录
    private class AutoLoginTask extends AsyncTask<Void, Void, LoginBean> {
        @Override
        protected LoginBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.loginPost(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.LOGIN)),
                            "telephone", SharedPreferencesUtils.getTelephone(mContext),
                            "password", SharedPreferencesUtils.getPassword(mContext),
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
                    intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // 自动登录未成功，进入登录页
                    intent = new Intent(mContext, LoginActivity.class);
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
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
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
