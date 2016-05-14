package com.jinzht.pro1.activity;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.ProjectPhotosAdapter;
import com.jinzht.pro1.adapter.ProjectReportsAdapter;
import com.jinzht.pro1.adapter.ProjectTeamsAdapter;
import com.jinzht.pro1.adapter.RecyclerViewData;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.callback.ItemClickListener;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;
import com.jinzht.pro1.view.CircleImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 预选项目详情
 */
public class PreselectionDetailsActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout titleBtnLeft;// 返回键
    private TextView tvTitle;// 本页面标题，空
    private LinearLayout titleBtnRight2;// 收藏按钮
    private LinearLayout titleBtnRight;// 分享按钮
    private ImageButton preBtnService;// 客服按钮
    private RelativeLayout preBtnCollect;// 底部关注按钮
    private TextView preTvCollect;// 底部带星星的关注文字
    private TextView preTvCollectQuantity;// 关注数量
    private CircleImageView preIvImg;// 项目图标
    private TextView preTvTitle;// 顶部项目标题
    private TextView preTvCompanyName;// 公司名称
    private TextView preTvField;// 投资领率
    private TextView preTvAddr;// 公司所在地
    private TextView preTvTitle2;// 中间的项目标题
    private TextView preTvDesc;// 项目描述
    private RecyclerView preRvPhotos;// 项目照片
    private RelativeLayout preBtnMore;// 查看更多描述和照片按钮
    private ImageButton preBtnMoreImg;// 查看更多的图标
    private RecyclerView projectRvTeams;// 团队成员照片
    private RecyclerView projectRvReports;// 各类报表图标
    private TextView preTvCommentQuantity;// 评论数量
    private TextView preBtnMoreComment;// 查看更多评论按钮
    private ImageButton preBtnComment;// 评论按钮

    private boolean isOpen = false;// 项目描述的开关状态

    private List<Integer> photos1;// 闭合状态项目照片资源
    private List<Integer> photos2;// 展开状态项目照片资源
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
    protected int getResourcesId() {
        return R.layout.activity_preselection_details;
    }

    @Override
    protected void init() {
        UiHelp.setTranslucentStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        titleBtnLeft = (LinearLayout) findViewById(R.id.title_btn_left);// 返回键
        titleBtnLeft.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 本页标题
        tvTitle.setVisibility(View.GONE);
        titleBtnRight2 = (LinearLayout) findViewById(R.id.title_btn_right2);// 收藏按钮
        titleBtnRight2.setOnClickListener(this);
        titleBtnRight = (LinearLayout) findViewById(R.id.title_btn_right);// 分享按钮
        titleBtnRight.setOnClickListener(this);
        preBtnService = (ImageButton) findViewById(R.id.pre_btn_service);// 客服按钮
        preBtnService.setOnClickListener(this);
        preBtnCollect = (RelativeLayout) findViewById(R.id.pre_btn_collect);// 底部关注按钮
        preBtnCollect.setOnClickListener(this);
        preTvCollect = (TextView) findViewById(R.id.pre_tv_collect);// 底部带星星的关注文字
        preTvCollectQuantity = (TextView) findViewById(R.id.pre_tv_collect_quantity);// 关注数量
        preIvImg = (CircleImageView) findViewById(R.id.pre_iv_img);// 项目图标
        preTvTitle = (TextView) findViewById(R.id.pre_tv_title);// 顶部项目标题
        preTvCompanyName = (TextView) findViewById(R.id.pre_tv_company_name);// 公司名称
        preTvField = (TextView) findViewById(R.id.pre_tv_field);// 投资领率
        preTvAddr = (TextView) findViewById(R.id.pre_tv_addr);// 公司所在地
        preTvTitle2 = (TextView) findViewById(R.id.pre_tv_title2);// 中间的项目标题
        preTvDesc = (TextView) findViewById(R.id.pre_tv_desc);// 项目描述
        preRvPhotos = (RecyclerView) findViewById(R.id.pre_rv_photos);// 项目照片
        preBtnMore = (RelativeLayout) findViewById(R.id.pre_btn_more);// 查看更多描述和照片按钮
        preBtnMore.setOnClickListener(this);
        preBtnMoreImg = (ImageButton) findViewById(R.id.pre_btn_more_img);// 查看更多的图标
        projectRvTeams = (RecyclerView) findViewById(R.id.project_rv_teams);// 团队成员照片
        projectRvReports = (RecyclerView) findViewById(R.id.project_rv_reports);// 各类报表图标
        preTvCommentQuantity = (TextView) findViewById(R.id.pre_tv_comment_quantity);// 评论数量
        preBtnMoreComment = (TextView) findViewById(R.id.pre_btn_more_comment);// 查看更多评论按钮
        preBtnMoreComment.setOnClickListener(this);

        preBtnComment = (ImageButton) findViewById(R.id.pre_btn_comment);// 评论按钮
        preBtnComment.setOnClickListener(this);

        // 项目照片处理
        initPhotos();
        // 团队成员处理
        initTeam();
        // 报表处理
        initReport();
        // 评论列表处理
        initComment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_btn_left:// 返回上一页
                finish();
                break;
            case R.id.title_btn_right2:// 点击收藏
                SuperToastUtils.showSuperToast(this, 2, "收藏");
                break;
            case R.id.title_btn_right:// 点击分享
                SuperToastUtils.showSuperToast(this, 2, "分享");
                break;
            case R.id.pre_btn_service:// 打电话给客服
                SuperToastUtils.showSuperToast(this, 2, "客服");
                break;
            case R.id.pre_btn_collect:// 点击关注
                SuperToastUtils.showSuperToast(this, 2, "关注");
                break;
            case R.id.pre_btn_more:// 点击查看更多描述和照片
                if (isOpen) {// 打开状态，点击关闭
                    descClose();
                    isOpen = false;
                } else {// 关闭状态，点击打开
                    descOpen();
                    isOpen = true;
                }
                break;
            case R.id.pre_btn_more_comment:// 点击查看跟多评论.
                SuperToastUtils.showSuperToast(this, 2, "更多评论");
                break;
            case R.id.pre_btn_comment:// 点击发表评论
                SuperToastUtils.showSuperToast(this, 2, "发表评论");
        }
    }

    // 闭合TextView
    private void descClose() {
        preTvDesc.setEllipsize(TextUtils.TruncateAt.END);
        preTvDesc.setMaxLines(3);
        preBtnMoreImg.setBackgroundResource(R.mipmap.icon_bottom_more);
        // 项目照片
        preRvPhotos.setAdapter(photosAdapter1);
    }

    // 打开TextView
    private void descOpen() {
        preTvDesc.setEllipsize(null);
        preTvDesc.setMaxLines(Integer.MAX_VALUE);
        preBtnMoreImg.setBackgroundResource(R.mipmap.icon_bottom_less);
        // 项目照片
        photos2 = new ArrayList<>(Arrays.asList(R.mipmap.photo1, R.mipmap.photo2, R.mipmap.photo1, R.mipmap.photo2, R.mipmap.photo1));
        photosAdapter2 = new ProjectPhotosAdapter(mContext, photos2);
        photosAdapter2.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                SuperToastUtils.showSuperToast(mContext, 2, "点击了" + postion + "张照片");
            }
        });
        preRvPhotos.setAdapter(photosAdapter2);
    }

    // 项目照片处理
    private void initPhotos() {
        // 准备数据
        photos1 = new ArrayList<>(Arrays.asList(R.mipmap.photo1, R.mipmap.photo2, R.mipmap.photo1));
        photosAdapter1 = new ProjectPhotosAdapter(mContext, photos1);
        // 填充数据
        RecyclerViewData.setGrid(preRvPhotos, mContext, photosAdapter1);
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
        RecyclerViewData.setHorizontal(projectRvTeams, mContext, teamsAdapter);
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
        RecyclerViewData.setHorizontal(projectRvReports, mContext, reportsAdapter);
        // 报表点击事件
        reportsAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                SuperToastUtils.showSuperToast(mContext, 2, "点击了" + postion + "张图片");
            }
        });
    }

    // 评论列表处理
    private void initComment() {

    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
