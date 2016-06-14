package com.jinzht.pro1.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.FullBaseActivity;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.NetWorkUtils;
import com.jinzht.pro1.utils.SharePreferencesUtils;
import com.jinzht.pro1.utils.StringUtils;
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
//                if (!SharePreferencesUtils.getIsNotFirst(mContext)) {
//                    第一次打开应用，进入引导页
//                            intent = new Intent(mContext, GuideActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else {
                    if (SharePreferencesUtils.getIsLogin(mContext) && !NetWorkUtils.getNetWorkType(WelcomeActivity.this).equals(NetWorkUtils.NETWORK_TYPE_DISCONNECT)) {
                        // 非第一次，已登录，进入主页MainActivity
                        intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // 非第一次，没有登录，进入登录页
                        intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
//                }
            }
        }).start();
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
