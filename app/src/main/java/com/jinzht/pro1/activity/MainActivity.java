package com.jinzht.pro1.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.MainFragmentAdapter;
import com.jinzht.pro1.application.MyApplication;
import com.jinzht.pro1.utils.ACache;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.view.NoScrollViewPager;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

/**
 * 主页
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private NoScrollViewPager mainViewpager;// 主页的4个模块
    private RadioGroup mainBottomTab;// 主页的4个模块按钮组
    private RadioButton mainBtnProject;// 项目按钮
    private RadioButton mainBtnInvestor;// 投资人按钮
    private ImageButton mainBtnMe;// 个人中心按钮
    private RadioButton mainBtnCircle;// 圈子按钮
    private RadioButton mainBtnActivity;// 活动按钮

    private ExitReceiver exitReceiver = new ExitReceiver();// 退出应用的广播接收者
    public static final String EXITACTION = "action.exit";// 退出应用的广播接收者的action

    public String TAG;// 当前activity的tag
    public Context mContext;// Activity的Context
    public ACache aCache;// 缓存工具类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 取消应用标题
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);// 转场动画
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_main);
//        ShareSDK.initSDK(this);// 分享
        JPushInterface.init(getApplicationContext());// 极光推送
        mContext = getApplicationContext();
        aCache = ACache.get(mContext);
        MobclickAgent.openActivityDurationTrack(false);// 友盟
        MobclickAgent.setSessionContinueMillis(30000l);// 友盟
        UiHelp.setTranslucentStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        TAG = getRunningActivityName();

        // 注册退出广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction(EXITACTION);
        registerReceiver(exitReceiver, filter);

        initView();
        initData();
    }

    private void initView() {
        mainViewpager = (NoScrollViewPager) findViewById(R.id.main_viewpager);// 主页的4个模块
        mainBottomTab = (RadioGroup) findViewById(R.id.main_bottom_tab);// 主页的4个模块按钮组
        mainBtnProject = (RadioButton) findViewById(R.id.main_btn_project);// 项目按钮
        mainBtnInvestor = (RadioButton) findViewById(R.id.main_btn_investor);// 投资人按钮
        mainBtnMe = (ImageButton) findViewById(R.id.main_btn_me);// 个人中心按钮
        mainBtnMe.setOnClickListener(this);
        mainBtnCircle = (RadioButton) findViewById(R.id.main_btn_circle);// 圈子按钮
        mainBtnActivity = (RadioButton) findViewById(R.id.main_btn_activity);// 活动按钮

        mainBottomTab.setOnCheckedChangeListener(this);
//        // 让ViewPager不可左右滑动，但可以响应点击事件
//        mainViewpager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
    }

    private void initData() {
        mainViewpager.setAdapter(new MainFragmentAdapter(getSupportFragmentManager()));
        mainViewpager.setCurrentItem(0);
        // 主页模块的ViewPager和RadioGroup联动
        mainViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mainBottomTab.check(R.id.main_btn_project);
                        break;
                    case 1:
                        mainBottomTab.check(R.id.main_btn_investor);
                        break;
                    case 2:
                        mainBottomTab.check(R.id.main_btn_circle);
                        break;
                    case 3:
                        mainBottomTab.check(R.id.main_btn_activity);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_me:// 跳转到个人中心界面
                SuperToastUtils.showSuperToast(this, 2, "个人中心");
                break;
        }
    }

    // BottomTab的四个单选按钮
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_btn_project:// 选择了项目
                mainViewpager.setCurrentItem(0, false);
                break;
            case R.id.main_btn_investor:// 选择了投资人
                mainViewpager.setCurrentItem(1, false);
                break;
            case R.id.main_btn_circle:// 选择了圈子
                mainViewpager.setCurrentItem(2, false);
                break;
            case R.id.main_btn_activity:// 选择了活动
                mainViewpager.setCurrentItem(3, false);
                break;
        }
    }

    // 退出应用的广播接收者
    class ExitReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MainActivity.this.finish();
        }
    }

    private String getRunningActivityName() {
        String contextString = this.toString();
        return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
        JPushInterface.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ShareSDK.stopSDK(this);
    }
}
