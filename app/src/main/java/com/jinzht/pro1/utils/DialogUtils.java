package com.jinzht.pro1.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jinzht.pro1.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 各种弹框
 */
public class DialogUtils {

    // 活动列表中立即点击立即报名弹窗
    public static void activityApplyDialog(final Activity activity) {
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setView(new EditText(activity));
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_activity_apply);
        RelativeLayout bgEdt = (RelativeLayout) window.findViewById(R.id.dialog_activity_bg_edt);
        final EditText edt = (EditText) window.findViewById(R.id.dialog_activity_edt);
        Button btnCancel = (Button) window.findViewById(R.id.dialog_activity_btn_cancel);
        Button btnConfirm = (Button) window.findViewById(R.id.dialog_activity_btn_confirm);
        // dialog弹出后自动弹出键盘
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               imm.showSoftInput(edt, 0);
                           }
                       },
                100);
        bgEdt.setOnClickListener(new View.OnClickListener() {// 点击输入框背景弹出键盘
            @Override
            public void onClick(View v) {
                imm.showSoftInput(edt, 0);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {// 取消
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {// 确认报名
            @Override
            public void onClick(View v) {
                SuperToastUtils.showSuperToast(activity, 2, "已报名");
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
            }
        });
    }

    // 项目中心中投资人忽略收到的项目是弹窗
    public static void ingorePro(final Activity activity) {
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_ingore_project);
        ImageView btnIngore = (ImageView) window.findViewById(R.id.dialog_ingore_pro_btn_ingore);
        ImageView btnLook = (ImageView) window.findViewById(R.id.dialog_ingore_pro_btn_look);
        btnIngore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuperToastUtils.showSuperToast(activity, 2, "忽略");
            }
        });
        btnLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuperToastUtils.showSuperToast(activity, 2, "看一下");
            }
        });
    }

    // 弹出金条下落动画
    public static void goldAnim(final Activity activity) {
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        window.setContentView(R.layout.activity_gold_anim);
        ImageView gold = (ImageView) window.findViewById(R.id.iv_goad);
        Button btnConfirm = (Button) window.findViewById(R.id.btn_confirm);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(gold, "y", 0, 850f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(gold, "scaleX", 0, 1.0f);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(gold, "scaleY", 0, 1.0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(2000);
        animSet.setInterpolator(new LinearInterpolator());
        animSet.playTogether(anim1, anim2, anim3);
        animSet.start();
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
