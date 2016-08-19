package com.jinzht.pro.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.activity.LoginActivity;
import com.jinzht.pro.activity.SetUserTypeActivity;
import com.jinzht.pro.application.MyApplication;
import com.jinzht.pro.bean.EventMsg;
import com.jinzht.pro.bean.IsAuthenticBean;
import com.jinzht.pro.bean.LoginBean;
import com.jinzht.pro.callback.ErrorException;
import com.jinzht.pro.callback.ProgressBarCallBack;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpException;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.view.LoadingProssbar;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import de.greenrobot.event.EventBus;

/**
 * 全屏Activity的基类
 */
public abstract class FullBaseActivity extends Activity implements ProgressBarCallBack, ErrorException {

    public String TAG;// 当前activity的tag
    public Context mContext;// Application的Context
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
        MobclickAgent.openActivityDurationTrack(false);// 友盟
        MobclickAgent.setSessionContinueMillis(30000l);// 友盟
//        UiHelp.setSameStatus(true, this);// 设置透明状态栏
        UiHelp.setFullScreenStatus(this);// 设置状态栏跟随应用背景

        // 请求权限
        verifyStoragePermissions(this);

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

    // 自动登录
    public class AutoLoginTask extends AsyncTask<Void, Void, LoginBean> {
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
            dismissProgressDialog();
            if (loginBean != null && loginBean.getStatus() == 200) {
                // 保存userId
                SharedPreferencesUtils.saveExtUserId(mContext, String.valueOf(loginBean.getData().getExtUserId()));
                SharedPreferencesUtils.saveUserId(mContext, loginBean.getData().getUserId());
                if (loginBean.getData().getIdentityType().getIdentiyTypeId() == -1) {
                    // 未选择身份类型，进入选择身份类型
                    setUserTypeDialog();
                } else {
                    // 保存userType，后续操作
                    SharedPreferencesUtils.saveUserType(mContext, loginBean.getData().getIdentityType().getIdentiyTypeId());
                    EventBus.getDefault().postSticky(new EventMsg("登录成功"));
                    // 保存是否认证的状态
                    IsAuthenticTask isAuthenticTask = new IsAuthenticTask();
                    isAuthenticTask.execute();
                    // 继续之前的动作
                    doAgain();
                }
            } else {
                // 自动登录未成功，进入登录页
                SuperToastUtils.showSuperToast(mContext, 2, "登录状态已改变，请重新登录");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    // 选择身份弹窗
    private void setUserTypeDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this, R.style.Custom_Dialog).create();
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_setusertype);
        TextView btnConfirm = (TextView) window.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 未选择身份类型，进入选择身份类型
                Intent intent = new Intent(mContext, SetUserTypeActivity.class);
                if (SharedPreferencesUtils.getIsWechatLogin(mContext)) {
                    intent.putExtra("isWechatLogin", 1);
                }
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });
    }

    // 登录成功后继续之前的操作
    public void doAgain() {

    }
}
