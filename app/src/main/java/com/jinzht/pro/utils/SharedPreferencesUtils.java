package com.jinzht.pro.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 配置文件
 */
public class SharedPreferencesUtils {

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

    // 保存用户名(即电话号码)和密码
    public static void saveInformation(Context context, String telephone, String passwd) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("telephone", telephone);
        editor.putString("password", passwd);
        editor.commit();
    }

    // 获取用户电话号码
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
        editor.putBoolean("isLogin", isAuto);
        editor.commit();
    }

    // 获取是否登录的状态
    public static boolean getIsLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sp.getBoolean("isLogin", false);
    }

    // 保存是否微信登录
    public static void setIsWechatLogin(Context context, boolean isAuto) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isWechatLogin", isAuto);
        editor.commit();
    }

    // 获取是否微信登录
    public static boolean getIsWechatLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sp.getBoolean("isWechatLogin", false);
    }

    // 保存微信昵称
    public static void saveWechatNick(Context context, String nick) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("nick", nick);
        editor.commit();
    }

    // 获取微信昵称
    public static String getWechatNick(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sp.getString("nick", "");
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

    // 保存是否选择身份
    public static void setChoseUserType(Context context, boolean isAuto) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("choseUsertype", isAuto);
        editor.commit();
    }

    // 获取是否选择身份
    public static boolean getChoseUserType(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sp.getBoolean("choseUsertype", false);
    }

    // 保存用户身份类型
    public static void saveUserType(Context context, int usertype) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("usertype", usertype);
        editor.commit();
    }

    // 获取用户身份类型
    public static int getUserType(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sp.getInt("usertype", -1);
    }

    // 保存是否是首次使用应用
    public static void setIsNotFirst(Context context, boolean isAuto) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isNotFirst", isAuto);
        editor.commit();
    }

    // 获取是否是首次使用应用
    public static boolean getIsNotFirst(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sp.getBoolean("isNotFirst", false);
    }

    // 保存extUserId，仅作为支付时使用
    public static void saveExtUserId(Context context, String userId) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("extUserId", userId);
        editor.commit();
    }

    // 获取extUserId，仅作为支付时使用
    public static String getExtUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sp.getString("extUserId", "");
    }

    // 保存userId，仅作为支付时使用
    public static void saveUserId(Context context, String userId) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userId", userId);
        editor.commit();
    }

    // 获取userId，仅作为支付时使用
    public static String getUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sp.getString("userId", "");
    }

    // 保存本地用户头像
    public static void saveLocalFavicon(Context context, String favicon) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("localFavicon", favicon);
        editor.commit();
    }

    // 获取本地用户头像
    public static String getLocalFavicon(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sp.getString("localFavicon", "");
    }

    // 保存用网络户头像
    public static void saveOnlineFavicon(Context context, String favicon) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("onlineFavicon", favicon);
        editor.commit();
    }

    // 获取网络用户头像
    public static String getOnlineFavicon(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sp.getString("onlineFavicon", "");
    }

    // 保存当日登录日期
    public static void saveTodayFirstDate(Context context, String date) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("todayDate", date);
        editor.commit();
    }

    // 获取当日登录日期
    public static String getTodayFirstDate(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userconfs", Activity.MODE_PRIVATE);
        return sp.getString("todayDate", "");
    }
}
