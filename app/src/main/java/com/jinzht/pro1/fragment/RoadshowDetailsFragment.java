package com.jinzht.pro1.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.ProjectPhotosAdapter;
import com.jinzht.pro1.adapter.ProjectReportsAdapter;
import com.jinzht.pro1.adapter.ProjectTeamsAdapter;
import com.jinzht.pro1.adapter.RecyclerViewData;
import com.jinzht.pro1.base.BaseFragment;
import com.jinzht.pro1.bean.ProjectCommentBean;
import com.jinzht.pro1.bean.ProjectDetailBean;
import com.jinzht.pro1.callback.ItemClickListener;
import com.jinzht.pro1.utils.SuperToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 路演项目详情页中的详情
 */
public class RoadshowDetailsFragment extends BaseFragment implements View.OnClickListener {

    private TextView roadshowTvTitle;// 项目标题
    private TextView roadshowAmountPlan;// 融资金额
    private TextView roadshowAmountFinanced;// 已融金额
    private TextView roadshowTvTime;// 起止时间
    private TextView roadshowTvAddr;// 公司所在地
    private TextView roadshowTvDesc;// 项目描述
    private RecyclerView rvPhotos;// 项目照片
    private RelativeLayout roadshowBtnMore;// 更多按钮
    private ImageButton roadshowImgMore;// 更多按钮图标
    private TextView roadshowTag;// 融资状态标签
    private RecyclerView rvTeams;// 团队成员表
    private RecyclerView projectRvReports;// 项目报表
    private View emptyView;// 填充色块

    private boolean isOpen = false;// 项目描述的开关状态

    private List<String> photos = new ArrayList<>();// 项目照片资源
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
    private List<ProjectCommentBean.DataBean> commentsData = new ArrayList<>();// 评论列表
    public final static int RESULT_CODE = 0;
    public boolean needRefresh = false;// 是否进行了交互，返回时是否刷新

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roadshow_details, container, false);
        roadshowTvTitle = (TextView) view.findViewById(R.id.roadshow_tv_title);// 项目标题
        roadshowAmountPlan = (TextView) view.findViewById(R.id.roadshow_amount_plan);// 融资金额
        roadshowAmountFinanced = (TextView) view.findViewById(R.id.roadshow_amount_financed);// 已融金额
        roadshowTvTime = (TextView) view.findViewById(R.id.roadshow_tv_time);// 起止时间
        roadshowTvAddr = (TextView) view.findViewById(R.id.roadshow_tv_addr);// 公司所在地
        roadshowTvDesc = (TextView) view.findViewById(R.id.roadshow_tv_desc);// 项目描述
        rvPhotos = (RecyclerView) view.findViewById(R.id.roadshow_rv_photos);// 项目照片
        roadshowBtnMore = (RelativeLayout) view.findViewById(R.id.roadshow_btn_more);// 更多按钮
        roadshowBtnMore.setOnClickListener(this);
        roadshowImgMore = (ImageButton) view.findViewById(R.id.roadshow_img_more);// 更多按钮图标
        roadshowTag = (TextView) view.findViewById(R.id.roadshow_tag);// 融资状态标签
        rvTeams = (RecyclerView) view.findViewById(R.id.project_rv_teams);// 团队成员表
        projectRvReports = (RecyclerView) view.findViewById(R.id.project_rv_reports);// 项目报表
        emptyView = view.findViewById(R.id.empty_view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        emptyView.setVisibility(View.GONE);
        // 项目照片处理
        initPhotos();
        // 团队成员处理
        initTeam();
        // 报表处理
        initReport();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.roadshow_btn_more:// 显示更多描述信息和照片
                if (isOpen) {// 打开状态，点击关闭
                    descClose();
                    isOpen = false;
                    emptyView.setVisibility(View.GONE);
                } else {// 关闭状态，点击打开
                    descOpen();
                    isOpen = true;
                    emptyView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    // 闭合TextView
    private void descClose() {
        roadshowTvDesc.setEllipsize(TextUtils.TruncateAt.END);
        roadshowTvDesc.setMaxLines(3);
        roadshowImgMore.setBackgroundResource(R.mipmap.icon_bottom_more);
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
        roadshowTvDesc.setEllipsize(null);
        roadshowTvDesc.setMaxLines(Integer.MAX_VALUE);
        roadshowImgMore.setBackgroundResource(R.mipmap.icon_bottom_less);
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

    // 项目照片处理
    private void initPhotos() {
        // 准备数据
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
                SuperToastUtils.showSuperToast(mContext, 2, "点击了" + position + "张照片");
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemSubViewClick(View view, int position) {

            }
        });
    }

    // 团队成员处理
    private void initTeam() {
        // 准备数据
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
                SuperToastUtils.showSuperToast(mContext, 2, "点击了" + position + "张照片");
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
        if (reportDatas == null) {
            return;
        }
        // 准备数据
        for (ProjectDetailBean.DataBean.ExtrBean bean : reportDatas) {
            reportImgs.add(bean.getIcon());
            reportNames.add(bean.getContent());
        }
        reportsAdapter = new ProjectReportsAdapter(mContext, reportImgs, reportNames);
        // 填充数据
        RecyclerViewData.setHorizontal(projectRvReports, mContext, reportsAdapter);
        // 报表点击事件
        reportsAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SuperToastUtils.showSuperToast(mContext, 2, "点击了" + position + "张图片");
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemSubViewClick(View view, int position) {

            }
        });
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
