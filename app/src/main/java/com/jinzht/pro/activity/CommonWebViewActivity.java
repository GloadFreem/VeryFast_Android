package com.jinzht.pro.activity;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.utils.DialogUtils;
import com.jinzht.pro.utils.ShareUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.UiHelp;

/**
 * 通用的WebView
 */
public class CommonWebViewActivity extends BaseActivity {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private LinearLayout titleBtnRight2;// 收藏按钮
    private LinearLayout btnShare;// 分享
    private ProgressBar progressBar;// 进度条
    private WebView webview;// 网页

    @Override
    protected int getResourcesId() {
        return R.layout.activity_yibao_web_view;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.title_btn_left);// 返回
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                }
            }
        });
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        if (!StringUtils.isBlank(getIntent().getStringExtra("title"))) {
            tvTitle.setText(getIntent().getStringExtra("title"));
        }
        titleBtnRight2 = (LinearLayout) findViewById(R.id.title_btn_right2);// 收藏按钮
        titleBtnRight2.setVisibility(View.GONE);
        btnShare = (LinearLayout) findViewById(R.id.title_btn_right);// 分享
        if ("banner".equals(getIntent().getStringExtra("TAG"))) {
            btnShare.setVisibility(View.VISIBLE);// banner显示分享按钮
        } else {
            btnShare.setVisibility(View.GONE);
        }
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils shareUtils = new ShareUtils(CommonWebViewActivity.this);
                DialogUtils.newShareDialog(
                        CommonWebViewActivity.this,
                        shareUtils,
                        getIntent().getStringExtra("title"),
                        getIntent().getStringExtra("content"),
                        getIntent().getStringExtra("imgUrl"),
                        getIntent().getStringExtra("url"));
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);// 进度条
        webview = (WebView) findViewById(R.id.webview);// 网页

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);// 支持JavaScript
        webSettings.setSaveFormData(false);// 不保存表单
        webSettings.setAppCacheEnabled(true);// 启用缓存
        webview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
                    webview.goBack();
                    return true;
                }
                return false;
            }
        });
        webview.setWebViewClient(new MyWebViewClient());
        webview.setWebChromeClient(new MyWebChromeClient());
        webview.loadUrl(getIntent().getStringExtra("url"));
    }

    private class MyWebViewClient extends WebViewClient {
        // 让webview自己处理后面的URL
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showProgressDialog();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dismissProgressDialog();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            view.loadUrl("file:///android_asset/error.html");
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            } else {
                if (View.INVISIBLE == progressBar.getVisibility()) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                progressBar.setProgress(newProgress);
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
