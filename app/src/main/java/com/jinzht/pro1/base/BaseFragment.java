package com.jinzht.pro1.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.jinzht.pro1.activity.LoginActivity;
import com.jinzht.pro1.bean.LoginBean;
import com.jinzht.pro1.callback.ErrorException;
import com.jinzht.pro1.callback.LoginAgainCallBack;
import com.jinzht.pro1.callback.ProgressBarCallBack;
import com.jinzht.pro1.utils.ACache;
import com.jinzht.pro1.utils.AESUtils;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.FastJsonTools;
import com.jinzht.pro1.utils.MD5Utils;
import com.jinzht.pro1.utils.NetWorkUtils;
import com.jinzht.pro1.utils.OkHttpException;
import com.jinzht.pro1.utils.OkHttpUtils;
import com.jinzht.pro1.utils.SharedPreferencesUtils;
import com.jinzht.pro1.view.LoadingProssbar;
import com.umeng.analytics.MobclickAgent;

import cn.sharesdk.framework.ShareSDK;

/**
 * Fragment的基类
 */
public abstract class BaseFragment extends Fragment implements ProgressBarCallBack, ErrorException, LoginAgainCallBack {

    private String className;// 当前类名
    protected String TAG;// 当前类名
    protected Context mContext;// Application的Context
    protected FragmentActivity mActivity;// 当前Fragment的父类Activ
    protected ACache aCache;// 缓存工具类
    protected LoadingProssbar dialog;// 加载进度条
    OkHttpException okHttpException = new OkHttpException(this);// okHttp的异常

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        className = getClass().getName();
        TAG = className.substring(className.lastIndexOf(".") + 1, className.length());
        mContext = activity.getApplicationContext();
        aCache = ACache.get(mContext);
        mActivity = getActivity();
        ShareSDK.initSDK(mContext);// 分享
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
    public void showProgressDialog(String message) {
        if (dialog == null) {
            dialog = new LoadingProssbar(getActivity());
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

    @Override
    public void successRefresh() {

    }

    public void loginAgain() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
