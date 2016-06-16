package com.jinzht.pro1.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro1.R;
import com.jinzht.pro1.adapter.ProjectPhotosAdapter;
import com.jinzht.pro1.adapter.ProjectReportsAdapter;
import com.jinzht.pro1.adapter.ProjectTeamsAdapter;
import com.jinzht.pro1.adapter.RecyclerViewData;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.bean.CommonBean;
import com.jinzht.pro1.bean.ProjectDetailBean;
import com.jinzht.pro1.bean.ShareBean;
import com.jinzht.pro1.callback.ItemClickListener;
import com.jinzht.pro1.utils.AESUtils;
import com.jinzht.pro1.utils.Constant;
import com.jinzht.pro1.utils.FastJsonTools;
import com.jinzht.pro1.utils.MD5Utils;
import com.jinzht.pro1.utils.NetWorkUtils;
import com.jinzht.pro1.utils.OkHttpUtils;
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

    private LinearLayout btnBack;// 返回键
    private TextView tvTitle;// 本页面标题，空
    private LinearLayout btnCollect;// 收藏按钮
    private LinearLayout btnShare;// 分享按钮
    private ImageButton btnService;// 客服按钮
    private RelativeLayout btnBottomCollect;// 底部关注按钮
    private TextView tvCollect;// 底部带星星的关注文字
    private CircleImageView ivImg;// 项目图标
    private TextView tvProTitle;// 顶部项目标题
    private TextView tvCompanyName;// 公司名称
    private TextView tvAddr;// 公司所在地
    private TextView tvPorTitle2;// 中间的项目标题
    private TextView tvDesc;// 项目描述
    private RecyclerView rvPhotos;// 项目照片
    private RelativeLayout btnMore;// 查看更多描述和照片按钮
    private ImageButton btnMoreImg;// 查看更多的图标
    private RecyclerView rvTeams;// 团队成员照片
    private RecyclerView rvReports;// 各类报表图标
    private TextView tvCommentsQuantity;// 评论数量
    private TextView btnMoreComments;// 查看更多评论按钮
    private RelativeLayout rlComment1;// 评论1布局
    private CircleImageView ivCommentFavicon1;// 评论1头像
    private TextView tvCommentName1;// 评论1姓名
    private TextView tvCommentTime1;// 评论1发布时间
    private TextView tvCommentContent1;// 评论1内容
    private View ivLineComments;// 评论12之间分割线
    private RelativeLayout rlComment2;// 评论2布局
    private CircleImageView ivCommentFavicon2;// 评论2头像
    private TextView tvCommentName2;// 评论2姓名
    private TextView tvCommentTime2;// 评论2发布时间
    private TextView tvCommentContent2;// 评论2内容
    private ImageButton btnComment;// 评论按钮

    private boolean isOpen = false;// 项目描述的开关状态

    private List<String> photos = new ArrayList<>();// 项目照片资源
    private ProjectPhotosAdapter photosAdapter;// 项目照片的适配器

    private List<String> favicons = new ArrayList<>();// 团队成员图片资源
    private List<String> names = new ArrayList<>();// 团队成员姓名集合
    private List<String> positions = new ArrayList<>();// 团队成员职位集合
    private ProjectTeamsAdapter teamsAdapter;// 团队成员数据适配器

    private List<Integer> reportImgs = new ArrayList<>();// 报表图标
    private List<String> reportNames = new ArrayList<>();// 报表名
    private ProjectReportsAdapter reportsAdapter;// 各类报表数据适配器

    private ProjectDetailBean.DataBean data;
    public final static int RESULT_CODE = 0;
    public boolean needRefresh = false;// 是否进行了交互，返回时是否刷新

    @Override
    protected int getResourcesId() {
        return R.layout.activity_preselection_details;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        findView();
        GetDetailTask getDetailTask = new GetDetailTask();
        getDetailTask.execute();
    }

    private void findView() {
        btnBack = (LinearLayout) findViewById(R.id.title_btn_left);// 返回键
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 本页标题
        tvTitle.setVisibility(View.GONE);
        btnCollect = (LinearLayout) findViewById(R.id.title_btn_right2);// 收藏按钮
        btnCollect.setOnClickListener(this);
        btnShare = (LinearLayout) findViewById(R.id.title_btn_right);// 分享按钮
        btnShare.setOnClickListener(this);
        btnService = (ImageButton) findViewById(R.id.pre_btn_service);// 客服按钮
        btnService.setOnClickListener(this);
        btnBottomCollect = (RelativeLayout) findViewById(R.id.pre_btn_collect);// 底部关注按钮
        btnBottomCollect.setOnClickListener(this);
        tvCollect = (TextView) findViewById(R.id.pre_tv_collect);// 底部带星星的关注文字
        ivImg = (CircleImageView) findViewById(R.id.pre_iv_img);// 项目图标
        tvProTitle = (TextView) findViewById(R.id.pre_tv_title);// 顶部项目标题
        tvCompanyName = (TextView) findViewById(R.id.pre_tv_company_name);// 公司名称
        tvAddr = (TextView) findViewById(R.id.pre_tv_addr);// 公司所在地
        tvPorTitle2 = (TextView) findViewById(R.id.pre_tv_title2);// 中间的项目标题
        tvDesc = (TextView) findViewById(R.id.pre_tv_desc);// 项目描述
        rvPhotos = (RecyclerView) findViewById(R.id.pre_rv_photos);// 项目照片
        btnMore = (RelativeLayout) findViewById(R.id.pre_btn_more);// 查看更多描述和照片按钮
        btnMore.setOnClickListener(this);
        btnMoreImg = (ImageButton) findViewById(R.id.pre_btn_more_img);// 查看更多的图标
        rvTeams = (RecyclerView) findViewById(R.id.project_rv_teams);// 团队成员照片
        rvReports = (RecyclerView) findViewById(R.id.project_rv_reports);// 各类报表图标
        tvCommentsQuantity = (TextView) findViewById(R.id.pre_tv_comment_quantity);// 评论数量
        btnMoreComments = (TextView) findViewById(R.id.pre_btn_more_comment);// 查看更多评论按钮
        btnMoreComments.setOnClickListener(this);
        rlComment1 = (RelativeLayout) findViewById(R.id.rl_comment1);// 评论1布局
        ivCommentFavicon1 = (CircleImageView) findViewById(R.id.item_comment_favicon1);// 评论1头像
        tvCommentName1 = (TextView) findViewById(R.id.item_comment_name1);// 评论1姓名
        tvCommentTime1 = (TextView) findViewById(R.id.item_comment_time1);// 评论1发布时间
        tvCommentContent1 = (TextView) findViewById(R.id.item_comment_content1);// 评论1内容
        ivLineComments = (View) findViewById(R.id.iv_line_comments);// 评论12之间分割线
        rlComment2 = (RelativeLayout) findViewById(R.id.rl_comment2);// 评论2布局
        ivCommentFavicon2 = (CircleImageView) findViewById(R.id.item_comment_favicon2);// 评论2头像
        tvCommentName2 = (TextView) findViewById(R.id.item_comment_name2);// 评论2姓名
        tvCommentTime2 = (TextView) findViewById(R.id.item_comment_time2);// 评论2发布时间
        tvCommentContent2 = (TextView) findViewById(R.id.item_comment_content2);// 评论2内容
        btnComment = (ImageButton) findViewById(R.id.pre_btn_comment);// 评论按钮
        btnComment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_btn_left:// 返回上一页
                onBackPressed();
                break;
            case R.id.title_btn_right2:// 点击关注
                SuperToastUtils.showSuperToast(this, 2, "关注");
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

    @Override
    public void onBackPressed() {
        if (needRefresh) {
            Intent intent = new Intent();
            intent.putExtra("needRefresh", needRefresh);
            setResult(RESULT_CODE, intent);
        }
        finish();
    }

    // 闭合TextView
    private void descClose() {
        tvDesc.setEllipsize(TextUtils.TruncateAt.END);
        tvDesc.setMaxLines(3);
        btnMoreImg.setBackgroundResource(R.mipmap.icon_bottom_more);
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
        btnMoreImg.setBackgroundResource(R.mipmap.icon_bottom_less);
        // 项目照片
        photos.clear();
        if (data.getProjectimageses().size() == 0) {
            return;
        }
        for (ProjectDetailBean.DataBean.ProjectimagesesBean bean : data.getProjectimageses()) {
            photos.add(bean.getImageUrl());
        }
        photosAdapter.notifyDataSetChanged();
    }

    // 加载数据
    private void initData() {
//        preBtnCollect关注按钮
//        preTvCollect关注文字
        Glide.with(mContext).load(data.getStartPageImage()).into(ivImg);
        tvProTitle.setText(data.getAbbrevName());
        tvCompanyName.setText(data.getFullName());
        tvAddr.setText(data.getAddress());
        tvPorTitle2.setText(data.getAbbrevName());
        tvDesc.setText(data.getDescription());
        // 项目照片处理
        initPhotos();
        // 团队成员处理
        initTeam();
        // 报表处理
        initReport();
        // 评论列表处理
        initComment();
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
        for (ProjectDetailBean.DataBean.MembersBean bean : data.getMembers()) {
//            favicons.add(bean.get);
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
        // 准备数据
        reportImgs = new ArrayList<Integer>(Arrays.asList(R.mipmap.icon_report1, R.mipmap.icon_report2, R.mipmap.icon_report3, R.mipmap.icon_report4));
        reportNames = new ArrayList<>(Arrays.asList("财务\n状况", "融资\n方案", "退出\n渠道", "商业\n计划书"));
        reportsAdapter = new ProjectReportsAdapter(mContext, reportImgs, reportNames);
        // 填充数据
        RecyclerViewData.setHorizontal(rvReports, mContext, reportsAdapter);
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

    // 评论列表处理
    private void initComment() {

    }

    // 获取详情
    private class GetDetailTask extends AsyncTask<Void, Void, ProjectDetailBean> {
        @Override
        protected ProjectDetailBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETPROJECTDETAIL)),
                            "projectId", getIntent().getStringExtra("id"),
                            Constant.BASE_URL + Constant.GETPROJECTDETAIL,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("预选项目详情", body);
                return FastJsonTools.getBean(body, ProjectDetailBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ProjectDetailBean projectDetailBean) {
            super.onPostExecute(projectDetailBean);
            if (projectDetailBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (projectDetailBean.getStatus() == 200) {
                    data = projectDetailBean.getData();
                    if (data != null) {
                        initData();
                    }
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, projectDetailBean.getMessage());
                }
            }
        }
    }

    // 关注
    private class CollectTask extends AsyncTask<Void, Void, CommonBean> {
        int flag;

        public CollectTask(int flag) {
            this.flag = flag;
        }

        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.COLLECTPROJECT)),
                            "projectId", String.valueOf(data.getProjectId()),
                            "flag", String.valueOf(flag),
                            Constant.BASE_URL + Constant.COLLECTPROJECT,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("关注项目返回信息", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            if (commonBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (commonBean.getStatus() == 200) {
                    if (flag == 1) {
//                        data.setCollected(true);
//                        data.setCollectCount(data.getCollectCount() + 1);
                    } else if (flag == 2) {
//                        data.setCollected(false);
//                        data.setCollectCount(data.getCollectCount() - 1);
                    }
//                    initData();
                    needRefresh = true;
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, commonBean.getMessage());
                }
            }
        }
    }

    // 分享
    private class ShareTask extends AsyncTask<Void, Void, ShareBean> {
        @Override
        protected ShareBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
//                    body = OkHttpUtils.post(
//                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY,Constant.SHAREPROJECT)),
//                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("分享项目返回信息", body);
                return FastJsonTools.getBean(body, ShareBean.class);
            } else {
                return null;
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
