package com.jinzht.pro1.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.MainFragmentAdapter;
import com.jinzht.pro1.base.BaseFragmentActivity;
import com.jinzht.pro1.bean.BannerInfoBean;
import com.jinzht.pro1.utils.AESUtils;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.FastJsonTools;
import com.jinzht.pro1.utils.MD5Utils;
import com.jinzht.pro1.utils.NetWorkUtils;
import com.jinzht.pro1.utils.OkHttpUtils;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.view.NoScrollViewPager;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 主页
 */
public class MainActivity extends BaseFragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private NoScrollViewPager mainViewpager;// 主页的4个模块
    private RadioGroup mainBottomTab;// 主页的4个模块按钮组
    private RadioButton mainBtnProject;// 项目按钮
    private RadioButton mainBtnInvestor;// 投资人按钮
    private ImageButton mainBtnMe;// 个人中心按钮
    private RadioButton mainBtnCircle;// 圈子按钮
    private RadioButton mainBtnActivity;// 活动按钮

    @Override
    protected int getResourcesId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        initView();
        GetBannerInfo getBannerInfo = new GetBannerInfo();
        getBannerInfo.execute();
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
    }

    private void initData() {
        mainViewpager.setAdapter(new MainFragmentAdapter(getSupportFragmentManager()));
        mainViewpager.setOffscreenPageLimit(4);
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
//                DialogUtils.goldAnim(this, 8, 9);
                Intent intent = new Intent(this, PersonalCenterActivity.class);
                startActivity(intent);
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

    // 获取banner数据
    private class GetBannerInfo extends AsyncTask<Void, Void, BannerInfoBean> {
        @Override
        protected BannerInfoBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETBANNERINFO)),
                            Constant.BASE_URL + Constant.GETBANNERINFO,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("banner数据", body);
                return FastJsonTools.getBean(body, BannerInfoBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(BannerInfoBean bannerInfoBean) {
            super.onPostExecute(bannerInfoBean);
            if (bannerInfoBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (bannerInfoBean.getStatus() == 200) {
                    List<BannerInfoBean.DataBean> data = bannerInfoBean.getData();
                    EventBus.getDefault().postSticky(data);
                    initData();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, bannerInfoBean.getMessage());
                }
            }
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
