package com.jinzht.pro1.utils;

import android.content.Context;
import android.util.Log;

import com.jinzht.pro1.application.MyApplication;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

/**
 * OkHttp访问网络工具类
 */
public class OkHttpUtils {

    // 登录
    public static String loginPost(String partner, String key1, String value1, String key2, String value2, String url, Context context) throws IOException {
        String body = "";
        RequestBody formBody = new FormEncodingBuilder()
                .add("key", "jinzht_server_security")
                .add("partner", partner)
                .add(key1, value1)
                .add(key2, value2)
                .add("platform", String.valueOf(0))
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
                    Log.i("session", "session不存在");
                } else {
                    Log.i("session", response.header("Set-Cookie").toString());
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
    public static String wechatLoginPost(String partner, String key, String value, String url, Context context) throws IOException {
        String body = "";
        RequestBody formBody = new FormEncodingBuilder()
                .add("key", "jinzht_server_security")
                .add("partner", partner)
                .add(key, value)
                .add("platform", String.valueOf(0))
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
                    Log.i("session", "session不存在");
                } else {
                    Log.i("session", response.header("Set-Cookie").toString());
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

    // 注册
    public static String registerPost(String partner, String key1, String value1, String key2, String value2, String key3, String value3, String key4, String value4, String url, Context context) throws IOException {
        String body = "";
        RequestBody formBody = new FormEncodingBuilder()
                .add("key", "jinzht_server_security")
                .add("partner", partner)
                .add(key1, value1)
                .add(key2, value2)
                .add(key3, value3)
                .add(key4, value4)
                .add("platform", String.valueOf(0))
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
                    Log.i("session", "session不存在");
                } else {
                    Log.i("session", response.header("Set-Cookie").toString());
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

    // 两个参数的请求
    public static String post(String partner, String key1, String value1, String key2, String value2, String url, Context context) throws IOException {
        String body = "";
        RequestBody formBody = new FormEncodingBuilder()
                .add("key", "jinzht_server_security")
                .add("partner", partner)
                .add(key1, value1)
                .add(key2, value2)
                .add("platform", String.valueOf(0))
                .build();
        Request request = new Request.Builder()
                .addHeader("Cookie", SharePreferencesUtils.getSession(context))
                .url(url)
                .post(formBody)
                .build();
        Response response = MyApplication.getInstance().okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            body = response.body().string();
            try {
                if (StringUtils.isBlank(response.header("Set-Cookie").toString())) {
                    Log.i("session", "session不存在");
                } else {
                    Log.i("session", response.header("Set-Cookie").toString());
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

    // 只传入partner的请求
    public static String post(String partner, String url, Context context) throws IOException {
        String body = "";
        RequestBody formBody = new FormEncodingBuilder()
                .add("key", "jinzht_server_security")
                .add("partner", partner)
                .add("platform", String.valueOf(0))
                .build();
        Request request = new Request.Builder()
                .addHeader("Cookie", SharePreferencesUtils.getSession(context))
                .url(url)
                .post(formBody)
                .build();
        Response response = MyApplication.getInstance().okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            body = response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
        return body;
    }

    // 一个参数的请求
    public static String post(String partner, String key1, String value1, String url, Context context) throws IOException {
        String body = "";
        RequestBody formBody = new FormEncodingBuilder()
                .add("key", "jinzht_server_security")
                .add("partner", partner)
                .add(key1, value1)
                .add("platform", String.valueOf(0))
                .build();
        Request request = new Request.Builder()
                .addHeader("Cookie", SharePreferencesUtils.getSession(context))
                .url(url)
                .post(formBody)
                .build();
        Response response = MyApplication.getInstance().okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            body = response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
        return body;
    }

    // 重置密码
    public static String resetPwdPost(String partner, String key1, String value1, String key2, String value2, String key3, String value3, String url, Context context) throws IOException {
        String body = "";
        RequestBody formBody = new FormEncodingBuilder()
                .add("key", "jinzht_server_security")
                .add("partner", partner)
                .add(key1, value1)
                .add(key2, value2)
                .add(key3, value3)
                .add("platform", String.valueOf(0))
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
                    Log.i("session", "session不存在");
                } else {
                    Log.i("session", response.header("Set-Cookie").toString());
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

    // 选择投资人类型
    public static String usertypePost(String partner, String key1, String value1, String key2, String value2, String url, Context context) throws IOException {
        String body = "";
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        builder.addFormDataPart("key", "jinzht_server_security");
        builder.addFormDataPart("partner", partner);
        builder.addFormDataPart(key1, value1);
        builder.addFormDataPart(key2, key2 + ".jpg", RequestBody.create(MultipartBuilder.FORM, new File(value2)));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", SharePreferencesUtils.getSession(context))
                .post(requestBody)
                .build();
        Response response = MyApplication.getInstance().okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            body = response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
        return body;
    }

    // 选择投资人类型，不上传头像
    public static String usertypePost(String partner, String key1, String value1, String url, Context context) throws IOException {
        String body = "";
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        builder.addFormDataPart("key", "jinzht_server_security");
        builder.addFormDataPart("partner", partner);
        builder.addFormDataPart(key1, value1);
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", SharePreferencesUtils.getSession(context))
                .post(requestBody)
                .build();
        Response response = MyApplication.getInstance().okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            body = response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
        return body;
    }
}
