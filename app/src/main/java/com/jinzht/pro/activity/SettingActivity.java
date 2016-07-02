package com.jinzht.pro.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jinzht.pro.R;
import com.jinzht.pro.application.MyApplication;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.CommonBean;
import com.jinzht.pro.bean.UpdateBean;
import com.jinzht.pro.service.DownloadAppService;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.DataCleanManager;
import com.jinzht.pro.utils.DialogUtils;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.view.numberprogressbar.NumberProgressBar;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 软件设置
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private RelativeLayout btnChangePwd;// 修改密码
    private RelativeLayout btnChangeTel;// 修改手机
    private ToggleButton toggleVoice;// 声音开关
    private ToggleButton toggleShake;// 震动开关
    private TextView btnCleanCache;// 清理缓存
    private TextView btnUpdate;// 版本更新
    private TextView btnExit;// 退出登录

    private UpdateBean.DataBean updateData;// 新版更新信息
    private NumberProgressBar progressBar;// 跟新时的进度条
    public static int downloading_progress = 0;// 下载进度

    @Override
    protected int getResourcesId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("软件设置");
        btnChangePwd = (RelativeLayout) findViewById(R.id.setting_btn_change_pwd);// 修改密码
        btnChangePwd.setOnClickListener(this);
        btnChangeTel = (RelativeLayout) findViewById(R.id.setting_btn_change_tel);// 修改手机
        btnChangeTel.setOnClickListener(this);
        toggleVoice = (ToggleButton) findViewById(R.id.setting_toggle_voice);// 声音开关
        toggleVoice.setOnCheckedChangeListener(this);
        toggleShake = (ToggleButton) findViewById(R.id.setting_toggle_shark);// 震动开关
        toggleShake.setOnCheckedChangeListener(this);
        btnCleanCache = (TextView) findViewById(R.id.setting_btn_clean_cache);// 清理缓存
        btnCleanCache.setOnClickListener(this);
        btnUpdate = (TextView) findViewById(R.id.setting_btn_update);// 版本更新
        btnUpdate.setOnClickListener(this);
        btnExit = (TextView) findViewById(R.id.setting_btn_exit);// 退出登录
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.setting_btn_change_pwd:// 修改密码
                Intent intent = new Intent(this, ChangePwdActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_btn_change_tel:// 修改手机号码
                Intent intent1 = new Intent(this, ChangeTelActivity.class);
                startActivity(intent1);
                break;
            case R.id.setting_btn_clean_cache:// 清理缓存
                DataCleanManager.cleanCache(mContext);
                DialogUtils.confirmDialog(this, "缓存清理成功!", "确定");
                break;
            case R.id.setting_btn_update:// 版本更新
                UpdateTask updateTask = new UpdateTask();
                updateTask.execute();
                break;
            case R.id.setting_btn_exit:// 退出登录
                LogOutTask logOutTask = new LogOutTask();
                logOutTask.execute();
                break;
        }
    }

    // 开关控件
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == toggleVoice) {
            if (isChecked) {
//                SuperToastUtils.showSuperToast(this, 2, "声音开");
            } else {
//                SuperToastUtils.showSuperToast(this, 2, "声音关");
            }
        }
        if (buttonView == toggleShake) {
            if (isChecked) {
//                SuperToastUtils.showSuperToast(this, 2, "震动开");
            } else {
//                SuperToastUtils.showSuperToast(this, 2, "震动关");
            }
        }

    }

    // 注销登录
    private class LogOutTask extends AsyncTask<Void, Void, CommonBean> {
        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.LOGOUT)),
                            Constant.BASE_URL + Constant.LOGOUT,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("注销登录", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            if (commonBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (commonBean.getStatus() == 200) {
                    SharedPreferencesUtils.saveInformation(mContext, "", "");
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                    MyApplication.getInstance().exit();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, commonBean.getMessage());
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
            if (updateBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (updateBean.getStatus() == 200) {
                    updateData = updateBean.getData();
                    try {
                        if (!StringUtils.isEquals(UiHelp.getVersionName(SettingActivity.this), updateData.getVersionStr())) {
                            if (updateData.isForce()) {
                                // 强制更新
                                coerceUpdateDialog(updateData.getContent());
                            } else {
                                // 提示更新
                                remindUpdateDialog(updateData.getContent());
                            }
                        } else {
                            DialogUtils.confirmDialog(SettingActivity.this, "已是最新版本", "确定");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, updateBean.getMessage());
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
                Intent intent = new Intent(SettingActivity.this, DownloadAppService.class);
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

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
