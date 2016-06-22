package com.jinzht.pro.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.jinzht.pro.activity.LoginActivity;
import com.jinzht.pro.application.MyApplication;
import com.jinzht.pro.bean.LoginBean;
import com.jinzht.pro.callback.ErrorException;
import com.jinzht.pro.callback.LoginAgainCallBack;
import com.jinzht.pro.callback.ProgressBarCallBack;
import com.jinzht.pro.utils.ACache;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpException;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.view.LoadingProssbar;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

/**
 * 普通Activity的基类
 */
public abstract class BaseActivity extends Activity implements ProgressBarCallBack, LoginAgainCallBack, ErrorException {

    protected String TAG;// 当前activity的tag
    protected Context mContext;// Application的Context
    protected ACache aCache;// 缓存工具类
    LoadingProssbar dialog;// 加载进度条
    OkHttpException okHttpException = new OkHttpException(this);// okHttp的异常

    private ExitReceiver exitReceiver = new ExitReceiver();// 退出应用的广播接收者
    public static final String EXITACTION = "action.exit";// 退出应用的广播接收者的action

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);// 分享
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 取消应用标题
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);// 转场动画
        setContentView(getResourcesId());
        MyApplication.getInstance().addActivity(this);
        JPushInterface.init(getApplicationContext());// 极光推送
        mContext = getApplicationContext();
        TAG = getRunningActivityName();
        aCache = ACache.get(mContext);
        MobclickAgent.openActivityDurationTrack(false);// 友盟
        MobclickAgent.setSessionContinueMillis(30000l);// 友盟
//        UiHelp.setSameStatus(true, this);// 透明状态栏

        // 注册退出广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction(EXITACTION);
        registerReceiver(exitReceiver, filter);
        init();
    }

    // 退出应用的广播接收者
    class ExitReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BaseActivity.this.finish();
        }
    }

    protected abstract int getResourcesId();

    protected abstract void init();

    private String getRunningActivityName() {
        String contextString = this.toString();
        return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
        dismissProgressDialog();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
        JPushInterface.onResume(this);
    }

    // 异步登录任务，防止用户掉线
    public class LoginTask extends AsyncTask<Void, Void, LoginBean> {
        @Override
        protected LoginBean doInBackground(Void... voids) {
            String body = "";
            if (!NetWorkUtils.getNetWorkType(mContext).equals(NetWorkUtils.NETWORK_TYPE_DISCONNECT)) {
                try {
                    body = OkHttpUtils.loginPost(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.LOGIN)),
                            "telephone", SharedPreferencesUtils.getTelephone(mContext),
                            "password", SharedPreferencesUtils.getPassword(mContext),
//                            "telephone", "18729342354",
//                            "password", "111111",
                            "regId", JPushInterface.getRegistrationID(mContext),
                            Constant.BASE_URL + Constant.LOGIN,
                            mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("登录接口返回值", body);
                return FastJsonTools.getBean(body, LoginBean.class);
            } else {
                return null;
            }
        }

        // 从后台拿到用户数据后登录并保存相关数据，登录失败则跳转至登录界面
        @Override
        protected void onPostExecute(LoginBean loginBean) {
            super.onPostExecute(loginBean);
            if (loginBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                return;
            } else {
                if (loginBean.getStatus() == 200) {// 登录成功
//                    SharedPreferencesUtils.setIsLogin(mContext, true);// 保存登录状态
                    successRefresh();
                } else {
//                    SharedPreferencesUtils.setIsLogin(mContext, false);
                    SuperToastUtils.showSuperToast(mContext, 2, loginBean.getMessage());
                    loginAgain();
                }
            }
        }
    }

    // 登录成功时刷新
    @Override
    public void successRefresh() {

    }

    // 再次登录，跳转至LoginActivity
    private void loginAgain() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(exitReceiver);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        ShareSDK.stopSDK(this);
    }

    // 显示加载中的进度条
    @Override
    public void showProgressDialog(String message) {
        if (dialog == null) {
            dialog = new LoadingProssbar(this);
            dialog.show();
        } else {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    @Override
    public void dismissProgressDialog() {
        if (this.dialog != null) {
            dialog.dismiss();
        }
    }
}