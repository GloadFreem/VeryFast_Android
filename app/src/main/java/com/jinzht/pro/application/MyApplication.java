package com.jinzht.pro.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.jinzht.pro.view.GlideImageLoader;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义MyApplication，初始化一些东西
 */
public class MyApplication extends Application {

    private static MyApplication instance;// 单例模式
    private static Context context;
    private static Handler handler;
    private static int mainThreadId;

    public List<Activity> activityList = new ArrayList<Activity>();// 退出时清除所有Activity，避免内存溢出

    public OkHttpClient okHttpClient = new OkHttpClient();// 初始化OkHttpClient

    // 单例化
    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //context经常使用到。Toast、new View
        context = getApplicationContext();
        //得到主线程的handler对象，维护的是主线程的MessageQueue
        handler = new Handler();
        //哪个方法调用了myTid，myTid返回的就是那个方法所在的线程id
        mainThreadId = android.os.Process.myTid();

        JPushInterface.setDebugMode(true);// 设置开启日志，发布时需关闭
        JPushInterface.init(this);// 初始化

        okHttpClient.setConnectTimeout(5, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(5, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);

        // 配置图片选择器
        ThemeConfig theme = ThemeConfig.DARK;
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)// 开启相机
                .setEnableEdit(false)// 关闭编辑
                .setEnableCrop(false)// 关闭剪裁
                .setEnableRotate(false)// 关闭旋转
                .setCropSquare(false)// 剪裁正方形
                .setEnablePreview(false)// 开启预览
                .setMutiSelectMaxSize(9)// 最多9张
                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, new GlideImageLoader(), theme)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);

    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }

    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }
}
