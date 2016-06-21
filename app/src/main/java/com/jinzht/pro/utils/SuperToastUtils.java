package com.jinzht.pro.utils;

import android.content.Context;
import android.view.Gravity;

import com.jinzht.pro.view.supertoasts.SuperToast;

/**
 * 弹出自定义Toast
 */
public class SuperToastUtils {

    public static void showSuperToast(Context context, int flag, String message) {
        final SuperToast superToast = new SuperToast(context);
        toastEffect(flag, superToast);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.setBackground(SuperToast.Background.ORANGE);
        superToast.setTextSize(SuperToast.TextSize.MEDIUM);
        superToast.setText(message);
        superToast.setGravity(Gravity.CENTER, 10, 50);
        superToast.show();
    }

    public static void showSuperToast(Context context, int flag, int message) {
        final SuperToast superToast = new SuperToast(context);
        toastEffect(flag, superToast);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.setBackground(SuperToast.Background.ORANGE);
        superToast.setTextSize(SuperToast.TextSize.MEDIUM);
        superToast.setText(context.getResources().getString(message));
        superToast.setGravity(Gravity.CENTER, 10, 50);
        superToast.show();
    }

    // 弹出方式
    private static void toastEffect(int flag, SuperToast superToast) {
        switch (flag) {
            case 0:
                superToast.setAnimations(SuperToast.Animations.FADE);// 淡入
                break;
            case 1:
                superToast.setAnimations(SuperToast.Animations.FLYIN);// 飞入
                break;
            case 2:
                superToast.setAnimations(SuperToast.Animations.POPUP);// 弹出
                break;
            case 3:
                superToast.setAnimations(SuperToast.Animations.SCALE);// 渐进
                break;
        }
    }
}
