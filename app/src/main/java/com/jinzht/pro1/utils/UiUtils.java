package com.jinzht.pro1.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;

import com.jinzht.pro1.application.MyApplication;

/**
 * 提供ui操作的工具类
 */
public class UiUtils {

    // 返回context对象
    public static Context getContext() {
        return MyApplication.getContext();
    }

    // 返回主线程的handler
    public static Handler getHandler() {
        return MyApplication.getHandler();
    }

    // 得到主线程的id
    public static int getMainThreadId() {
        return MyApplication.getMainThreadId();
    }

    // 得到字符串资源
    public static String getString(int id) {
        return getContext().getResources().getString(id);
    }

    // 得到字符串数组
    public static String[] getStringArray(int id) {
        return getContext().getResources().getStringArray(id);
    }

    // 得到图片
    public static Drawable getDrawable(int id) {
        return getContext().getResources().getDrawable(id);
    }

    // 获取到颜色
    public static int getColor(int id) {
        return getContext().getResources().getColor(id);
    }

    // 获取颜色的状态选择器
    public static ColorStateList getColorStateList(int id) {
        return getContext().getResources().getColorStateList(id);
    }

    // dimen
    public static int getDimen(int id) {
        return getContext().getResources().getDimensionPixelSize(id);
    }

    // dp--px
    public static int dip2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);// 加上0.5 为了四舍五入
    }

    // 判断当前线程是否运行在主线程当中
    public static boolean isRunOnUiThread() {
        // 得到当前的线程，跟主线程进行比较
        int currentThreadId = android.os.Process.myTid();
        return currentThreadId == getMainThreadId();
    }

    // 保证传递进来的r一定是在主线程中运行
    public static void runOnUiThread(Runnable r) {
        if (isRunOnUiThread()) {
            r.run();//
        } else {
            getHandler().post(r);// 将r放到handler所在的那个线程进行处理
        }
    }

    public static View inflateView(int layoutId) {
        return View.inflate(getContext(), layoutId, null);
    }

    //对应的xml文件当中的shape标签
    public static GradientDrawable getGradientDrawable(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radius);
        drawable.setGradientType(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        return drawable;
    }

    //对应的是xml文件当中的selector标签
    public static StateListDrawable getStateListDrawable(
            Drawable pressedDrawable, Drawable normalDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        // 添加规则
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed},
                pressedDrawable);
        stateListDrawable.addState(new int[]{}, normalDrawable);

        return stateListDrawable;
    }

    // 重置图片尺寸
    public static Bitmap resizeBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) w) / width;
        float scaleheight = ((float) h) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleheight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    // 剪裁图片
    public static void crop(Uri uri, int scaleX, int scaleY, int sizeX, int sizeY, Activity activity, int requestCode) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", scaleX);
        intent.putExtra("aspectY", scaleY);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", sizeX);
        intent.putExtra("outputY", sizeY);
        intent.putExtra("scale", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        activity.startActivityForResult(intent, requestCode);
    }
}
