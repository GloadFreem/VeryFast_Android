package com.jinzht.pro.base;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;

import com.jinzht.pro.application.MyApplication;
import com.jinzht.pro.bean.IsAuthenticBean;
import com.jinzht.pro.callback.ErrorException;
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
import com.jinzht.pro.view.LoadingProssbar;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

/**
 * 普通Activity的基类
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements ProgressBarCallBack, ErrorException {

    protected String TAG;// 当前activity的tag
    protected Context mContext;// Application的Context
    protected ACache aCache;// 缓存工具类
    LoadingProssbar dialog;// 加载进度条
    OkHttpException okHttpException = new OkHttpException(this);// okHttp的异常

    private ExitReceiver exitReceiver = new ExitReceiver();// 退出应用的广播接收者
    public static final String EXITACTION = "action.exit";// 退出应用的广播接收者的action

    protected boolean clickable = true;// 是否可点击

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };

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

        // 请求权限
        verifyStoragePermissions(this);

        init();
    }

    // 退出应用的广播接收者
    class ExitReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BaseFragmentActivity.this.finish();
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
    public void showProgressDialog() {
        if (dialog == null) {
            dialog = new LoadingProssbar(this);
            dialog.setCanceledOnTouchOutside(false);
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

    public class IsAuthenticTask extends AsyncTask<Void, Void, IsAuthenticBean> {
        @Override
        protected IsAuthenticBean doInBackground(Void... params) {
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                String body = "";
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.ISAUTHENTIC)),
                            Constant.BASE_URL + Constant.ISAUTHENTIC,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("是否已认证", body);
                return FastJsonTools.getBean(body, IsAuthenticBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(IsAuthenticBean isAuthenticBean) {
            super.onPostExecute(isAuthenticBean);
            if (isAuthenticBean != null && isAuthenticBean.getStatus() == 200) {
                if (isAuthenticBean.getData() != null) {
                    String isAuthentic = isAuthenticBean.getData().getName();
                    SharedPreferencesUtils.saveIsAuthentic(mContext, isAuthentic);
                }
            }
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
