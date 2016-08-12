package com.jinzht.pro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.WebBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.utils.UiUtils;

/**
 * 关于平台界面
 */
public class AboutUsActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private TextView tvVerson;// 版本号
    private RelativeLayout btnIntroduce;// 平台介绍
    private RelativeLayout btnGudie;// 新手指南
    private RelativeLayout btnAgreement;// 用户协议
    private RelativeLayout btnStatement;// 免责声明
    private RelativeLayout btnFeedback;// 意见反馈

    @Override
    protected int getResourcesId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("关于平台");
        tvVerson = (TextView) findViewById(R.id.about_us_tv_verson);// 版本号
        btnIntroduce = (RelativeLayout) findViewById(R.id.about_us_btn_introduce);// 平台介绍
        btnIntroduce.setOnClickListener(this);
        btnGudie = (RelativeLayout) findViewById(R.id.about_us_btn_gudie);// 新手指南
        btnGudie.setOnClickListener(this);
        btnAgreement = (RelativeLayout) findViewById(R.id.about_us_btn_agreement);// 用户协议
        btnAgreement.setOnClickListener(this);
        btnStatement = (RelativeLayout) findViewById(R.id.about_us_btn_statement);// 免责声明
        btnStatement.setOnClickListener(this);
        btnFeedback = (RelativeLayout) findViewById(R.id.about_us_btn_feedback);// 意见反馈
        btnFeedback.setOnClickListener(this);
        try {
            tvVerson.setText(UiUtils.getString(R.string.app_name) + UiHelp.getVersionName(this));
        } catch (Exception e) {
            tvVerson.setText(UiUtils.getString(R.string.app_name));
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
            case R.id.about_us_btn_introduce:// 进入平台介绍
                if (clickable) {
                    clickable = false;
                    WebViewTask webViewTask1 = new WebViewTask(Constant.PLATFORMINTRODUCE, "平台介绍");
                    webViewTask1.execute();
                }
                break;
            case R.id.about_us_btn_gudie:// 进入新手指南
                if (clickable) {
                    clickable = false;
                    WebViewTask webViewTask2 = new WebViewTask(Constant.NEWUSERGUIDE, "新手指南");
                    webViewTask2.execute();
                }
                break;
            case R.id.about_us_btn_agreement:// 进入用户协议
                if (clickable) {
                    clickable = false;
                    WebViewTask webViewTask3 = new WebViewTask(Constant.USERPROTOCOL, "用户协议");
                    webViewTask3.execute();
                }
                break;
            case R.id.about_us_btn_statement:// 进入免责声明
                if (clickable) {
                    clickable = false;
                    WebViewTask webViewTask4 = new WebViewTask(Constant.DISCLAIMER, "免责声明");
                    webViewTask4.execute();
                }
                break;
            case R.id.about_us_btn_feedback:// 进入意见反馈
                if (clickable) {
                    clickable = false;
                    Intent intent = new Intent(this, FeedbackActivity.class);
                    startActivity(intent);
                }
                clickable = true;
                break;
        }
    }

    // 各个webview
    private class WebViewTask extends AsyncTask<Void, Void, WebBean> {
        private String url;
        private String title;

        public WebViewTask(String url, String title) {
            this.url = url;
            this.title = title;
        }

        @Override
        protected WebBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, url)),
                            Constant.BASE_URL + url,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i(title, body);
                return FastJsonTools.getBean(body, WebBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(WebBean webBean) {
            super.onPostExecute(webBean);
            clickable = true;
            if (webBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
            } else {
                if (webBean.getStatus() == 200) {
                    if (!StringUtils.isBlank(webBean.getData().getUrl())) {
                        Intent intent = new Intent(mContext, CommonWebViewActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("url", webBean.getData().getUrl());
                        startActivity(intent);
                    }
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, webBean.getMessage());
                }
            }
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
