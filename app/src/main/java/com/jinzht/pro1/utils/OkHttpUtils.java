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
import java.util.List;

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

    // 4个参数的请求
    public static String post(String partner, String key1, String value1, String key2, String value2, String key3, String value3, String key4, String value4, String url, Context context) throws IOException {
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

    // 选择投资人类型
    public static String usertypePost(String partner, String key1, String value1, String key2, String value2, String key3, String value3, String url, Context context) throws IOException {
        String body = "";
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        builder.addFormDataPart("key", "jinzht_server_security");
        builder.addFormDataPart("partner", partner);
        builder.addFormDataPart(key1, value1);
        builder.addFormDataPart(key2, value2);
        builder.addFormDataPart(key3, key3 + ".jpg", RequestBody.create(MultipartBuilder.FORM, new File(value3)));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Cookie", SharePreferencesUtils.getSession(context))
                .url(url)
                .post(requestBody)
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

    // 选择投资人类型，不上传头像
    public static String usertypePost(String partner, String key1, String value1, String key2, String value2, String url, Context context) throws IOException {
        String body = "";
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        builder.addFormDataPart("key", "jinzht_server_security");
        builder.addFormDataPart("partner", partner);
        builder.addFormDataPart(key1, value1);
        builder.addFormDataPart(key2, value2);
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Cookie", SharePreferencesUtils.getSession(context))
                .url(url)
                .post(requestBody)
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

    // 认证
    public static String authenticate(String partner, String key1, String value1, String key2, String value2, String key3, String value3, String key4, String value4, String key5, String value5, String key6, String value6, String key7, String value7, String key8, String value8, String key9, String value9, String key10, String value10, String key11, String value11, String key12, String value12, String key13, String value13, String key14, String value14, String url, Context context) throws IOException {
        String body = "";
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        builder.addFormDataPart("key", "jinzht_server_security");
        builder.addFormDataPart("partner", partner);
        builder.addFormDataPart(key1, value1);
        builder.addFormDataPart(key2, key2 + ".jpg", RequestBody.create(MultipartBuilder.FORM, new File(value2)));
        builder.addFormDataPart(key3, key3 + ".jpg", RequestBody.create(MultipartBuilder.FORM, new File(value3)));
        builder.addFormDataPart(key4, value4);
        builder.addFormDataPart(key5, value5);
        builder.addFormDataPart(key6, value6);
        builder.addFormDataPart(key7, value7);
        builder.addFormDataPart(key8, value8);
        builder.addFormDataPart(key9, value9);
        builder.addFormDataPart(key10, key10 + ".jpg", RequestBody.create(MultipartBuilder.FORM, new File(value10)));
        builder.addFormDataPart(key11, value11);
        builder.addFormDataPart(key12, value12);
        builder.addFormDataPart(key13, value13);
        builder.addFormDataPart(key14, value14);
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Cookie", SharePreferencesUtils.getSession(context))
                .url(url)
                .post(requestBody)
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

    // 认证不上传营业执照
    public static String authenticate(String partner, String key1, String value1, String key2, String value2, String key3, String value3, String key4, String value4, String key5, String value5, String key6, String value6, String key7, String value7, String key8, String value8, String key9, String value9, String key12, String value12, String key13, String value13, String key14, String value14, String url, Context context) throws IOException {
        String body = "";
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        builder.addFormDataPart("key", "jinzht_server_security");
        builder.addFormDataPart("partner", partner);
        builder.addFormDataPart(key1, value1);
        builder.addFormDataPart(key2, key2 + ".jpg", RequestBody.create(MultipartBuilder.FORM, new File(value2)));
        builder.addFormDataPart(key3, key3 + ".jpg", RequestBody.create(MultipartBuilder.FORM, new File(value3)));
        builder.addFormDataPart(key4, value4);
        builder.addFormDataPart(key5, value5);
        builder.addFormDataPart(key6, value6);
        builder.addFormDataPart(key7, value7);
        builder.addFormDataPart(key8, value8);
        builder.addFormDataPart(key9, value9);
        builder.addFormDataPart(key12, value12);
        builder.addFormDataPart(key13, value13);
        builder.addFormDataPart(key14, value14);
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Cookie", SharePreferencesUtils.getSession(context))
                .url(url)
                .post(requestBody)
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

    // 圈子发表动态
    public static String releaseCirclePost(String partner, String key1, String value1, String key2, List<String> photos, String url, Context context) throws IOException {
        String body = "";
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        builder.addFormDataPart("key", "jinzht_server_security");
        builder.addFormDataPart("partner", partner);
        builder.addFormDataPart(key1, value1);
        if (photos != null && photos.size() != 0) {
            for (int i = 0; i < photos.size(); i++) {
                builder.addFormDataPart(key2, key2 + ".jpg", RequestBody.create(MultipartBuilder.FORM, new File(photos.get(i))));
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Cookie", SharePreferencesUtils.getSession(context))
                .url(url)
                .post(requestBody)
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
}
