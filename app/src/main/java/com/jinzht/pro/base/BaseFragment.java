package com.jinzht.pro.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.activity.LoginActivity;
import com.jinzht.pro.activity.SetUserTypeActivity;
import com.jinzht.pro.bean.EventMsg;
import com.jinzht.pro.bean.IsAuthenticBean;
import com.jinzht.pro.bean.LoginBean;
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
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.view.LoadingProssbar;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import de.greenrobot.event.EventBus;

/**
 * Fragment的基类
 */
public abstract class BaseFragment extends Fragment implements ProgressBarCallBack, ErrorException {

    private String className;// 当前类名
    protected String TAG;// 当前类名
    protected Context mContext;// Application的Context
    protected FragmentActivity mActivity;// 当前Fragment的父类Activ
    protected ACache aCache;// 缓存工具类
    protected LoadingProssbar dialog;// 加载进度条
    OkHttpException okHttpException = new OkHttpException(this);// okHttp的异常

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        className = getClass().getName();
        TAG = className.substring(className.lastIndexOf(".") + 1, className.length());
        mContext = activity.getApplicationContext();
        aCache = ACache.get(mContext);
        mActivity = getActivity();
        ShareSDK.initSDK(mContext);// 分享

        // 请求权限
        verifyStoragePermissions(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainScreen");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        ShareSDK.stopSDK(mContext);
    }

    // 显示加载中的进度条
    @Override
    public void showProgressDialog() {
        if (dialog == null) {
            dialog = new LoadingProssbar(getActivity());
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
            dialog = null;
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
//                finish();
            }
        }
    }

    // 选择身份弹窗
    private void setUserTypeDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.Custom_Dialog).create();
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
//                finish();
            }
        });
    }

    // 登录成功后继续之前的操作
    public void doAgain() {

    }
}
