package com.jinzht.pro.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.activity.CertificationIDCardActivity;
import com.jinzht.pro.activity.MainActivity;
import com.jinzht.pro.activity.WechatVerifyActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 各种弹框
 */
public class DialogUtils {
    // 活动列表中立即点击立即报名弹窗
    public static void activityApplyDialog(final Activity activity) {
        final AlertDialog dialog = new AlertDialog.Builder(activity, R.style.Custom_Dialog).create();
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

    // 项目中心中投资人忽略收到的项目时弹窗
    public static void ingorePro(final Activity activity) {
        final AlertDialog dialog = new AlertDialog.Builder(activity, R.style.Custom_Dialog).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_two_btn);
        TextView btnCancel = (TextView) window.findViewById(R.id.btn_cancel);
        TextView btnConfirm = (TextView) window.findViewById(R.id.btn_confirm);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuperToastUtils.showSuperToast(activity, 2, "忽略");
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuperToastUtils.showSuperToast(activity, 2, "看一下");
            }
        });
    }

    // 弹出金条下落动画
    public static void goldAnim(final Activity activity, int quantityToday, int quantityTomrrow) {
        final AlertDialog dialog = new AlertDialog.Builder(activity, R.style.Custom_Dialog).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        window.setContentView(R.layout.dialog_gold_anim);
        final TextView second = (TextView) window.findViewById(R.id.tv_second);// 倒计时
        TextView today = (TextView) window.findViewById(R.id.tv_today);// 今天获得金条数
        today.setText(String.valueOf(quantityToday));
        TextView tomorrow = (TextView) window.findViewById(R.id.tv_tomorrow);// 明天获得金条数
        tomorrow.setText(String.valueOf(quantityTomrrow));
        TextView btnConfirm = (TextView) window.findViewById(R.id.btn_confirm);// 确定按钮
        ImageView golds = (ImageView) window.findViewById(R.id.iv_golds);// 一堆金条
        ImageView gold1 = (ImageView) window.findViewById(R.id.iv_gold_1);// 每根金条
        ImageView gold2 = (ImageView) window.findViewById(R.id.iv_gold_2);
        ImageView gold3 = (ImageView) window.findViewById(R.id.iv_gold_3);
        ImageView gold4 = (ImageView) window.findViewById(R.id.iv_gold_4);
        ImageView gold5 = (ImageView) window.findViewById(R.id.iv_gold_5);
        ImageView gold6 = (ImageView) window.findViewById(R.id.iv_gold_6);
        ImageView gold7 = (ImageView) window.findViewById(R.id.iv_gold_7);
        ImageView gold8 = (ImageView) window.findViewById(R.id.iv_gold_8);
        ImageView gold9 = (ImageView) window.findViewById(R.id.iv_gold_9);

        // 重置一堆金条的位置
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        int y = (int) (height * 0.4) - UiUtils.dip2px(100);// 屏幕高的0.4倍-200，因为一堆金条高400px
        Log.i("控件Y值", String.valueOf(y));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, y, 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        golds.setLayoutParams(layoutParams);

        // 倒计时
        final int[] i = {3};
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                i[0]--;
                second.setText(i[0] + "s");
                handler.postDelayed(this, 1000);
                if (i[0] < 0) {
                    dialog.dismiss();
                }
            }
        };
        handler.postDelayed(runnable, 1000);

        // 一堆金条渐显动画
        ObjectAnimator goldsAnim1 = ObjectAnimator.ofFloat(golds, "alpha", 0f, 0f);
        ObjectAnimator goldsAnim2 = ObjectAnimator.ofFloat(golds, "alpha", 0f, 1f);
        goldsAnim1.setDuration(100);
        goldsAnim2.setDuration(2000);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(goldsAnim2).after(goldsAnim1);
        animSet.start();

        float h = (float) (height * 0.4);
        // 每根金条掉落动画
        ObjectAnimator gold1Anim1 = ObjectAnimator.ofFloat(gold1, "y", -200f, h);
        ObjectAnimator gold1Anim2 = ObjectAnimator.ofFloat(gold1, "scaleX", 0f, 1f);
        ObjectAnimator gold1Anim3 = ObjectAnimator.ofFloat(gold1, "scaleY", 0f, 1f);
        ObjectAnimator gold1Anim4 = ObjectAnimator.ofFloat(gold1, "alpha", 0f, 1f);
        AnimatorSet animSetGold1 = new AnimatorSet();
        animSetGold1.setDuration(500);
        animSetGold1.setInterpolator(new LinearInterpolator());
        animSetGold1.playTogether(gold1Anim1, gold1Anim2, gold1Anim3, gold1Anim4);
        animSetGold1.start();

        ObjectAnimator gold2Anim1 = ObjectAnimator.ofFloat(gold2, "y", -200f, h);
        ObjectAnimator gold2Anim2 = ObjectAnimator.ofFloat(gold2, "scaleX", 0.5f, 1f);
        ObjectAnimator gold2Anim3 = ObjectAnimator.ofFloat(gold2, "scaleY", 0.5f, 1f);
        ObjectAnimator gold2Anim4 = ObjectAnimator.ofFloat(gold2, "alpha", 0f, 1f);
        ObjectAnimator gold2Anim5 = ObjectAnimator.ofFloat(gold2, "alpha", 0f, 0f);
        gold2Anim5.setDuration(100);
        gold2Anim5.start();
        AnimatorSet animSetGold2 = new AnimatorSet();
        animSetGold2.setDuration(500);
        animSetGold2.setInterpolator(new LinearInterpolator());
        animSetGold2.playTogether(gold2Anim1, gold2Anim2, gold2Anim3, gold2Anim4);
        animSetGold2.setStartDelay(100);
        animSetGold2.start();

        ObjectAnimator gold3Anim1 = ObjectAnimator.ofFloat(gold3, "y", -200f, h);
        ObjectAnimator gold3Anim2 = ObjectAnimator.ofFloat(gold3, "scaleX", 0.5f, 1f);
        ObjectAnimator gold3Anim3 = ObjectAnimator.ofFloat(gold3, "scaleY", 0.5f, 1f);
        ObjectAnimator gold3Anim4 = ObjectAnimator.ofFloat(gold3, "alpha", 0f, 1f);
        ObjectAnimator gold3Anim5 = ObjectAnimator.ofFloat(gold3, "alpha", 0f, 0f);
        gold3Anim5.setDuration(200);
        gold3Anim5.start();
        AnimatorSet animSetGold3 = new AnimatorSet();
        animSetGold3.setDuration(500);
        animSetGold3.setInterpolator(new LinearInterpolator());
        animSetGold3.playTogether(gold3Anim1, gold3Anim2, gold3Anim3, gold3Anim4);
        animSetGold3.setStartDelay(200);
        animSetGold3.start();

        ObjectAnimator gold4Anim1 = ObjectAnimator.ofFloat(gold4, "y", -200f, h);
        ObjectAnimator gold4Anim2 = ObjectAnimator.ofFloat(gold4, "scaleX", 0.5f, 1f);
        ObjectAnimator gold4Anim3 = ObjectAnimator.ofFloat(gold4, "scaleY", 0.5f, 1f);
        ObjectAnimator gold4Anim4 = ObjectAnimator.ofFloat(gold4, "alpha", 0f, 1f);
        ObjectAnimator gold4Anim5 = ObjectAnimator.ofFloat(gold4, "alpha", 0f, 0f);
        gold4Anim5.setDuration(300);
        gold4Anim5.start();
        AnimatorSet animSetGold4 = new AnimatorSet();
        animSetGold4.setDuration(500);
        animSetGold4.setInterpolator(new LinearInterpolator());
        animSetGold4.playTogether(gold4Anim1, gold4Anim2, gold4Anim3, gold4Anim4);
        animSetGold4.setStartDelay(300);
        animSetGold4.start();

        ObjectAnimator gold5Anim1 = ObjectAnimator.ofFloat(gold5, "y", -200f, h);
        ObjectAnimator gold5Anim2 = ObjectAnimator.ofFloat(gold5, "scaleX", 0.5f, 1f);
        ObjectAnimator gold5Anim3 = ObjectAnimator.ofFloat(gold5, "scaleY", 0.5f, 1f);
        ObjectAnimator gold5Anim4 = ObjectAnimator.ofFloat(gold5, "alpha", 0f, 1f);
        ObjectAnimator gold5Anim5 = ObjectAnimator.ofFloat(gold5, "alpha", 0f, 0f);
        gold5Anim5.setDuration(400);
        gold5Anim5.start();
        AnimatorSet animSetGold5 = new AnimatorSet();
        animSetGold5.setDuration(500);
        animSetGold5.setInterpolator(new LinearInterpolator());
        animSetGold5.playTogether(gold5Anim1, gold5Anim2, gold5Anim3, gold5Anim4);
        animSetGold5.setStartDelay(400);
        animSetGold5.start();

        ObjectAnimator gold6Anim1 = ObjectAnimator.ofFloat(gold6, "y", -200f, h);
        ObjectAnimator gold6Anim2 = ObjectAnimator.ofFloat(gold6, "scaleX", 0.5f, 1f);
        ObjectAnimator gold6Anim3 = ObjectAnimator.ofFloat(gold6, "scaleY", 0.5f, 1f);
        ObjectAnimator gold6Anim4 = ObjectAnimator.ofFloat(gold6, "alpha", 0f, 1f);
        ObjectAnimator gold6Anim5 = ObjectAnimator.ofFloat(gold6, "alpha", 0f, 0f);
        gold6Anim5.setDuration(500);
        gold6Anim5.start();
        AnimatorSet animSetGold6 = new AnimatorSet();
        animSetGold6.setDuration(500);
        animSetGold6.setInterpolator(new LinearInterpolator());
        animSetGold6.playTogether(gold6Anim1, gold6Anim2, gold6Anim3, gold6Anim4);
        animSetGold6.setStartDelay(500);
        animSetGold6.start();

        ObjectAnimator gold7Anim1 = ObjectAnimator.ofFloat(gold7, "y", -200f, h);
        ObjectAnimator gold7Anim2 = ObjectAnimator.ofFloat(gold7, "scaleX", 0.5f, 1f);
        ObjectAnimator gold7Anim3 = ObjectAnimator.ofFloat(gold7, "scaleY", 0.5f, 1f);
        ObjectAnimator gold7Anim4 = ObjectAnimator.ofFloat(gold7, "alpha", 0f, 1f);
        ObjectAnimator gold7Anim5 = ObjectAnimator.ofFloat(gold7, "alpha", 0f, 0f);
        gold7Anim5.setDuration(600);
        gold7Anim5.start();
        AnimatorSet animSetGold7 = new AnimatorSet();
        animSetGold7.setDuration(500);
        animSetGold7.setInterpolator(new LinearInterpolator());
        animSetGold7.playTogether(gold7Anim1, gold7Anim2, gold7Anim3, gold7Anim4);
        animSetGold7.setStartDelay(600);
        animSetGold7.start();

        ObjectAnimator gold8Anim1 = ObjectAnimator.ofFloat(gold8, "y", -200f, h);
        ObjectAnimator gold8Anim2 = ObjectAnimator.ofFloat(gold8, "scaleX", 0.5f, 1f);
        ObjectAnimator gold8Anim3 = ObjectAnimator.ofFloat(gold8, "scaleY", 0.5f, 1f);
        ObjectAnimator gold8Anim4 = ObjectAnimator.ofFloat(gold8, "alpha", 0f, 1f);
        ObjectAnimator gold8Anim5 = ObjectAnimator.ofFloat(gold8, "alpha", 0f, 0f);
        gold8Anim5.setDuration(700);
        gold8Anim5.start();
        AnimatorSet animSetGold8 = new AnimatorSet();
        animSetGold8.setDuration(500);
        animSetGold8.setInterpolator(new LinearInterpolator());
        animSetGold8.playTogether(gold8Anim1, gold8Anim2, gold8Anim3, gold8Anim4);
        animSetGold8.setStartDelay(700);
        animSetGold8.start();

        ObjectAnimator gold9Anim1 = ObjectAnimator.ofFloat(gold9, "y", -200f, h);
        ObjectAnimator gold9Anim2 = ObjectAnimator.ofFloat(gold9, "scaleX", 0.5f, 1f);
        ObjectAnimator gold9Anim3 = ObjectAnimator.ofFloat(gold9, "scaleY", 0.5f, 1f);
        ObjectAnimator gold9Anim4 = ObjectAnimator.ofFloat(gold9, "alpha", 0f, 1f);
        ObjectAnimator gold9Anim5 = ObjectAnimator.ofFloat(gold9, "alpha", 0f, 0f);
        gold9Anim5.setDuration(800);
        gold9Anim5.start();
        AnimatorSet animSetGold9 = new AnimatorSet();
        animSetGold9.setDuration(500);
        animSetGold9.setInterpolator(new LinearInterpolator());
        animSetGold9.playTogether(gold9Anim1, gold9Anim2, gold9Anim3, gold9Anim4);
        animSetGold9.setStartDelay(800);
        animSetGold9.start();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // 弹出分享
    public static void shareDialog(final Activity activity, final View parent, final ShareUtils shareUtils, final String title, final String content, final String imgurl, final String url) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_share, null);
        final PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        TextView btnQq = (TextView) view.findViewById(R.id.btn_qq);
        TextView btnWechat = (TextView) view.findViewById(R.id.btn_wechat);
        TextView btnWechatCircle = (TextView) view.findViewById(R.id.btn_wechat_circle);
        TextView btnMsg = (TextView) view.findViewById(R.id.btn_msg);
        ImageView btnExit = (ImageView) view.findViewById(R.id.btn_exit);
        // QQ
        btnQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.qq(title, content, imgurl, url);
                popupWindow.dismiss();
            }
        });
        // 微信
        btnWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.wechat(title, content, imgurl, url);
                popupWindow.dismiss();
            }
        });
        // 朋友圈
        btnWechatCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.wechatcircle(title, content, imgurl, url);
                popupWindow.dismiss();
            }
        });
        // 短信
        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.msg(title, content, imgurl, url);
                popupWindow.dismiss();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    // 一个确定按钮的弹窗
    public static void confirmDialog(Activity activity, String content, String confirm) {
        final AlertDialog dialog = new AlertDialog.Builder(activity, R.style.Custom_Dialog).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_confirm);
        ImageView ivTag = (ImageView) window.findViewById(R.id.iv_tag);
        TextView tvContent = (TextView) window.findViewById(R.id.tv_content);
        TextView btnConfirm = (TextView) window.findViewById(R.id.btn_confirm);
        if ("您的信息正在认证中，通过后方可查看！".equals(content)) {
            ivTag.setImageResource(R.mipmap.icon_exclamation);
        }
        if ("您的身份为项目方，不能查看其他项目方的资料".equals(content)) {
            ivTag.setImageResource(R.mipmap.icon_exclamation);
        }
        tvContent.setText(content);
        btnConfirm.setText(confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // dialog形式弹出分享
    public static void newShareDialog(final Activity activity, final ShareUtils shareUtils, final String title, final String content, final String imgurl, final String url) {
        final AlertDialog dialog = new AlertDialog.Builder(activity, R.style.Custom_Dialog).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        final Window window = dialog.getWindow();
        // 设置无边距的弹窗
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setContentView(R.layout.dialog_share);
        window.setGravity(Gravity.BOTTOM);//此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.ShareDialog_Anim);
        TextView btnQq = (TextView) window.findViewById(R.id.btn_qq);
        TextView btnWechat = (TextView) window.findViewById(R.id.btn_wechat);
        TextView btnWechatCircle = (TextView) window.findViewById(R.id.btn_wechat_circle);
        TextView btnMsg = (TextView) window.findViewById(R.id.btn_msg);
        ImageView btnExit = (ImageView) window.findViewById(R.id.btn_exit);
        // QQ
        btnQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.qq(title, content, imgurl, url);
                dialog.dismiss();
            }
        });
        // 微信
        btnWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.wechat(title, content, imgurl, url);
                dialog.dismiss();
            }
        });
        // 朋友圈
        btnWechatCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.wechatcircle(title, content, imgurl, url);
                dialog.dismiss();
            }
        });
        // 短信
        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.msg(title, content, imgurl, url);
                dialog.dismiss();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // 确定后进入主页的弹窗
    public static void confirmToMainDialog(final Activity activity, String content) {
        final AlertDialog dialog = new AlertDialog.Builder(activity, R.style.Custom_Dialog).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_confirm);
        ImageView ivTag = (ImageView) window.findViewById(R.id.iv_tag);
        TextView tvContent = (TextView) window.findViewById(R.id.tv_content);
        TextView btnConfirm = (TextView) window.findViewById(R.id.btn_confirm);
        tvContent.setText(content);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    // 提示去实名认证的弹窗
    public static void goAuthentic(final Activity activity) {
        final AlertDialog dialog = new AlertDialog.Builder(activity, R.style.Custom_Dialog).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_two_btn);
        TextView tvContent = (TextView) window.findViewById(R.id.tv_content);
        TextView btnCancel = (TextView) window.findViewById(R.id.btn_cancel);
        TextView btnConfirm = (TextView) window.findViewById(R.id.btn_confirm);
        if ("未认证".equals(SharedPreferencesUtils.getIsAuthentic(activity))) {
            tvContent.setText("您还没有进行实名认证\n请先实名认证");
        } else if ("认证失败".equals(SharedPreferencesUtils.getIsAuthentic(activity))) {
            tvContent.setText("您的实名认证未通过\n请继续认证");
        }
        btnCancel.setText("取消");
        btnConfirm.setText("确定");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (SharedPreferencesUtils.getIsWechatLogin(activity)) {
                    intent.setClass(activity, WechatVerifyActivity.class);
                } else {
                    intent.setClass(activity, CertificationIDCardActivity.class);
                }
                intent.putExtra("usertype", SharedPreferencesUtils.getUserType(activity));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                dialog.dismiss();
            }
        });
    }
}
