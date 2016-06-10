package com.jinzht.pro1.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.mob.tools.utils.UIHandler;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 分享工具类
 */
public class ShareUtils implements PlatformActionListener, Handler.Callback {

    private static final int MSG_ACTION_CCALLBACK = 1;

    private Activity activity;

    public ShareUtils(Activity activity) {
        this.activity = activity;
    }

    public void qq(String title, String content, String imgurl, String url) {
        Platform.ShareParams qq = new Platform.ShareParams();
        qq.setTitle(title);
        qq.setText(content);
        qq.setImageUrl(imgurl);
        qq.setTitleUrl(url);
        qq.setShareType(Platform.SHARE_WEBPAGE);
        Platform platform = ShareSDK.getPlatform(activity, QQ.NAME);
        platform.setPlatformActionListener(this);
        platform.share(qq);
    }

    public void wechat(String title, String content, String imgurl, String url) {
        Platform.ShareParams wechat = new Platform.ShareParams();
        wechat.setTitle(title);
        wechat.setText(content);
        wechat.setImageUrl(imgurl);
        wechat.setUrl(url);
        wechat.setShareType(Platform.SHARE_WEBPAGE);
        Platform platform = ShareSDK.getPlatform(activity, Wechat.NAME);
        platform.setPlatformActionListener(this);
        platform.share(wechat);
    }

    public void wechatcircle(String title, String content, String imgurl, String url) {
        Platform.ShareParams wechatcircle = new Platform.ShareParams();
        wechatcircle.setTitle(title);
        wechatcircle.setText(content);
        wechatcircle.setImageUrl(imgurl);
        wechatcircle.setUrl(url);
        wechatcircle.setShareType(Platform.SHARE_WEBPAGE);
        Platform platform = ShareSDK.getPlatform(activity, WechatMoments.NAME);
        platform.setPlatformActionListener(this);
        platform.share(wechatcircle);
    }

    public void msg(String title, String content, String imgurl, String url) {
        ShortMessage.ShareParams msg = new ShortMessage.ShareParams();
        msg.setAddress("");
        msg.setText("专注中国成长型企业股权投融资" + url);
        msg.setShareType(Platform.SHARE_TEXT);
        Platform platform = ShareSDK.getPlatform(activity, ShortMessage.NAME);
        platform.setPlatformActionListener(this);
        platform.share(msg);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Message msg = Message.obtain();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = i;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        throwable.printStackTrace();

        Message msg = Message.obtain();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = i;
        msg.obj = throwable;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Message msg = Message.obtain();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = i;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.arg1) {
            case 1:
//                SuperToastUtils.showSuperToast(activity, 2, "分享成功");
                break;
            case 2:
                SuperToastUtils.showSuperToast(activity, 2, "分享失败");
                break;
            case 3:
//                SuperToastUtils.showSuperToast(activity, 3, "取消分享");
                break;
        }
        return false;
    }
}
