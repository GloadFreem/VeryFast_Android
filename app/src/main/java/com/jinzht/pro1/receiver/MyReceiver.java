package com.jinzht.pro1.receiver;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jinzht.pro1.R;
import com.jinzht.pro1.eventbus.MainNoticeEvent;
import com.jinzht.pro1.utils.UiHelp;

import org.json.JSONObject;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

/**
 * 自定义的极光推送的接收器
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        // 指定定制的 Notification Layout
        CustomPushNotificationBuilder builder = new
                CustomPushNotificationBuilder(context,
                R.layout.customer_notitfication_layout,
                R.id.icon,
                R.id.title,
                R.id.text);
        // 指定最顶层状态栏小图标
        builder.statusBarDrawable = R.mipmap.ic_launcher;
        // 指定下拉状态栏时显示的通知图标
        builder.layoutIconDrawable = R.mipmap.ic_launcher;
        // 通知栏默认参数
        builder.notificationDefaults = Notification.DEFAULT_ALL;
        // 设置builder的样式编号为3，发送时指定编号发送
        JPushInterface.setPushNotificationBuilder(3, builder);
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//        	processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiversACTION_NOTIFICATION_RECEIVED] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiversACTION_NOTIFICATION_RECEIVED] 接收到推送下来的通知的ID: " + notifactionId);
            if (bundle.getString(JPushInterface.EXTRA_EXTRA) != null) {
                try {
                    JSONObject jsonObject = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    if (!jsonObject.isNull("api")) {
                        if (jsonObject.getString("api").equals("msg")) {
                            EventBus.getDefault().post(new MainNoticeEvent("receiver"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            if (bundle.getString(JPushInterface.EXTRA_EXTRA) != null) {
                try {
                    jsonJudge(context, bundle.getString(JPushInterface.EXTRA_EXTRA));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.d(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    private void jsonJudge(Context context, String body) throws Exception {
        JSONObject jsonObject = new JSONObject(body);
        if (jsonObject.isNull("api")) {
            return;
        }
        if (jsonObject.getString("api").equals("project")) {// 项目
            if (!UiHelp.isTopActivity(context)) {
                Log.d(TAG, "not_top");
                UiHelp.openJinZht(context, "com.jinzht.pro");
            }
            // progect中分了两种类型，纯web(仅有url)和news，news就是有id和url
            if (jsonObject.opt("id") == null || jsonObject.opt("url") == null) {
                return;
            }
            String id = jsonObject.opt("id") + "";
            // TODO: 2016/4/29 跳转至融资中的项目详情页
//            if (!StringUtils.isEmpty(id)) {
//                Intent intent = new Intent(context, InvestFinacingDetailsActivity.class);
//                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("id", id);
//                context.startActivity(intent);
//            }
        } else if (jsonObject.getString("api").equals("msg")) {// 消息
            if (!UiHelp.isTopActivity(context)) {
                Log.d(TAG, "not_top");
                UiHelp.openJinZht(context, "com.jinzht.pro");
            }
            if (jsonObject.opt("id") == null) {
                return;
            }
            String id = jsonObject.opt("id") + "";
            // TODO: 2016/4/29 跳转至互动专栏页
//            if (!StringUtils.isEmpty(id)) {
//                Intent intent = new Intent(context, InteractActivity.class);
//                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("id", id);
//                context.startActivity(intent);
//            }
        } else if (jsonObject.getString("api").equals("web")) {// 网页
            if (!UiHelp.isTopActivity(context)) {
                Log.d(TAG, "not_top");
                UiHelp.openJinZht(context, "com.jinzht.pro");
            }
            if (jsonObject.opt("id") == null || jsonObject.opt("url") == null) {
                return;
            }
            String id = jsonObject.opt("id") + "";
            // TODO: 2016/4/29 跳转到一个网页
//            if (!StringUtils.isEmpty(id)) {
//                Intent intent = new Intent(context, WebViewActivity.class);
//                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("url", jsonObject.optString("url"));
//                context.startActivity(intent);
            // TODO: 2016/4/29 跳转到新三板的详情页
//            } else if (!StringUtils.isEquals(id, "null") && !jsonObject.optString("url").equals("")) {
//                Intent intent = new Intent(context, ThreeWebviewActivity.class);
//                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("id", jsonObject.optString("id"));
//                intent.putExtra("url", jsonObject.optString("url"));
//                context.startActivity(intent);
//            }
        } else if (jsonObject.getString("api").equals("feeling")) {
            {
//				if (!UiHelp.isTopActivity(context)) {
//					Log.e(TAG, "not_top");
//					UiHelp.openJinZht(context, "com.jinzht.pro");
//				}
//				Intent i = new Intent(context, MainActivity.class);
//				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				context.startActivity(i);
//				MainActivity.vpFragment.setCurrentItem(3);
            }
        }
    }
}
