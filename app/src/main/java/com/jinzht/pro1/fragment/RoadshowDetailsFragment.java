package com.jinzht.pro1.fragment;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.HorizontalRecyclerViewData;
import com.jinzht.pro1.adapter.ProjectPhotosAdapter;
import com.jinzht.pro1.adapter.ProjectReportsAdapter;
import com.jinzht.pro1.adapter.ProjectTeamsAdapter;
import com.jinzht.pro1.base.BaseFragment;
import com.jinzht.pro1.callback.ItemClickListener;
import com.jinzht.pro1.utils.SuperToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
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
    private RecyclerView roadshowRvPhotos;// 项目照片
    private RelativeLayout roadshowBtnMore;// 更多按钮
    private ImageButton roadshowImgMore;// 更多按钮图标
    private TextView roadshowTag;// 融资状态标签
    private RecyclerView projectRvTeams;// 团队成员表
    private RecyclerView projectRvReports;// 项目报表
    private View emptyView;// 填充色块

    private boolean isOpen = false;// 项目描述的开关状态

    private List<Integer> imageViews1;// 闭合状态项目照片资源
    private List<Integer> imageViews2;// 展开状态项目照片资源
    private ProjectPhotosAdapter photosAdapter1;// 闭合状态项目照片的适配器
    private ProjectPhotosAdapter photosAdapter2;// 展开状态项目照片的适配器

    private List<Integer> favicons;// 团队成员图片资源
    private List<String> names;// 团队成员姓名集合
    private List<String> positions;// 团队成员职位集合
    private ProjectTeamsAdapter teamsAdapter;// 团队成员数据适配器

    private List<Integer> reportImgs;// 报表图标
    private List<String> reportNames;// 报表名
    private ProjectReportsAdapter reportsAdapter;// 各类报表数据适配器

    @Override
    protected int setLayout(LayoutInflater inflater) {
        return R.layout.fragment_roadshow_details;
    }

    @Override
    protected void onFirstUserVisible() {
        roadshowTvTitle = (TextView) mActivity.findViewById(R.id.roadshow_tv_title);// 项目标题
        roadshowAmountPlan = (TextView) mActivity.findViewById(R.id.roadshow_amount_plan);// 融资金额
        roadshowAmountFinanced = (TextView) mActivity.findViewById(R.id.roadshow_amount_financed);// 已融金额
        roadshowTvTime = (TextView) mActivity.findViewById(R.id.roadshow_tv_time);// 起止时间
        roadshowTvAddr = (TextView) mActivity.findViewById(R.id.roadshow_tv_addr);// 公司所在地
        roadshowTvDesc = (TextView) mActivity.findViewById(R.id.roadshow_tv_desc);// 项目描述
        roadshowRvPhotos = (RecyclerView) mActivity.findViewById(R.id.roadshow_rv_photos);// 项目照片
        roadshowBtnMore = (RelativeLayout) mActivity.findViewById(R.id.roadshow_btn_more);// 更多按钮
        roadshowBtnMore.setOnClickListener(this);
        roadshowImgMore = (ImageButton) mActivity.findViewById(R.id.roadshow_img_more);// 更多按钮图标
        roadshowTag = (TextView) mActivity.findViewById(R.id.roadshow_tag);// 融资状态标签
        projectRvTeams = (RecyclerView) mActivity.findViewById(R.id.project_rv_teams);// 团队成员表
        projectRvReports = (RecyclerView) mActivity.findViewById(R.id.project_rv_reports);// 项目报表
        emptyView = mActivity.findViewById(R.id.empty_view);
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
        roadshowRvPhotos.setAdapter(photosAdapter1);
    }

    // 打开TextView
    private void descOpen() {
        roadshowTvDesc.setEllipsize(null);
        roadshowTvDesc.setMaxLines(Integer.MAX_VALUE);
        roadshowImgMore.setBackgroundResource(R.mipmap.icon_bottom_less);
        // 项目照片
        imageViews2 = new ArrayList<Integer>(Arrays.asList(R.mipmap.photo1, R.mipmap.photo2, R.mipmap.photo1, R.mipmap.photo2, R.mipmap.photo1));
        photosAdapter2 = new ProjectPhotosAdapter(mContext, imageViews2);
        photosAdapter2.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                SuperToastUtils.showSuperToast(mContext, 2, "点击了" + postion + "张照片");
            }
        });
        roadshowRvPhotos.setAdapter(photosAdapter2);
    }

    // 项目照片处理
    private void initPhotos() {
        // 使RecyclerView保持固定的大小，该信息被用于自身的优化。
        roadshowRvPhotos.setHasFixedSize(true);
        // 列表布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        roadshowRvPhotos.setLayoutManager(gridLayoutManager);
        // 准备图片
        imageViews1 = new ArrayList<Integer>(Arrays.asList(R.mipmap.photo1, R.mipmap.photo2, R.mipmap.photo1));
        photosAdapter1 = new ProjectPhotosAdapter(mContext, imageViews1);
        roadshowRvPhotos.setAdapter(photosAdapter1);
        // 项目照片的点击事件
        photosAdapter1.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                SuperToastUtils.showSuperToast(mContext, 2, "点击了" + postion + "张照片");
            }
        });
    }

    // 团队成员处理
    private void initTeam() {
        // 准备数据
        favicons = new ArrayList<Integer>(Arrays.asList(R.mipmap.favicon_test, R.mipmap.favicon_test, R.mipmap.favicon_test, R.mipmap.favicon_test, R.mipmap.favicon_test, R.mipmap.favicon_test, R.mipmap.favicon_test));
        names = new ArrayList<>(Arrays.asList("张三丰", "李四", "王五", "赵六", "呵呵", "赵六", "呵呵"));
        positions = new ArrayList<>(Arrays.asList("项目经理", "UI", "Java", "IOS", "Android", "IOS", "Android"));
        teamsAdapter = new ProjectTeamsAdapter(mContext, favicons, names, positions);
        // 填充数据
        HorizontalRecyclerViewData.setData(projectRvTeams, mContext, teamsAdapter);
        // 团队成员的点击事件
        teamsAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                SuperToastUtils.showSuperToast(mContext, 2, "点击了" + postion + "张照片");
            }
        });
    }

    // 报表处理
    private void initReport() {
        // 准备数据
        reportImgs = new ArrayList<Integer>(Arrays.asList(R.mipmap.icon_report1, R.mipmap.icon_report2, R.mipmap.icon_report3, R.mipmap.icon_report4));
        reportNames = new ArrayList<>(Arrays.asList("财务\n状况", "融资\n方案", "退出\n渠道", "商业\n计划书"));
        reportsAdapter = new ProjectReportsAdapter(mContext, reportImgs, reportNames);
        // 填充数据
        HorizontalRecyclerViewData.setData(projectRvReports, mContext, reportsAdapter);
        // 报表点击事件
        reportsAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                SuperToastUtils.showSuperToast(mContext, 2, "点击了" + postion + "张图片");
            }
        });
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
}
