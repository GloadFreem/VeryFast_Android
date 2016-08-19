package com.jinzht.pro.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.activity.CommonWebViewActivity;
import com.jinzht.pro.activity.MessageActivity;
import com.jinzht.pro.activity.PreselectionDetailsActivity;
import com.jinzht.pro.activity.RoadshowDetailsActivity;
import com.jinzht.pro.base.BaseFragment;
import com.jinzht.pro.bean.BannerInfoBean;
import com.jinzht.pro.bean.EventMsg;
import com.jinzht.pro.bean.HaveNotReadMessageBean;
import com.jinzht.pro.bean.PreselectionProjectListBean;
import com.jinzht.pro.bean.RoadshowProjectListBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.CacheUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiUtils;
import com.jinzht.pro.view.BannerRoundProgressBar;
import com.jinzht.pro.view.CircleImageView;
import com.jinzht.pro.view.PullToRefreshLayout;
import com.jinzht.pro.view.PullableListView;
import com.jinzht.pro.view.RoundProgressBar;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 项目界面
 */
public class ProjectFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout titleBtnLeft;// title的左侧按钮，站内信
    private ImageView titleIvLeft;// 站内信图标
    private LinearLayout pageError;// 错误页面
    private ImageView btnTryagain;// 重试按钮
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 项目列表
    private MyAdapter myAdapter = new MyAdapter();

    private Runnable bannerRunnable;// banner自动轮播
    private Handler handler;// 用于自动轮播
    private boolean isAuto = true;// banner是否轮播
    private List<ImageView> imageViews = new ArrayList<>();// banner的图片View
    private int prePointIndex = 0;// 上一个指示点的位置

    private Handler mHandler;// 用于圆形进度
    private int proTotal = 0;// 要显示的全部进度
    private int progress = 0;// 当前进度
    private boolean progressStop = false;// 正在滑动的标识
    private ProThread thread;// banner的进度条的线程
    private List<BannerInfoBean.DataBean> bannerData = new ArrayList<>();

    private int flag = 0;// 0是路演项目，1是预选项目

    private int rPages = 0;
    List<RoadshowProjectListBean.DataBean> rDatas = new ArrayList<>();// 路演项目列表数据集合

    private int pPages = 0;
    List<PreselectionProjectListBean.DataBean> pDatas = new ArrayList<>();// 数据集合

    private int rPOSITION = 0;
    private int pPOSITION = 0;
    private final static int REQUEST_CODE = 1;

    // 缓存
    private String cacheBanner;
    private String cacheRoadshowList;
    private String cachePreselectionList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, container, false);
        titleBtnLeft = (LinearLayout) view.findViewById(R.id.title_btn_left);// title的左侧按钮
        titleBtnLeft.setOnClickListener(this);
        titleIvLeft = (ImageView) view.findViewById(R.id.title_iv_left);// 站内信图标
        pageError = (LinearLayout) view.findViewById(R.id.page_error);// 错误页面
        btnTryagain = (ImageView) view.findViewById(R.id.btn_tryagain);// 重试按钮
        btnTryagain.setOnClickListener(this);
        refreshView = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);// 刷新布局
        listview = (PullableListView) view.findViewById(R.id.listview);// 项目列表
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        refreshView.setOnRefreshListener(new PullListener());
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                    if (flag == 0) {
                        rPOSITION = position - 1;
                        Intent intent = new Intent(mContext, RoadshowDetailsActivity.class);
                        intent.putExtra("id", String.valueOf(rDatas.get(position - 1).getProjectId()));
                        startActivityForResult(intent, REQUEST_CODE);
                    } else {
                        pPOSITION = position - 1;
                        Intent intent = new Intent(mContext, PreselectionDetailsActivity.class);
                        intent.putExtra("id", String.valueOf(pDatas.get(position - 1).getProjectId()));
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                } else {
                    Intent intent = new Intent(mContext, CommonWebViewActivity.class);
                    intent.putExtra("title", "项目详情");
                    intent.putExtra("url", "file:///android_asset/error.html");
                    startActivity(intent);
                }
            }
        });
        isAuto = true;

        // 读取缓存
        cacheBanner = (String) CacheUtils.readObject(Constant.CACHE_BANNER);
        if (!StringUtils.isBlank(cacheBanner)) {
            BannerInfoBean bannerInfoBean = FastJsonTools.getBean(cacheBanner, BannerInfoBean.class);
            if (bannerInfoBean != null && bannerInfoBean.getStatus() == 200) {
                bannerData = bannerInfoBean.getData();
                if (bannerData != null && bannerData.size() != 0) {
                    initListHeader();
                }
            }
            Log.i("缓存的banner", cacheBanner + "");
        } else {
            pageError.setVisibility(View.VISIBLE);
            refreshView.setVisibility(View.INVISIBLE);
        }
        cacheRoadshowList = (String) CacheUtils.readObject(Constant.CACHE_ROADSHOW_LIST);
        if (!StringUtils.isBlank(cacheRoadshowList)) {
            RoadshowProjectListBean roadshowProjectListBean = FastJsonTools.getBean(cacheRoadshowList, RoadshowProjectListBean.class);
            if (roadshowProjectListBean != null && roadshowProjectListBean.getStatus() == 200) {
                rDatas = roadshowProjectListBean.getData();
                if (rDatas != null) {
                    listview.setAdapter(myAdapter);
                }
            }
        } else {
            pageError.setVisibility(View.VISIBLE);
            refreshView.setVisibility(View.INVISIBLE);
        }
        cachePreselectionList = (String) CacheUtils.readObject(Constant.CACHE_PRESELECTION_LIST);
        if (!StringUtils.isBlank(cachePreselectionList)) {
            PreselectionProjectListBean preselectionProjectListBean = FastJsonTools.getBean(cachePreselectionList, PreselectionProjectListBean.class);
            if (preselectionProjectListBean != null && preselectionProjectListBean.getStatus() == 200) {
                pDatas = preselectionProjectListBean.getData();
                if (pDatas != null) {
                    listview.setAdapter(myAdapter);
                }
            }
        } else {
            pageError.setVisibility(View.VISIBLE);
            refreshView.setVisibility(View.INVISIBLE);
        }

//        GetBannerInfo getBannerInfo = new GetBannerInfo();
//        getBannerInfo.execute();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HaveNotReadMessageTask haveNotReadMessageTask = new HaveNotReadMessageTask();
                haveNotReadMessageTask.execute();
            }
        }, 10000);
    }

//    // 接收banner资源后
//    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
//    public void getBannerInfo(BannerInfoBean bean) {
//        bannerData = bean.getData();
//        if (bannerData != null && bannerData.size() != 0) {
//            // 处理banner
//            initListHeader();
//            GetRoadshowProjectListTask getRoadshowProjectListTask = new GetRoadshowProjectListTask(0);
//            getRoadshowProjectListTask.execute();
//            GetPreselectionProjectListTask getPreselectionProjectListTask = new GetPreselectionProjectListTask(0);
//            getPreselectionProjectListTask.execute();
//        }
//    }

    // 接收是否登录成功的提示
    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void getLoginInfo(EventMsg msg) {
        if ("登录成功".equals(msg.getMsg())) {
            GetBannerInfo getBannerInfo = new GetBannerInfo();
            getBannerInfo.execute();
        }
    }

    // 接收推送的站内信，改变未读状态
    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void getReceivedMsg(EventMsg msg) {
        if ("收到Msg".equals(msg.getMsg())) {
            titleIvLeft.setImageResource(R.mipmap.message_full);
        }
        if ("点击Msg".equals(msg.getMsg())) {
            titleIvLeft.setImageResource(R.mipmap.message_empty);
        }
    }

    // 添加banner头布局
    private void initListHeader() {
        if (listview.getHeaderViewsCount() != 0) {
            listview.removeHeaderView(listview.getChildAt(0));
            handler = null;
            bannerRunnable = null;
        }
        final View header = LayoutInflater.from(mContext).inflate(R.layout.item_banner_and_tab, null);
        final ViewPager projectVpBanner = (ViewPager) header.findViewById(R.id.project_vp_banner);// banner轮播条
        final LinearLayout projectBannerBottombg = (LinearLayout) header.findViewById(R.id.project_banner_bottombg);// banner底部的阴影
        final TextView projectBannerTitle = (TextView) header.findViewById(R.id.project_banner_title);// banner标题
        final TextView projectBannerDesc = (TextView) header.findViewById(R.id.project_banner_desc);// banner描述
        final LinearLayout projectBannerPoints = (LinearLayout) header.findViewById(R.id.project_banner_points);// banner指示点
        RadioGroup projectRgTab = (RadioGroup) header.findViewById(R.id.project_rg_tab);// 项目页签的RadioGroup
        final RelativeLayout rlProgress = (RelativeLayout) header.findViewById(R.id.project_banner_rl_progress);// banner上的圆形进度条框架
        final BannerRoundProgressBar bannerProgress = (BannerRoundProgressBar) header.findViewById(R.id.project_banner_progress);// banner上的圆形进度条
        final RadioButton projectRbtnRoadshow = (RadioButton) header.findViewById(R.id.project_rbtn_roadshow);// 路演项目按钮
        final RadioButton projectRbtnPreselection = (RadioButton) header.findViewById(R.id.project_rbtn_preselection);// 预选项目按钮
        // 准备图片
        imageViews.clear();
        projectBannerPoints.removeAllViews();
        for (int i = 0; i < bannerData.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            // 设置图片缩放类型
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(mContext).load(bannerData.get(i).getBody().getImage()).into(imageView);
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
        projectVpBanner.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Integer.MAX_VALUE;// 为了实现无限循环
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                final int newPosition = position % imageViews.size();
                ImageView imageView = imageViews.get(newPosition);
                // 把要返回的控件添加到容器
                ViewGroup parent = (ViewGroup) imageView.getParent();
                if (parent != null) {
                    parent.removeAllViews();
                }
                container.addView(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bannerData.get(newPosition).getType().equals("Project")) {
                            if (bannerData.get(newPosition).getExtr().getFinancestatus().getName().equals("预选项目")) {
                                Intent intent = new Intent(mContext, PreselectionDetailsActivity.class);
                                intent.putExtra("id", String.valueOf(bannerData.get(newPosition).getExtr().getProjectId()));
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(mContext, RoadshowDetailsActivity.class);
                                intent.putExtra("id", String.valueOf(bannerData.get(newPosition).getExtr().getProjectId()));
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(mContext, CommonWebViewActivity.class);
                            intent.putExtra("TAG", "banner");
                            intent.putExtra("title", bannerData.get(newPosition).getBody().getName());
                            intent.putExtra("content", bannerData.get(newPosition).getBody().getDescription());
                            intent.putExtra("imgUrl", bannerData.get(newPosition).getBody().getImage());
                            intent.putExtra("url", bannerData.get(newPosition).getBody().getUrl());
                            startActivity(intent);
                        }
                    }
                });
                return imageView;
            }

            // 删除条目
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        // 监听banner的滑动
        projectVpBanner.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                int newPosition = position % imageViews.size();
                // 把前一个点变成normal
                projectBannerPoints.getChildAt(prePointIndex).setEnabled(false);
                if (projectBannerPoints.getChildAt(newPosition - 1) != null) {
                    projectBannerPoints.getChildAt(newPosition - 1).setEnabled(false);
                }
                if (projectBannerPoints.getChildAt(newPosition + 3) != null) {
                    projectBannerPoints.getChildAt(newPosition + 3).setEnabled(false);
                }
                // 把相应位置的点变成selected
                projectBannerPoints.getChildAt(newPosition).setEnabled(true);
                // 当滑到某一页时，改变文字
                projectBannerTitle.setText(bannerData.get(newPosition).getBody().getName());
                projectBannerDesc.setText(bannerData.get(newPosition).getBody().getDescription());
                // 改变圆形进度条
                if (bannerData.get(newPosition).getType().equals("Project")) {
                    if (bannerData.get(newPosition).getExtr().getRoadshows().get(0).getRoadshowplan().getFinancedMount() == 0) {
                        projectBannerBottombg.setVisibility(View.GONE);
                        rlProgress.setVisibility(View.GONE);
                    } else {
                        projectBannerBottombg.setVisibility(View.VISIBLE);
                        rlProgress.setVisibility(View.VISIBLE);
                        bannerProgress.setTextBottom(String.valueOf(bannerData.get(newPosition).getExtr().getRoadshows().get(0).getRoadshowplan().getFinanceTotal()));
                        proTotal = (int) ((double) (bannerData.get(newPosition).getExtr().getRoadshows().get(0).getRoadshowplan().getFinancedMount()) / (double) (bannerData.get(newPosition).getExtr().getRoadshows().get(0).getRoadshowplan().getFinanceTotal()) * 100);
                        // banner的圆形进度条开始动
                        startBannerProgress();

                        // 控制banner进度条，更新UI
                        if (mHandler == null) {
                            mHandler = new Handler() {
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
                        }

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
        projectBannerTitle.setText(bannerData.get(0).getBody().getName());
        projectBannerDesc.setText(bannerData.get(0).getBody().getDescription());
        // 初始化第0个点
        projectBannerPoints.getChildAt(0).setEnabled(true);
        // 实现往复无限滑动，设置当前条目位置为一个大值
        int center = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imageViews.size();
        projectVpBanner.setCurrentItem(center);

        // 自动轮播
        if (isAuto) {
            handler = new Handler();
            bannerRunnable = new Runnable() {
                @Override
                public void run() {
                    synchronized (projectVpBanner) {
                        projectVpBanner.setCurrentItem(projectVpBanner.getCurrentItem() + 1);
                        header.postDelayed(this, 4000);
                    }
                }
            };
            header.postDelayed(bannerRunnable, 4000);
        }

        // 设置tab的单选事件
        projectRgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.project_rbtn_roadshow:// 选择了路演项目
                        flag = 0;
                        projectRbtnRoadshow.setTextColor(UiUtils.getColor(R.color.custom_orange));
                        projectRbtnPreselection.setTextColor(UiUtils.getColor(R.color.bg_text));
                        isAuto = false;
                        myAdapter.notifyDataSetChanged();
                        break;
                    case R.id.project_rbtn_preselection:// 选择了预选项目
                        flag = 1;
                        projectRbtnPreselection.setTextColor(UiUtils.getColor(R.color.custom_orange));
                        projectRbtnRoadshow.setTextColor(UiUtils.getColor(R.color.bg_text));
                        isAuto = false;
                        myAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });
        listview.addHeaderView(header);
    }

    private class MyAdapter extends BaseAdapter {
        private ViewHolder holder = null;

        @Override
        public int getCount() {
            if (flag == 0) {
                if (rDatas != null && rDatas.size() != 0) {
                    return rDatas.size();
                } else {
                    return 0;
                }
            } else {
                if (pDatas != null && pDatas.size() != 0) {
                    return pDatas.size();
                } else {
                    return 0;
                }
            }
        }

        @Override
        public Object getItem(int position) {
            if (flag == 0) {
                return rDatas.get(position);
            } else {
                return pDatas.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (flag == 0) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                if (getItemViewType(position) == 0) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_project_roadshow, null);
                    holder.itemProjectImg = (CircleImageView) convertView.findViewById(R.id.item_project_img);
                    holder.itemProjectTitle = (TextView) convertView.findViewById(R.id.item_project_title);
                    holder.itemProjectAddr = (TextView) convertView.findViewById(R.id.item_project_addr);
                    holder.itemCompletedTag = (ImageView) convertView.findViewById(R.id.iv_tag_completed);
                    holder.itemProjectCompname = (TextView) convertView.findViewById(R.id.item_project_compname);
                    holder.itemProjectField1 = (TextView) convertView.findViewById(R.id.item_project_field1);
                    holder.itemProjectField2 = (TextView) convertView.findViewById(R.id.item_project_field2);
                    holder.itemProjectField3 = (TextView) convertView.findViewById(R.id.item_project_field3);
                    holder.itemProjectPopularity = (TextView) convertView.findViewById(R.id.item_project_popularity);
                    holder.itemProjectTime = (TextView) convertView.findViewById(R.id.item_project_time);
                    holder.itemProjectAmount = (TextView) convertView.findViewById(R.id.item_project_amount);
                    holder.itemProjectProgress = (RoundProgressBar) convertView.findViewById(R.id.item_project_progress);
                } else {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_project_preselect, null);
                    holder.itemProjectImg = (CircleImageView) convertView.findViewById(R.id.item_project_img);
                    holder.itemProjectTitle = (TextView) convertView.findViewById(R.id.item_project_title);
                    holder.itemProjectAddr = (TextView) convertView.findViewById(R.id.item_project_addr);
                    holder.itemProjectCompname = (TextView) convertView.findViewById(R.id.item_project_compname);
                    holder.itemProjectField1 = (TextView) convertView.findViewById(R.id.item_project_field1);
                    holder.itemProjectField2 = (TextView) convertView.findViewById(R.id.item_project_field2);
                    holder.itemProjectField3 = (TextView) convertView.findViewById(R.id.item_project_field3);
                    holder.itemProjectPopularity = (TextView) convertView.findViewById(R.id.item_project_popularity);
                    holder.itemProjectAmount = (TextView) convertView.findViewById(R.id.item_project_amount);
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (getItemViewType(position) == 0) {
                // 路演项目列表
                Glide.with(mContext).load(rDatas.get(position).getStartPageImage()).into(holder.itemProjectImg);
                holder.itemProjectTitle.setText(rDatas.get(position).getAbbrevName());
                holder.itemProjectAddr.setText(rDatas.get(position).getAddress());
                if ("融资成功".equals(rDatas.get(position).getFinancestatus().getName())) {
                    holder.itemCompletedTag.setVisibility(View.VISIBLE);
                } else {
                    holder.itemCompletedTag.setVisibility(View.INVISIBLE);
                }
                holder.itemProjectCompname.setText(rDatas.get(position).getFullName());
                String[] fields = rDatas.get(position).getIndustoryType().split("，");
                if (fields.length == 0) {
                    holder.itemProjectField1.setVisibility(View.INVISIBLE);
                    holder.itemProjectField2.setVisibility(View.INVISIBLE);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 1) {
                    holder.itemProjectField1.setVisibility(View.VISIBLE);
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setVisibility(View.INVISIBLE);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 2) {
                    holder.itemProjectField1.setVisibility(View.VISIBLE);
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setVisibility(View.VISIBLE);
                    holder.itemProjectField2.setText(fields[1]);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 3) {
                    holder.itemProjectField1.setVisibility(View.VISIBLE);
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setVisibility(View.VISIBLE);
                    holder.itemProjectField2.setText(fields[1]);
                    holder.itemProjectField3.setVisibility(View.VISIBLE);
                    holder.itemProjectField3.setText(fields[2]);
                }
                holder.itemProjectPopularity.setText(String.valueOf(rDatas.get(position).getCollectionCount()));
                holder.itemProjectTime.setText(String.valueOf(rDatas.get(position).getTimeLeft()) + "天");
                holder.itemProjectAmount.setText(rDatas.get(position).getRoadshows().get(0).getRoadshowplan().getFinanceTotal() + "万");
                int progress = (int) ((double) (rDatas.get(position).getRoadshows().get(0).getRoadshowplan().getFinancedMount()) / (double) (rDatas.get(position).getRoadshows().get(0).getRoadshowplan().getFinanceTotal()) * 100);
                holder.itemProjectProgress.setProgress(progress);
            } else {
                // 预选项目列表
                Glide.with(mContext).load(pDatas.get(position).getStartPageImage()).into(holder.itemProjectImg);
                holder.itemProjectTitle.setText(pDatas.get(position).getAbbrevName());
                holder.itemProjectAddr.setText(pDatas.get(position).getAddress());
                holder.itemProjectCompname.setText(pDatas.get(position).getFullName());
                String[] fields = pDatas.get(position).getIndustoryType().split("，");
                if (fields.length == 0) {
                    holder.itemProjectField1.setVisibility(View.INVISIBLE);
                    holder.itemProjectField2.setVisibility(View.INVISIBLE);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 1) {
                    holder.itemProjectField1.setVisibility(View.VISIBLE);
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setVisibility(View.INVISIBLE);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 2) {
                    holder.itemProjectField1.setVisibility(View.VISIBLE);
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setVisibility(View.VISIBLE);
                    holder.itemProjectField2.setText(fields[1]);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 3) {
                    holder.itemProjectField1.setVisibility(View.VISIBLE);
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setVisibility(View.VISIBLE);
                    holder.itemProjectField2.setText(fields[1]);
                    holder.itemProjectField3.setVisibility(View.VISIBLE);
                    holder.itemProjectField3.setText(fields[2]);
                }
                holder.itemProjectPopularity.setText(String.valueOf(pDatas.get(position).getCollectionCount()));
                holder.itemProjectAmount.setText(pDatas.get(position).getRoadshows().get(0).getRoadshowplan().getFinanceTotal() + "万");
            }
            return convertView;
        }

        class ViewHolder {
            private CircleImageView itemProjectImg;
            private TextView itemProjectTitle;
            private TextView itemProjectAddr;
            private ImageView itemCompletedTag;// 路演项目独有
            private TextView itemProjectCompname;
            private TextView itemProjectField1;
            private TextView itemProjectField2;
            private TextView itemProjectField3;
            private TextView itemProjectPopularity;
            private TextView itemProjectTime;// 路演项目独有
            private TextView itemProjectAmount;
            private RoundProgressBar itemProjectProgress;// 路演项目独有
        }
    }

    private void startBannerProgress() {
        thread = new ProThread();
        thread.start();
    }

    // banner的圆形进度条的线程
    private class ProThread extends Thread {
        @Override
        public void run() {
            while (!progressStop) {
                try {
                    progress += 1;
                    Message msg = new Message();
                    msg.obj = progress;
                    Thread.sleep(30);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_btn_left:// 点击进入站内信
                Intent intent = new Intent(mContext, MessageActivity.class);
                startActivity(intent);
                titleIvLeft.setImageResource(R.mipmap.message_empty);
                break;
            case R.id.btn_tryagain:// 重试加载网络
                if (clickable) {
                    clickable = false;
                    GetBannerInfo getBannerInfo = new GetBannerInfo();
                    getBannerInfo.execute();
                }
                break;
        }
    }

    // 获取banner数据
    private class GetBannerInfo extends AsyncTask<Void, Void, BannerInfoBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (StringUtils.isBlank(cacheBanner) && StringUtils.isBlank(cacheRoadshowList) && StringUtils.isBlank(cachePreselectionList)) {
                showProgressDialog();
            }
        }

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
            clickable = true;
            if (bannerInfoBean == null) {
                dismissProgressDialog();
//                pageError.setVisibility(View.VISIBLE);
//                refreshView.setVisibility(View.GONE);
            } else {
                if (bannerInfoBean.getStatus() == 200) {
                    pageError.setVisibility(View.GONE);
                    refreshView.setVisibility(View.VISIBLE);
                    bannerData = bannerInfoBean.getData();
                    if (bannerData != null && bannerData.size() != 0) {
                        listview.setBackgroundResource(R.color.bg_main);
                        initListHeader();
                        // 缓存数据
                        CacheUtils.saveObject(JSON.toJSONString(bannerInfoBean), Constant.CACHE_BANNER);

                        GetRoadshowProjectListTask getRoadshowProjectListTask = new GetRoadshowProjectListTask(0);
                        getRoadshowProjectListTask.execute();
                        GetPreselectionProjectListTask getPreselectionProjectListTask = new GetPreselectionProjectListTask(0);
                        getPreselectionProjectListTask.execute();
                    } else {
//                        listview.setBackgroundResource(R.mipmap.bg_empty);
                    }
                } else {
                    dismissProgressDialog();
//                    pageError.setVisibility(View.VISIBLE);
//                    refreshView.setVisibility(View.GONE);
                }
            }
        }
    }

    // 获取路演项目列表
    private class GetRoadshowProjectListTask extends AsyncTask<Void, Void, RoadshowProjectListBean> {
        private int page;

        public GetRoadshowProjectListTask(int page) {
            this.page = page;
        }

        @Override
        protected RoadshowProjectListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETPROJECTLIST)),
                            "page", String.valueOf(page),
                            "type", "0",
                            Constant.BASE_URL + Constant.GETPROJECTLIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("路演项目列表", body);
                return FastJsonTools.getBean(body, RoadshowProjectListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(RoadshowProjectListBean roadshowProjectListBean) {
            super.onPostExecute(roadshowProjectListBean);
            dismissProgressDialog();
            if (roadshowProjectListBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (roadshowProjectListBean.getStatus() == 200) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        rDatas = roadshowProjectListBean.getData();
                        if (rDatas != null) {
                            listview.setAdapter(myAdapter);
                            // 缓存数据
                            CacheUtils.saveObject(JSON.toJSONString(roadshowProjectListBean), Constant.CACHE_ROADSHOW_LIST);
                        }
                    } else {
                        if (roadshowProjectListBean.getData() != null && roadshowProjectListBean.getData().size() != 0) {
                            for (RoadshowProjectListBean.DataBean dataBean : roadshowProjectListBean.getData()) {
                                rDatas.add(dataBean);
                            }
                            isAuto = false;
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                } else if (roadshowProjectListBean.getStatus() == 201) {
                    rPages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, roadshowProjectListBean.getMessage());
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                }
            }
        }
    }

    // 获取预选项目列表
    private class GetPreselectionProjectListTask extends AsyncTask<Void, Void, PreselectionProjectListBean> {
        private int page;

        public GetPreselectionProjectListTask(int page) {
            this.page = page;
        }

        @Override
        protected PreselectionProjectListBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETPROJECTLIST)),
                            "page", String.valueOf(page),
                            "type", "1",
                            Constant.BASE_URL + Constant.GETPROJECTLIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("预选项目列表", body);
                return FastJsonTools.getBean(body, PreselectionProjectListBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(PreselectionProjectListBean preselectionProjectListBean) {
            super.onPostExecute(preselectionProjectListBean);
            if (preselectionProjectListBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, R.string.net_error);
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (preselectionProjectListBean.getStatus() == 200) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        pDatas = preselectionProjectListBean.getData();
                        if (pDatas != null) {
                            listview.setAdapter(myAdapter);
                            // 缓存数据
                            CacheUtils.saveObject(JSON.toJSONString(preselectionProjectListBean), Constant.CACHE_PRESELECTION_LIST);
                        }
                    } else {
                        if (preselectionProjectListBean.getData() != null && preselectionProjectListBean.getData().size() != 0) {
                            for (PreselectionProjectListBean.DataBean dataBean : preselectionProjectListBean.getData()) {
                                pDatas.add(dataBean);
                            }
                            isAuto = false;
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                } else if (preselectionProjectListBean.getStatus() == 201) {
                    pPages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, preselectionProjectListBean.getMessage());
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                }
            }
        }
    }

    // 下拉刷新和上拉加载
    private class PullListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉刷新
            if (flag == 0) {
                rPages = 0;
                GetRoadshowProjectListTask getRoadshowProjectListTask = new GetRoadshowProjectListTask(0);
                getRoadshowProjectListTask.execute();
            } else {
                pPages = 0;
                GetPreselectionProjectListTask getPreselectionProjectListTask = new GetPreselectionProjectListTask(0);
                getPreselectionProjectListTask.execute();
            }
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            if (flag == 0) {
                rPages++;
                Log.i("路演项目页码", String.valueOf(rPages));
                GetRoadshowProjectListTask getRoadshowProjectListTask = new GetRoadshowProjectListTask(rPages);
                getRoadshowProjectListTask.execute();
            } else {
                pPages++;
                Log.i("预选项目页码", String.valueOf(pPages));
                GetPreselectionProjectListTask getPreselectionProjectListTask = new GetPreselectionProjectListTask(pPages);
                getPreselectionProjectListTask.execute();
            }
        }
    }

    // 是否有未读的站内信
    private class HaveNotReadMessageTask extends AsyncTask<Void, Void, HaveNotReadMessageBean> {
        @Override
        protected HaveNotReadMessageBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.HAVENOTREADMESSAGE)),
                            Constant.BASE_URL + Constant.HAVENOTREADMESSAGE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("是否有未读站内信", body);
                return FastJsonTools.getBean(body, HaveNotReadMessageBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(HaveNotReadMessageBean haveNotReadMessageBean) {
            super.onPostExecute(haveNotReadMessageBean);
            if (haveNotReadMessageBean != null && haveNotReadMessageBean.getStatus() == 200 && haveNotReadMessageBean.getData().isFlag()) {
                titleIvLeft.setImageResource(R.mipmap.message_full);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            if (resultCode == RoadshowDetailsActivity.RESULT_CODE) {
                if (data.getIntExtra("FLAG", 0) == 1) {// 在详情中关注了项目
                    rDatas.get(rPOSITION).setCollectionCount(rDatas.get(rPOSITION).getCollectionCount() + 1);
                } else if (data.getIntExtra("FLAG", 0) == 2) {// 在详情中取消了关注
                    rDatas.get(rPOSITION).setCollectionCount(rDatas.get(rPOSITION).getCollectionCount() - 1);
                }
                myAdapter.notifyDataSetChanged();
            }
            if (resultCode == PreselectionDetailsActivity.RESULT_CODE) {
                if (data.getIntExtra("FLAG", 0) == 1) {// 在详情中关注了项目
                    pDatas.get(pPOSITION).setCollectionCount(pDatas.get(pPOSITION).getCollectionCount() + 1);
                } else if (data.getIntExtra("FLAG", 0) == 2) {// 在详情中取消了关注
                    pDatas.get(pPOSITION).setCollectionCount(pDatas.get(pPOSITION).getCollectionCount() - 1);
                }
                myAdapter.notifyDataSetChanged();
            }
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
        isAuto = false;
        if (thread != null) {// 停止进度条
            thread.stopThread();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isAuto = false;
        if (thread != null) {// 停止进度条
            thread.stopThread();
        }
        EventBus.getDefault().unregister(this);
    }
}
