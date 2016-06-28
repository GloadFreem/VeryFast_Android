package com.jinzht.pro.base;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.bean.YeepayCallBackBean;
import com.jinzht.pro.bean.YeepaySignBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.thoughtworks.xstream.XStream;

/**
 * 易宝支付统一WebView
 */
public abstract class YeepayWebViewActivity extends BaseActivity {

    protected LinearLayout btnBack;// 返回
    protected TextView tvTitle;// 标题
    protected ProgressBar progressBar;// 进度条
    protected WebView webview;// 网页

    protected XStream xStream;
    protected String request;// 请求参数
    protected String sign;// 签名
    protected String backSign;// 返回签名
    protected YeepayCallBackBean callBackBean;// 返回信息

    @Override
    protected int getResourcesId() {
        return R.layout.activity_yibao_web_view;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
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
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);// 进度条
        webview = (WebView) findViewById(R.id.webview);// 网页

        setWebTitle();

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);// 支持JavaScript
        webSettings.setSaveFormData(false);// 不保存表单
        webSettings.setAppCacheEnabled(true);// 启用缓存
        webview.setWebViewClient(new MyWebViewClient());
        webview.setWebChromeClient(new MyWebChromeClient());
        webview.addJavascriptInterface(getHtmlObject(), "fromJS");// fromJS是给js识别的名称
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

        xStream = new XStream();

        getRequest();
    }

    protected abstract void setWebTitle();// 设置页面标题

    protected abstract void getRequest();// 获取请求参数

    // 获取签名
    public class GetSignTask extends AsyncTask<Void, Void, YeepaySignBean> {
        @Override
        protected YeepaySignBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SIGN)),
                            "method", "sign",
                            "req", request,
                            "sign", "",
                            "type", "0",
                            Constant.BASE_URL + Constant.SIGN,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("获取签名", body);
                return FastJsonTools.getBean(body, YeepaySignBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(YeepaySignBean yeepaySignBean) {
            super.onPostExecute(yeepaySignBean);
            if (yeepaySignBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (yeepaySignBean.getStatus() == 200) {
                    sign = yeepaySignBean.getData().getSign();
                    loadUrl();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, yeepaySignBean.getMessage());
                }
            }
        }
    }

    protected abstract void loadUrl();

    private class MyWebViewClient extends WebViewClient {
        // 让webview自己处理后面的URL
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.i("要开始的URL", url);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (url.endsWith("swiftRecharge") || url.endsWith("swiftBindCardAndRecharge")) {
                view.loadUrl("javascript:submit()");
            }
            super.onPageFinished(view, url);
        }

        //        @Override
//        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//            super.onReceivedError(view, errorCode, description, failingUrl);
//            String imgPath = "file:///android_asset/error_pages.png";
//            String data = "<HTML><Div align=\"center\"  margin=\"0px\"><IMG src=\"" + imgPath + "\" margin=\"0px\"/></Div>";
//            view.loadDataWithBaseURL(imgPath, data, "text/html", "utf-8", null);
//        }
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

    // 让JS调用Java方法
    private Object getHtmlObject() {
        Object insertObj = new Object() {
            @JavascriptInterface
            public void getResp(final String respResult, final String signResult) {
                Log.i("返回的XML", respResult);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        saveResult(respResult);
                        saveSign(signResult);
                    }
                });
            }
        };
        return insertObj;
    }

    protected void saveResult(String respResult) {
        xStream.processAnnotations(YeepayCallBackBean.class);// 指定要解析到的Bean
        if (respResult != null) {
            respResult = respResult.replaceAll("&lt;", "<" + "");
            respResult = respResult.replaceAll("&quot;", "\"" + "");
            respResult = respResult.replaceAll("&gt;", ">" + "");
            Log.i("返回参数", respResult);
            callBackBean = (YeepayCallBackBean) xStream.fromXML(respResult);
        }
        if (callBackBean != null) {
            Log.i("返回参数Bean", callBackBean.toString());
        }
    }

    protected abstract void saveSign(String signResult);

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
