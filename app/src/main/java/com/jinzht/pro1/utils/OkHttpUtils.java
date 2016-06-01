package com.jinzht.pro1.utils;

import android.content.Context;
import android.util.Log;

import com.jinzht.pro1.application.MyApplication;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * OkHttp访问网络工具类
 */
public class OkHttpUtils {

    // 登录
    public static String loginPost(String str1, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String url, Context context) throws IOException {
        String body = "";
        RequestBody formBody = new FormEncodingBuilder()
                .add(str1, str2)
                .add(str3, str4)
                .add(str5, str6)
                .add(str7, str8)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = MyApplication.getInstance().okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            body = response.body().string();
            try {
                if (StringUtils.isBlank(response.header("Set-Cookie").toString())) {
                    SuperToastUtils.showSuperToast(context, 2, "session不存在");
                } else {
                    Log.e("session", response.header("Set-Cookie").toString());
                    SharePreferencesUtils.saveSession(context, response.header("Set-Cookie").toString().split(";")[0]);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            throw new IOException("Unexpected code " + response);
        }
        return body;
    }

    // 微信登录
    public static String wechatLoginPost(String partner, String key, String value, int platform, String url, Context context) throws IOException {
        String body = "";
        RequestBody formBody = new FormEncodingBuilder()
                .add("Key", "jinzht_server_security")
                .add("partner", partner)
                .add(key, value)
                .add("platform", String.valueOf(platform))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = MyApplication.getInstance().okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            body = response.body().string();
            try {
                if (StringUtils.isBlank(response.header("Set-Cookie").toString())) {
                    SuperToastUtils.showSuperToast(context, 2, "session不存在");
                } else {
                    Log.e("session", response.header("Set-Cookie").toString());
                    SharePreferencesUtils.saveSession(context, response.header("Set-Cookie").toString().split(";")[0]);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            throw new IOException("Unexpected code " + response);
        }
        return body;
    }
}
