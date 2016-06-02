package com.jinzht.pro1.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 配置文件
 */
public class SharePreferencesUtils {

    // 保存session
    public static void saveSession(Context context, String session) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("session", session);
        editor.commit();
    }

    // 获取session
    public static String getSession(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sp.getString("session", "");
    }

    // 保存用户名电话和密码
    public static void saveInformation(Context context, String telephone, String passwd) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("telephone", telephone);
        editor.putString("password", passwd);
        editor.commit();
    }

    // 获取用户名电话
    public static String getTelephone(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sp.getString("telephone", "");
    }

    // 获取密码
    public static String getPassword(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sp.getString("password", "");
    }

    // 保存是否登录的状态
    public static void setIsLogin(Context context, boolean isAuto) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("islogin", isAuto);
        editor.commit();
    }

    // 获取是否登录的状态
    public static boolean getIsLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sp.getBoolean("islogin", false);
    }

    // 保存是否实名认证的状态
    public static void setAuth(Context context, boolean isAuto) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isAuth", isAuto);
        editor.commit();
    }

    // 获取是否实名认证的状态
    public static boolean getAuth(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sp.getBoolean("isAuth", false);
    }

    public static void setPerfectInformation(Context context, boolean isAuto) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isPerfect", isAuto);
        editor.commit();
    }

    public static boolean getPerfectInformation(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sp.getBoolean("isPerfect", false);
    }
}
