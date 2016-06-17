package com.jinzht.pro1.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.ProjectFragmentAdapter;
import com.jinzht.pro1.base.BaseFragment;
import com.jinzht.pro1.bean.BannerInfoBean;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiUtils;
import com.jinzht.pro1.view.BannerRoundProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 项目界面
 */
public class ProjectFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private LinearLayout titleBtnLeft;// title的左侧按钮
    private ImageView titleIvLeft;// title左侧图标，站内信
    private ViewPager projectVpBanner;// banner轮播条
    private LinearLayout projectBannerBottombg;// banner底部的阴影
    private TextView projectBannerTitle;// banner标题
    private TextView projectBannerDesc;// banner描述
    private LinearLayout projectBannerPoints;// banner指示点
    private RadioGroup projectRgTab;// 项目页签的RadioGroup
    private RadioButton projectRbtnRoadshow;// 路演项目按钮
    private RadioButton projectRbtnPreselection;// 预选项目按钮
    private ViewPager projectVpType;// 路演项目和预选项目
    private RelativeLayout rlProgress;// banner上的圆形进度条框架
    private BannerRoundProgressBar bannerProgress;// banner上的圆形进度条

    private List<ImageView> imageViews;// banner的图片View
    private int prePointIndex = 0;// 记录当前指示点的位置
    private ScheduledExecutorService scheduledExecutorService;// 定时任务
    private BannerRun bannerRun;// banner自动轮播任务
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            projectVpBanner.setCurrentItem(projectVpBanner.getCurrentItem() + 1);
        }
    };

    private int proTotal = 0;// 要显示的全部进度
    private int progress = 0;// 当前进度
    private boolean progressStop = false;// 正在滑动的标识
    private ProThread thread;// banner的进度条的线程

    private List<BannerInfoBean.DataBean> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, container, false);
        titleBtnLeft = (LinearLayout) view.findViewById(R.id.title_btn_left);// title的左侧按钮
        titleBtnLeft.setOnClickListener(this);
        titleIvLeft = (ImageView) view.findViewById(R.id.title_iv_left);// title左侧图标，站内信
        projectVpBanner = (ViewPager) view.findViewById(R.id.project_vp_banner);// banner轮播条
        projectBannerBottombg = (LinearLayout) view.findViewById(R.id.project_banner_bottombg);// banner底部的阴影
        projectBannerTitle = (TextView) view.findViewById(R.id.project_banner_title);// banner标题
        projectBannerDesc = (TextView) view.findViewById(R.id.project_banner_desc);// banner描述
        projectBannerPoints = (LinearLayout) view.findViewById(R.id.project_banner_points);// banner指示点
        projectRgTab = (RadioGroup) view.findViewById(R.id.project_rg_tab);// 项目页签的RadioGroup
        projectRbtnRoadshow = (RadioButton) view.findViewById(R.id.project_rbtn_roadshow);// 路演项目按钮
        projectRbtnPreselection = (RadioButton) view.findViewById(R.id.project_rbtn_preselection);// 预选项目按钮
        projectVpType = (ViewPager) view.findViewById(R.id.project_vp_type);// 路演项目和预选项目
        rlProgress = (RelativeLayout) view.findViewById(R.id.project_banner_rl_progress);// banner上的圆形进度条框架
        bannerProgress = (BannerRoundProgressBar) view.findViewById(R.id.project_banner_progress);// banner上的圆形进度条
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EventBus.getDefault().register(this);

        // 设置tab的单选事件
        projectRgTab.setOnCheckedChangeListener(this);
        // 给项目类型填充数据
        projectVpType.setAdapter(new ProjectFragmentAdapter(getChildFragmentManager()));
        projectVpType.setCurrentItem(0);
        // 设置项目Tab和项目ViewPager联动
        projectVpType.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        projectRgTab.check(R.id.project_rbtn_roadshow);
                        break;
                    case 1:
                        projectRgTab.check(R.id.project_rbtn_preselection);
                        break;
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void getBannerInfo(List<BannerInfoBean.DataBean> data) {
        this.data = data;
        if (this.data.size() != 0) {
            // 处理banner
            bannerPrepare();
        }
    }

    // 处理banner
    private void bannerPrepare() {
        // 准备图片
        imageViews = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            // 设置图片缩放类型
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(mContext).load(data.get(i).getBody().getImage()).into(imageView);
            imageViews.add(imageView);
            // 创建圆点指示器
            ImageView point = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UiUtils.dip2px(3), UiUtils.dip2px(3));
            if (i != 0) {
                params.leftMargin = 10;
            }
            point.setLayoutParams(params);
            point.setEnabled(false);
            point.setBackgroundResource(R.drawable.selector_banner_point);
            // 将点点指示器添加到线性容器中
            projectBannerPoints.addView(point);
        }
        // 给banner填充数据
        projectVpBanner.setAdapter(new BannerAdapter());
        // 监听banner的滑动
        projectVpBanner.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                int newPosition = position % imageViews.size();
                // 把前一个点变成normal
                projectBannerPoints.getChildAt(prePointIndex).setEnabled(false);
                // 把相应位置的点变成selected
                projectBannerPoints.getChildAt(newPosition).setEnabled(true);
                // 当滑到某一页时，改变文字
                projectBannerTitle.setText(data.get(newPosition).getBody().getName());
                projectBannerDesc.setText(data.get(newPosition).getBody().getDescription());
                // 改变圆形进度条
                if (data.get(newPosition).getType().equals("Project")) {
                    if (data.get(newPosition).getExtr().getRoadshows().get(0).getRoadshowplan().getFinancedMount() == 0) {
                        projectBannerBottombg.setVisibility(View.GONE);
                        rlProgress.setVisibility(View.GONE);
                    } else {
                        projectBannerBottombg.setVisibility(View.VISIBLE);
                        rlProgress.setVisibility(View.VISIBLE);
                        bannerProgress.setTextBottom(String.valueOf(data.get(newPosition).getExtr().getRoadshows().get(0).getRoadshowplan().getFinanceTotal()));
                        proTotal = (int) ((double) (data.get(newPosition).getExtr().getRoadshows().get(0).getRoadshowplan().getFinancedMount()) / (double) (data.get(newPosition).getExtr().getRoadshows().get(0).getRoadshowplan().getFinanceTotal()) * 100);
                        // banner的圆形进度条开始动
                        startBannerProgress();
                        if (progressStop) {
                            bannerProgress.setProgress(proTotal);
                        }
                    }
                } else {
                    projectBannerBottombg.setVisibility(View.GONE);
                    rlProgress.setVisibility(View.GONE);
                }
                prePointIndex = newPosition;
            }
        });
        // 初始化第0页
        projectBannerTitle.setText(data.get(0).getBody().getName());
        projectBannerDesc.setText(data.get(0).getBody().getDescription());
        // 初始化第0个点
        projectBannerPoints.getChildAt(0).setEnabled(true);
        // 实现往复无限滑动，设置当前条目位置为一个大值
        int center = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
                % imageViews.size();
        projectVpBanner.setCurrentItem(center);
        // 自动轮播
        autoPlay();
    }

    // banner自动轮播
    private void autoPlay() {
        if (bannerRun == null) {
            bannerRun = new BannerRun();
        }
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(bannerRun, 4, 4, TimeUnit.SECONDS);
    }

    private class BannerRun implements Runnable {
        @Override
        public void run() {
            synchronized (projectVpBanner) {
                handler.obtainMessage().sendToTarget();
            }
        }
    }

    private void startBannerProgress() {
        thread = new ProThread();
        thread.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_btn_left:// 点击进入站内信
                SuperToastUtils.showSuperToast(mContext, 2, "站内信");
                break;
        }
    }

    // 项目页签的RadioGroup选择
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.project_rbtn_roadshow:// 选择了路演项目
                projectVpType.setCurrentItem(0);
                projectRbtnRoadshow.setTextColor(UiUtils.getColor(R.color.custom_orange));
                projectRbtnPreselection.setTextColor(UiUtils.getColor(R.color.bg_text));
                break;
            case R.id.project_rbtn_preselection:// 选择了预选项目
                projectVpType.setCurrentItem(1);
                projectRbtnPreselection.setTextColor(UiUtils.getColor(R.color.custom_orange));
                projectRbtnRoadshow.setTextColor(UiUtils.getColor(R.color.bg_text));
                break;
        }
    }

    // banner的数据适配器
    private class BannerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;// 为了实现无限循环
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final int newPosition = position % imageViews.size();
            ImageView imageView = imageViews.get(newPosition);
            // 把要返回的控件添加到容器
            container.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SuperToastUtils.showSuperToast(mContext, 2, "点击了第" + newPosition + "张图片");
                }
            });

            return imageView;
        }

        // 删除条目
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    // 控制banner进度条，更新UI
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int temp = (int) msg.obj;
            if (proTotal - temp > 0) {
                bannerProgress.setProgress(temp);
            } else {
                bannerProgress.setProgress(proTotal);
                thread.stopThread();
            }
        }
    };

    // banner的圆形进度条的线程
    private class ProThread extends Thread {
        @Override
        public void run() {
            while (!progressStop) {
                try {
                    progress += 1;
                    Message msg = new Message();
                    msg.obj = progress;
                    Thread.sleep(50);
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stopThread() {
            progressStop = true;
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();// banner停止自动轮播
        }
        if (thread != null) {// 停止进度条
            thread.stopThread();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();// banner停止自动轮播
        }
        if (thread != null) {// 停止进度条
            thread.stopThread();
        }
        EventBus.getDefault().unregister(this);
    }
}
