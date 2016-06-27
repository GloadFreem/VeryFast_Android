package com.jinzht.pro.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.adapter.ProjectPhotosAdapter;
import com.jinzht.pro.adapter.ProjectReportsAdapter;
import com.jinzht.pro.adapter.ProjectTeamsAdapter;
import com.jinzht.pro.adapter.RecyclerViewData;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.CommonBean;
import com.jinzht.pro.bean.CustomerServiceBean;
import com.jinzht.pro.bean.ProjectCollectBean;
import com.jinzht.pro.bean.ProjectCommentBean;
import com.jinzht.pro.bean.ProjectDetailBean;
import com.jinzht.pro.bean.ShareBean;
import com.jinzht.pro.callback.ItemClickListener;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.DateUtils;
import com.jinzht.pro.utils.DialogUtils;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.ShareUtils;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.utils.UiUtils;
import com.jinzht.pro.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 预选项目详情
 */
public class PreselectionDetailsActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回键
    private TextView tvTitle;// 本页面标题，空
    private LinearLayout btnCollect;// 收藏按钮
    private ImageView ivCollect;// 收藏图标
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
    private List<ProjectCommentBean.DataBean> commentsData = new ArrayList<>();// 评论列表
    private int FLAG = 0;// 关注或取消关注的标识
    private int needRefresh = 0;// 是否需要在项目列表中刷新
    public final static int RESULT_CODE = 3;
    private final static int REQUEST_CODE = 1;

    private String comment = "";// 输入的评论内容
    private PopupWindow popupWindow;// 评论输入弹框

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
        GetCommentsTask getCommentsTask = new GetCommentsTask();
        getCommentsTask.execute();
    }

    private void findView() {
        btnBack = (LinearLayout) findViewById(R.id.title_btn_left);// 返回键
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 本页标题
        tvTitle.setVisibility(View.GONE);
        btnCollect = (LinearLayout) findViewById(R.id.title_btn_right2);// 收藏按钮
        btnCollect.setOnClickListener(this);
        ivCollect = (ImageView) findViewById(R.id.title_iv_right2);// 收藏图标
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
                if (data.isCollected()) {
                    CollectTask collectTask = new CollectTask(2);
                    collectTask.execute();
                } else {
                    CollectTask collectTask = new CollectTask(1);
                    collectTask.execute();
                }
                break;
            case R.id.title_btn_right:// 点击分享
                ShareTask shareTask = new ShareTask();
                shareTask.execute();
                break;
            case R.id.pre_btn_service:// 打电话给客服
                CustomerServiceTask customerServiceTask = new CustomerServiceTask();
                customerServiceTask.execute();
                break;
            case R.id.pre_btn_collect:// 点击关注
                if (data.isCollected()) {
                    CollectTask collectTask = new CollectTask(2);
                    collectTask.execute();
                } else {
                    CollectTask collectTask = new CollectTask(1);
                    collectTask.execute();
                }
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
                Intent intent = new Intent(this, PreselectionAllCommentsActivity.class);
                intent.putExtra("id", String.valueOf(data.getProjectId()));
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.pre_btn_comment:// 点击发表评论
                CommentDialog();
        }
    }

    @Override
    public void onBackPressed() {
        if (needRefresh % 2 != 0 && FLAG != 0) {
            Intent intent = new Intent();
            intent.putExtra("FLAG", FLAG);
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
        for (ProjectDetailBean.DataBean.ProjectBean.ProjectimagesesBean bean : data.getProjectimageses()) {
            photos.add(bean.getImageUrl());
        }
        photosAdapter.notifyDataSetChanged();
    }

    // 加载数据
    private void initData() {
        initCollect();
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
    }

    // 关注处理
    private void initCollect() {
        if (data.isCollected()) {
            ivCollect.setBackgroundResource(R.mipmap.icon_collected);
            btnBottomCollect.setBackgroundResource(R.mipmap.icon_bg_invest_gray);
            tvCollect.setCompoundDrawablesWithIntrinsicBounds(UiUtils.getDrawable(R.mipmap.icon_collected), null, null, null);
            tvCollect.setText("已关注(" + data.getCollectionCount() + ")");
        } else {
            ivCollect.setBackgroundResource(R.mipmap.icon_collect);
            btnBottomCollect.setBackgroundResource(R.mipmap.icon_bg_invest);
            tvCollect.setCompoundDrawablesWithIntrinsicBounds(UiUtils.getDrawable(R.mipmap.icon_collect), null, null, null);
            tvCollect.setText("关注(" + data.getCollectionCount() + ")");
        }
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
        tvCommentsQuantity.setText("(" + commentsData.size() + ")");
        if (commentsData.size() == 0) {
            rlComment1.setVisibility(View.GONE);
            ivLineComments.setVisibility(View.GONE);
            rlComment2.setVisibility(View.GONE);
        } else if (commentsData.size() == 1) {
            rlComment1.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(commentsData.get(0).getUsers().getHeadSculpture()).into(ivCommentFavicon1);
            tvCommentName1.setText(commentsData.get(0).getUsers().getName());
            tvCommentTime1.setText(DateUtils.timeLogic(commentsData.get(0).getCommentDate()));
            tvCommentContent1.setText(commentsData.get(0).getContent());
            rlComment2.setVisibility(View.GONE);
        } else if (commentsData.size() >= 2) {
            rlComment1.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(commentsData.get(0).getUsers().getHeadSculpture()).into(ivCommentFavicon1);
            tvCommentName1.setText(commentsData.get(0).getUsers().getName());
            tvCommentTime1.setText(DateUtils.timeLogic(commentsData.get(0).getCommentDate()));
            tvCommentContent1.setText(commentsData.get(0).getContent());
            rlComment2.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(commentsData.get(1).getUsers().getHeadSculpture()).into(ivCommentFavicon2);
            tvCommentName2.setText(commentsData.get(1).getUsers().getName());
            tvCommentTime2.setText(DateUtils.timeLogic(commentsData.get(1).getCommentDate()));
            tvCommentContent2.setText(commentsData.get(1).getContent());
        }
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
                    data = projectDetailBean.getData().getProject();
                    reportDatas = projectDetailBean.getData().getExtr();
                    if (data != null) {
                        initData();
                    }
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, projectDetailBean.getMessage());
                }
            }
        }
    }

    // 获取评论列表
    private class GetCommentsTask extends AsyncTask<Void, Void, ProjectCommentBean> {
        @Override
        protected ProjectCommentBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETPROJECTCOMMENTS)),
                            "projectId", getIntent().getStringExtra("id"),
                            "page", "0",
                            Constant.BASE_URL + Constant.GETPROJECTCOMMENTS,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("评论列表返回信息", body);
                return FastJsonTools.getBean(body, ProjectCommentBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ProjectCommentBean projectCommentBean) {
            super.onPostExecute(projectCommentBean);
            if (projectCommentBean != null) {
                if (projectCommentBean.getStatus() == 200) {
                    commentsData = projectCommentBean.getData();
                    if (commentsData != null) {
                        initComment();
                    }
                }
            }
        }
    }

    // 关注
    private class CollectTask extends AsyncTask<Void, Void, ProjectCollectBean> {
        int flag;

        public CollectTask(int flag) {
            this.flag = flag;
        }

        @Override
        protected ProjectCollectBean doInBackground(Void... params) {
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
                return FastJsonTools.getBean(body, ProjectCollectBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ProjectCollectBean projectCollectBean) {
            super.onPostExecute(projectCollectBean);
            if (projectCollectBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (projectCollectBean.getStatus() == 200) {
                    if (flag == 1) {
                        data.setCollected(true);
                        data.setCollectionCount(data.getCollectionCount() + 1);
                        FLAG = 1;
                    } else if (flag == 2) {
                        data.setCollected(false);
                        data.setCollectionCount(data.getCollectionCount() - 1);
                        FLAG = 2;
                    }
                    needRefresh++;
                    initCollect();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, projectCollectBean.getMessage());
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
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SHAREPROJECT)),
                            "type", "1",
                            "projectId", String.valueOf(data.getProjectId()),
                            Constant.BASE_URL + Constant.SHAREPROJECT,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("分享返回信息", body);
                return FastJsonTools.getBean(body, ShareBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ShareBean shareBean) {
            super.onPostExecute(shareBean);
            if (shareBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                return;
            } else {
                if (shareBean.getStatus() == 200) {
                    ShareUtils shareUtils = new ShareUtils(PreselectionDetailsActivity.this);
                    DialogUtils.shareDialog(PreselectionDetailsActivity.this, btnBottomCollect, shareUtils, data.getAbbrevName(), data.getDescription(), data.getStartPageImage(), shareBean.getData().getUrl());
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, shareBean.getMessage());
                }
            }
        }
    }

    // 评论
    private class CommentTask extends AsyncTask<Void, Void, CommonBean> {
        private String content;

        public CommentTask(String content) {
            this.content = content;
        }

        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.COMMENTPROJECT)),
                            "projectId", String.valueOf(data.getProjectId()),
                            "content", content,
                            Constant.BASE_URL + Constant.COMMENTPROJECT,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("评论返回信息", body);
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
                    popupWindow.dismiss();
                    comment = "";
                    GetCommentsTask getCommentsTask = new GetCommentsTask();
                    getCommentsTask.execute();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, commonBean.getMessage());
                }
            }
        }
    }

    // 弹出评论输入框
    private void CommentDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_comment, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        final EditText edComment = (EditText) view.findViewById(R.id.ed_comment);
        edComment.setText(comment);
        edComment.setSelection(comment.length());
        TextView btn = (TextView) view.findViewById(R.id.btn_comment);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isBlank(edComment.getText().toString())) {
                    CommentTask commentTask = new CommentTask(edComment.getText().toString());
                    commentTask.execute();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入评论内容");
                }
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(btnComment, Gravity.BOTTOM, 0, 0);
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               imm.showSoftInput(edComment, 0);
                           }
                       },
                100);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!StringUtils.isBlank(edComment.getText().toString())) {
                    comment = edComment.getText().toString();
                } else {
                    comment = "";
                }
                imm.hideSoftInputFromWindow(edComment.getWindowToken(), 0);
            }
        });
    }

    // 客服接口
    private class CustomerServiceTask extends AsyncTask<Void, Void, CustomerServiceBean> {
        @Override
        protected CustomerServiceBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.CUSTOMERSERVICE)),
                            Constant.BASE_URL + Constant.CUSTOMERSERVICE,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("客服信息", body);
                return FastJsonTools.getBean(body, CustomerServiceBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CustomerServiceBean customerServiceBean) {
            super.onPostExecute(customerServiceBean);
            if (customerServiceBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                return;
            } else {
                if (customerServiceBean.getStatus() == 200) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + customerServiceBean.getData().getTel());
                    intent.setData(data);
                    startActivity(intent);
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, customerServiceBean.getMessage());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            if (resultCode == PreselectionAllCommentsActivity.RESULT_CODE) {
                if (data.getBooleanExtra("needRefresh", false)) {// 在全部评论里进行了交互
                    GetCommentsTask getCommentsTask = new GetCommentsTask();
                    getCommentsTask.execute();
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
