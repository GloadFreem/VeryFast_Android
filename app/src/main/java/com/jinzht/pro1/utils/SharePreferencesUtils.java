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
        SharedPreferences mySharedPreferences = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("session", session);
        editor.commit();
    }

    // 获取session
    public static String getSession(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("session", "");
    }

    // 保存用户名电话和密码
    public static void saveInformation(Context context, String telephone, String passwd) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("telephone", telephone);
        editor.putString("password", passwd);
        editor.commit();
    }

    // 获取用户名电话
    public static String getTelephone(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("telephone", "");
    }

    // 获取密码
    public static String getPassword(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("password", "");
    }

    // 保存是否登录的状态
    public static void setIsLogin(Context context, boolean isAuto) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("islogin", isAuto);
        editor.commit();
    }

    // 获取是否登录的状态
    public static boolean getIsLogin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sharedPreferences.getBoolean("islogin", false);
    }

    // 保存是否实名认证的状态
    public static void setAuth(Context context, String isAuto) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("auth", isAuto);
        editor.commit();
    }

    // 获取是否实名认证的状态
    public static String getAuth(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("auth", "");
    }
}
