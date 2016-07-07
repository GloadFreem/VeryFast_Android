package com.jinzht.pro.receiver;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jinzht.pro.R;
import com.jinzht.pro.activity.ActivityDetailActivity;
import com.jinzht.pro.activity.CommonWebViewActivity;
import com.jinzht.pro.activity.MessageActivity;
import com.jinzht.pro.activity.RoadshowDetailsActivity;
import com.jinzht.pro.bean.EventMsg;
import com.jinzht.pro.bean.JPushBean;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.SharedPreferencesUtils;
import com.jinzht.pro.utils.UiHelp;

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
        Log.i(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        // 指定定制的 Notification Layout
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(context,
                R.layout.notitfication_jpush,
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
            Log.i(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.i(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//        	processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.i(TAG, "[MyReceiversACTION_NOTIFICATION_RECEIVED] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.i(TAG, "[MyReceiversACTION_NOTIFICATION_RECEIVED] 接收到推送下来的通知的ID: " + notifactionId);
            if (bundle.getString(JPushInterface.EXTRA_EXTRA) != null) {
                JPushBean jPushBean = FastJsonTools.getBean(bundle.getString(JPushInterface.EXTRA_EXTRA), JPushBean.class);
                if ("authentic".equals(jPushBean.getType()) ||
                        "invest".equals(jPushBean.getType()) ||
                        "withdraw".equals(jPushBean.getType()) ||
                        "goldcon".equals(jPushBean.getType()) ||
                        "system".equals(jPushBean.getType())) {
                    EventBus.getDefault().postSticky(new EventMsg("收到Msg"));
                }
                if ("authentic".equals(jPushBean.getType()) && "成功".equals(jPushBean.getExt())) {
                    SharedPreferencesUtils.saveIsAuthentic(context, "已认证");
                }
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.i(TAG, "[MyReceiver] 用户点击打开了通知");
            if (bundle.getString(JPushInterface.EXTRA_EXTRA) != null) {
                try {
                    jsonJudge(context, bundle.getString(JPushInterface.EXTRA_EXTRA));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.i(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.i(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.i(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
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
        Log.i("点击的推送", body);
        EventBus.getDefault().postSticky(new EventMsg("点击Msg"));
        JPushBean jPushBean = FastJsonTools.getBean(body, JPushBean.class);
        if (!UiHelp.isTopActivity(context)) {
            UiHelp.openJinZht(context, "com.jinzht.pro");
        }
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        switch (jPushBean.getType()) {
            case "web":// 网页
                intent.setClass(context, CommonWebViewActivity.class);
                intent.putExtra("title", jPushBean.getExt());
                intent.putExtra("url", jPushBean.getContent());
                context.startActivity(intent);
                break;
            case "project":// 路演项目
                intent.setClass(context, RoadshowDetailsActivity.class);
                intent.putExtra("id", jPushBean.getContent());
                context.startActivity(intent);
                break;
            case "authentic":// 认证进度
                intent.setClass(context, MessageActivity.class);
                context.startActivity(intent);
                break;
            case "invest":// 投资状况
                intent.setClass(context, MessageActivity.class);
                context.startActivity(intent);
                break;
            case "withdraw":// 提现到账情况
                intent.setClass(context, MessageActivity.class);
                context.startActivity(intent);
                break;
            case "goldcon":// 金条信息
                intent.setClass(context, MessageActivity.class);
                context.startActivity(intent);
                break;
            case "system":// 系统通知
                intent.setClass(context, MessageActivity.class);
                context.startActivity(intent);
                break;
            case "action":// 活动
                intent.setClass(context, ActivityDetailActivity.class);
                intent.putExtra("id", Integer.parseInt(jPushBean.getContent()));
                context.startActivity(intent);
                break;
        }

        JSONObject jsonObject = new JSONObject(body);
        if (jsonObject.isNull("api")) {
            return;
        }
        if (jsonObject.getString("api").equals("project")) {// 项目
            if (!UiHelp.isTopActivity(context)) {
                Log.i(TAG, "not_top");
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
                Log.i(TAG, "not_top");
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
                Log.i(TAG, "not_top");
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
//					Log.i(TAG, "not_top");
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
