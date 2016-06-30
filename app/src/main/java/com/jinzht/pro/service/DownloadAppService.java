package com.jinzht.pro.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import com.jinzht.pro.R;
import com.jinzht.pro.activity.SettingActivity;

/**
 * 更新APP时后台下载
 */
public class DownloadAppService extends Service {

    private NotificationManager notificationManager;
    private int old_projress = 0;
    private boolean isFirstStart = false;

    @Override
    public void onCreate() {
        super.onCreate();
        isFirstStart = true;
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.i("后台下载", String.valueOf(SettingActivity.downloading_progress));
        handler.handleMessage(new Message());
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (SettingActivity.downloading_progress > 99) {
                notificationManager.cancel(0);// 下载完成后状态栏取消
                stopSelf();// 终止服务
                return;
            }
            if (SettingActivity.downloading_progress > old_projress) {
                // 自定义标题栏视图
                displayNotificationMessage(SettingActivity.downloading_progress);
            }
            isFirstStart = false;
            Message message = handler.obtainMessage();
            handler.sendMessage(message);// 递归调用，更新进度条
            old_projress = SettingActivity.downloading_progress;
        }
    };

    private void displayNotificationMessage(int count) {
        // Notification的Intent，即点击后转向的Activity
        Intent intent = new Intent(this, getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // 创建Notifcation对象，设置图标，提示文字
        Notification notification = new Notification(R.mipmap.ic_launcher, "DnwoLoadManager", System.currentTimeMillis());
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        // 自定义Notification
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notitfication_update);
        remoteViews.setTextViewText(R.id.text, "当前进度" + count + "%");
        remoteViews.setProgressBar(R.id.progress, 100, count, false);
        notification.contentView = remoteViews;
        notification.contentIntent = pendingIntent;
        notificationManager.notify(0, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
