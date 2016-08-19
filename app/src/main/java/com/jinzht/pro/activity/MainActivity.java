package com.jinzht.pro.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.adapter.MainFragmentAdapter;
import com.jinzht.pro.application.MyApplication;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.base.BaseFragmentActivity;
import com.jinzht.pro.base.FullBaseActivity;
import com.jinzht.pro.bean.BannerInfoBean;
import com.jinzht.pro.bean.EventMsg;
import com.jinzht.pro.bean.GoldAward;
import com.jinzht.pro.bean.IsLoginBean;
import com.jinzht.pro.bean.UpdateBean;
import com.jinzht.pro.service.DownloadAppService;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.CacheUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.DialogUtils;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.view.NoScrollViewPager;
import com.jinzht.pro.view.numberprogressbar.NumberProgressBar;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.greenrobot.event.EventBus;

/**
 * 主页
 */
public class MainActivity extends BaseFragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private RelativeLayout rlWithoutNet;// 无网络的提示布局
    private NoScrollViewPager mainViewpager;// 主页的4个模块
    private RadioGroup mainBottomTab;// 主页的4个模块按钮组
    private RadioButton mainBtnProject;// 项目按钮
    private RadioButton mainBtnInvestor;// 投资人按钮
    private ImageButton mainBtnMe;// 个人中心按钮
    private RadioButton mainBtnCircle;// 圈子按钮
    private RadioButton mainBtnActivity;// 活动按钮

    private UpdateBean.DataBean updateData;// 新版更新信息
    private NumberProgressBar progressBar;// 跟新时的进度条
    public static int downloading_progress = 0;// 下载进度

    private Handler netHandler;// 用于每隔5秒检测

    @Override
    protected int getResourcesId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        initView();
//        GetBannerInfo getBannerInfo = new GetBannerInfo();
//        getBannerInfo.execute();
        initData();

        // 登录
        if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
            IsLoginTask isLoginTask = new IsLoginTask();
            isLoginTask.execute();
        }

//        // 当天首次登录，送金条
//        if (isTodayFirstLaunch()) {
//            GetLoginGoldAward getLoginGoldAward = new GetLoginGoldAward();
//            getLoginGoldAward.execute();
//        }
//
//        // 保存是否认证的状态
//        IsAuthenticTask isAuthenticTask = new IsAuthenticTask();
//        isAuthenticTask.execute();

        // 5s后检查更新
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UpdateTask updateTask = new UpdateTask();
                updateTask.execute();
            }
        }, 5000);
    }

    private void initView() {
        rlWithoutNet = (RelativeLayout) findViewById(R.id.rl_without_net);// 无网络的提示布局
        mainViewpager = (NoScrollViewPager) findViewById(R.id.main_viewpager);// 主页的4个模块
        mainBottomTab = (RadioGroup) findViewById(R.id.main_bottom_tab);// 主页的4个模块按钮组
        mainBtnProject = (RadioButton) findViewById(R.id.main_btn_project);// 项目按钮
        mainBtnInvestor = (RadioButton) findViewById(R.id.main_btn_investor);// 投资人按钮
        mainBtnMe = (ImageButton) findViewById(R.id.main_btn_me);// 个人中心按钮
        mainBtnMe.setOnClickListener(this);
        mainBtnCircle = (RadioButton) findViewById(R.id.main_btn_circle);// 圈子按钮
        mainBtnActivity = (RadioButton) findViewById(R.id.main_btn_activity);// 活动按钮

        mainBottomTab.setOnCheckedChangeListener(this);
    }

    private void initData() {
        // 检测是否有网络
        if (NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
            rlWithoutNet.setVisibility(View.VISIBLE);
        } else {
            rlWithoutNet.setVisibility(View.GONE);
        }
        netHandler = new Handler();
        netHandler.postDelayed(runnable, 5000);

        mainViewpager.setAdapter(new MainFragmentAdapter(getSupportFragmentManager()));
        mainViewpager.setOffscreenPageLimit(4);
        mainViewpager.setCurrentItem(0);
        // 主页模块的ViewPager和RadioGroup联动
        mainViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mainBottomTab.check(R.id.main_btn_project);
                        break;
                    case 1:
                        mainBottomTab.check(R.id.main_btn_investor);
                        break;
                    case 2:
                        mainBottomTab.check(R.id.main_btn_circle);
                        break;
                    case 3:
                        mainBottomTab.check(R.id.main_btn_activity);
                        break;
                }
            }
        });
    }

    // 5秒获取一次网络状态
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                rlWithoutNet.setVisibility(View.VISIBLE);
            } else {
                rlWithoutNet.setVisibility(View.GONE);
            }
            netHandler.postDelayed(this, 5000);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_me:// 跳转到个人中心界面
                Intent intent = new Intent(this, PersonalCenterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private long exitTime = 0;

    // 点击两次后退出应用
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            SuperToastUtils.showSuperToast(mContext, 2, "再按一次退出应用");
            exitTime = System.currentTimeMillis();
        } else {
            Intent intent1 = new Intent();
            intent1.setAction(BaseActivity.EXITACTION);
            sendBroadcast(intent1);// 发送退出广播
            Intent intent2 = new Intent();
            intent1.setAction(FullBaseActivity.EXITACTION);
            sendBroadcast(intent2);// 发送退出广播
            Intent intent3 = new Intent();
            intent1.setAction(BaseFragmentActivity.EXITACTION);
            sendBroadcast(intent3);// 发送退出广播

            MobclickAgent.onKillProcess(MainActivity.this);
            MyApplication.getInstance().exit();//遍历所有Activity 并finish

            finish();
            System.exit(0);
        }
    }

    // BottomTab的四个单选按钮
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_btn_project:// 选择了项目
                mainViewpager.setCurrentItem(0, false);
                break;
            case R.id.main_btn_investor:// 选择了投资人
                mainViewpager.setCurrentItem(1, false);
                break;
            case R.id.main_btn_circle:// 选择了圈子
                mainViewpager.setCurrentItem(2, false);
                break;
            case R.id.main_btn_activity:// 选择了活动
                mainViewpager.setCurrentItem(3, false);
                break;
        }
    }

    // 判断是否当日首次启动
    private boolean isTodayFirstLaunch() {
        String savedDate = SharedPreferencesUtils.getTodayFirstDate(mContext);
        Date date = new Date();
        SimpleDateFormat formate = new SimpleDateFormat("yyyyMMdd", Locale.SIMPLIFIED_CHINESE);
        String currentDate = formate.format(date);
        Log.i("保存的时期", savedDate);
        Log.i("当前时期", currentDate);
        if (!currentDate.equals(savedDate)) {
            SharedPreferencesUtils.saveTodayFirstDate(mContext, currentDate);
            return true;
        } else {
            return false;
        }
    }

    // 检查用户是否已登录
    private class IsLoginTask extends AsyncTask<Void, Void, IsLoginBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String cacheBanner = (String) CacheUtils.readObject(Constant.CACHE_BANNER);
            String cacheRoadshowList = (String) CacheUtils.readObject(Constant.CACHE_ROADSHOW_LIST);
            String cachePreselectionList = (String) CacheUtils.readObject(Constant.CACHE_PRESELECTION_LIST);
            if (StringUtils.isBlank(cacheBanner) && StringUtils.isBlank(cacheRoadshowList) && StringUtils.isBlank(cachePreselectionList)) {
                showProgressDialog();
            }
        }

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
                dismissProgressDialog();
                if (isLoginBean.getData().getIdentityType().getIdentiyTypeId() == -1) {
                    // 已登录，但未选择身份类型，进入选择身份类型
                    setUserTypeDialog();
                } else {
                    // 已登录，且已选择身份类型，进行后续操作
                    EventBus.getDefault().postSticky(new EventMsg("登录成功"));
                    // 当天首次登录，送金条
                    if (isTodayFirstLaunch()) {
                        GetLoginGoldAward getLoginGoldAward = new GetLoginGoldAward();
                        getLoginGoldAward.execute();
                    }
                    // 保存是否认证的状态
                    IsAuthenticTask isAuthenticTask = new IsAuthenticTask();
                    isAuthenticTask.execute();
                }
            } else {
                // 未登录，自动登录
                AutoLoginTask autoLoginTask = new AutoLoginTask();
                autoLoginTask.execute();
            }
        }
    }

    @Override
    public void doAgain() {
        super.doAgain();
        // 当天首次登录，送金条
        if (isTodayFirstLaunch()) {
            GetLoginGoldAward getLoginGoldAward = new GetLoginGoldAward();
            getLoginGoldAward.execute();
        }
    }

    // 获取当日登录金条奖励
    private class GetLoginGoldAward extends AsyncTask<Void, Void, GoldAward> {
        @Override
        protected GoldAward doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETGOLDAWARD)),
                            Constant.BASE_URL + Constant.GETGOLDAWARD,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("今日登录奖励金条", body);
                return FastJsonTools.getBean(body, GoldAward.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(GoldAward goldAward) {
            super.onPostExecute(goldAward);
            if (goldAward != null && goldAward.getStatus() == 200) {
                if (goldAward.getData().getCount() != 0) {
                    DialogUtils.goldAnim(MainActivity.this, goldAward.getData().getCount(), goldAward.getData().getCountTomorrow());
                }
            }
        }
    }

    // 获取banner数据
    private class GetBannerInfo extends AsyncTask<Void, Void, BannerInfoBean> {
        @Override
        protected BannerInfoBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETBANNERINFO)),
                            Constant.BASE_URL + Constant.GETBANNERINFO,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("banner数据", body);
                return FastJsonTools.getBean(body, BannerInfoBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(BannerInfoBean bannerInfoBean) {
            super.onPostExecute(bannerInfoBean);
            if (bannerInfoBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (bannerInfoBean.getStatus() == 200) {
                    EventBus.getDefault().postSticky(bannerInfoBean);
                    initData();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, bannerInfoBean.getMessage());
                }
            }
        }
    }

    // 检查版本更新
    private class UpdateTask extends AsyncTask<Void, Void, UpdateBean> {
        @Override
        protected UpdateBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.UPDATE)),
                            Constant.BASE_URL + Constant.UPDATE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("检查更新", body);
                return FastJsonTools.getBean(body, UpdateBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(UpdateBean updateBean) {
            super.onPostExecute(updateBean);
            if (updateBean != null && updateBean.getStatus() == 200) {
                updateData = updateBean.getData();
                try {
                    if (!StringUtils.isEquals(UiHelp.getVersionName(MainActivity.this), updateData.getVersionStr())) {
                        if (updateData.isForce()) {
                            // 强制更新
                            coerceUpdateDialog(updateData.getContent());
                        } else {
                            // 提示更新
                            remindUpdateDialog(updateData.getContent());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 强制更新对话框
    private void coerceUpdateDialog(String content) {
        final AlertDialog dialog = new AlertDialog.Builder(this, R.style.Custom_Dialog).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_coerce_update);
        TextView tvContent = (TextView) window.findViewById(R.id.tv_content);
        TextView btnUpdate = (TextView) window.findViewById(R.id.btn_update);
        tvContent.setText(content);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDownloadProgress();// 显示进度
                new Thread() {
                    @Override
                    public void run() {
                        downloadNewApp();
                    }
                }.start();
                dialog.dismiss();
            }
        });
    }

    // 提示更新对话框
    private void remindUpdateDialog(String content) {
        final AlertDialog dialog = new AlertDialog.Builder(this, R.style.Custom_Dialog).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_remind_update);
        TextView tvContent = (TextView) window.findViewById(R.id.tv_content);
        TextView btnUpdate = (TextView) window.findViewById(R.id.btn_update);
        TextView btnGiveup = (TextView) window.findViewById(R.id.btn_giveup);
        tvContent.setText(content);
        btnGiveup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDownloadProgress();// 显示进度
                new Thread() {
                    @Override
                    public void run() {
                        downloadNewApp();
                    }
                }.start();
                dialog.dismiss();
            }
        });
    }

    // 显示更新进度对话框
    private void showDownloadProgress() {
        final AlertDialog dialog = new AlertDialog.Builder(this, R.style.Custom_Dialog).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_update_progress);
        progressBar = (NumberProgressBar) window.findViewById(R.id.progress_bar);
        TextView btnBackstage = (TextView) window.findViewById(R.id.btn_backstage);
        progressBar.setMax(100);
        btnBackstage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownloadAppService.class);
                startService(intent);
                dialog.dismiss();
            }
        });
    }

    // 下载新版APP
    private void downloadNewApp() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 有读写权限
            Log.i("更新地址", updateData.getUrl());
            Request request = new Request.Builder().url(updateData.getUrl()).build();
            try {
                Response response = MyApplication.getInstance().okHttpClient.newCall(request).execute();
                InputStream inputStream = response.body().byteStream();
                float size = response.body().contentLength();
                File file = new File(Environment.getExternalStorageDirectory(), "jinzht_new.apk");
                FileOutputStream fos = new FileOutputStream(file);
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                byte[] bytes = new byte[1024];
                int length = -1;
                float count = 0;
                while ((length = bis.read(bytes)) != -1) {
                    fos.write(bytes, 0, length);
                    count += length;
                    sendMsg(0, (int) (count * 100 / size));// 正在下载
                }
                sendMsg(1, 0);
                fos.close();
                bis.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                sendMsg(-1, 0);
            }
        }
    }

    // 更新时handler发送msg，0是进度，1是成功，-1是失败
    private void sendMsg(int flag, int progress) {
        Message msg = Message.obtain();
        msg.what = flag;
        msg.arg1 = progress;
        handler.sendMessage(msg);
    }

    // 显示下载进度，成功后直接安装，失败则提示
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 0:// 正在下载，显示进度
                        downloading_progress = msg.arg1;
                        progressBar.setProgress(downloading_progress);
                        break;
                    case 1:// 下载成功，安装
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "jinzht_new.apk")), "application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    case -1:// 下载失败，提示用户
                        String error = msg.getData().getString("error");
                        SuperToastUtils.showSuperToast(mContext, 2, error);
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        netHandler.removeCallbacks(runnable);
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
