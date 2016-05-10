package com.jinzht.pro1.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.jinzht.pro1.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.user_loading)
                .showImageOnFail(R.mipmap.user_loadingfail)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片解码类型，默认是ARGB_8888，使用RGB_565会比使用ARGB_8888少消耗2倍的内
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2)// 线程优先级
                .denyCacheImageMultipleSizesInMemory()// 强制UIL在内存中不能存储内容相同但大小不同的图像。
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())// 使用MD5加密命名
                .tasksProcessingOrder(QueueProcessingType.LIFO)// 设置图片下载和显示的工作队列排序，LIFO后进先出，FIFO先进先出
                .build();
        ImageLoader.getInstance().init(config);
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
