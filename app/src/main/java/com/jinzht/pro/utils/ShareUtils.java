package com.jinzht.pro.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.jinzht.pro.bean.EventMsg;
import com.mob.tools.utils.UIHandler;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import de.greenrobot.event.EventBus;

/**
 * 分享工具类
 */
public class ShareUtils implements PlatformActionListener, Handler.Callback {

    private static final int COMPLETE = 0;
    private static final int ERROR = 1;
    private static final int CANCEL = 2;

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
        msg.setText("金指投：专注中国成长型企业股权投融资" + url);
//        msg.setShareType(Platform.SHARE_TEXT);
        msg.setShareType(Platform.SHARE_WEBPAGE);
        Platform platform = ShareSDK.getPlatform(activity, ShortMessage.NAME);
        platform.setPlatformActionListener(this);
        platform.share(msg);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Message msg = Message.obtain();
        msg.what = COMPLETE;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        throwable.printStackTrace();

        Message msg = Message.obtain();
        msg.what = ERROR;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Message msg = Message.obtain();
        msg.what = CANCEL;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case COMPLETE:
                SuperToastUtils.showSuperToast(activity, 2, "分享成功");
                EventBus.getDefault().post(new EventMsg("分享成功"));
                break;
            case ERROR:
                SuperToastUtils.showSuperToast(activity, 2, "分享失败");
                break;
            case CANCEL:
                SuperToastUtils.showSuperToast(activity, 3, "取消分享");
                break;
        }
        return false;
    }
}
