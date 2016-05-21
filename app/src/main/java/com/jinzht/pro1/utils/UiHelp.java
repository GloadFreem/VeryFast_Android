package com.jinzht.pro1.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.view.SystemBarTintManager;

import java.util.List;

/**
 * UI线程的工具类
 */
public class UiHelp {

    //判断应用是否位于堆栈的顶层，接收到极光推送的消息时用到
    public static boolean isTopActivity(Context context) {
        String packageName = "com.jinzht.pro";
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    // 打开金指投应用，接收到极光推送的消息时用到
    public static void openJinZht(Context context, String packageName) {
        try {
            Intent intent = new Intent();
            PackageManager packageManager = context.getPackageManager();
            intent = packageManager.getLaunchIntentForPackage(packageName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }

    // 设置系统状态栏与应用标题栏背景一致
    @TargetApi(19)
    public static void setSameStatus(boolean on, Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = context.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
            // 创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(context);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            // 激活导航栏设置就是华为p6底下的导航栏
            tintManager.setNavigationBarTintEnabled(true);
            // 设置一个颜色给系统栏
            tintManager.setTintColor(context.getResources().getColor(R.color.title_bg));
            // 设置一个样式背景给导航栏
            tintManager.setNavigationBarTintResource(R.color.title_bg);
            // 设置一个状态栏资源
//        tintManager.setStatusBarTintDrawable(MyDrawable);
        }
    }

    // 设置系统状态栏跟随应用背景
    @TargetApi(19)
    public static void setFullScreenStatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    // 根据返回码弹出相应的toast
    public static void printMsg(int code, Object object, Context context) {
        switch (code) {
            case 0:
                if (object != null) {
                    SuperToastUtils.showSuperToast(context, 2, object + "");
                }
                break;
            case 1:
                if (object != null) {
                    SuperToastUtils.showSuperToast(context, 2, object + "");
                }
                break;
        }
    }

    // 解决ListView嵌套在ScrollView中只显示一行数据的问题
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
