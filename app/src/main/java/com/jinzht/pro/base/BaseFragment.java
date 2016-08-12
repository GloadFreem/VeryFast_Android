package com.jinzht.pro.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

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

import cn.sharesdk.framework.ShareSDK;

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
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
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
