package com.jinzht.pro1.fragment;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.ProjectFragmentAdapter;
import com.jinzht.pro1.base.BaseFragment;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiUtils;
import com.jinzht.pro1.view.BannerRoundProgressBar;

import java.util.ArrayList;
import java.util.List;

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
    private BannerRoundProgressBar bannerProgress;// banner上的圆形进度条

    private List<ImageView> imageViews;// banner的图片资源
    private String[] bannerTitles;// banner图片的标题
    private String[] bannerDescs; // banner图片的描述
    private int prePointIndex = 0;// 记录当前指示点的位置
    private boolean isAutoPlay = true;// banner是否自动轮播
    private Runnable bannerRunnable;// banner自动轮播任务

    private int proTotal = 80;// 要显示的全部进度
    private int progress = 0;// 当前进度
    private boolean isGoing = false;// 正在滑动的标识
    private ProThread thread;// banner的进度条的线程

    @Override
    protected int setLayout(LayoutInflater inflater) {
        return R.layout.fragment_project;
    }

    @Override
    protected void onFirstUserVisible() {
        findView();

        // 处理banner
        bannerPrepare();

        // banner的圆形进度条开始动
        startBannerProgress();

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


    private void findView() {
        titleBtnLeft = (LinearLayout) mActivity.findViewById(R.id.title_btn_left);// title的左侧按钮
        titleBtnLeft.setOnClickListener(this);
        titleIvLeft = (ImageView) mActivity.findViewById(R.id.title_iv_left);// title左侧图标，站内信
        projectVpBanner = (ViewPager) mActivity.findViewById(R.id.project_vp_banner);// banner轮播条
        projectBannerBottombg = (LinearLayout) mActivity.findViewById(R.id.project_banner_bottombg);// banner底部的阴影
        projectBannerTitle = (TextView) mActivity.findViewById(R.id.project_banner_title);// banner标题
        projectBannerDesc = (TextView) mActivity.findViewById(R.id.project_banner_desc);// banner描述
        projectBannerPoints = (LinearLayout) mActivity.findViewById(R.id.project_banner_points);// banner指示点
        projectRgTab = (RadioGroup) mActivity.findViewById(R.id.project_rg_tab);// 项目页签的RadioGroup
        projectRbtnRoadshow = (RadioButton) mActivity.findViewById(R.id.project_rbtn_roadshow);// 路演项目按钮
        projectRbtnPreselection = (RadioButton) mActivity.findViewById(R.id.project_rbtn_preselection);// 预选项目按钮
        projectVpType = (ViewPager) mActivity.findViewById(R.id.project_vp_type);// 路演项目和预选项目
        bannerProgress = (BannerRoundProgressBar) mActivity.findViewById(R.id.project_banner_project);// banner上的圆形进度条
    }

    // 处理banner
    private void bannerPrepare() {
        bannerTitles = new String[]{"一", "二", "三", "四"};
        bannerDescs = new String[]{"1111", "2222", "3333", "4444"};
        // 准备图片
        imageViews = new ArrayList<ImageView>();
        int[] imgIds = new int[]{R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d};
        for (int i = 0; i < imgIds.length; i++) {
            ImageView imageView = new ImageView(mContext);
//            // 设置图片缩放类型
//            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//            imageView.setImageResource(imgIds[i]);
            imageView.setBackgroundResource(imgIds[i]);
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
                // 当滑到某一页时，改变文字
                projectBannerTitle.setText(bannerTitles[newPosition]);
                projectBannerDesc.setText(bannerDescs[newPosition]);
                // 把相应位置的点变成selected
                projectBannerPoints.getChildAt(newPosition).setEnabled(true);
                prePointIndex = newPosition;
            }
        });
        // 初始化第0页
        projectBannerTitle.setText(bannerTitles[0]);
        projectBannerDesc.setText(bannerDescs[0]);
        // 初始化第0个点
        projectBannerPoints.getChildAt(0).setEnabled(true);
        // 实现往复无限滑动，设置当前条目位置为一个大值
        int center = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
                % imageViews.size();
        projectVpBanner.setCurrentItem(center);
        // 自动轮播
        autoPlay();
        UiUtils.getHandler().postDelayed(bannerRunnable, 4000);
    }

    // banner自动轮播
    private void autoPlay() {
        bannerRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // 切换到下一页
                    UiUtils.getHandler().postDelayed(this, 4000);
                    projectVpBanner.setCurrentItem(projectVpBanner.getCurrentItem() + 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
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

    @Override
    protected void onUserVisble() {
    }

    @Override
    protected void onFirstUserInvisble() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isAutoPlay = false;// 销毁时停止自动轮播
        if (thread != null) {// 停止进度条
            thread.stopThread();
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
            int newPosition = position % imageViews.size();
            ImageView imageView = imageViews.get(newPosition);
            // 把要返回的控件添加到容器
            container.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SuperToastUtils.showSuperToast(mContext, 2, "点击了第" + position + "张图片");
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
            while (!isGoing) {
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
            isGoing = true;
        }
    }

}
