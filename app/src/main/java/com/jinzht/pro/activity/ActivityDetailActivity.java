package com.jinzht.pro.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.adapter.ActivityPhotosAdapter;
import com.jinzht.pro.adapter.RecyclerViewData;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.ActivityAllApplysBean;
import com.jinzht.pro.bean.ActivityAllCommentsBean;
import com.jinzht.pro.bean.ActivityApplyBean;
import com.jinzht.pro.bean.ActivityCommentBean;
import com.jinzht.pro.bean.ActivityDetailBean;
import com.jinzht.pro.bean.ActivityPriseBean;
import com.jinzht.pro.bean.CommonBean;
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
import com.jinzht.pro.utils.SharedPreferencesUtils;
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
 * 活动详情页
 */
public class ActivityDetailActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView title;// 页面标题
    private Button btnApply;// 报名按钮
    private Button btnShare;// 分享按钮
    private TextView activityTitle;// 活动标题
    private TextView activityDesc;// 活动描述
    private RecyclerView activityPhotos;// 活动照片
    private RelativeLayout btnMore;// 展开更多描述和照片
    private ImageView imgMore;// 展开更多描述和照片的图标
    private TextView activityTime;// 活动时间
    private TextView activityNum;// 活动人数
    private TextView isFree;// 是否免费
    private TextView activityAddr;// 活动地点
    private TextView activityDistance;// 活动距离
    private TextView applyQuantity;// 报名人数
    private TextView btnQuantityTotle;// 查看全部报名人数
    private RelativeLayout rlApply1;// 报名人布局
    private CircleImageView favicon1;// 报名人头像
    private TextView name1;// 报名人姓名
    private TextView position1;// 报名人职位
    private TextView time1;// 报名时间
    private RelativeLayout rlApply2;
    private CircleImageView favicon2;
    private TextView name2;
    private TextView position2;
    private TextView time2;
    private RelativeLayout rlApply3;
    private CircleImageView favicon3;
    private TextView name3;
    private TextView position3;
    private TextView time3;
    private RelativeLayout rlApply4;
    private CircleImageView favicon4;
    private TextView name4;
    private TextView position4;
    private TextView time4;
    private RelativeLayout rlApply5;
    private CircleImageView favicon5;
    private TextView name5;
    private TextView position5;
    private TextView time5;
    private TextView btnCommentTotle;// 查看全部点赞评论
    private TextView praiseNames;// 点赞人
    private TextView comment1;// 评论
    private TextView comment2;
    private TextView comment3;
    private TextView comment4;
    private TextView comment5;
    private TextView comment6;
    private ImageButton btnPraise;// 点赞按钮
    private ImageButton btnComment;// 评论按钮

    private ActivityDetailBean.DataBean data = new ActivityDetailBean.DataBean();// 内容数据
    private List<ActivityDetailBean.DataBean.ActionimagesBean> photos = new ArrayList<>();// 活动照片资源
    private ArrayList<String> urls = new ArrayList<>();// 点击图片查看大图的URL
    private ActivityPhotosAdapter photosAdapter;// 活动照片的适配器
    private boolean isOpen = false;// 活动描述的开关状态

    private List<ActivityAllApplysBean.DataBean> applys = new ArrayList<>();// 全部报名人
    private List<String> prises = new ArrayList<>();//  全部点赞人
    private List<ActivityAllCommentsBean.DataBean.CommentsBean> comments = new ArrayList<>();// 全部评论人

    private String comment = "";// 输入的评论内容
    private PopupWindow popupWindow;// 评论输入弹框
    private AlertDialog dialog;// 报名弹框
    private boolean ISATTENDED = false;// 是否在详情页点了报名

    public final static int RESULT_CODE = 0;
    private final static int REQUEST_CODE = 1;

    @Override
    protected int getResourcesId() {
        return R.layout.activity_activity_detail;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致
        findView();
        GetActivityDetailTask getActivityDetailTask = new GetActivityDetailTask();
        getActivityDetailTask.execute();
        GetAllApplysTask getAllApplysTask = new GetAllApplysTask();
        getAllApplysTask.execute();
        GetAllCommentsTask getAllCommentsTask = new GetAllCommentsTask();
        getAllCommentsTask.execute();
    }

    private void findView() {
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_title);// 页面标题
        title.setText("活动详情");
        btnApply = (Button) findViewById(R.id.activity_btn_apply);// 报名按钮
        btnApply.setOnClickListener(this);
        btnShare = (Button) findViewById(R.id.activity_btn_share);// 分享按钮
        btnShare.setOnClickListener(this);
        activityTitle = (TextView) findViewById(R.id.activity_tv_title);// 活动标题
        activityDesc = (TextView) findViewById(R.id.activity_tv_desc);// 活动描述
        activityPhotos = (RecyclerView) findViewById(R.id.activity_rv_photos);// 活动照片
        btnMore = (RelativeLayout) findViewById(R.id.activity_btn_more);// 展开更多内容
        btnMore.setOnClickListener(this);
        imgMore = (ImageView) findViewById(R.id.activity_img_more);// 展开更多图标
        activityTime = (TextView) findViewById(R.id.activity_tv_time);// 活动时间
        activityNum = (TextView) findViewById(R.id.activity_tv_num);// 活动人数
        isFree = (TextView) findViewById(R.id.activity_tv_free);// 是否免费
        activityAddr = (TextView) findViewById(R.id.activity_tv_addr);// 活动地点
        activityDistance = (TextView) findViewById(R.id.activity_tv_distance);// 距离
        applyQuantity = (TextView) findViewById(R.id.activity_tv_quantity);// 报名人数
        btnQuantityTotle = (TextView) findViewById(R.id.activity_btn_quantity_totle);// 查看全部报名人数按钮
        btnQuantityTotle.setOnClickListener(this);
        rlApply1 = (RelativeLayout) findViewById(R.id.rl_apply1);// 报名人整体布局
//        rlApply1.setOnClickListener(this);
        favicon1 = (CircleImageView) findViewById(R.id.activity_iv_favicon1);// 报名人头像
        name1 = (TextView) findViewById(R.id.activity_tv_name1);// 报名人姓名
        position1 = (TextView) findViewById(R.id.activity_tv_position1);// 报名人职位
        time1 = (TextView) findViewById(R.id.activity_tv_time1);// 报名时间
        rlApply2 = (RelativeLayout) findViewById(R.id.rl_apply2);
//        rlApply2.setOnClickListener(this);
        favicon2 = (CircleImageView) findViewById(R.id.activity_iv_favicon2);
        name2 = (TextView) findViewById(R.id.activity_tv_name2);
        position2 = (TextView) findViewById(R.id.activity_tv_position2);
        time2 = (TextView) findViewById(R.id.activity_tv_time2);
        rlApply3 = (RelativeLayout) findViewById(R.id.rl_apply3);
//        rlApply3.setOnClickListener(this);
        favicon3 = (CircleImageView) findViewById(R.id.activity_iv_favicon3);
        name3 = (TextView) findViewById(R.id.activity_tv_name3);
        position3 = (TextView) findViewById(R.id.activity_tv_position3);
        time3 = (TextView) findViewById(R.id.activity_tv_time3);
        rlApply4 = (RelativeLayout) findViewById(R.id.rl_apply4);
//        rlApply4.setOnClickListener(this);
        favicon4 = (CircleImageView) findViewById(R.id.activity_iv_favicon4);
        name4 = (TextView) findViewById(R.id.activity_tv_name4);
        position4 = (TextView) findViewById(R.id.activity_tv_position4);
        time4 = (TextView) findViewById(R.id.activity_tv_time4);
        rlApply5 = (RelativeLayout) findViewById(R.id.rl_apply5);
//        rlApply5.setOnClickListener(this);
        favicon5 = (CircleImageView) findViewById(R.id.activity_iv_favicon5);
        name5 = (TextView) findViewById(R.id.activity_tv_name5);
        position5 = (TextView) findViewById(R.id.activity_tv_position5);
        time5 = (TextView) findViewById(R.id.activity_tv_time5);
        btnCommentTotle = (TextView) findViewById(R.id.activity_btn_comment_totle);// 查看全部点赞和评论按钮
        btnCommentTotle.setOnClickListener(this);
        praiseNames = (TextView) findViewById(R.id.activity_tv_praise);// 点赞人
        comment1 = (TextView) findViewById(R.id.activity_tv_comment1);// 评论
        comment1.setOnClickListener(this);
        comment2 = (TextView) findViewById(R.id.activity_tv_comment2);
        comment2.setOnClickListener(this);
        comment3 = (TextView) findViewById(R.id.activity_tv_comment3);
        comment3.setOnClickListener(this);
        comment4 = (TextView) findViewById(R.id.activity_tv_comment4);
        comment4.setOnClickListener(this);
        comment5 = (TextView) findViewById(R.id.activity_tv_comment5);
        comment5.setOnClickListener(this);
        comment6 = (TextView) findViewById(R.id.activity_tv_comment6);// 省略号，多余5条才显示
        btnPraise = (ImageButton) findViewById(R.id.activity_btn_praise);// 点赞按钮
        btnPraise.setOnClickListener(this);
        btnComment = (ImageButton) findViewById(R.id.activity_btn_comment);// 评论按钮
        btnComment.setOnClickListener(this);
    }

    // 加载活动内容数据
    private void initData() {
        activityTitle.setText(data.getName());
        activityDesc.setText(data.getDescription());
        String time = data.getStartTime();
        activityTime.setText(time.substring(0, 10).replaceAll("-", ".") + " " + DateUtils.getWeek(time) + " " + time.substring(11, 16));
        activityNum.setText(data.getMemberLimit() + "人");
        if (data.getType() == 1) {
            isFree.setText("免费");
        } else {
            isFree.setText("付费");
        }
        activityAddr.setText(data.getAddress());
        activityDistance.setText("");
        initPhotos();

        // 是否过期
        int i = DateUtils.timeDiff4Mins(data.getEndTime());
        Log.i("时间差", String.valueOf(i));
        if (DateUtils.timeDiff4Mins(data.getEndTime()) > 0) {
            // 已过期
            btnApply.setText("已过期");
            btnApply.setBackgroundColor(Color.parseColor("#cccccc"));
            btnApply.setClickable(false);
        } else {
            // 未过期
            if (data.isAttended()) {
                btnApply.setText("已报名");
                btnApply.setBackgroundColor(Color.parseColor("#cccccc"));
                btnApply.setClickable(false);
            } else {
                btnApply.setText("我要报名");
                btnApply.setBackgroundColor(UiUtils.getColor(R.color.custom_orange));
                btnApply.setClickable(true);
            }
        }
    }

    // 活动照片处理
    private void initPhotos() {
        // 准备数据
        photos.clear();
        if (data.getActionimages().size() == 0) {
            return;
        } else if (data.getActionimages().size() == 1) {
            photos.add(data.getActionimages().get(0));
        } else if (data.getActionimages().size() == 2) {
            photos.add(data.getActionimages().get(0));
            photos.add(data.getActionimages().get(1));
        } else if (data.getActionimages().size() >= 3) {
            photos.add(data.getActionimages().get(0));
            photos.add(data.getActionimages().get(1));
            photos.add(data.getActionimages().get(2));
        }
        photosAdapter = new ActivityPhotosAdapter(mContext, photos);
        // 填充数据
        RecyclerViewData.setGrid(activityPhotos, mContext, photosAdapter, 3);

        urls.clear();
        for (ActivityDetailBean.DataBean.ActionimagesBean bean : photos) {
            urls.add(bean.getUrl());
        }
        // 活动照片的点击事件
        photosAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                imageBrower(position, urls);
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

    // 加载全部报名人
    private void initApplys() {
        applyQuantity.setText("(" + applys.size() + ")");
        if (applys.size() == 0) {
            rlApply1.setVisibility(View.GONE);
            rlApply2.setVisibility(View.GONE);
            rlApply3.setVisibility(View.GONE);
            rlApply4.setVisibility(View.GONE);
            rlApply5.setVisibility(View.GONE);
        }
        if (applys.size() == 1) {
            Glide.with(mContext).load(applys.get(0).getUsers().getHeadSculpture()).into(favicon1);
            name1.setText(applys.get(0).getUsers().getAuthentics().get(0).getName());
            position1.setText(applys.get(0).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(0).getUsers().getAuthentics().get(0).getPosition());
            time1.setText(DateUtils.timeLogic(applys.get(0).getEnrollDate()));

            rlApply2.setVisibility(View.GONE);
            rlApply3.setVisibility(View.GONE);
            rlApply4.setVisibility(View.GONE);
            rlApply5.setVisibility(View.GONE);
        }
        if (applys.size() == 2) {
            Glide.with(mContext).load(applys.get(0).getUsers().getHeadSculpture()).into(favicon1);
            name1.setText(applys.get(0).getUsers().getAuthentics().get(0).getName());
            position1.setText(applys.get(0).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(0).getUsers().getAuthentics().get(0).getPosition());
            time1.setText(DateUtils.timeLogic(applys.get(0).getEnrollDate()));

            Glide.with(mContext).load(applys.get(1).getUsers().getHeadSculpture()).into(favicon2);
            name2.setText(applys.get(1).getUsers().getAuthentics().get(0).getName());
            position2.setText(applys.get(1).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(1).getUsers().getAuthentics().get(0).getPosition());
            time2.setText(DateUtils.timeLogic(applys.get(1).getEnrollDate()));

            rlApply3.setVisibility(View.GONE);
            rlApply4.setVisibility(View.GONE);
            rlApply5.setVisibility(View.GONE);
        }
        if (applys.size() == 3) {
            Glide.with(mContext).load(applys.get(0).getUsers().getHeadSculpture()).into(favicon1);
            name1.setText(applys.get(0).getUsers().getAuthentics().get(0).getName());
            position1.setText(applys.get(0).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(0).getUsers().getAuthentics().get(0).getPosition());
            time1.setText(DateUtils.timeLogic(applys.get(0).getEnrollDate()));

            Glide.with(mContext).load(applys.get(1).getUsers().getHeadSculpture()).into(favicon2);
            name2.setText(applys.get(1).getUsers().getAuthentics().get(0).getName());
            position2.setText(applys.get(1).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(1).getUsers().getAuthentics().get(0).getPosition());
            time2.setText(DateUtils.timeLogic(applys.get(1).getEnrollDate()));

            Glide.with(mContext).load(applys.get(2).getUsers().getHeadSculpture()).into(favicon3);
            name3.setText(applys.get(2).getUsers().getAuthentics().get(0).getName());
            position3.setText(applys.get(2).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(2).getUsers().getAuthentics().get(0).getPosition());
            time3.setText(DateUtils.timeLogic(applys.get(2).getEnrollDate()));

            rlApply4.setVisibility(View.GONE);
            rlApply5.setVisibility(View.GONE);
        }
        if (applys.size() == 4) {
            Glide.with(mContext).load(applys.get(0).getUsers().getHeadSculpture()).into(favicon1);
            name1.setText(applys.get(0).getUsers().getAuthentics().get(0).getName());
            position1.setText(applys.get(0).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(0).getUsers().getAuthentics().get(0).getPosition());
            time1.setText(DateUtils.timeLogic(applys.get(0).getEnrollDate()));

            Glide.with(mContext).load(applys.get(1).getUsers().getHeadSculpture()).into(favicon2);
            name2.setText(applys.get(1).getUsers().getAuthentics().get(0).getName());
            position2.setText(applys.get(1).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(1).getUsers().getAuthentics().get(0).getPosition());
            time2.setText(DateUtils.timeLogic(applys.get(1).getEnrollDate()));

            Glide.with(mContext).load(applys.get(2).getUsers().getHeadSculpture()).into(favicon3);
            name3.setText(applys.get(2).getUsers().getAuthentics().get(0).getName());
            position3.setText(applys.get(2).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(2).getUsers().getAuthentics().get(0).getPosition());
            time3.setText(DateUtils.timeLogic(applys.get(2).getEnrollDate()));

            Glide.with(mContext).load(applys.get(3).getUsers().getHeadSculpture()).into(favicon4);
            name4.setText(applys.get(3).getUsers().getAuthentics().get(0).getName());
            position4.setText(applys.get(3).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(3).getUsers().getAuthentics().get(0).getPosition());
            time4.setText(DateUtils.timeLogic(applys.get(3).getEnrollDate()));

            rlApply5.setVisibility(View.GONE);
        }
        if (applys.size() >= 5) {
            Glide.with(mContext).load(applys.get(0).getUsers().getHeadSculpture()).into(favicon1);
            name1.setText(applys.get(0).getUsers().getAuthentics().get(0).getName());
            position1.setText(applys.get(0).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(0).getUsers().getAuthentics().get(0).getPosition());
            time1.setText(DateUtils.timeLogic(applys.get(0).getEnrollDate()));

            Glide.with(mContext).load(applys.get(1).getUsers().getHeadSculpture()).into(favicon2);
            name2.setText(applys.get(1).getUsers().getAuthentics().get(0).getName());
            position2.setText(applys.get(1).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(1).getUsers().getAuthentics().get(0).getPosition());
            time2.setText(DateUtils.timeLogic(applys.get(1).getEnrollDate()));

            Glide.with(mContext).load(applys.get(2).getUsers().getHeadSculpture()).into(favicon3);
            name3.setText(applys.get(2).getUsers().getAuthentics().get(0).getName());
            position3.setText(applys.get(2).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(2).getUsers().getAuthentics().get(0).getPosition());
            time3.setText(DateUtils.timeLogic(applys.get(2).getEnrollDate()));

            Glide.with(mContext).load(applys.get(3).getUsers().getHeadSculpture()).into(favicon4);
            name4.setText(applys.get(3).getUsers().getAuthentics().get(0).getName());
            position4.setText(applys.get(3).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(3).getUsers().getAuthentics().get(0).getPosition());
            time4.setText(DateUtils.timeLogic(applys.get(3).getEnrollDate()));

            Glide.with(mContext).load(applys.get(4).getUsers().getHeadSculpture()).into(favicon5);
            name5.setText(applys.get(4).getUsers().getAuthentics().get(0).getName());
            position5.setText(applys.get(4).getUsers().getAuthentics().get(0).getCompanyName() + applys.get(4).getUsers().getAuthentics().get(0).getPosition());
            time5.setText(DateUtils.timeLogic(applys.get(4).getEnrollDate()));
        }
    }

    private String str;// 转换字体的临时字符串
    private SpannableString span;// 设置TextView不同字体

    // 加载全部评论
    private void initComments() {
        if (prises != null && prises.size() != 0) {
            praiseNames.setText(prises.toString().substring(1, prises.toString().length() - 1));
        }

        if (comments.size() == 0) {
            comment1.setText("暂无评论");
            comment2.setVisibility(View.GONE);
            comment3.setVisibility(View.GONE);
            comment4.setVisibility(View.GONE);
            comment5.setVisibility(View.GONE);
            comment6.setVisibility(View.GONE);
        }
        if (comments.size() == 1) {
            if (StringUtils.isBlank(comments.get(0).getAtUserName())) {
                String name = comments.get(0).getUserName();
                str = name + ": " + comments.get(0).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment1.setText(span);
            } else {
                String name = comments.get(0).getUserName();
                String atName = comments.get(0).getAtUserName();
                str = name + " 回复 " + atName + ": " + comments.get(0).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), name.length() + 4, name.length() + 4 + atName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment1.setText(span);
            }
            comment2.setVisibility(View.GONE);
            comment3.setVisibility(View.GONE);
            comment4.setVisibility(View.GONE);
            comment5.setVisibility(View.GONE);
            comment6.setVisibility(View.GONE);
        }
        if (comments.size() == 2) {
            if (StringUtils.isBlank(comments.get(0).getAtUserName())) {
                String name = comments.get(0).getUserName();
                str = name + ": " + comments.get(0).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment1.setText(span);
            } else {
                String name = comments.get(0).getUserName();
                String atName = comments.get(0).getAtUserName();
                str = name + " 回复 " + atName + ": " + comments.get(0).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), name.length() + 4, name.length() + 4 + atName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment1.setText(span);
            }

            if (StringUtils.isBlank(comments.get(1).getAtUserName())) {
                String name = comments.get(1).getUserName();
                str = name + ": " + comments.get(1).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment2.setText(span);
            } else {
                String name = comments.get(1).getUserName();
                String atName = comments.get(1).getAtUserName();
                str = name + " 回复 " + atName + ": " + comments.get(1).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), name.length() + 4, name.length() + 4 + atName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment2.setText(span);
            }
            comment3.setVisibility(View.GONE);
            comment4.setVisibility(View.GONE);
            comment5.setVisibility(View.GONE);
            comment6.setVisibility(View.GONE);
        }
        if (comments.size() == 3) {
            if (StringUtils.isBlank(comments.get(0).getAtUserName())) {
                String name = comments.get(0).getUserName();
                str = name + ": " + comments.get(0).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment1.setText(span);
            } else {
                String name = comments.get(0).getUserName();
                String atName = comments.get(0).getAtUserName();
                str = name + " 回复 " + atName + ": " + comments.get(0).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), name.length() + 4, name.length() + 4 + atName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment1.setText(span);
            }

            if (StringUtils.isBlank(comments.get(1).getAtUserName())) {
                String name = comments.get(1).getUserName();
                str = name + ": " + comments.get(1).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment2.setText(span);
            } else {
                String name = comments.get(1).getUserName();
                String atName = comments.get(1).getAtUserName();
                str = name + " 回复 " + atName + ": " + comments.get(1).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), name.length() + 4, name.length() + 4 + atName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment2.setText(span);
            }

            if (StringUtils.isBlank(comments.get(2).getAtUserName())) {
                String name = comments.get(2).getUserName();
                str = name + ": " + comments.get(2).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment3.setText(span);
            } else {
                String name = comments.get(2).getUserName();
                String atName = comments.get(2).getAtUserName();
                str = name + " 回复 " + atName + ": " + comments.get(2).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), name.length() + 4, name.length() + 4 + atName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment3.setText(span);
            }
            comment4.setVisibility(View.GONE);
            comment5.setVisibility(View.GONE);
            comment6.setVisibility(View.GONE);
        }
        if (comments.size() == 4) {
            if (StringUtils.isBlank(comments.get(0).getAtUserName())) {
                String name = comments.get(0).getUserName();
                str = name + ": " + comments.get(0).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment1.setText(span);
            } else {
                String name = comments.get(0).getUserName();
                String atName = comments.get(0).getAtUserName();
                str = name + " 回复 " + atName + ": " + comments.get(0).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), name.length() + 4, name.length() + 4 + atName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment1.setText(span);
            }

            if (StringUtils.isBlank(comments.get(1).getAtUserName())) {
                String name = comments.get(1).getUserName();
                str = name + ": " + comments.get(1).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment2.setText(span);
            } else {
                String name = comments.get(1).getUserName();
                String atName = comments.get(1).getAtUserName();
                str = name + " 回复 " + atName + ": " + comments.get(1).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), name.length() + 4, name.length() + 4 + atName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment2.setText(span);
            }

            if (StringUtils.isBlank(comments.get(2).getAtUserName())) {
                String name = comments.get(2).getUserName();
                str = name + ": " + comments.get(2).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment3.setText(span);
            } else {
                String name = comments.get(2).getUserName();
                String atName = comments.get(2).getAtUserName();
                str = name + " 回复 " + atName + ": " + comments.get(2).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), name.length() + 4, name.length() + 4 + atName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment3.setText(span);
            }

            if (StringUtils.isBlank(comments.get(3).getAtUserName())) {
                String name = comments.get(3).getUserName();
                str = name + ": " + comments.get(3).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment4.setText(span);
            } else {
                String name = comments.get(3).getUserName();
                String atName = comments.get(3).getAtUserName();
                str = name + " 回复 " + atName + ": " + comments.get(3).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), name.length() + 4, name.length() + 4 + atName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment4.setText(span);
            }
            comment5.setVisibility(View.GONE);
            comment6.setVisibility(View.GONE);
        }
        if (comments.size() >= 5) {
            if (StringUtils.isBlank(comments.get(0).getAtUserName())) {
                String name = comments.get(0).getUserName();
                str = name + ": " + comments.get(0).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment1.setText(span);
            } else {
                String name = comments.get(0).getUserName();
                String atName = comments.get(0).getAtUserName();
                str = name + " 回复 " + atName + ": " + comments.get(0).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), name.length() + 4, name.length() + 4 + atName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment1.setText(span);
            }

            if (StringUtils.isBlank(comments.get(1).getAtUserName())) {
                String name = comments.get(1).getUserName();
                str = name + ": " + comments.get(1).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment2.setText(span);
            } else {
                String name = comments.get(1).getUserName();
                String atName = comments.get(1).getAtUserName();
                str = name + " 回复 " + atName + ": " + comments.get(1).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), name.length() + 4, name.length() + 4 + atName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment2.setText(span);
            }

            if (StringUtils.isBlank(comments.get(2).getAtUserName())) {
                String name = comments.get(2).getUserName();
                str = name + ": " + comments.get(2).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment3.setText(span);
            } else {
                String name = comments.get(2).getUserName();
                String atName = comments.get(2).getAtUserName();
                str = name + " 回复 " + atName + ": " + comments.get(2).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), name.length() + 4, name.length() + 4 + atName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment3.setText(span);
            }

            if (StringUtils.isBlank(comments.get(3).getAtUserName())) {
                String name = comments.get(3).getUserName();
                str = name + ": " + comments.get(3).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment4.setText(span);
            } else {
                String name = comments.get(3).getUserName();
                String atName = comments.get(3).getAtUserName();
                str = name + " 回复 " + atName + ": " + comments.get(3).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), name.length() + 4, name.length() + 4 + atName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment4.setText(span);
            }

            if (StringUtils.isBlank(comments.get(4).getAtUserName())) {
                String name = comments.get(4).getUserName();
                str = name + ": " + comments.get(4).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment5.setText(span);
            } else {
                String name = comments.get(4).getUserName();
                String atName = comments.get(4).getAtUserName();
                str = name + " 回复 " + atName + ": " + comments.get(4).getContent();
                span = new SpannableString(str);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(UiUtils.getColor(R.color.custom_orange)), name.length() + 4, name.length() + 4 + atName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comment5.setText(span);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                onBackPressed();
                break;
            case R.id.activity_btn_apply:// 点击报名
                applyDialog();
                break;
            case R.id.activity_btn_share:// 点击分享
                ShareTask shareTask = new ShareTask();
                shareTask.execute();
                break;
            case R.id.activity_btn_more:// 展开更多内容
                if (isOpen) {// 打开状态，点击关闭
                    descClose();
                    isOpen = false;
                } else {// 关闭状态，点击打开
                    descOpen();
                    isOpen = true;
                }
                break;
            case R.id.activity_btn_quantity_totle:// 查看全部报名人
                Intent intent1 = new Intent(this, ActivityAllApplys.class);
                intent1.putExtra("id", data.getActionId());
                startActivity(intent1);
                break;
//            case R.id.rl_apply1:// 报名人1
//                SuperToastUtils.showSuperToast(this, 2, "报名人1");
//                break;
//            case R.id.rl_apply2:// 报名人2
//                SuperToastUtils.showSuperToast(this, 2, "报名人2");
//                break;
//            case R.id.rl_apply3:// 报名人3
//                SuperToastUtils.showSuperToast(this, 2, "报名人3");
//                break;
//            case R.id.rl_apply4:// 报名人4
//                SuperToastUtils.showSuperToast(this, 2, "报名人4");
//                break;
//            case R.id.rl_apply5:// 报名人5
//                SuperToastUtils.showSuperToast(this, 2, "报名人5");
//                break;
            case R.id.activity_btn_comment_totle:// 查看全部点赞和评论
                Intent intent2 = new Intent(this, ActivityAllComments.class);
                intent2.putExtra("id", data.getActionId());
                intent2.putExtra("flag", data.isFlag());
                startActivityForResult(intent2, REQUEST_CODE);
                break;
            case R.id.activity_tv_comment1:// 回复评论1
                if (comments.get(0).getUsersByUserId().getUserId() == SharedPreferencesUtils.getUserId(mContext)) {
                    // 弹框提示删除
                    showDeleteWindow(comment1, 0);
                } else {
                    CommentDialog(String.valueOf(comments.get(0).getUsersByUserId().getUserId()), comments.get(0).getUserName());
                }
                break;
            case R.id.activity_tv_comment2:// 回复评论2
                if (comments.get(1).getUsersByUserId().getUserId() == SharedPreferencesUtils.getUserId(mContext)) {
                    // 弹框提示删除
                    showDeleteWindow(comment2, 1);
                } else {
                    CommentDialog(String.valueOf(comments.get(1).getUsersByUserId().getUserId()), comments.get(1).getUserName());
                }
                break;
            case R.id.activity_tv_comment3:// 回复评论3
                if (comments.get(2).getUsersByUserId().getUserId() == SharedPreferencesUtils.getUserId(mContext)) {
                    // 弹框提示删除
                    showDeleteWindow(comment3, 2);
                } else {
                    CommentDialog(String.valueOf(comments.get(2).getUsersByUserId().getUserId()), comments.get(2).getUserName());
                }
                break;
            case R.id.activity_tv_comment4:// 回复评论4
                if (comments.get(3).getUsersByUserId().getUserId() == SharedPreferencesUtils.getUserId(mContext)) {
                    // 弹框提示删除
                    showDeleteWindow(comment4, 3);
                } else {
                    CommentDialog(String.valueOf(comments.get(3).getUsersByUserId().getUserId()), comments.get(3).getUserName());
                }
                break;
            case R.id.activity_tv_comment5:// 回复评论5
                if (comments.get(4).getUsersByUserId().getUserId() == SharedPreferencesUtils.getUserId(mContext)) {
                    // 弹框提示删除
                    showDeleteWindow(comment5, 4);
                } else {
                    CommentDialog(String.valueOf(comments.get(4).getUsersByUserId().getUserId()), comments.get(4).getUserName());
                }
                break;
            case R.id.activity_btn_praise:// 点赞
                if (data.isFlag()) {
                    ActivityPriseTask activityPriseTask = new ActivityPriseTask(2);
                    activityPriseTask.execute();
                } else {
                    ActivityPriseTask activityPriseTask = new ActivityPriseTask(1);
                    activityPriseTask.execute();
                }
                break;
            case R.id.activity_btn_comment:// 评论
                CommentDialog("", "");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (ISATTENDED) {
            Intent intent = new Intent();
            intent.putExtra("ISATTENDED", ISATTENDED);
            setResult(RESULT_CODE, intent);
        }
        finish();
    }

    // 闭合TextView
    private void descClose() {
        activityDesc.setEllipsize(TextUtils.TruncateAt.END);
        activityDesc.setMaxLines(3);
        imgMore.setBackgroundResource(R.mipmap.icon_activity_more);
        // 项目照片
        photos.clear();
        if (data.getActionimages().size() == 0) {
            return;
        } else if (data.getActionimages().size() == 1) {
            photos.add(data.getActionimages().get(0));
        } else if (data.getActionimages().size() == 2) {
            photos.add(data.getActionimages().get(0));
            photos.add(data.getActionimages().get(1));
        } else if (data.getActionimages().size() >= 3) {
            photos.add(data.getActionimages().get(0));
            photos.add(data.getActionimages().get(1));
            photos.add(data.getActionimages().get(2));
        }
        photosAdapter.notifyDataSetChanged();
        urls.clear();
        for (ActivityDetailBean.DataBean.ActionimagesBean bean : photos) {
            urls.add(bean.getUrl());
        }
    }

    // 打开TextView
    private void descOpen() {
        activityDesc.setEllipsize(null);
        activityDesc.setMaxLines(Integer.MAX_VALUE);
        imgMore.setBackgroundResource(R.mipmap.icon_activity_less);
        // 项目照片
        photos.clear();
        if (data.getActionimages().size() == 0) {
            return;
        }
        for (ActivityDetailBean.DataBean.ActionimagesBean bean : data.getActionimages()) {
            photos.add(bean);
        }
        photosAdapter.notifyDataSetChanged();
        urls.clear();
        for (ActivityDetailBean.DataBean.ActionimagesBean bean : photos) {
            urls.add(bean.getUrl());
        }
    }

    // 获取活动内容详情
    private class GetActivityDetailTask extends AsyncTask<Void, Void, ActivityDetailBean> {
        @Override
        protected ActivityDetailBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETACTIVITYDETAIL)),
                            "contentId", String.valueOf(getIntent().getIntExtra("id", 0)),
                            Constant.BASE_URL + Constant.GETACTIVITYDETAIL,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("活动详情", body);
                return FastJsonTools.getBean(body, ActivityDetailBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ActivityDetailBean activityDetailBean) {
            super.onPostExecute(activityDetailBean);
            if (activityDetailBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (activityDetailBean.getStatus() == 200) {
                    data = activityDetailBean.getData();
                    if (data != null) {
                        initData();
                    }
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, activityDetailBean.getMessage());
                }
            }
        }
    }

    // 获取全部报名人
    private class GetAllApplysTask extends AsyncTask<Void, Void, ActivityAllApplysBean> {
        @Override
        protected ActivityAllApplysBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETALLACTIVITYAPPLYS)),
                            "contentId", String.valueOf(getIntent().getIntExtra("id", 0)),
                            "page", "0",
                            Constant.BASE_URL + Constant.GETALLACTIVITYAPPLYS,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("全部报名人", body);
                return FastJsonTools.getBean(body, ActivityAllApplysBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ActivityAllApplysBean activityAllApplysBean) {
            super.onPostExecute(activityAllApplysBean);
            if (activityAllApplysBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (activityAllApplysBean.getStatus() == 200) {
                    applys = activityAllApplysBean.getData();
                    if (applys != null) {
                        initApplys();
                    }
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, activityAllApplysBean.getMessage());
                }
            }
        }
    }

    // 获取全部评论人
    private class GetAllCommentsTask extends AsyncTask<Void, Void, ActivityAllCommentsBean> {
        @Override
        protected ActivityAllCommentsBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETALLACTIVITYCOMMENTS)),
                            "contentId", String.valueOf(getIntent().getIntExtra("id", 0)),
                            "page", "0",
                            Constant.BASE_URL + Constant.GETALLACTIVITYCOMMENTS,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("全部评论人", body);
                return FastJsonTools.getBean(body, ActivityAllCommentsBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ActivityAllCommentsBean activityAllCommentsBean) {
            super.onPostExecute(activityAllCommentsBean);
            if (activityAllCommentsBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (activityAllCommentsBean.getStatus() == 200) {
                    prises = activityAllCommentsBean.getData().getPrises();
                    comments = activityAllCommentsBean.getData().getComments();
                    initComments();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, activityAllCommentsBean.getMessage());
                }
            }
        }
    }

    // 点赞
    private class ActivityPriseTask extends AsyncTask<Void, Void, ActivityPriseBean> {
        private int flag;

        public ActivityPriseTask(int flag) {
            this.flag = flag;
        }

        @Override
        protected ActivityPriseBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.PRISEACTIVITY)),
                            "contentId", String.valueOf(data.getActionId()),
                            "flag", String.valueOf(flag),
                            Constant.BASE_URL + Constant.PRISEACTIVITY,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("点赞信息", body);
                return FastJsonTools.getBean(body, ActivityPriseBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ActivityPriseBean activityPriseBean) {
            super.onPostExecute(activityPriseBean);
            if (activityPriseBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (activityPriseBean.getStatus() == 200) {
                    if (flag == 1) {
                        data.setFlag(true);
                        prises.add(0, activityPriseBean.getData().getName());
                    } else {
                        data.setFlag(false);
                        prises.remove(activityPriseBean.getData().getName());
                    }
                    initComments();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, activityPriseBean.getMessage());
                }
            }
        }
    }

    // 评论
    private class ActivityCommentTask extends AsyncTask<Void, Void, ActivityCommentBean> {
        private String atUserId;
        private String content;

        public ActivityCommentTask(String atUserId, String content) {
            this.atUserId = atUserId;
            this.content = content;
        }

        @Override
        protected ActivityCommentBean doInBackground(Void... params) {
            String body = "";
            int flag;
            if (StringUtils.isBlank(atUserId)) {
                flag = 1;// 评论
            } else {
                flag = 2;// 回复
            }
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.COMMENTACTIVITY)),
                            "contentId", String.valueOf(data.getActionId()),
                            "content", content,
                            "atUserId", atUserId,
                            "flag", String.valueOf(flag),
                            Constant.BASE_URL + Constant.COMMENTACTIVITY,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("评论返回信息", body);
                return FastJsonTools.getBean(body, ActivityCommentBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ActivityCommentBean activityCommentBean) {
            super.onPostExecute(activityCommentBean);
            if (activityCommentBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (activityCommentBean.getStatus() == 200) {
                    popupWindow.dismiss();
                    comment = "";
                    GetAllCommentsTask getAllCommentsTask = new GetAllCommentsTask();
                    getAllCommentsTask.execute();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, activityCommentBean.getMessage());
                }
            }
        }
    }

    // 弹出评论输入框
    private void CommentDialog(final String atUserId, String atUserName) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_comment, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        final EditText edComment = (EditText) view.findViewById(R.id.ed_comment);
        edComment.setText(comment);
        if (!StringUtils.isBlank(atUserId)) {
            edComment.setHint("回复" + atUserName);
        }
        edComment.setSelection(comment.length());
        TextView btnComment = (TextView) view.findViewById(R.id.btn_comment);
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isBlank(edComment.getText().toString())) {
                    ActivityCommentTask activityCommentTask = new ActivityCommentTask(atUserId, edComment.getText().toString());
                    activityCommentTask.execute();
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, "请输入评论内容");
                }
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(btnApply, Gravity.BOTTOM, 0, 0);
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

    // 报名弹框
    private void applyDialog() {
        dialog = new AlertDialog.Builder(ActivityDetailActivity.this, R.style.Custom_Dialog).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setView(new EditText(mContext));
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_activity_apply);
        RelativeLayout bgEdt = (RelativeLayout) window.findViewById(R.id.dialog_activity_bg_edt);
        final EditText edt = (EditText) window.findViewById(R.id.dialog_activity_edt);
        Button btnCancel = (Button) window.findViewById(R.id.dialog_activity_btn_cancel);
        Button btnConfirm = (Button) window.findViewById(R.id.dialog_activity_btn_confirm);
        // dialog弹出后自动弹出键盘
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               imm.showSoftInput(edt, 0);
                           }
                       },
                100);
        bgEdt.setOnClickListener(new View.OnClickListener() {// 点击输入框背景弹出键盘
            @Override
            public void onClick(View v) {
                imm.showSoftInput(edt, 0);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {// 取消
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {// 确认报名
            @Override
            public void onClick(View v) {
                ApplyTask applyTask = new ApplyTask(edt.getText().toString());
                applyTask.execute();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
            }
        });
    }

    // 报名接口
    private class ApplyTask extends AsyncTask<Void, Void, ActivityApplyBean> {
        private String content;

        public ApplyTask(String content) {
            this.content = content;
        }

        @Override
        protected ActivityApplyBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.APPLYACTIVITY)),
                            "contentId", String.valueOf(data.getActionId()),
                            "content", content,
                            Constant.BASE_URL + Constant.APPLYACTIVITY,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("报名返回信息", body);
                return FastJsonTools.getBean(body, ActivityApplyBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ActivityApplyBean activityApplyBean) {
            super.onPostExecute(activityApplyBean);
            if (activityApplyBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
            } else {
                if (activityApplyBean.getStatus() == 200) {
                    SuperToastUtils.showSuperToast(mContext, 2, activityApplyBean.getMessage());
                    dialog.dismiss();
                    btnApply.setText("已报名");
                    btnApply.setBackgroundColor(Color.parseColor("#cccccc"));
                    btnApply.setClickable(false);
                    ISATTENDED = true;
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, activityApplyBean.getMessage());
                }
            }
        }
    }

    // 分享活动
    private class ShareTask extends AsyncTask<Void, Void, ShareBean> {
        @Override
        protected ShareBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.SHAREACTIVITY)),
                            "type", "4",
                            "contentId", String.valueOf(data.getActionId()),
                            Constant.BASE_URL + Constant.SHAREACTIVITY,
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
            } else {
                if (shareBean.getStatus() == 200) {
                    ShareUtils shareUtils = new ShareUtils(ActivityDetailActivity.this);
                    DialogUtils.newShareDialog(ActivityDetailActivity.this, shareUtils, data.getName(), data.getDescription(), shareBean.getData().getImage(), shareBean.getData().getUrl());
                } else {
                    SuperToastUtils.showSuperToast(mContext, 2, shareBean.getMessage());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            if (resultCode == ActivityAllComments.RESULT_CODE) {
                if (data.getBooleanExtra("needRefresh", false)) {// 在全部评论里进行了交互
                    GetAllCommentsTask getAllCommentsTask = new GetAllCommentsTask();
                    getAllCommentsTask.execute();
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

    // 删除评论弹窗
    private void showDeleteWindow(View view, final int position) {
        ImageButton button = new ImageButton(mContext);
        button.setBackgroundResource(R.mipmap.icon_delete);
        final PopupWindow popupWindow = new PopupWindow(button, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        int[] location = new int[2];
        view.getLocationInWindow(location);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCommentTask deleteCommentTask = new DeleteCommentTask(comments.get(position).getCommentId());
                deleteCommentTask.execute();
                comments.remove(position);
                initComments();
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getWidth() / 2 - UiUtils.dip2px(34), location[1] - UiUtils.dip2px(33));
    }

    // 删除活动评论
    private class DeleteCommentTask extends AsyncTask<Void, Void, CommonBean> {
        private int commentId;

        public DeleteCommentTask(int commentId) {
            this.commentId = commentId;
        }

        @Override
        protected CommonBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.DELETEACTIVITYCOMMENT)),
                            "commentId", String.valueOf(commentId),
                            Constant.BASE_URL + Constant.DELETEACTIVITYCOMMENT,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("删除评论", body);
                return FastJsonTools.getBean(body, CommonBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonBean commonBean) {
            super.onPostExecute(commonBean);
            if (commonBean != null) {
                Log.i("删除评论完成", commonBean.getMessage());
            }
        }
    }
}
