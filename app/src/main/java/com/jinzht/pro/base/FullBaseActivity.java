package com.jinzht.pro.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.jinzht.pro.activity.LoginActivity;
import com.jinzht.pro.application.MyApplication;
import com.jinzht.pro.bean.LoginBean;
import com.jinzht.pro.callback.ErrorException;
import com.jinzht.pro.callback.LoginAgainCallBack;
import com.jinzht.pro.callback.ProgressBarCallBack;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpException;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.view.LoadingProssbar;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

/**
 * 全屏Activity的基类
 */
public abstract class FullBaseActivity extends Activity implements ProgressBarCallBack, LoginAgainCallBack, ErrorException {

    public String TAG;// 当前activity的tag
    public Context mContext;// Application的Context
    LoadingProssbar dialog;// 加载进度条
    OkHttpException okHttpException = new OkHttpException(this);// okHttp的异常

    private ExitReceiver exitReceiver = new ExitReceiver();// 退出应用的广播接收者
    public static final String EXITACTION = "action.exit";// 退出应用的广播接收者的action

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);// 分享
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 取消应用标题
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);// 转场动画
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
        setContentView(getResourcesId());
        MyApplication.getInstance().addActivity(this);
        JPushInterface.init(getApplicationContext());// 极光推送
        mContext = getApplicationContext();
        TAG = getRunningActivityName();
        MobclickAgent.setSessionContinueMillis(30000l);// 友盟
//        UiHelp.setSameStatus(true, this);// 设置透明状态栏
        UiHelp.setFullScreenStatus(this);// 设置状态栏跟随应用背景
        init();
    }

    protected abstract int getResourcesId();

    protected abstract void init();

    // 退出应用的广播接收者
    class ExitReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            FullBaseActivity.this.finish();
        }
    }

    // 正在运行的activity名
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
                            "telePhone", SharedPreferencesUtils.getTelephone(mContext),
                            "passWord", SharedPreferencesUtils.getPassword(mContext),
                            "regId", JPushInterface.getRegistrationID(mContext),
                            Constant.BASE_URL + Constant.LOGIN,
                            mContext);
                    Log.i("登录接口返回值", body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return FastJsonTools.getBean(body, LoginBean.class);
            } else {
                return null;
            }
        }

        // 从后台拿到用户数据后登录并保存相关数据，登录失败则跳转至登录界面
        @Override
        protected void onPostExecute(LoginBean loginBean) {
            super.onPostExecute(loginBean);
//            if (loginBean == null) {
//                return;
//            } else {
//                if (loginBean.getCode() == 0) {// 登录成功
//                    UiHelp.printMsg(loginBean.getCode(), loginBean.getMessage(), mContext);// 根据返回码弹出对应toast
//                    SharedPreferencesUtils.setIsLogin(mContext, true);// 保存登录状态
////                    SharedPreferencesUtils.setChoseUserType(mContext, loginBean.getData().getInfo());// 保存是否完善信息的状态
//                    SharedPreferencesUtils.setAuth(mContext, String.valueOf(loginBean.getData().getAuth()));// 保存是否实名认证的状态
//                    successRefresh();
//                } else if (loginBean.getCode() == -1) {// 登录失败，跳转至登录页面
//                    SharedPreferencesUtils.setIsLogin(mContext, false);
//                    loginAgain();
//                } else {
//                    SharedPreferencesUtils.setIsLogin(mContext, false);
//                    loginAgain();
//                    UiHelp.printMsg(loginBean.getCode(), loginBean.getMessage(), mContext);
//                }
//            }
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
