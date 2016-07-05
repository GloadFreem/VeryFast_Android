package com.jinzht.pro.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.activity.CommonWebViewActivity;
import com.jinzht.pro.activity.ImagePagerActivity;
import com.jinzht.pro.adapter.ProjectPhotosAdapter;
import com.jinzht.pro.adapter.ProjectReportsAdapter;
import com.jinzht.pro.adapter.ProjectTeamsAdapter;
import com.jinzht.pro.adapter.RecyclerViewData;
import com.jinzht.pro.base.BaseFragment;
import com.jinzht.pro.bean.ProjectDetailBean;
import com.jinzht.pro.callback.ItemClickListener;
import com.jinzht.pro.view.RoundProgressBar;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 路演项目详情页中的详情
 */
public class RoadshowDetailsFragment extends BaseFragment implements View.OnClickListener {

    private RoundProgressBar roadshowProgress;// 圆形进度条
    private ImageView ivTag;// 融资状态标签
    private TextView tvTitle;// 项目标题
    private TextView tvAmountTotal;// 融资金额
    private TextView tvAmountFinanced;// 已融金额
    private TextView tvTime;// 起止时间
    private TextView tvAddr;// 公司所在地
    private TextView tvDesc;// 项目描述
    private RecyclerView rvPhotos;// 项目照片
    private RelativeLayout btnMore;// 更多按钮
    private ImageView ivMore;// 更多按钮图标
    private RecyclerView rvTeams;// 团队成员表
    private RecyclerView rvReports;// 项目报表

    private boolean isOpen = false;// 项目描述的开关状态

    private ArrayList<String> photos = new ArrayList<>();// 项目照片资源
    private ProjectPhotosAdapter photosAdapter;// 项目照片的适配器

    private List<String> favicons = new ArrayList<>();// 团队成员图片资源
    private List<String> names = new ArrayList<>();// 团队成员姓名集合
    private List<String> positions = new ArrayList<>();// 团队成员职位集合
    private ProjectTeamsAdapter teamsAdapter;// 团队成员数据适配器

    private List<String> reportImgs = new ArrayList<>();// 报表图标
    private List<String> reportNames = new ArrayList<>();// 报表名
    private ProjectReportsAdapter reportsAdapter;// 各类报表数据适配器

    private ProjectDetailBean.DataBean.ProjectBean data;// 项目数据
    private List<ProjectDetailBean.DataBean.ExtrBean> reportDatas = new ArrayList<>();// 报表数据
    public final static int RESULT_CODE = 0;
    public boolean needRefresh = false;// 是否进行了交互，返回时是否刷新

    private boolean progressStop = false;// 圆形进度条正在滑动的标识
    private ProThread progressThread;// 圆形进度条的线程
    private int proTotal = 0;// 要显示的全部进度 = 已融资/需融资*100
    private int progress = 0;// 当前进度

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roadshow_details, container, false);
        roadshowProgress = (RoundProgressBar) view.findViewById(R.id.roadshow_progress);// 圆形进度条
        ivTag = (ImageView) view.findViewById(R.id.iv_tag);// 融资状态标签
        tvTitle = (TextView) view.findViewById(R.id.roadshow_tv_title);// 项目标题
        tvAmountTotal = (TextView) view.findViewById(R.id.roadshow_amount_plan);// 融资金额
        tvAmountFinanced = (TextView) view.findViewById(R.id.roadshow_amount_financed);// 已融金额
        tvTime = (TextView) view.findViewById(R.id.roadshow_tv_time);// 起止时间
        tvAddr = (TextView) view.findViewById(R.id.roadshow_tv_addr);// 公司所在地
        tvDesc = (TextView) view.findViewById(R.id.roadshow_tv_desc);// 项目描述
        rvPhotos = (RecyclerView) view.findViewById(R.id.roadshow_rv_photos);// 项目照片
        btnMore = (RelativeLayout) view.findViewById(R.id.roadshow_btn_more);// 更多按钮
        btnMore.setOnClickListener(this);
        ivMore = (ImageView) view.findViewById(R.id.roadshow_img_more);// 更多按钮图标
        rvTeams = (RecyclerView) view.findViewById(R.id.project_rv_teams);// 团队成员表
        rvReports = (RecyclerView) view.findViewById(R.id.project_rv_reports);// 项目报表
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void getDetailData(ProjectDetailBean.DataBean bean) {
        data = bean.getProject();
        reportDatas = bean.getExtr();
        if (data != null) {
            initData();
        }
        if (reportDatas.size() != 0) {
            // 报表处理
            initReport();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.roadshow_btn_more:// 显示更多描述信息和照片
                if (isOpen) {// 打开状态，点击关闭
                    descClose();
                    isOpen = false;
                } else {// 关闭状态，点击打开
                    descOpen();
                    isOpen = true;
                }
                break;
        }
    }

    // 闭合TextView
    private void descClose() {
        tvDesc.setEllipsize(TextUtils.TruncateAt.END);
        tvDesc.setMaxLines(3);
        ivMore.setBackgroundResource(R.mipmap.icon_bottom_more);
        // 项目照片
        photos.clear();
        if (data.getProjectimageses().size() == 0) {
            return;
        } else if (data.getProjectimageses().size() == 1) {
            photos.add(data.getProjectimageses().get(0).getImageUrl());
        } else if (data.getProjectimageses().size() == 2) {
            photos.add(data.getProjectimageses().get(0).getImageUrl());
            photos.add(data.getProjectimageses().get(1).getImageUrl());
        } else if (data.getProjectimageses().size() >= 3) {
            photos.add(data.getProjectimageses().get(0).getImageUrl());
            photos.add(data.getProjectimageses().get(1).getImageUrl());
            photos.add(data.getProjectimageses().get(2).getImageUrl());
        }
        photosAdapter.notifyDataSetChanged();
    }

    // 打开TextView
    private void descOpen() {
        tvDesc.setEllipsize(null);
        tvDesc.setMaxLines(Integer.MAX_VALUE);
        ivMore.setBackgroundResource(R.mipmap.icon_bottom_less);
        // 项目照片
        photos.clear();
        if (data.getProjectimageses().size() == 0) {
            return;
        }
        for (ProjectDetailBean.DataBean.ProjectBean.ProjectimagesesBean bean : data.getProjectimageses()) {
            photos.add(bean.getImageUrl());
        }
        photosAdapter.notifyDataSetChanged();
    }

    // 加载数据
    private void initData() {
        switch (data.getFinancestatus().getName()) {
            case "待路演":
                ivTag.setBackgroundResource(R.mipmap.tag_detail_dailuyan);
                break;
            case "融资中":
                ivTag.setBackgroundResource(R.mipmap.tag_detail_rongzizhong);
                break;
            case "融资成功":
                ivTag.setBackgroundResource(R.mipmap.tag_detail_comlete);
                break;
            case "融资失败":
                ivTag.setBackgroundResource(R.mipmap.tag_detail_fail);
                break;
        }
        tvTitle.setText(data.getAbbrevName());
        tvAmountTotal.setText(data.getRoadshows().get(0).getRoadshowplan().getFinanceTotal() + "万");
        tvAmountFinanced.setText(data.getRoadshows().get(0).getRoadshowplan().getFinancedMount() + "万");
        String beginDate = data.getRoadshows().get(0).getRoadshowplan().getBeginDate().substring(0, 10).replaceAll("-", ".");
        String endDate = data.getRoadshows().get(0).getRoadshowplan().getEndDate().substring(0, 10).replaceAll("-", ".");
        tvTime.setText(beginDate + "——" + endDate);
        tvAddr.setText(data.getAddress());
        tvDesc.setText(data.getDescription());
        // 项目照片处理
        initPhotos();
        // 团队成员处理
        initTeam();
        // 圆形进度条处理
        proTotal = (int) ((double) (data.getRoadshows().get(0).getRoadshowplan().getFinancedMount()) / (double) (data.getRoadshows().get(0).getRoadshowplan().getFinanceTotal()) * 100);
        progressThread = new ProThread();
        progressThread.start();
        if (progressStop) {
            roadshowProgress.setProgress(proTotal);
        }
    }

    // 项目照片处理
    private void initPhotos() {
        // 准备数据
        photos.clear();
        if (data.getProjectimageses().size() == 0) {
            return;
        } else if (data.getProjectimageses().size() == 1) {
            photos.add(data.getProjectimageses().get(0).getImageUrl());
        } else if (data.getProjectimageses().size() == 2) {
            photos.add(data.getProjectimageses().get(0).getImageUrl());
            photos.add(data.getProjectimageses().get(1).getImageUrl());
        } else if (data.getProjectimageses().size() >= 3) {
            photos.add(data.getProjectimageses().get(0).getImageUrl());
            photos.add(data.getProjectimageses().get(1).getImageUrl());
            photos.add(data.getProjectimageses().get(2).getImageUrl());
        }
        photosAdapter = new ProjectPhotosAdapter(mContext, photos);
        // 填充数据
        RecyclerViewData.setGrid(rvPhotos, mContext, photosAdapter, 3);
        // 项目照片的点击事件
        photosAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                imageBrower(position, photos);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemSubViewClick(View view, int position) {

            }
        });
    }

    // 查看大图
    private void imageBrower(int position, ArrayList<String> urls) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    // 团队成员处理
    private void initTeam() {
        // 准备数据
        favicons.clear();
        names.clear();
        positions.clear();
        if (data.getTeams() == null) {
            return;
        }
        for (ProjectDetailBean.DataBean.ProjectBean.TeamsBean bean : data.getTeams()) {
            favicons.add(bean.getIcon());
            names.add(bean.getName());
            positions.add(bean.getPosition());
        }
        teamsAdapter = new ProjectTeamsAdapter(mContext, favicons, names, positions);
        // 填充数据
        RecyclerViewData.setHorizontal(rvTeams, mContext, teamsAdapter);
        // 团队成员的点击事件
        teamsAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, CommonWebViewActivity.class);
                intent.putExtra("title", "团队成员");
                intent.putExtra("url", data.getTeams().get(position).getUrl());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemSubViewClick(View view, int position) {

            }
        });
    }

    // 报表处理
    private void initReport() {
        // 准备数据
        reportImgs.clear();
        reportNames.clear();
        if (reportDatas == null) {
            return;
        }
        for (ProjectDetailBean.DataBean.ExtrBean bean : reportDatas) {
            reportImgs.add(bean.getIcon());
            reportNames.add(bean.getContent());
        }
        reportsAdapter = new ProjectReportsAdapter(mContext, reportImgs, reportNames);
        // 填充数据
        RecyclerViewData.setHorizontal(rvReports, mContext, reportsAdapter);
        // 报表点击事件
        reportsAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, CommonWebViewActivity.class);
                intent.putExtra("title", reportDatas.get(position).getContent());
                intent.putExtra("url", reportDatas.get(position).getUrl());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemSubViewClick(View view, int position) {

            }
        });
    }

    // 项目圆形进度条的线程
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

    // 控制项目圆形进度条，更新UI
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int temp = (int) msg.obj;
            if (proTotal - temp > 0) {
                roadshowProgress.setProgress(temp);
            } else {
                roadshowProgress.setProgress(proTotal);
                progressThread.stopThread();
            }
        }
    };

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressThread != null) {// 停止进度条
            progressThread.stopThread();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressThread != null) {// 停止进度条
            progressThread.stopThread();
        }
        EventBus.getDefault().unregister(this);
    }
}
